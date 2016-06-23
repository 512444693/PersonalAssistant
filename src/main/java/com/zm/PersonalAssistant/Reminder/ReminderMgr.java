package com.zm.PersonalAssistant.Reminder;

import com.zm.PersonalAssistant.utils.LunarCalendar;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by zhangmin on 2016/6/23.
 */
public class ReminderMgr {
    private Logger log = Logger.getLogger(ReminderMgr.class);
    private List<Reminder> list = new ArrayList<>();
    private static ReminderMgr instance = new ReminderMgr();

    private ReminderMgr(){}


    public void add(Reminder reminder){
        list.add(reminder);
        log.info("Add a reminder : " + reminder);
        sort();
    }

    public String getNotify(){
        LunarCalendar timeNow = new LunarCalendar();
        StringBuilder ret = new StringBuilder();
        String aNotifyStr = "";
        for(Reminder reminder : list){
            aNotifyStr = reminder.getNotify(timeNow);
            if(!aNotifyStr.equals("")){
                ret.append(aNotifyStr);
                ret.append("\r\n");
            }
        }
        sort();
        log.debug("Get reminders : " + ret.toString());
        return ret.toString();
    }

    private void sort(){
        Collections.sort(list);
    }

    public static ReminderMgr getInstance() {
        return instance;
    }
}
