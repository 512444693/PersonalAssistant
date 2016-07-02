package com.zm.PersonalAssistant.DataPersistence;

import com.zm.PersonalAssistant.Reminder.Reminder;
import com.zm.PersonalAssistant.Reminder.ReminderMgr;
import com.zm.PersonalAssistant.Reminder.Repeat;
import com.zm.PersonalAssistant.utils.LunarCalendar;
import static com.zm.PersonalAssistant.utils.Log.log;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;
import static org.junit.matchers.JUnitMatchers.containsString;

/**
 * Created by zhangmin on 2016/7/1.
 */
public class SerializeObjectTest {

    @BeforeClass
    public static void genFile() {
        ReminderMgr rm = ReminderMgr.getInstance();

        LunarCalendar timeToSchool = new LunarCalendar(false, 2016, 6, 22, 14, 0);
        Reminder reminder1 = new Reminder(false, timeToSchool.clone(), Repeat.DAY, "该上课了", "1/hour");
        rm.add(reminder1);
        reminder1.setNumber(123);
        timeToSchool = new LunarCalendar(1991, 11, 16, 0, 0);
        Reminder reminder2 = new Reminder(false, timeToSchool.clone(), Repeat.YEAR, "张三的生日", "");
        rm.add(reminder2);

        rm.getNotify(new LunarCalendar(false, 2016, 6, 22, 13, 0));

        SerializeObject.serialize(rm);
    }

    @AfterClass
    public static void cleanFile() {
        File file = new File(SerializeObject.DIRECTORY_PATH + ReminderMgr.class.getName());
        if(file.exists()) {
            file.delete();
        }
    }

    @Test
    public void testSerialize() {

        File file = new File(SerializeObject.DIRECTORY_PATH + ReminderMgr.class.getName());
        //Assert
        assertTrue(file.exists());
        assertTrue(file.isFile());
    }

    @Test
    public void testDeserialize() {
        //Arrange Act
        ReminderMgr rm2 = (ReminderMgr) SerializeObject.deserialize(ReminderMgr.class);

        //Assert
        assertThat(rm2.getAllReminderStr(), containsString("该上课了"));
        assertThat(rm2.getAllReminderStr(), containsString("张三的生日"));
    }

    @Test
    public void deserialize_object_is_not_equal_to_original_object() {
        //Arrange Act
        ReminderMgr rm2 = (ReminderMgr) SerializeObject.deserialize(ReminderMgr.class);

        //Assert
        assertTrue(ReminderMgr.getInstance() != rm2);
    }

    @Test
    public void deserialize_object_work_well() {
        //Arrange Act
        ReminderMgr rm2 = (ReminderMgr) SerializeObject.deserialize(ReminderMgr.class);

        LunarCalendar timeToSchool = new LunarCalendar(false, 2016, 7, 2, 14, 0);
        Reminder reminder1 = new Reminder(false, timeToSchool.clone(), Repeat.DAY, "该睡觉了", "1/hour");
        rm2.add(reminder1);

        String notify = rm2.getNotify(new LunarCalendar(false, 2016, 6, 22, 13, 0));
        assertThat(notify, containsString("张三的生日"));
        assertThat(notify, not(containsString("该上课了")));
        assertThat(notify, not(containsString("该睡觉了")));

        String allRemindersStr = rm2.getAllReminderStr();
        assertThat(allRemindersStr, containsString("张三的生日"));
        assertThat(allRemindersStr, containsString("该上课了"));
        assertThat(allRemindersStr, containsString("该睡觉了"));

        rm2.removeAccordingToNumber(123);
        allRemindersStr = rm2.getAllReminderStr();
        assertThat(allRemindersStr, containsString("张三的生日"));
        assertThat(allRemindersStr, containsString("该睡觉了"));
        assertThat(allRemindersStr, not(containsString("该上课了")));

    }

    @Test
    public void deserializeFail () {
        assertNull(SerializeObject.deserialize(this.getClass()));
    }
}