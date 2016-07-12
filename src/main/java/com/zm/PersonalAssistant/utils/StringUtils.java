package com.zm.PersonalAssistant.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zhangmin on 2016/6/17.
 */
public class StringUtils {
    public static String removeStart(String str, String regex) {
        Pattern pattern = Pattern.compile("^" + regex + "(\\S+)$");
        Matcher matcher = pattern.matcher(str);
        if(matcher.find()){
            return matcher.group(1);
        }
        return str;
    }

    public static int getStartInt(String str){
        Pattern pattern = Pattern.compile("^(\\d+)\\S*");
        Matcher matcher = pattern.matcher(str);
        if(matcher.find()){
            return Integer.parseInt(matcher.group(1));
        }
        throw new IllegalArgumentException("Can't find Integer in begin of the string");
    }
}
