package com.coderpwh.grammar_processor.entry;
/**
 * 有限状态机的各种状态。
 */
public enum DfaState {
    Initial,

    If, While, Id_if1, Id_if2, Else, Id_else1, Id_else2, Id_else3, Id_else4, Int,
    Id_int1, Id_int2, Id_int3, Id, GT, GE,
    QY,CF,NQ,JE,Repeat,
    EQ,LT,LE,OUT,IN,
    Assignment,
    And,
    Or,
    Not,
    WRITE,   //<<
    READ,    //>>
    Do, //do
    Then, //then
    End,
    Until,   //until
    ZSs, //tiny注释
    ZSe,
    Plus, Minus, Star, Slash,Jing,

    SemiColon,
    LeftParen,
    RightParen,

    IntLiteral,
    StringLiteral,
    StringBegin,
    KU,
    Include,Notes,
    MULCM,   //多行注释
    MULCM_B,
    MULCM_M,
    Charset

}
