package com.zm.PersonalAssistant;

import com.zm.PersonalAssistant.DataPersistence.DropBox;
import com.zm.PersonalAssistant.DataPersistence.SyncToCloud;
import com.zm.PersonalAssistant.server.Server;
import org.apache.log4j.PropertyConfigurator;


/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        Server.getInstance().start();
    }
}
