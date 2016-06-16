package com.zm.PersonalAssistant.DataBackup;

/**
 * Created by zhangmin on 2016/6/16.
 */
public abstract class CloudPlatform {

    public abstract void uploadFile(String localFilePath, String remotePath);
}
