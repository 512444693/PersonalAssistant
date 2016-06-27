package com.zm.PersonalAssistant.configuration;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by zhangmin on 2016/6/27.
 */
public class Config {

    private static final Logger log = Logger.getLogger(Config.class);

    private Properties properties;

    private boolean useDropBox;

    private int checkInterval;

    public Config(String filePath) throws IOException {
        properties = new Properties();
        try {
            properties.load(this.getInputStream(filePath));
        } catch (IOException e) {
            log.error("读取配置文件失败");
            throw e;
        }

        String useDropBoxStr = properties.getProperty("useDropBox");
        if(useDropBoxStr == null || (!useDropBoxStr.equals("true") && !useDropBoxStr.equals("false"))){
            log.error("useDropBox 配置错误");
            throw new IllegalStateException("useDropBox 配置错误");
        } else {
            this.useDropBox = Boolean.parseBoolean(useDropBoxStr);
        }

        String checkIntervalStr = properties.getProperty("checkInterval");
        if(checkIntervalStr == null) {
            log.error("checkInterval 配置错误");
            throw new IllegalStateException("checkInterval 配置错误");
        } else {
            this.checkInterval = Integer.parseInt(checkIntervalStr);
        }

        log.info("配置如下 : " + properties.toString());
    }

    //for test
    protected InputStream getInputStream(String filePath) {
        InputStream ret = getClass().getResourceAsStream(filePath);
        if(ret == null){
            log.error("Can not find file " + filePath);
            throw new IllegalArgumentException("Can not find file " + filePath);
        }
        return ret;
    }

    public boolean isUseDropBox() {
        return useDropBox;
    }

    public int getCheckInterval() {
        return checkInterval;
    }
}
