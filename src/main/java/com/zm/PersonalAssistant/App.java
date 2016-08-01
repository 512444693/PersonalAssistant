package com.zm.PersonalAssistant;

import com.zm.PersonalAssistant.server.Server;


/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) {
        Server.getInstance().start();
    }
}
