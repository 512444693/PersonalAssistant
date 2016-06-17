package com.zm.PersonalAssistant.utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by zhangmin on 2016/6/17.
 */
public class StringUtilsTest {
    @Test
    public void testRemoveStart(){
        assertEquals("abc", StringUtils.removeStart("abcabc", "abc"));
        assertEquals("a123abc", StringUtils.removeStart("a123abc", "123"));
        assertEquals("abc123", StringUtils.removeStart("abc123", "123"));
        assertEquals("abc123", StringUtils.removeStart("abc123", ""));

        assertEquals("abc", StringUtils.removeStart("./abc", "[../|./]+"));
        assertEquals("abc", StringUtils.removeStart("../abc", "[../|./]+"));
        assertEquals("abc", StringUtils.removeStart(".././.././abc", "[../|./]+"));
    }
}
