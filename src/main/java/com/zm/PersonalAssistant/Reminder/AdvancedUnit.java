package com.zm.PersonalAssistant.Reminder;

/**
 * Created by zhangmin on 2016/6/21.
 */
public enum AdvancedUnit {
    MINUTE("分钟") { public int getMaxNum() { return 59; } },
    HOUR("小时") { public int getMaxNum() { return 23; } },
    DAY("天") { public int getMaxNum() { return 29; } },
    WEEK("星期") { public int getMaxNum() { return 4; } },
    MONTH("月") { public int getMaxNum() { return 11; } };

    private String describe;

    AdvancedUnit(String describe) { this.describe = describe; }

    public String getDescribe() { return this.describe; }

    public int getMaxNum(){
        return 0;
    }
}
