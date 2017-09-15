package com.zm.PersonalAssistant.configuration;

import com.zm.frame.conf.Config;

import java.io.IOException;
import static com.zm.frame.log.Log.log;

/**
 * Created by zhangmin on 2016/6/27.
 */
public class MyConfig extends Config {

    //是否使用dropbox云同步
    private boolean useDropBox;
    //事件检查时间间隔
    private int checkInterval;
    //持久化线程检查时间间隔
    private int persistenceInterval;

    //微信
    private String corpid;
    private String corpsecret;
    //区别于access_token
    private String token;
    private String encodingAESKey;
    // 微信用户
    private String user;
    private int agentid;

    //监听http端口
    private String port;


    public MyConfig(String filePath) throws IOException {
        super(filePath);

        this.useDropBox = getBoolean("useDropBox");
        this.checkInterval = getInt("checkInterval");
        this.persistenceInterval = getInt("persistenceInterval");
        this.corpid = getString("corpid");
        this.corpsecret = getString("corpsecret");
        this.token = getString("token");
        this.encodingAESKey = getString("encodingAESKey");
        this.user = getString("user");
        this.port = getString("port");
        this.agentid = getInt("agentid");
        log.info("配置如下 : " + properties.toString());
    }

    public boolean isUseDropBox() {
        return useDropBox;
    }

    public int getCheckInterval() {
        return checkInterval;
    }

    public int getPersistenceInterval() {
        return persistenceInterval;
    }

    public String getCorpid() {
        return corpid;
    }

    public String getCorpsecret() {
        return corpsecret;
    }

    public String getToken() {
        return token;
    }

    public String getEncodingAESKey() {
        return encodingAESKey;
    }

    public String getPort() {
        return port;
    }

    public String getUser() {
        return user;
    }

    public int getAgentid() {
        return agentid;
    }
}
