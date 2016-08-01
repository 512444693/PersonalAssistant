package com.zm.PersonalAssistant.configuration;

import java.io.IOException;
import static com.zm.PersonalAssistant.utils.Log.log;

/**
 * Created by zhangmin on 2016/6/27.
 */
public class MyConfig extends Config {

    private boolean useDropBox;

    private int checkInterval;

    private int persistenceInterval;

    private int recInterval;

    private String mailUser;

    private String mailPassword;

    private String mailTo;

    private String mailSmtpHost;

    private String mailImapHost;
    public MyConfig(String filePath) throws IOException {
        super(filePath);

        this.useDropBox = getBoolean("useDropBox");
        this.checkInterval = getInt("checkInterval");
        this.persistenceInterval = getInt("persistenceInterval");
        this.recInterval = getInt("recInterval");
        this.mailUser = getString("mailUser");
        this.mailPassword = getString("mailPassword");
        this.mailTo = getString("mailTo");
        this.mailSmtpHost = getString("mailSmtpHost");
        this.mailImapHost = getString("mailImapHost");
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

    public int getRecInterval() {
        return recInterval;
    }

    public String getMailUser() {
        return mailUser;
    }

    public String getMailPassword() {
        return mailPassword;
    }

    public String getMailTo() {
        return mailTo;
    }

    public String getMailSmtpHost() {
        return mailSmtpHost;
    }

    public String getMailImapHost() {
        return mailImapHost;
    }
}
