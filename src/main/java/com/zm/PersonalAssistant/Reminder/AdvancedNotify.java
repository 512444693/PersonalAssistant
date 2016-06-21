package com.zm.PersonalAssistant.Reminder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangmin on 2016/6/21.
 */
public class AdvancedNotify {
    public int num;
    public AdvancedUnit unit;
    public boolean alreadyNotify;

    public static List<AdvancedNotify> createList(String advancedNotifyStr) {
        List<AdvancedNotify> ret = new ArrayList<>();
        return ret;
    }

    //TODO : 提前通知测试，单个提前，多个一起提前等

    //TODO : 正式通知测试

    //TODO : 提前几 分钟、小时、天、月 通知测试

    //TODO : 永不，每天，每周 ，每月， 每年 重复测试

    //TODO : 添加，删除，更新（得到通知） Reminder 列表顺序测试

    //TODO : 得到几天内的Reminder列表
}
