package com.zm.PersonalAssistant.Reminder;

import com.zm.PersonalAssistant.utils.LunarCalendar;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by zhangmin on 2016/6/22.
 */
public class ReminderMgrTest {
    public void testConstructor(){
        List<Reminder> list = new ArrayList();
        class ReminderStub extends Reminder{
            public ReminderStub(boolean lunar, LunarCalendar remindTime, Repeat repeat, String info, String advancedNotifyStr){
                super(lunar, remindTime, repeat, info, advancedNotifyStr);
            }
            @Override
            public String toString(){
                return this.getInfo();
            }
        }
        LunarCalendar timeToSchool = new LunarCalendar(false, 2016, 6, 22, 14, 0);
        Reminder reminder1 = new ReminderStub(false, timeToSchool.clone(), Repeat.DAY, "该上课了", "");
        list.add(reminder1);

        timeToSchool = new LunarCalendar(1991, 11, 16, 0, 0);
        Reminder reminder2 = new ReminderStub(false, timeToSchool.clone(), Repeat.DAY, "张三的生日", "");
        list.add(reminder2);

        timeToSchool = new LunarCalendar(1992, 11, 16, 0, 0);
        Reminder reminder3 = new ReminderStub(false, timeToSchool.clone(), Repeat.DAY, "李四的生日", "");
        list.add(reminder3);

        timeToSchool = new LunarCalendar(false, 2016, 6, 25, 0, 0);
        Reminder reminder4 = new ReminderStub(false, timeToSchool.clone(), Repeat.DAY, "该上班了", "");
        list.add(reminder4);

        System.out.println(list);
        Collections.sort(list);
        System.out.println(list);
        list.remove(reminder3);
        System.out.println(list);
    }

    //TODO: (多少分钟/小时/天/星期/月/年)后  按（阳历/农历） 重复（从不/每天/每周/每月/每年） 并且提前(几分钟/小时/天/周/月/从不)  提醒我(做什么)
    //TODO : (几年几月几号几时几分)           按（阳历/农历） 重复（从不/每天/每周/每月/每年） 并且提前(几分钟/小时/天/周/月/从不)  提醒我(做什么)


    //TODO : 添加，更新（得到通知） Reminder 列表顺序测试

    //TODO : 不重复提醒的一天后被删除

    //TODO : 得到几天内的Reminder列表

    //TODO : 得到接下来多少个Reminder列表

    //TODO : 得到所有Reminder

    @Test
    public void testGetNextDaysList(){

    }

}
