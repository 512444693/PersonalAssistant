package com.zm.PersonalAssistant.utils;

import org.junit.Assert;
import org.junit.Test;

import java.util.GregorianCalendar;

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

    //TODO ：闰月, ｛没有闰月，有闰月｛第一个月，第二个月｝｝
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
    public void testToString(){
        LunarCalendar lc = new LunarCalendar(NORMAL[0], NORMAL[1], NORMAL[2]);
        assertTrue(lc.toString() != null);
        assertTrue(!lc.toString().equals(""));
        System.out.println(lc.toString());
        for(int i = 0; i < 30; i++){
            lc.addDate(30);
            System.out.println(lc.toString());
        }
    }
}
