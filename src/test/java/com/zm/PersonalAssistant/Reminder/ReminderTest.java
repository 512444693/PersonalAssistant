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

//TODO : 提前通知测试，单个提前，多个一起提前等

//TODO : 正式通知测试

//TODO : 提前几 分钟、小时、天、月 通知测试

//TODO : 永不，每天，每周 ，每月， 每年 重复测试

//TODO : 添加，删除，更新（得到通知） Reminder 列表顺序测试

//TODO : 得到几天内的Reminder列表
