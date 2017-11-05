package com.googry.coinonehelper.util;

/**
 * Created by seokjunjeong on 2017. 10. 20..
 */

public class NumberConvertUtil {
    private static final String DOLLAR = "$";
    private static final String COMMA = ",";
    private static final String EMPTY = "";
    private static final String SPACE = " ";
    private static final String STAR = "*";
    private static final String PERCENT = "%";

    public static double convertDollarStringToDouble(String value) {
        value = removeDollarString(value);
        value = removeCommaString(value);
        value = removeSpaceString(value);
        value = removeStarString(value);
        return Double.valueOf(value);
    }
    public static float convertDollarStringToFloat(String value) {
        value = removeDollarString(value);
        value = removeCommaString(value);
        value = removeSpaceString(value);
        value = removeStarString(value);
        return Float.valueOf(value);
    }

    public static int convertDollarStringToInteger(String value) {
        value = removeDollarString(value);
        value = removeCommaString(value);
        value = removeSpaceString(value);
        value = removeStarString(value);
        return Integer.valueOf(value);
    }
    public static long convertDollarStringToLong(String value) {
        value = removeDollarString(value);
        value = removeCommaString(value);
        value = removeSpaceString(value);
        value = removeStarString(value);
        return Long.valueOf(value);
    }

    public static float convertPercentStringToFloat(String value) {
        value = removeCommaString(value);
        value = removeSpaceString(value);
        value = removeStarString(value);
        value = removePercentString(value);
        return Float.valueOf(value);
    }

    public static String removeDollarString(String value) {
        return value.replace(DOLLAR, EMPTY);
    }

    public static String removeCommaString(String value) {
        return value.replace(COMMA, EMPTY);
    }

    public static String removeSpaceString(String value) {
        return value.replace(SPACE, EMPTY);
    }

    public static String removeStarString(String value) {
        return value.replace(STAR, EMPTY);
    }

    public static String removePercentString(String value) {
        return value.replace(PERCENT, EMPTY);
    }
}
