package com.zm.PersonalAssistant.DataBackup;

import com.dropbox.core.*;
import com.dropbox.core.json.JsonReader;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.DbxPathV2;
import com.dropbox.core.v2.files.*;
import com.zm.PersonalAssistant.utils.StringUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

/**
 * Created by zhangmin on 2016/6/16.
 */
public class DropBox extends CloudPlatform {

    private static final Logger log = Logger.getLogger(DropBox.class);
    private final DbxClientV2 dbxClient;

    private static final long CHUNKED_UPLOAD_CHUNK_SIZE = 8L << 20; // 8MiB
    private static final int CHUNKED_UPLOAD_MAX_ATTEMPTS = 5;
    private static final String CURRENT_OR_PARENT_DIRECTORY_REGEX_LINUX = "[./|../]+";
    private static final String CURRENT_OR_PARENT_DIRECTORY_REGEX_WIN = "[.\\\\|..\\\\]+";

    public DropBox(String authFile){
        DbxAuthInfo authInfo = null;
        try {
            authInfo = DbxAuthInfo.Reader.readFromFile(authFile);
        } catch (JsonReader.FileLoadException ex) {
            log.error("Error loading <auth-file>: " + ex.getMessage());
            System.exit(1);
        }

        DbxRequestConfig requestConfig = new DbxRequestConfig("PersonalAssistant-upload-file");
        dbxClient = new DbxClientV2(requestConfig, authInfo.getAccessToken(), authInfo.getHost());
    }

    @Override
    public void uploadFile(String localFilePath) {
        File localFile = new File(localFilePath);

        //将文件最外层目录当作DropBox的基目录
        String remotePath ="/" + StringUtils.removeStart(StringUtils.removeStart(localFilePath, CURRENT_OR_PARENT_DIRECTORY_REGEX_WIN), CURRENT_OR_PARENT_DIRECTORY_REGEX_LINUX);

        String pathError = DbxPathV2.findError(remotePath);
        if (pathError != null) {
            log.error("Invalid <dropbox-path>: " + pathError);
            return;
        }
        if (localFile.length() <= (2 * CHUNKED_UPLOAD_CHUNK_SIZE)) {
            uploadFile(dbxClient, localFile, remotePath);
        } else {
            chunkedUploadFile(dbxClient, localFile, remotePath);
        }
    }

    private void uploadFile(DbxClientV2 dbxClient, File localFile, String dropboxPath) {
        try (InputStream in = new FileInputStream(localFile)) {
            FileMetadata metadata = dbxClient.files().uploadBuilder(dropboxPath)
                    .withMode(WriteMode.OVERWRITE)
                    .withClientModified(new Date(localFile.lastModified()))
                    .uploadAndFinish(in);

            log.debug("Successfully uploading to Dropbox: " + metadata.getName());
        } catch (UploadErrorException ex) {
            log.error("Error uploading to Dropbox: " + ex.getMessage());
        } catch (DbxException ex) {
            log.error("Error uploading to Dropbox: " + ex.getMessage());
        } catch (IOException ex) {
            log.error("Error reading from file \"" + localFile + "\": " + ex.getMessage());
        }
    }

    private  void chunkedUploadFile(DbxClientV2 dbxClient, File localFile, String dropboxPath) {
        long size = localFile.length();

        // assert our file is at least the chunk upload size. We make this assumption in the code
        // below to simplify the logic.
        if (size < CHUNKED_UPLOAD_CHUNK_SIZE) {
            log.error("File too small, use upload() instead.");
            return;
        }

        long uploaded = 0L;
        DbxException thrown = null;

        // Chunked uploads have 3 phases, each of which can accept uploaded bytes:
        //
        //    (1)  Start: initiate the upload and get an upload session ID
        //    (2) Append: upload chunks of the file to append to our session
        //    (3) Finish: commit the upload and close the session
        //
        // We track how many bytes we uploaded to determine which phase we should be in.
        String sessionId = null;
        for (int i = 0; i < CHUNKED_UPLOAD_MAX_ATTEMPTS; ++i) {
            if (i > 0) {
                log.debug(String.format("Retrying chunked upload (%d / %d attempts)\n", i + 1, CHUNKED_UPLOAD_MAX_ATTEMPTS));
            }

            try (InputStream in = new FileInputStream(localFile)) {
                // if this is a retry, make sure seek to the correct offset
                in.skip(uploaded);

                // (1) Start
                if (sessionId == null) {
                    sessionId = dbxClient.files().uploadSessionStart()
                            .uploadAndFinish(in, CHUNKED_UPLOAD_CHUNK_SIZE)
                            .getSessionId();
                    uploaded += CHUNKED_UPLOAD_CHUNK_SIZE;
                    printProgress(uploaded, size);
                }

                UploadSessionCursor cursor = new UploadSessionCursor(sessionId, uploaded);

                // (2) Append
                while ((size - uploaded) > CHUNKED_UPLOAD_CHUNK_SIZE) {
                    dbxClient.files().uploadSessionAppendV2(cursor)
                            .uploadAndFinish(in, CHUNKED_UPLOAD_CHUNK_SIZE);
                    uploaded += CHUNKED_UPLOAD_CHUNK_SIZE;
                    printProgress(uploaded, size);
                    cursor = new UploadSessionCursor(sessionId, uploaded);
                }

                // (3) Finish
                long remaining = size - uploaded;
                CommitInfo commitInfo = CommitInfo.newBuilder(dropboxPath)
                        //.withMode(WriteMode.ADD)
                        .withMode(WriteMode.OVERWRITE)
                        .withClientModified(new Date(localFile.lastModified()))
                        .build();
                FileMetadata metadata = dbxClient.files().uploadSessionFinish(cursor, commitInfo)
                        .uploadAndFinish(in, remaining);

                log.debug("Successfully uploading to Dropbox: " + metadata.getName());
                return;
            } catch (RetryException ex) {
                thrown = ex;
                // RetryExceptions are never automatically retried by the client for uploads. Must
                // catch this exception even if DbxRequestConfig.getMaxRetries() > 0.
                try {
                    sleepQuietly(ex.getBackoffMillis());
                } catch (InterruptedException exInter) {
                    // just exit
                    log.error("Error uploading to Dropbox: interrupted during backoff.");
                    return;
                }
                continue;
            } catch (NetworkIOException ex) {
                thrown = ex;
                // network issue with Dropbox (maybe a timeout?) try again
                continue;
            } catch (UploadSessionLookupErrorException ex) {
                if (ex.errorValue.isIncorrectOffset()) {
                    thrown = ex;
                    // server offset into the stream doesn't match our offset (uploaded). Seek to
                    // the expected offset according to the server and try again.
                    uploaded = ex.errorValue
                            .getIncorrectOffsetValue()
                            .getCorrectOffset();
                    continue;
                } else {
                    // Some other error occurred, give up.
                    log.error("Error uploading to Dropbox: " + ex.getMessage());
                    return;
                }
            } catch (UploadSessionFinishErrorException ex) {
                if (ex.errorValue.isLookupFailed() && ex.errorValue.getLookupFailedValue().isIncorrectOffset()) {
                    thrown = ex;
                    // server offset into the stream doesn't match our offset (uploaded). Seek to
                    // the expected offset according to the server and try again.
                    uploaded = ex.errorValue
                            .getLookupFailedValue()
                            .getIncorrectOffsetValue()
                            .getCorrectOffset();
                    continue;
                } else {
                    // some other error occurred, give up.
                    log.error("Error uploading to Dropbox: " + ex.getMessage());
                    return;
                }
            } catch (DbxException ex) {
                log.error("Error uploading to Dropbox: " + ex.getMessage());
                return;
            } catch (IOException ex) {
                log.error("Error reading from file \"" + localFile + "\": " + ex.getMessage());
                return;
            }
        }

        // if we made it here, then we must have run out of attempts
        log.error("Maxed out upload attempts to Dropbox. Most recent error: " + thrown.getMessage());
    }

    private void printProgress(long uploaded, long size) {
        log.debug(String.format("Uploaded %12d / %12d bytes (%5.2f%%)\n", uploaded, size, 100 * (uploaded / (double) size)));
    }

    private void sleepQuietly(long millis) throws InterruptedException {
            Thread.sleep(millis);
    }
}
