package com.zm.PersonalAssistant.DataBackup;

import com.dropbox.core.util.StringUtil;
import com.dropbox.core.v2.files.DbxUserFilesRequests;
import com.zm.PersonalAssistant.utils.StringUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;

/**
 * Created by zhangmin on 2016/6/16.
 */
public class SyncToCloud {
    private CloudPlatform cloudPlatform;
    private static final Logger log = Logger.getLogger(SyncToCloud.class);

    public SyncToCloud(CloudPlatform cloudPlatform) {
        this.cloudPlatform = cloudPlatform;
    }

    public void upload(String filePath) {
        uploadExcept(filePath, "");
    }

    public void uploadExcept(String filePath, String exceptPath){
        File file = new File(filePath);
        if(!file.exists()){
            log.error("File or directory " + filePath + " is not exists, no file uploaded");
            return;
        }
        //比较两个文件或目录的规范路径是否一致
        File exceptFile = new File(exceptPath);
        try {
            if(file.getCanonicalPath().equals(exceptFile.getCanonicalPath())){
                return;
            }
        } catch (IOException e) {
            log.error(e);
            return;
        }

        if(file.isFile()){
            cloudPlatform.uploadFile(file.getPath());
        }
        else if (file.isDirectory()){
            File[] files = file.listFiles();
            for(File tmp : files){
                uploadExcept(tmp.getPath(), exceptPath);
            }
        }
    }
}
