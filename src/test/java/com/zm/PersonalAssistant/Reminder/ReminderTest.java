package com.zm.PersonalAssistant.Reminder;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.*;
import static org.junit.matchers.JUnitMatchers.containsString;

/**
 * Created by zhangmin on 2016/6/21.
 */
public class ReminderTest {
    @Test
    public void testConstructor(){
        //Arrange Act
        LunarCalendar timeBirthday = new LunarCalendar(1991, 11, 16);
        Reminder reminder = new Reminder(true, timeBirthday, Repeat.YEAR, "张三的生日", "2/day;4/day");

        //Assert
        assertEquals(true, reminder.isLunar());
        assertEquals(0, timeBirthday.compareTo(reminder.getRemindTime()));
        assertEquals(Repeat.YEAR, reminder.getRepeat());
        assertEquals("张三的生日", reminder.getInfo());
        assertEquals(2, reminder.getAdvancedNotifyList().size());
        assertFalse(reminder.isDeletable());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArg(){
        LunarCalendar timeBirthday = new LunarCalendar(1991, 11, 16);
        new Reminder(true, timeBirthday, Repeat.DAY, "张三的生日", "2/month;4/day");
    }

    @Test
    public void testBasic(){
        //Arrange
        LunarCalendar timeBirthday = new LunarCalendar(false, 2016, 12, 21);
        Reminder reminder = new Reminder(false, timeBirthday, Repeat.YEAR, "张三的生日", "1/month;1/day;1/hour");
        LunarCalendar timeNow = new LunarCalendar(false, 2016, 11, 20, 23, 59);

        //Act,Assert
        String notify = reminder.getNotify(timeNow);
        assertEquals("", notify);
        assertFalse(reminder.getAdvancedNotifyList().get(0).alreadyNotify);
        assertFalse(reminder.getAdvancedNotifyList().get(1).alreadyNotify);
        assertFalse(reminder.getAdvancedNotifyList().get(2).alreadyNotify);

        timeNow.addMinute(1);//now is 2016-11-21 00:00
        notify = reminder.getNotify(timeNow);
        assertFalse(notify.equals(""));
        assertTrue(reminder.getAdvancedNotifyList().get(0).alreadyNotify);
        assertFalse(reminder.getAdvancedNotifyList().get(1).alreadyNotify);
        assertFalse(reminder.getAdvancedNotifyList().get(2).alreadyNotify);

        timeNow.addDate(29);//now is 2016-12-20 00:00
        notify = reminder.getNotify(timeNow);
        assertFalse(notify.equals(""));
        assertTrue(reminder.getAdvancedNotifyList().get(0).alreadyNotify);
        assertTrue(reminder.getAdvancedNotifyList().get(1).alreadyNotify);
        assertFalse(reminder.getAdvancedNotifyList().get(2).alreadyNotify);

        timeNow.addHour(23);//now is 2016-12-20 23:00
        notify = reminder.getNotify(timeNow);
        assertFalse(notify.equals(""));
        assertTrue(reminder.getAdvancedNotifyList().get(0).alreadyNotify);
        assertTrue(reminder.getAdvancedNotifyList().get(1).alreadyNotify);
        assertTrue(reminder.getAdvancedNotifyList().get(2).alreadyNotify);

        timeNow.addHour(1);//now is 2016-12-21 00:00
        notify = reminder.getNotify(timeNow);
        assertFalse(notify.equals(""));
        assertFalse(reminder.getAdvancedNotifyList().get(0).alreadyNotify);
        assertFalse(reminder.getAdvancedNotifyList().get(1).alreadyNotify);
        assertFalse(reminder.getAdvancedNotifyList().get(2).alreadyNotify);
        assertFalse(reminder.isDeletable());
        timeNow.addYear(1);
        assertEquals(0, timeNow.compareTo(reminder.getRemindTime()));//正式通知后，按照每年重复，往后加一年
    }

    @Test
    public void testAdvancedNotify(){
        LunarCalendar timeBirthday = new LunarCalendar(2016, 11, 16);
        Reminder reminder = new Reminder(true, timeBirthday, Repeat.YEAR, "张三的生日", "1/month;1/day;1/hour;1/minute;1/week");
        LunarCalendar timeNow = new LunarCalendar(2016, 11, 13, 12, 10);
        String notify = reminder.getNotify(timeNow);
        assertFalse(notify.equals(""));
        assertTrue(reminder.getAdvancedNotifyList().get(0).alreadyNotify);
        assertFalse(reminder.getAdvancedNotifyList().get(1).alreadyNotify);
        assertFalse(reminder.getAdvancedNotifyList().get(2).alreadyNotify);
        assertFalse(reminder.getAdvancedNotifyList().get(3).alreadyNotify);
        assertTrue(reminder.getAdvancedNotifyList().get(4).alreadyNotify);
        notify = reminder.getNotify(timeNow);
        System.out.println(notify);
        assertTrue(notify.equals(""));

        timeNow = new LunarCalendar(2016, 11, 15, 23, 59);
        notify = reminder.getNotify(timeNow);
        assertFalse(notify.equals(""));
        assertTrue(reminder.getAdvancedNotifyList().get(0).alreadyNotify);
        assertTrue(reminder.getAdvancedNotifyList().get(1).alreadyNotify);
        assertTrue(reminder.getAdvancedNotifyList().get(2).alreadyNotify);
        assertTrue(reminder.getAdvancedNotifyList().get(3).alreadyNotify);
        assertTrue(reminder.getAdvancedNotifyList().get(4).alreadyNotify);

        assertFalse(reminder.isDeletable());
    }

    @Test
    public void testOfficialNotify(){
        LunarCalendar timeToSchool = new LunarCalendar(false, 2016, 6, 22, 14, 00);
        Reminder reminder = new Reminder(false, timeToSchool, Repeat.YEAR, "该上课了", "");
        LunarCalendar timeNow = new LunarCalendar(false, 2016, 6, 22, 11, 10);
        String notify = reminder.getNotify(timeNow);
        assertThat(notify, is(""));

        timeNow = new LunarCalendar(false, 2016, 6, 22, 15, 10);
        notify = reminder.getNotify(timeNow);
        assertThat(notify, is(not("")));
        assertEquals(0, new LunarCalendar(false, 2017, 6, 22, 14, 00).compareTo(reminder.getRemindTime()));
    }

    @Test
    public void testLunarMonthNotify(){
        LunarCalendar timeBirthday = new LunarCalendar(2017, 6, 20);//2017-7-13, 2017-8-11，农历都是2017-6-20
        Reminder reminder = new Reminder(true, timeBirthday, Repeat.YEAR, "张三的生日", "1/day");
        LunarCalendar timeNow = new LunarCalendar(false, 2017, 7, 13);
        String notify = reminder.getNotify(timeNow);
        assertThat(notify, containsString("下月是闰月"));
        assertEquals(0, new LunarCalendar(false, 2017, 8, 11).compareTo(reminder.getRemindTime()));

        //提前提醒
        timeNow = new LunarCalendar(false, 2017, 8, 10);
        notify = reminder.getNotify(timeNow);
        assertFalse(notify.equals(""));
        assertEquals(0, new LunarCalendar(false, 2017, 8, 11).compareTo(reminder.getRemindTime()));

        timeNow = new LunarCalendar(false, 2017, 8, 11);
        notify = reminder.getNotify(timeNow);
        assertThat(notify, containsString("本月是闰月"));
        assertEquals(0, new LunarCalendar(2018, 6, 20).compareTo(reminder.getRemindTime()));

        timeNow = new LunarCalendar(2018, 6, 20);
        notify = reminder.getNotify(timeNow);
        assertFalse(notify.equals(""));
        assertEquals(0, new LunarCalendar(2019, 6, 20).compareTo(reminder.getRemindTime()));
    }

    @Test
    public void testRepeatNever(){
        LunarCalendar timeBirthday = new LunarCalendar(2016, 11, 16);
        Reminder reminder = new Reminder(true, timeBirthday, Repeat.NEVER, "张三的生日", "1/day");
        LunarCalendar timeNow = new LunarCalendar(2016, 11, 16, 0, 1);
        String notify = reminder.getNotify(timeNow);
        assertThat(notify, is(not("")));
        assertEquals(0, new LunarCalendar(2016, 11, 16).compareTo(reminder.getRemindTime()));
        assertTrue(reminder.isDeletable());
    }

    @Test
    public void testRepeatDay(){
        LunarCalendar timeToSchool = new LunarCalendar(false, 2016, 6, 22, 14, 00);
        Reminder reminder = new Reminder(false, timeToSchool, Repeat.DAY, "该上课了", "");
        LunarCalendar timeNow = new LunarCalendar(false, 2016, 6, 22, 15, 10);
        String notify = reminder.getNotify(timeNow);
        assertThat(notify, is(not("")));
        assertFalse(reminder.isDeletable());
        assertEquals(0, new LunarCalendar(false, 2016, 6, 23, 14, 00).compareTo(reminder.getRemindTime()));
    }

    @Test
    public void testRepeatWeek(){
        LunarCalendar timeToSchool = new LunarCalendar(false, 2016, 6, 22, 14, 00);
        Reminder reminder = new Reminder(false, timeToSchool, Repeat.WEEK, "该上课了", "");
        LunarCalendar timeNow = new LunarCalendar(false, 2016, 6, 22, 15, 10);
        String notify = reminder.getNotify(timeNow);
        assertThat(notify, is(not("")));
        assertFalse(reminder.isDeletable());
        assertEquals(0, new LunarCalendar(false, 2016, 6, 29, 14, 00).compareTo(reminder.getRemindTime()));
    }

    @Test
    public void testRepeatMonth(){
        LunarCalendar timeToSchool = new LunarCalendar(false, 2016, 6, 22, 14, 00);
        Reminder reminder = new Reminder(false, timeToSchool, Repeat.MONTH, "该上课了", "");
        LunarCalendar timeNow = new LunarCalendar(false, 2016, 6, 22, 15, 10);
        String notify = reminder.getNotify(timeNow);
        assertThat(notify, is(not("")));
        assertFalse(reminder.isDeletable());
        assertEquals(0, new LunarCalendar(false, 2016, 7, 22, 14, 00).compareTo(reminder.getRemindTime()));
    }

    @Test
    public void testRepeatYear(){
        LunarCalendar timeToSchool = new LunarCalendar(false, 2016, 6, 22, 14, 00);
        Reminder reminder = new Reminder(false, timeToSchool, Repeat.YEAR, "该上课了", "");
        LunarCalendar timeNow = new LunarCalendar(false, 2016, 6, 22, 15, 10);
        String notify = reminder.getNotify(timeNow);
        assertThat(notify, is(not("")));
        assertFalse(reminder.isDeletable());
        assertEquals(0, new LunarCalendar(false, 2017, 6, 22, 14, 00).compareTo(reminder.getRemindTime()));
    }
}
