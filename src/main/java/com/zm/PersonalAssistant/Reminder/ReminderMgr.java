package com.zm.PersonalAssistant.Reminder;

import com.zm.PersonalAssistant.utils.LunarCalendar;
import org.apache.log4j.Logger;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by zhangmin on 2016/6/23.
 */
public class ReminderMgr {
    private Logger log = Logger.getLogger(ReminderMgr.class);
    private List<Reminder> list = new ArrayList<>();
    private static ReminderMgr instance = new ReminderMgr();

    private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private final Lock readLock = readWriteLock.readLock();
    private final Lock writeLock = readWriteLock.writeLock();

    private ReminderMgr(){}

    @Override
    public String toString(){
        StringBuilder ret = new StringBuilder();
        Reminder reminder;
        readLock.lock();
        for(int i = 0; i < list.size(); i++) {
            reminder = list.get(i);
            ret.append(i).append(". ").append(reminder).append("\r\n");
        }
        readLock.unlock();
        return ret.toString();
    }

    public void add(Reminder reminder){
        writeLock.lock();
        list.add(reminder);
        log.info("Add a reminder : " + reminder);
        writeLock.unlock();
        sort();
    }

    public void remove(int index) {
        writeLock.lock();
        if(index < 0 || index >= list.size()){
            log.error("Remove reminder fail：没有index为" + index + "的Reminder");
            throw new IllegalArgumentException("没有index为" + index + "的Reminder");
        }
        Reminder reminder = list.remove(index);
        writeLock.unlock();
        log.info("Remove a reminder : " + index + ". " + reminder);
    }

    public String getNotify(LunarCalendar timeNow){
        StringBuilder ret = new StringBuilder();
        String aNotifyStr = "";
        writeLock.lock();
        //遍历，删除过期的reminder，用迭代来删除以免出现问题
        Iterator<Reminder> reminderIterator = list.iterator();
        while (reminderIterator.hasNext()){
            Reminder reminder = reminderIterator.next();
            aNotifyStr = reminder.getNotify(timeNow);
            if(!aNotifyStr.equals("")){
                ret.append(aNotifyStr);
                ret.append("\r\n");
            }
            if(reminder.isDeletable()){
                reminderIterator.remove();
                log.info("Remove a expired reminder : " + reminder);
            }
        }
        writeLock.unlock();
        sort();
        log.debug("Get notifies : " + ret.toString());
        return ret.toString();
    }

    private void sort(){
        writeLock.lock();
        Collections.sort(list);
        writeLock.unlock();
    }

    public static ReminderMgr getInstance() {
        return instance;
    }

    //for test
    protected List<Reminder> getAllReminders() {
        return list;
    }
}
