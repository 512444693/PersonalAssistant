package com.zm.PersonalAssistant;

import com.zm.PersonalAssistant.UI.Mail;
import com.zm.PersonalAssistant.configuration.MyConfig;
import com.zm.PersonalAssistant.server.Server;
import com.zm.PersonalAssistant.utils.LunarCalendar;
import static com.zm.PersonalAssistant.utils.Log.log;

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
        //TODO : 邮件在linux下的编码问题
        String text = new LunarCalendar().toString() + "6666666666666这是怎么回事";
        String text2 = "8888888888这是怎么回事";
        System.out.println("========1==========");
        System.out.println(text);
        System.out.println(text2);

        log.debug(text);
        log.debug(text2);

        //mail.send(text);*/
    }
}
