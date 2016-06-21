package com.zm.PersonalAssistant.Reminder;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by Administrator on 2016/6/21.
 */
public class AdvancedNotifyTest {
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorIllegalArgMinute(){
        new AdvancedNotify(60, AdvancedUnit.MINUTE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorIllegalArgHour(){
        new AdvancedNotify(24, AdvancedUnit.HOUR);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorIllegalArgDay(){
        new AdvancedNotify(30, AdvancedUnit.DAY);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorIllegalArgWeek(){
        new AdvancedNotify(5, AdvancedUnit.WEEK);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorIllegalArgMonth(){
        new AdvancedNotify(12, AdvancedUnit.MONTH);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorIllegalArgNumIsNegtive(){
        new AdvancedNotify(-2, AdvancedUnit.MONTH);
    }

    @Test
    public void testCreateList(){
        List<AdvancedNotify> list = AdvancedNotify.createList("2/MINUTE;3/hour;4/DAY;1/Week;6/MONTH");

        assertEquals(5, list.size());
        assertEquals(2, list.get(0).num);
        assertEquals(AdvancedUnit.MINUTE, list.get(0).unit);
        assertEquals(3, list.get(1).num);
        assertEquals(AdvancedUnit.HOUR, list.get(1).unit);
        assertEquals(4, list.get(2).num);
        assertEquals(AdvancedUnit.DAY, list.get(2).unit);
        assertEquals(1, list.get(3).num);
        assertEquals(AdvancedUnit.WEEK, list.get(3).unit);
        assertEquals(6, list.get(4).num);
        assertEquals(AdvancedUnit.MONTH, list.get(4).unit);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateListIllegalArgBeyondMaxNum(){
        List<AdvancedNotify> list = AdvancedNotify.createList("999/MINUTE;3/hour");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateListIllegalArgWrongStr(){
        List<AdvancedNotify> list = AdvancedNotify.createList("ddd;123");
    }

    public void testCreateListEmptyStr(){
        List<AdvancedNotify> list = AdvancedNotify.createList("");
        assertEquals(0, list.size());
    }
}
