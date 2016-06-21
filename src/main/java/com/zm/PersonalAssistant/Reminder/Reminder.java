package com.zm.PersonalAssistant.Reminder;

import com.zm.PersonalAssistant.utils.LunarCalendar;

import java.util.Date;
import java.util.List;

/**
 * Created by zhangmin on 2016/6/21.
 */
public class Reminder {
    private boolean lunar;
    private final LunarCalendar remindTime;
    private Repeat repeat;
    private String info;
    private List<AdvancedNotify> advancedNotifyList;
    private Date creationTime;

    public Reminder(boolean lunar, LunarCalendar remindTime, Repeat repeat, String info, String advancedNotifyStr) {
        this.lunar = lunar;
        this.remindTime = remindTime;
        this.repeat = repeat;
        this.info = info;
        advancedNotifyList = AdvancedNotify.createList(advancedNotifyStr);
        creationTime = new Date();
    }

    public boolean isLunar() {
        return lunar;
    }

    public LunarCalendar getRemindTime() {
        return remindTime;
    }

    public Repeat getRepeat() {
        return repeat;
    }

    public String getInfo() {
        return info;
    }

    public List<AdvancedNotify> getAdvancedNotifyList() {
        return advancedNotifyList;
    }
}
