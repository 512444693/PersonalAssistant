package com.zm.PersonalAssistant.Reminder;

import com.zm.PersonalAssistant.DataPersistence.Persistence;
import com.zm.PersonalAssistant.utils.LunarCalendar;
import static com.zm.PersonalAssistant.utils.Log.log;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by zhangmin on 2016/6/23.
 */
public class ReminderMgr implements Serializable,Persistence {

    private static final long serialVersionUID = 1L;

    private List<Reminder> list = new ArrayList<>();
    private transient static final ReminderMgr instance = new ReminderMgr();

    private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private final Lock readLock = readWriteLock.readLock();
    private final Lock writeLock = readWriteLock.writeLock();

    private int seq = 1000;
    private boolean changed = true;

    private ReminderMgr(){}

    public String toString(List<Reminder> listToDisplay){
        StringBuilder ret = new StringBuilder();
        Reminder reminder;
        readLock.lock();
        for(int i = 0; i < listToDisplay.size(); i++) {
            reminder = listToDisplay.get(i);
            ret.append(i).append(". ").append(reminder).append("\r\n");
        }
        readLock.unlock();
        return ret.toString();
    }

    public void add(Reminder reminder){
        writeLock.lock();
        reminder.setNumber(++seq);
        list.add(reminder);
        log.info("Add a reminder : " + reminder);
        sort();
        setChanged();
        writeLock.unlock();
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
                setChanged();
            }
            if(reminder.isDeletable()){
                reminderIterator.remove();
                setChanged();
                log.info("Remove a expired reminder : " + reminder);
            }
        }
        sort();
        writeLock.unlock();
        //log.debug("Get notifies : " + ret.toString());
        return ret.toString();
    }

    private void sort(){
        Collections.sort(list);
    }

    public static ReminderMgr getInstance() {
        return instance;
    }

    //for test
    protected List<Reminder> getAllReminders() {
        return list;
    }

    public String removeAccordingToNumber(int num){
        String ret = "";
        writeLock.lock();
        boolean found = false;
        for(Reminder reminder : list){
            if(reminder.getNumber() == num){
                found = true;
                list.remove(reminder);
                setChanged();
                log.info("Remove a reminder according to num " + num + " : " + reminder);
                ret = "成功删除提醒事项： " + reminder;
                break;
            }
        }
        if(!found){
            log.error("Remove reminder fail : Can not find a reminder whose number is " + num);
            throw new IllegalArgumentException("删除提醒事项失败，找不到编号为" + num + "的提醒事项");
        }
        writeLock.unlock();
        return ret;
    }

    public String getReminderStrNextDays(LunarCalendar timeNow, int days){
        List<Reminder> remindersInDays = new ArrayList<>();
        LunarCalendar sevenDayLater = new LunarCalendar(false, timeNow.getYear(), timeNow.getMonth(), timeNow.getDate());
        sevenDayLater.addDate(days);
        sevenDayLater.addMinute(-1);
        readLock.lock();
        for(Reminder reminder : this.list) {
            if(sevenDayLater.compareTo(reminder.getRemindTime()) >= 0 && timeNow.compareTo(reminder.getRemindTime()) <= 0){
                remindersInDays.add(reminder);
            }
        }
        String ret = toString(remindersInDays);
        readLock.unlock();
        log.debug("Get reminders in next " + days + "days : " + ret);
        return ret;
    }

    public String getReminderStrCount(int count){
        readLock.lock();
        String ret = toString(this.list.subList(0, count));
        readLock.unlock();
        log.debug("Get next " + count + " reminders: " + ret);
        return ret;
    }

    public String getAllReminderStr(){
        readLock.lock();
        String ret = toString(this.list);
        readLock.unlock();
        log.debug("Get all reminders: " + ret);
        return ret;
    }

    @Override
    public boolean changed() {
        return changed;
    }

    public void setChanged() {
        changed = true;
    }

    @Override
    public void clearChanged() {
        changed  = false;
    }
}
