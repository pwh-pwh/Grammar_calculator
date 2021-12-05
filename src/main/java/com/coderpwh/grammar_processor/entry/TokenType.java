package com.coderpwh.grammar_processor.entry;

/**
 * Token的类型
 */
public enum TokenType{
    Plus,   // +
    Minus,  // -
    Star,   // *
    Slash,  // /
    QY,     //%
    CF,      //^
    Repeat, //repeat
    And,
    Or,
    Not,
    GE,     // >=
    GT,     // >
    EQ,     // ==
    LE,     // <=
    LT,     // <
    IN,    //
    OUT,
    NQ,     //<>
    JE,    //-=
    SemiColon, // ;
    LeftParen, // (
    RightParen,// )

    Assignment,// =
    ZSs, //tiny注释
    ZSe,
    If,
    Else,

    Int,

    Identifier,     //标识符

    IntLiteral,     //整型字面量
    StringLiteral,   //字符串字面量

    WRITE,   //<<
    READ,    //>>

    Cin,
    While,
    Cout,
    Include,
    Main,
    Jing,
    Character,
    Char,
    Bool,
    TRUE,
    FALSE,
    Do, //do
    StringBegin,
    KU,              //库文件
    Notes,
    MULCM,   //多行注释
    SignedNum,
    Then, //then
    End,
    Until   //until

}
