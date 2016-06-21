package com.zm.PersonalAssistant.Reminder;

import java.util.ArrayList;
import java.util.List;

import static com.zm.PersonalAssistant.Reminder.AdvancedUnit.*;

/**
 * Created by zhangmin on 2016/6/21.
 */
public class AdvancedNotify {
    public int num;
    public AdvancedUnit unit;
    public boolean alreadyNotify;

    protected AdvancedNotify(int num, AdvancedUnit unit){
        this.num = num;
        this.unit = unit;
        if(num <= 0 || num > unit.getMaxNum())
            throw new IllegalArgumentException("Illegal Argument : Number is " + num + " and unit is " + unit);
        alreadyNotify = false;
    }

    //字符串格式为 "2/MINUTE;3/HOUR;4/DAY;5/WEEK;6/MONTH"
    public static List<AdvancedNotify> createList(String advancedNotifyStr) {
        List<AdvancedNotify> ret = new ArrayList<>();
        String[] notifyStrs = advancedNotifyStr.split(";");
        for(String notifyStr : notifyStrs){
            if(!notifyStr.equals("")){
                String[] fields = notifyStr.split("/");
                if(fields.length == 2) {
                    int num = Integer.parseInt(fields[0].trim());
                    AdvancedUnit unit = AdvancedUnit.valueOf(fields[1].trim().toUpperCase());
                    ret.add(new AdvancedNotify(num, unit));
                }else{
                    throw new IllegalArgumentException("Wrong Argument : " + advancedNotifyStr);
                }
            }
        }
        return ret;
    }
}
