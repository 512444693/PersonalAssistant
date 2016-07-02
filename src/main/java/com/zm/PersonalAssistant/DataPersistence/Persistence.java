package com.zm.PersonalAssistant.DataPersistence;

/**
 * Created by Administrator on 2016/7/2.
 */
public interface Persistence {
    public boolean changed();
    public void clearChanged();
}
