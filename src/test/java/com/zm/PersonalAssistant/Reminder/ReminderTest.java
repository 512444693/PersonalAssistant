package com.zm.PersonalAssistant.Reminder;

import com.zm.PersonalAssistant.utils.LunarCalendar;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by zhangmin on 2016/6/21.
 */
public class ReminderTest {
    @Test
    public void testConstructor(){
        //Arrange Act
        LunarCalendar timeBirthday = new LunarCalendar(1991, 11, 16);
        Reminder reminder = new Reminder(true, timeBirthday, Repeat.YEAR, "张三的生日", "2/day 1/hour");

        //Assert
        assertEquals(true, reminder.isLunar());
        assertEquals(0, timeBirthday.compareTo(reminder.getRemindTime()));
        assertEquals(Repeat.YEAR, reminder.getRepeat());
        assertEquals("张三的生日", reminder.getInfo());
        assertEquals(2, reminder.getAdvancedNotifyList().size());
    }
}
