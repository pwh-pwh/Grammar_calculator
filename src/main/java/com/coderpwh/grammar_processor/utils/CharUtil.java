package com.coderpwh.grammar_processor.utils;

public class CharUtil {
    //是否是字母
    public static boolean isAlpha(int ch) {
        return ch >= 'a' && ch <= 'z' || ch >= 'A' && ch <= 'Z';
    }

    //是否是数字
    public static boolean isDigit(int ch) {
        return ch >= '0' && ch <= '9';
    }

    //是否是空白字符
    public static boolean isBlank(int ch) {
        return ch == ' ' || ch == '\t' || ch == '\n';
    }
}