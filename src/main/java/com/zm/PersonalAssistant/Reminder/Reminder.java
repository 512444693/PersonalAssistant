package com.zm.PersonalAssistant.Reminder;

import com.zm.PersonalAssistant.utils.LunarCalendar;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by zhangmin on 2016/6/21.
 */
public class Reminder implements Comparable<Reminder>,Serializable {
    private final boolean lunar;
    private LunarCalendar remindTime;
    private final Repeat repeat;
    private final String info;
    private List<AdvancedNotify> advancedNotifyList;
    private final Date creationTime;
    private boolean deletable;

    //编号
    private final int number;
    private static int seq = 1000;

    //程序内部使用
    private LunarCalendar tempSaveForNextMonthIsLeap;

    private static final String notifyFormat = "【%s】\r\n事件:【%s】\r\n时间:【%s】\r\n附加:【%s】";
    private static final String[] notifyLeapMonthInfo = {"无", "下月是闰月", "本月是闰月"};
    private static final String toStringFormat = "【编号：%s】 【按照：%s】 【时间：%s】 【重复：%s】 【提前：%s通知】 【信息：%s】 【添加时间：%s】 【是否失效：%s】";

    public Reminder(boolean lunar, LunarCalendar remindTime, Repeat repeat, String info, String advancedNotifyStr) {
        this.lunar = lunar;
        this.remindTime = remindTime;
        this.repeat = repeat;
        this.info = info;
        advancedNotifyList = AdvancedNotify.createList(advancedNotifyStr);
        verifyRepeatAndAdvancedUnitMatch();
        creationTime = new Date();
        deletable = false;
        tempSaveForNextMonthIsLeap = null;
        number = genNumber();
    }

    protected int genNumber() {
        return ++seq;
    }

    private void verifyRepeatAndAdvancedUnitMatch() {
        for(AdvancedNotify aNotify : advancedNotifyList){
            if(repeat.ordinal() < (aNotify.unit.ordinal() - 1)){
                throw new IllegalArgumentException("Repeat is " + repeat + " and advanced unit is " + aNotify.unit);
            }
        }
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
    public boolean isDeletable() {
        return deletable;
    }

    public String getNotify(LunarCalendar timeNow) {
        String ret = "";
        if(!deletable){
            if(this.compare(timeNow, remindTime) >= 0){
                ret =  genNotifyStr("正式通知");
                resetAdvancedNotifyList();
                repeat();
            }else {
                for(AdvancedNotify aNotify : advancedNotifyList){
                    if(this.compare(timeNow, getAdvancedTime(aNotify)) >= 0){
                        ret = genNotifyStr("提前通知");
                        aNotify.alreadyNotify = true;
                    }
                }
            }
        }
        return ret;
    }

    private String genNotifyStr(String prefix){
        return String.format(notifyFormat, prefix, info, remindTime, notifyLeapMonthInfo[remindTime.isLeapMonth()]);
    }

    private void repeat() {
        //若本月的下一个月是闰月，则直接加到下一个月
        if(isLunar() && remindTime.isLeapMonth() == 1){
            tempSaveForNextMonthIsLeap = remindTime;
            remindTime.addChineseMonth(1);
            return;
        }else if(isLunar() && remindTime.isLeapMonth() == 2){
            if(tempSaveForNextMonthIsLeap != null){
                remindTime = tempSaveForNextMonthIsLeap;
            }
            tempSaveForNextMonthIsLeap = null;
        }
        switch (repeat){
            case NEVER:
                deletable = true; break;
            case DAY:
                remindTime.addDate(1); break;
            case WEEK:
                remindTime.addWeek(1); break;
            case MONTH:
                if(isLunar()){
                    remindTime.addChineseMonth(1);
                }else {
                    remindTime.addMonth(1);
                }
                break;
            case YEAR:
                if(isLunar()){
                    remindTime.addChineseYear(1);
                }else {
                    remindTime.addYear(1);
                }
                break;
        }
    }

    private void resetAdvancedNotifyList(){
        for(AdvancedNotify aNotify : advancedNotifyList){
            aNotify.alreadyNotify = false;
        }
    }

    //提前提醒时间全部按照阳历来算
    private LunarCalendar getAdvancedTime(AdvancedNotify aNotify){
        LunarCalendar clone = remindTime.clone();
        switch (aNotify.unit){
            case MINUTE:
                clone.addMinute(-aNotify.num); break;
            case HOUR:
                clone.addHour(-aNotify.num); break;
            case DAY:
                clone.addDate(-aNotify.num); break;
            case WEEK:
                clone.addWeek(-aNotify.num); break;
            case MONTH:
                clone.addMonth(-aNotify.num); break;
        }
        return clone;
    }

    private int compare(LunarCalendar i, LunarCalendar o){
        if(isLunar()){
            return i.lunarCompareTo(o);
        }else {
            return i.compareTo(o);
        }
    }

    @Override
    public int compareTo(Reminder o) {
        return this.remindTime.compareTo(o.getRemindTime());
    }

    @Override
    public String toString(){
        return String.format(toStringFormat, getNumber(), isLunar() ? "农历" : "阳历", remindTime.toString(),
                repeat.getDescribe(), getAdvancedNotifyStr(), info,
                creationTime.toString(), isDeletable() ? "是" : "否");
    }

    private String getAdvancedNotifyStr(){
        StringBuilder ret = new StringBuilder();
        for(AdvancedNotify aNotify : advancedNotifyList){
            ret.append(aNotify.num).append(aNotify.unit.getDescribe()).append(" ");
        }
        return ret.toString();
    }

    public int getNumber() {
        return number;
    }
}
