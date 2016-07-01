package com.zm.PersonalAssistant.DataPersistence;

import com.zm.PersonalAssistant.Reminder.Reminder;
import com.zm.PersonalAssistant.Reminder.ReminderMgr;
import com.zm.PersonalAssistant.Reminder.Repeat;
import com.zm.PersonalAssistant.utils.LunarCalendar;
import org.junit.Test;

import java.io.File;

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
        ReminderMgr rm = ReminderMgr.getInstance();
        LunarCalendar timeToSchool = new LunarCalendar(false, 2016, 6, 22, 14, 0);
        Reminder reminder1 = new Reminder(false, timeToSchool.clone(), Repeat.DAY, "该上课了", "1/hour");
        rm.add(reminder1);
        timeToSchool = new LunarCalendar(1991, 11, 16, 0, 0);
        Reminder reminder2 = new Reminder(false, timeToSchool.clone(), Repeat.NEVER, "张三的生日", "");
        rm.add(reminder2);

        File file = new File("com.zm.PersonalAssistant.Reminder.ReminderMgr");

        //Act
        SerializeObject.serialize(rm);

        //Assert
        assertTrue(file.exists());
        assertTrue(file.isFile());
    }

    @Test
    public void testDeserialize() {
        //Arrange
        ReminderMgr rm = ReminderMgr.getInstance();

        //Act
        ReminderMgr rm2 = (ReminderMgr) SerializeObject.deserialize(ReminderMgr.class);

        //Assert
        assertEquals(rm, rm2);
        assertThat(rm2.getAllReminderStr(), containsString("该上课了"));
        assertThat(rm2.getAllReminderStr(), containsString("张三的生日"));
    }


}
