package com.zm.PersonalAssistant.Reminder;

import com.zm.PersonalAssistant.Reminder.LunarCalendar;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by zhangmin on 2016/6/18.
 */
public class LunarCalendarTest {

    //依次为农历年月日，阳历年月日
    private static final int NORMAL[] = {2016, 3, 29, 2016, 5, 5};
    private static final int LUNAR_END_OF_YEAR[] = {2016, 12, 30, 2017, 1, 27};
    private static final int NEXT_MONTH_IS_LEAP[] = {2017, 6, 5, 2017, 6, 28};
    private static final int LEAP_MONTH[] = {2017, 6, 5, 2017, 7, 27};

    private void verifyLunarCalendar(LunarCalendar lc, int[] data){
        assertEquals(data[0], lc.getChineseYear());
        assertEquals(data[1], lc.getChineseMonth());
        assertEquals(data[2], lc.getChineseDate());
        assertEquals(data[3], lc.getYear());
        assertEquals(data[4], lc.getMonth());
        assertEquals(data[5], lc.getDate());
    }

    @Test
    public void testLunarConstructor(){
        //Arrange
        //Act
        LunarCalendar lc = new LunarCalendar(NORMAL[0], NORMAL[1], NORMAL[2]);

        //Assert
        verifyLunarCalendar(lc, NORMAL);
    }

    @Test
    public void testFourArgsConstructor(){
        //Arrange
        //Act
        LunarCalendar lc = new LunarCalendar(true , NORMAL[0], NORMAL[1], NORMAL[2]);
        LunarCalendar lc2 = new LunarCalendar(false , NORMAL[3], NORMAL[4], NORMAL[5]);

        //Assert
        verifyLunarCalendar(lc, NORMAL);

        verifyLunarCalendar(lc2, NORMAL);
    }
    @Test
    public void testDefaultConstructor(){
        //Arrange
        //Act
        LunarCalendar lc = new LunarCalendar();
        GregorianCalendar gc = new GregorianCalendar();

        //Assert
        assertEquals(gc.get(GregorianCalendar.YEAR), lc.getYear());
        assertEquals(gc.get(GregorianCalendar.MONTH) + 1, lc.getMonth());
        assertEquals(gc.get(GregorianCalendar.DATE), lc.getDate());
    }

    @Test
    public void testLunarCalendarEndOfTheYear(){
        //Arrange
        //Act
        LunarCalendar lc = new LunarCalendar(LUNAR_END_OF_YEAR[0], LUNAR_END_OF_YEAR[1], LUNAR_END_OF_YEAR[2]);

        //Assert
        verifyLunarCalendar(lc, LUNAR_END_OF_YEAR);
    }

    @Test
    public void testGregorianCalendarBeginOfTheYear(){
        //Arrange
        //Act
        LunarCalendar lc = new LunarCalendar(false, LUNAR_END_OF_YEAR[3], LUNAR_END_OF_YEAR[4], LUNAR_END_OF_YEAR[5]);

        //Assert
        verifyLunarCalendar(lc, LUNAR_END_OF_YEAR);
    }

    @Test
    public void testLeapMonth(){
        //Arrange
        //Act
        LunarCalendar lc = new LunarCalendar(false, NEXT_MONTH_IS_LEAP[3], NEXT_MONTH_IS_LEAP[4], NEXT_MONTH_IS_LEAP[5]);
        LunarCalendar lc2 = new LunarCalendar(false, LEAP_MONTH[3], LEAP_MONTH[4], LEAP_MONTH[5]);
        LunarCalendar lc3 = new LunarCalendar(false , NORMAL[3], NORMAL[4], NORMAL[5]);


        //Assert
        verifyLunarCalendar(lc, NEXT_MONTH_IS_LEAP);
        verifyLunarCalendar(lc2, LEAP_MONTH);
        assertEquals(0 ,lc3.isLeapMonth());
        assertEquals(1 ,lc.isLeapMonth());
        assertEquals(2 ,lc2.isLeapMonth());
    }

    @Test
    public void testAddDate(){
        //Arrange
        LunarCalendar lc = new LunarCalendar(NORMAL[0], NORMAL[1], NORMAL[2]);
        LunarCalendar lc2 = new LunarCalendar(NORMAL[0], NORMAL[1], NORMAL[2]);
        LunarCalendar lc3 = new LunarCalendar(NORMAL[0], NORMAL[1], NORMAL[2]);
        LunarCalendar lc4 = new LunarCalendar(NORMAL[0], NORMAL[1], NORMAL[2]);

        //Act
        lc.addDate(0);
        lc2.addDate(1);
        lc3.addDate(27);
        lc4.addDate(365);

        //Assert
        verifyLunarCalendar(lc, NORMAL);
        int[] tmp = {NORMAL[0], NORMAL[1], NORMAL[2] + 1, NORMAL[3], NORMAL[4], NORMAL[5] + 1};
        verifyLunarCalendar(lc2, tmp);
        int[] tmp2 = {NORMAL[0], NORMAL[1] + 1, (NORMAL[2] + 27) % 30, NORMAL[3], NORMAL[4] + 1, (NORMAL[5] + 27) % 31};
        verifyLunarCalendar(lc3, tmp2);
        int[] tmp3 = {NORMAL[0] + 1, 4, 10, NORMAL[3] + 1, NORMAL[4], NORMAL[5]};
        verifyLunarCalendar(lc4, tmp3);
    }

    @Test
    public void testToString() {
        LunarCalendar lc = new LunarCalendar(1991, 11, 16);
        assertTrue(lc.toString() != null);
        assertTrue(!lc.toString().equals(""));
    }

    @Test
    public void testHasHourMinuteConstructor(){
        //Arrange,Act
        LunarCalendar lc = new LunarCalendar(NORMAL[0], NORMAL[1], NORMAL[2], 0, 0);
        LunarCalendar lc2 = new LunarCalendar(true, NORMAL[0], NORMAL[1], NORMAL[2], 23, 59);
        LunarCalendar lc3 = new LunarCalendar(false, NORMAL[3], NORMAL[4], NORMAL[5], 11, 21);
        LunarCalendar lc4 = new LunarCalendar();

        //Assert
        verifyLunarCalendar(lc, NORMAL);
        verifyLunarCalendar(lc2, NORMAL);
        System.out.println(lc4);
        verifyLunarCalendar(lc3, NORMAL);
        assertEquals(0, lc.getHour());
        assertEquals(0, lc.getMinute());
        assertEquals(23, lc2.getHour());
        assertEquals(59, lc2.getMinute());
        assertEquals(11, lc3.getHour());
        assertEquals(21, lc3.getMinute());
        assertTrue(lc.getHour() <= 23);
        assertTrue(lc.getHour() >= 0);
        assertTrue(lc.getMinute() <= 59);
        assertTrue(lc.getHour() >= 0);
    }

    @Test
    public void testAddHour(){
        //Arrange
        LunarCalendar lc = new LunarCalendar(NORMAL[0], NORMAL[1], NORMAL[2], 0, 0);
        LunarCalendar lc2 = new LunarCalendar(false, NORMAL[3], NORMAL[4], NORMAL[5], 11, 21);

        //Act
        lc.addHour(13);
        lc2.addHour(15);

        //Assert
        assertEquals(13, lc.getHour());
        assertEquals(0, lc.getMinute());
        verifyLunarCalendar(lc, NORMAL);

        assertEquals(2, lc2.getHour());
        assertEquals(21, lc2.getMinute());
        verifyLunarCalendar(lc2, new int[]{NORMAL[0], NORMAL[1], NORMAL[2] + 1, NORMAL[3], NORMAL[4], NORMAL[5] + 1});
    }

    @Test
    public void testAddMinute(){
        //Arrange
        LunarCalendar lc = new LunarCalendar(NORMAL[0], NORMAL[1], NORMAL[2], 0, 0);
        LunarCalendar lc2 = new LunarCalendar(false, NORMAL[3], NORMAL[4], NORMAL[5], 23, 59);

        //Act
        lc.addMinute(13);
        lc2.addMinute(15);

        //Assert
        assertEquals(0, lc.getHour());
        assertEquals(13, lc.getMinute());
        verifyLunarCalendar(lc, NORMAL);

        assertEquals(0, lc2.getHour());
        assertEquals(14, lc2.getMinute());
        verifyLunarCalendar(lc2, new int[]{NORMAL[0], NORMAL[1], NORMAL[2] + 1, NORMAL[3], NORMAL[4], NORMAL[5] + 1});
    }

    @Test
    public void testAddWeek(){
        //Arrange
        LunarCalendar lc = new LunarCalendar(NORMAL[0], NORMAL[1], NORMAL[2]);
        LunarCalendar lc2 = new LunarCalendar(NORMAL[0], NORMAL[1], NORMAL[2]);

        //Act
        lc.addWeek(2);
        lc2.addWeek(-2);

        //Assert
        verifyLunarCalendar(lc, new int[]{NORMAL[0], 4, 13, NORMAL[3], NORMAL[4], 19});
        verifyLunarCalendar(lc2, new int[]{NORMAL[0], 3, 15, NORMAL[3], 4, 21});
    }

    @Test
    public void testAddChineseMonth(){
        //Arrange
        LunarCalendar lc = new LunarCalendar(NORMAL[0], NORMAL[1], NORMAL[2]);
        LunarCalendar lc2 = new LunarCalendar(NEXT_MONTH_IS_LEAP[0], NEXT_MONTH_IS_LEAP[1], NEXT_MONTH_IS_LEAP[2]);
        //LunarCalendar lc3 = new LunarCalendar(NORMAL[0], NORMAL[1], NORMAL[2]);

        //Act
        lc.addChineseMonth(12);
        lc2.addChineseMonth(1);
        //lc3.addChineseMonth(-1);

        //Assert
        verifyLunarCalendar(lc, new int[]{NORMAL[0] + 1, NORMAL[1], NORMAL[2], NORMAL[3] + 1, 4, 25});
        verifyLunarCalendar(lc2, LEAP_MONTH);
        //verifyLunarCalendar(lc3, new int[]{NORMAL[0] , NORMAL[1] - 1, NORMAL[2], NORMAL[3], 4 , 6});
    }

    @Test
    public void testAddMonth(){
        //Arrange
        LunarCalendar lc = new LunarCalendar(NORMAL[0], NORMAL[1], NORMAL[2]);

        //Act
        lc.addMonth(-1);

        //Assert
        verifyLunarCalendar(lc, new int[]{NORMAL[0], 2, 28, NORMAL[3], NORMAL[4] - 1, NORMAL[5]});
    }

    @Test
    public void testAddChineseYear(){
        //Arrange
        LunarCalendar lc = new LunarCalendar(NORMAL[0], NORMAL[1], NORMAL[2]);

        //Act
        lc.addChineseYear(-1);

        //Assert
        verifyLunarCalendar(lc, new int[]{NORMAL[0] - 1, NORMAL[1], NORMAL[2], 2015, 5, 17});
    }

    @Test
    public void testAddYear(){
        //Arrange
        LunarCalendar lc = new LunarCalendar(NORMAL[0], NORMAL[1], NORMAL[2]);

        //Act
        lc.addYear(2);

        //Assert
        verifyLunarCalendar(lc, new int[]{2018 ,3 ,20, NORMAL[3] + 2, NORMAL[4], NORMAL[5]});
    }

    @Test
    public void testCompareTo(){//测试不合格，需要将每个路径测试一遍
        LunarCalendar lc = new LunarCalendar(NORMAL[0], NORMAL[1], NORMAL[2], 0 , 0);
        LunarCalendar lc2 = new LunarCalendar(NORMAL[0], NORMAL[1], NORMAL[2]);
        LunarCalendar lc3 = new LunarCalendar(NORMAL[0], NORMAL[1], NORMAL[2]);
        lc3.addMinute(1);
        LunarCalendar lc4 = new LunarCalendar(NORMAL[0], NORMAL[1], NORMAL[2]);
        lc4.addMinute(-1);

        assertEquals(0, lc.compareTo(lc2));
        assertEquals(-1, lc.compareTo(lc3));
        assertEquals(1, lc.compareTo(lc4));
        lc3.addMinute(-1);lc4.addMinute(1);

        lc3.addChineseYear(1);lc4.addChineseYear(-1);
        assertEquals(-1, lc.compareTo(lc3));
        assertEquals(1, lc.compareTo(lc4));
        lc3.addChineseYear(-1);lc4.addChineseYear(-1);

        lc3.addDate(1);lc4.addDate(-1);
        assertEquals(-1, lc.compareTo(lc3));
        assertEquals(1, lc.compareTo(lc4));
        lc3.addDate(-1);lc4.addDate(-1);

        lc3.addHour(1);lc4.addHour(-1);
        assertEquals(-1, lc.compareTo(lc3));
        assertEquals(1, lc.compareTo(lc4));
        lc3.addHour(-1);lc4.addHour(-1);
    }

    @Test
    public void testClone(){
        LunarCalendar lc = new LunarCalendar(NORMAL[0], NORMAL[1], NORMAL[2], 18, 30);
        LunarCalendar clone = lc.clone();

        assertEquals(0, lc.compareTo(clone));
        verifyLunarCalendar(clone, NORMAL);
    }
}
