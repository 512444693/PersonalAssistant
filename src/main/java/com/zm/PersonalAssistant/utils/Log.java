package com.zm.PersonalAssistant.utils;

import com.zm.PersonalAssistant.server.Server;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * Created by Administrator on 2016/7/1.
 */
public class Log {
    static {
        PropertyConfigurator.configure(Server.CONFIGURATION_DIRECTORY_PATH +  "log4j.properties");
    }
    public static final Logger log = Logger.getLogger(Log.class);
}
