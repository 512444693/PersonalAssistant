package com.zm.PersonalAssistant;

import com.zm.PersonalAssistant.UI.Mail;
import com.zm.PersonalAssistant.configuration.MyConfig;
import com.zm.PersonalAssistant.server.Server;
import com.zm.PersonalAssistant.utils.LunarCalendar;

import javax.mail.MessagingException;
import java.io.IOException;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        Server.getInstance().start();

        /*MyConfig config = null;
        try {
            config = new MyConfig(Server.CONFIGURATION_DIRECTORY_PATH +  "conf.properties");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Mail mail = null;
        try {
            mail = new Mail(config.getMailSmtpHost(), config.getMailImapHost(),
                    config.getMailUser(), config.getMailPassword(), config.getMailTo());
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        mail.send(new LunarCalendar().toString());*/
        //TODO : 长时间接收邮件失败
        //TODO : crond脚本重启失败
        //TODO : 程序启动是否备份所有，配置
    }
}
