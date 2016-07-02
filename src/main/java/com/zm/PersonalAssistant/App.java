package com.zm.PersonalAssistant;

import com.zm.PersonalAssistant.DataPersistence.DropBox;
import com.zm.PersonalAssistant.DataPersistence.SyncToCloud;
import com.zm.PersonalAssistant.UI.Mail;
import com.zm.PersonalAssistant.server.Server;
import com.zm.PersonalAssistant.utils.LunarCalendar;
import org.apache.log4j.PropertyConfigurator;

import javax.mail.MessagingException;


/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        //Server.getInstance().start();
        Mail mail = null;
        try {
            mail = new Mail("information_rec@163.com", "zhangmin", "512444693@qq.com");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        mail.send(new LunarCalendar().toString());
    }
}
