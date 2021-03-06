package com.zm.PersonalAssistant.Reminder;

import org.junit.Before;
import org.junit.Test;
import java.util.*;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.matchers.JUnitMatchers.containsString;

/**
 * Created by zhangmin on 2016/6/22.
 */
public class ReminderMgrTest {
    @Before
    public void resetReminderMgr(){
        ReminderMgr rm = ReminderMgr.getInstance();
        List<Reminder> list = rm.getAllReminders();
        list.clear();
    }

    @Test
    public void testGetAllReminders(){
        ReminderMgr rm = ReminderMgr.getInstance();
        List<Reminder> list = rm.getAllReminders();
        assertEquals(0, list.size());

        LunarCalendar timeToSchool = new LunarCalendar(false, 2016, 6, 22, 14, 0);
        Reminder reminder1 = new Reminder(true, timeToSchool.clone(), Repeat.DAY, "该上课了", "1/hour");
        rm.add(reminder1);
        assertEquals(1, list.size());
    }

    @Test
    public void testAddAndOrder(){
        //Arrange
        ReminderMgr rm = ReminderMgr.getInstance();

        //Act
        LunarCalendar timeToSchool = new LunarCalendar(false, 2016, 6, 22, 14, 0);
        Reminder reminder1 = new Reminder(true, timeToSchool.clone(), Repeat.DAY, "该上课了", "1/hour");
        rm.add(reminder1);

        timeToSchool = new LunarCalendar(1991, 11, 16, 0, 0);
        Reminder reminder2 = new Reminder(false, timeToSchool.clone(), Repeat.DAY, "张三的生日", "");
        rm.add(reminder2);

        timeToSchool = new LunarCalendar(1992, 11, 16, 0, 0);
        Reminder reminder3 = new Reminder(false, timeToSchool.clone(), Repeat.DAY, "李四的生日", "");
        rm.add(reminder3);

        timeToSchool = new LunarCalendar(false, 2016, 6, 25, 0, 0);
        Reminder reminder4 = new Reminder(false, timeToSchool.clone(), Repeat.DAY, "该上班了", "");
        rm.add(reminder4);

        List<Reminder> list = rm.getAllReminders();
        //Assert
        assertEquals(4, list.size());
        testOrder(list);
    }

    @Test
    public void testRemoveAndOrder(){
        //Arrange
        ReminderMgr rm = ReminderMgr.getInstance();

        //Act
        LunarCalendar timeToSchool = new LunarCalendar(false, 2016, 6, 22, 14, 0);
        Reminder reminder1 = new Reminder(true, timeToSchool.clone(), Repeat.DAY, "该上课了", "1/hour");
        rm.add(reminder1);

        timeToSchool = new LunarCalendar(1991, 11, 16, 0, 0);
        Reminder reminder2 = new Reminder(false, timeToSchool.clone(), Repeat.DAY, "张三的生日", "");
        rm.add(reminder2);

        timeToSchool = new LunarCalendar(1992, 11, 16, 0, 0);
        Reminder reminder3 = new Reminder(false, timeToSchool.clone(), Repeat.DAY, "李四的生日", "");
        rm.add(reminder3);

        timeToSchool = new LunarCalendar(false, 2016, 6, 25, 0, 0);
        Reminder reminder4 = new Reminder(false, timeToSchool.clone(), Repeat.DAY, "该上班了", "");
        rm.add(reminder4);

        reminder1.setNumber(13);
        List<Reminder> list = rm.getAllReminders();
        rm.removeAccordingToNumber(13);
        assertEquals(3, list.size());
        testOrder(list);

        reminder2.setNumber(14);
        rm.removeAccordingToNumber(14);
        assertEquals(2, list.size());
        testOrder(list);
    }


    @Test(expected = IllegalArgumentException.class)
    public void testRemoveIllegalNoReminders(){
        ReminderMgr rm = ReminderMgr.getInstance();
        rm.removeAccordingToNumber(11);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveIllegal(){
        //Arrange
        ReminderMgr rm = ReminderMgr.getInstance();

        LunarCalendar timeToSchool = new LunarCalendar(false, 2016, 6, 22, 14, 0);
        Reminder reminder1 = new Reminder(true, timeToSchool.clone(), Repeat.DAY, "该上课了", "1/hour");
        rm.add(reminder1);

        timeToSchool = new LunarCalendar(1991, 11, 16, 0, 0);
        Reminder reminder2 = new Reminder(false, timeToSchool.clone(), Repeat.DAY, "张三的生日", "");
        rm.add(reminder2);

        timeToSchool = new LunarCalendar(1992, 11, 16, 0, 0);
        Reminder reminder3 = new Reminder(false, timeToSchool.clone(), Repeat.DAY, "李四的生日", "");
        rm.add(reminder3);

        timeToSchool = new LunarCalendar(false, 2016, 6, 25, 0, 0);
        Reminder reminder4 = new Reminder(false, timeToSchool.clone(), Repeat.DAY, "该上班了", "");
        rm.add(reminder4);

        //Act
        rm.removeAccordingToNumber(5);
    }

    @Test
    public void testGetNotifyAndOrder(){
        //Arrange
        ReminderMgr rm = ReminderMgr.getInstance();

        //Act
        LunarCalendar timeToSchool = new LunarCalendar(false, 2016, 6, 22, 14, 0);
        Reminder reminder1 = new Reminder(false, timeToSchool.clone(), Repeat.DAY, "该上课了", "1/hour");
        rm.add(reminder1);

        timeToSchool = new LunarCalendar(1991, 11, 16, 0, 0);
        Reminder reminder2 = new Reminder(false, timeToSchool.clone(), Repeat.NEVER, "张三的生日", "");
        rm.add(reminder2);

        timeToSchool = new LunarCalendar(1992, 11, 16, 0, 0);
        Reminder reminder3 = new Reminder(false, timeToSchool.clone(), Repeat.DAY, "李四的生日", "");
        rm.add(reminder3);

        timeToSchool = new LunarCalendar(false, 2016, 6, 25, 0, 0);
        Reminder reminder4 = new Reminder(false, timeToSchool.clone(), Repeat.DAY, "该上班了", "");
        rm.add(reminder4);

        List<Reminder> list = rm.getAllReminders();

        String notify = rm.getNotify(new LunarCalendar(false, 2000, 1, 1));
        assertThat(notify, containsString("张三"));
        assertThat(notify, containsString("李四"));

        //Assert
        assertEquals(3, list.size());
        testOrder(list);
    }

    private void testOrder(List<Reminder> list){
        Reminder one,next;
        for(int i = 0; i < (list.size() - 1); i++) {
            one = list.get(i);
            next = list.get(i + 1);
            assertTrue(next.compareTo(one) >= 0);
        }
    }

    @Test
    public void testGetAllReminderStr(){
        //Arrange
        ReminderMgr rm = ReminderMgr.getInstance();

        //Act
        LunarCalendar timeToSchool = new LunarCalendar(false, 2016, 6, 22, 14, 0);
        Reminder reminder1 = new Reminder(false, timeToSchool.clone(), Repeat.DAY, "该上课了", "1/hour");
        rm.add(reminder1);

        timeToSchool = new LunarCalendar(1991, 11, 16, 0, 0);
        Reminder reminder2 = new Reminder(false, timeToSchool.clone(), Repeat.NEVER, "张三的生日", "");
        rm.add(reminder2);

        timeToSchool = new LunarCalendar(1992, 11, 16, 0, 0);
        Reminder reminder3 = new Reminder(false, timeToSchool.clone(), Repeat.DAY, "李四的生日", "");
        rm.add(reminder3);

        timeToSchool = new LunarCalendar(false, 2016, 6, 25, 0, 0);
        Reminder reminder4 = new Reminder(false, timeToSchool.clone(), Repeat.DAY, "该上班了", "");
        rm.add(reminder4);

        //Assert
        assertThat(rm.getAllReminderStr(), containsString("该上课了"));
        assertThat(rm.getAllReminderStr(), containsString("张三的生日"));
        assertThat(rm.getAllReminderStr(), containsString("李四的生日"));
        assertThat(rm.getAllReminderStr(), containsString("该上班了"));
    }

    @Test
    public void testGetReminderStrCount(){
        //Arrange
        ReminderMgr rm = ReminderMgr.getInstance();

        //Act
        LunarCalendar timeToSchool = new LunarCalendar(false, 2016, 6, 22, 14, 0);
        Reminder reminder1 = new Reminder(false, timeToSchool.clone(), Repeat.DAY, "该上课了", "1/hour");
        rm.add(reminder1);

        timeToSchool = new LunarCalendar(1991, 11, 16, 0, 0);
        Reminder reminder2 = new Reminder(false, timeToSchool.clone(), Repeat.NEVER, "张三的生日", "");
        rm.add(reminder2);

        timeToSchool = new LunarCalendar(1992, 11, 16, 0, 0);
        Reminder reminder3 = new Reminder(false, timeToSchool.clone(), Repeat.DAY, "李四的生日", "");
        rm.add(reminder3);

        timeToSchool = new LunarCalendar(false, 2016, 6, 25, 0, 0);
        Reminder reminder4 = new Reminder(false, timeToSchool.clone(), Repeat.DAY, "该上班了", "");
        rm.add(reminder4);

        //Assert
        assertThat(rm.getReminderStrCount(2), containsString("张三的生日"));
        assertThat(rm.getReminderStrCount(2), containsString("李四的生日"));
        assertThat(rm.getReminderStrCount(2), not(containsString("该上课了")));
        assertThat(rm.getReminderStrCount(2), not(containsString("该上班了")));

        assertThat(rm.getReminderStrCount(0), is(""));
    }

    @Test
    public void testGetReminderStrNextDays(){
        //Arrange
        ReminderMgr rm = ReminderMgr.getInstance();

        //Act
        LunarCalendar timeToSchool = new LunarCalendar(false, 2016, 6, 26, 0, 0);
        Reminder reminder1 = new Reminder(false, timeToSchool.clone(), Repeat.DAY, "该上课了", "1/hour");
        rm.add(reminder1);

        timeToSchool = new LunarCalendar(false, 2016, 6, 25, 23, 59);
        Reminder reminder2 = new Reminder(false, timeToSchool.clone(), Repeat.NEVER, "张三的生日", "");
        rm.add(reminder2);

        timeToSchool = new LunarCalendar(false, 2016, 6, 22, 14, 57);
        Reminder reminder3 = new Reminder(false, timeToSchool.clone(), Repeat.DAY, "李四的生日", "");
        rm.add(reminder3);

        timeToSchool = new LunarCalendar(false, 2016, 6, 22, 14, 0);
        Reminder reminder4 = new Reminder(false, timeToSchool.clone(), Repeat.DAY, "该上班了", "");
        rm.add(reminder4);

        //Assert
        LunarCalendar timeNow = new LunarCalendar(false, 2016, 6, 22, 14, 57);
        assertThat(rm.getReminderStrNextDays(timeNow, 4),containsString("张三的生日"));
        assertThat(rm.getReminderStrNextDays(timeNow, 4),containsString("李四的生日"));
        assertThat(rm.getReminderStrNextDays(timeNow, 4),not(containsString("该上课了")));
        assertThat(rm.getReminderStrNextDays(timeNow, 4),not(containsString("该上班了")));
        assertThat(rm.getReminderStrNextDays(timeNow, 0), is(""));
    }
}
