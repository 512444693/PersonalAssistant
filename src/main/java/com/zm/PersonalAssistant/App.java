package com.zm.PersonalAssistant;

import com.zm.PersonalAssistant.DataBackup.DropBox;
import com.zm.PersonalAssistant.DataBackup.SyncToCloud;

import static org.mockito.Mockito.mock;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        DropBox dropBox = new DropBox("DropBox.auth");
        //DropBox dropBox = mock(DropBox.class);
        SyncToCloud syncToCloud = new SyncToCloud(dropBox);
        syncToCloud.upload(args[0]);
    }
}
