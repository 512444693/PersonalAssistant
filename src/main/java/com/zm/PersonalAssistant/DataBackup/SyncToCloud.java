package com.zm.PersonalAssistant.DataBackup;

import com.dropbox.core.v2.files.DbxUserFilesRequests;

import java.io.File;

/**
 * Created by zhangmin on 2016/6/16.
 */
public class SyncToCloud {
    private CloudPlatform cloudPlatform;

    public SyncToCloud(CloudPlatform cloudPlatform) {
        this.cloudPlatform = cloudPlatform;
    }

    public void upload(String filePath) {
        uploadExcept(filePath, "");
    }

    public void uploadExcept(String filePath, String exceptPath){
        File file = new File(filePath);
        if(!file.exists()){
            throw new IllegalArgumentException("File or directory is not exists");
        }
        if(file.getPath().equals(exceptPath)){
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
