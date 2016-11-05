package com.zm.PersonalAssistant;

import com.zm.PersonalAssistant.server.PAServer;


/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) {
        PAServer.getInstance().start();
    }
}
