package com.coderpwh.grammar_processor.entry;


import java.util.HashMap;
import java.util.Map;

public class KeyWorkMap {
    public static Map<String,TokenType> map = new HashMap<>();
    static {
        map.put("while",TokenType.While);
        map.put("if",TokenType.If);
        map.put("int",TokenType.Int);
        map.put("read",TokenType.READ);
        map.put("write",TokenType.WRITE);
        map.put("else",TokenType.Else);
        map.put("include",TokenType.Include);
        map.put("main",TokenType.Main);
        map.put("char",TokenType.Char);
        map.put("bool",TokenType.Bool);
        map.put("true",TokenType.TRUE);
        map.put("false",TokenType.FALSE);
        map.put("do",TokenType.Do);
        map.put("and",TokenType.And);
        map.put("or",TokenType.Or);
        map.put("not",TokenType.Not);
        map.put("repeat",TokenType.Repeat);
        map.put("until",TokenType.Until);
    }
}