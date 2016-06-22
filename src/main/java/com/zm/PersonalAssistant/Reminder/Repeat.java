package com.zm.PersonalAssistant.Reminder;

/**
 * Created by zhangmin on 2016/6/21.
 */
public enum Repeat {
    DAY("每天"),
    WEEK("每周"),
    MONTH("每月"),
    YEAR("每年"),
    NEVER("从不");

    private String describe;

    Repeat(String describe) { this.describe = describe; }

    public String getDescribe() { return this.describe; }
}
