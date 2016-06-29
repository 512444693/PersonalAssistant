package com.zm.PersonalAssistant.configuration;

import java.io.IOException;

/**
 * Created by zhangmin on 2016/6/27.
 */
public class MyConfig extends Config {

    private boolean useDropBox;

    private int checkInterval;

    public MyConfig(String filePath) throws IOException {
        super(filePath);

        this.useDropBox = getBoolean("useDropBox");
        this.checkInterval = getInt("checkInterval");
        log.info("配置如下 : " + properties.toString());
    }

    public boolean isUseDropBox() {
        return useDropBox;
    }

    public int getCheckInterval() {
        return checkInterval;
    }
}
