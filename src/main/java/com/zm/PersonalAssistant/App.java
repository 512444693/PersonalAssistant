package com.zm.PersonalAssistant;

import com.zm.PersonalAssistant.DataBackup.DropBox;
import com.zm.PersonalAssistant.DataBackup.SyncToCloud;


/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        if(args.length != 2){
            throw new IllegalStateException("<file-ready-to-upload> <except-file>");
        }
        DropBox dropBox = new DropBox("DropBox.auth");
        SyncToCloud syncToCloud = new SyncToCloud(dropBox);
        syncToCloud.uploadExcept(args[0], args[1]);

    }
}
