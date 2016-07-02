package com.zm.PersonalAssistant.DataPersistence;

import com.zm.PersonalAssistant.Reminder.Reminder;
import com.zm.PersonalAssistant.Reminder.ReminderMgr;
import com.zm.PersonalAssistant.Reminder.Repeat;
import com.zm.PersonalAssistant.utils.LunarCalendar;
import static com.zm.PersonalAssistant.utils.Log.log;
import org.junit.Test;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.matchers.JUnitMatchers.containsString;

/**
 * Created by zhangmin on 2016/7/1.
 */
public class SerializeObjectTest {

    @Test
    public void testSerialize() {
        //Arrange
        ReminderMgrStub rm = new ReminderMgrStub();

        LunarCalendar timeToSchool = new LunarCalendar(false, 2016, 6, 22, 14, 0);
        Reminder reminder1 = new Reminder(false, timeToSchool.clone(), Repeat.DAY, "该上课了", "1/hour");
        rm.add(reminder1);
        timeToSchool = new LunarCalendar(1991, 11, 16, 0, 0);
        Reminder reminder2 = new Reminder(false, timeToSchool.clone(), Repeat.YEAR, "张三的生日", "");
        rm.add(reminder2);

        rm.getNotify(new LunarCalendar(false, 2016, 6, 22, 13, 0));

        File file = new File(rm.getClass().getName());

        //Act
        SerializeObject.serialize(rm);

        //Assert
        assertTrue(file.exists());
        assertTrue(file.isFile());
    }

    @Test
    public void testDeserialize() {
        //Arrange
        //ReminderMgr rm = ReminderMgr.getInstance();

        //Act
        ReminderMgrStub rm2 = (ReminderMgrStub) SerializeObject.deserialize(ReminderMgrStub.class);

        //Assert
        //assertEquals(rm, rm2);
        assertThat(rm2.getAllReminderStr(), containsString("该上课了"));
        assertThat(rm2.getAllReminderStr(), containsString("张三的生日"));
    }

    //TODO : 反序列化后进行各种功能正确， 加入， 更新， 删除等
}

class ReminderMgrStub implements Serializable{
    private List<Reminder> list = new ArrayList<>();

    private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private final Lock readLock = readWriteLock.readLock();
    private final Lock writeLock = readWriteLock.writeLock();

    private int seq = 1000;

    public ReminderMgrStub(){}

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
            }
            if(reminder.isDeletable()){
                reminderIterator.remove();
                log.info("Remove a expired reminder : " + reminder);
            }
        }
        sort();
        writeLock.unlock();
        log.debug("Get notifies : " + ret.toString());
        return ret.toString();
    }

    private void sort(){
        Collections.sort(list);
    }

    //for test
    protected List<Reminder> getAllReminders() {
        return list;
    }

    public void removeAccordingToNumber(int num){
        writeLock.lock();
        boolean found = false;
        for(Reminder reminder : list){
            if(reminder.getNumber() == num){
                found = true;
                list.remove(reminder);
                log.info("Remove a reminder according to num " + num + " : " + reminder);
                break;
            }
        }
        if(!found){
            log.error("Remove reminder fail : Can not find a reminder whose number is " + num);
            throw new IllegalArgumentException("Remove reminder fail : Can not find a reminder whose number is " + num);
        }
        writeLock.unlock();
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
}
