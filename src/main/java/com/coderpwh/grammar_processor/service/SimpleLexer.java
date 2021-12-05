package com.coderpwh.grammar_processor.service;

import com.coderpwh.grammar_processor.entry.*;

import java.io.CharArrayReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import static com.coderpwh.grammar_processor.utils.CharUtil.*;
/**
 * 一个简单的手写的词法分析器。
 * 能够为后面的简单计算器、简单脚本语言产生Token。
 */
public class SimpleLexer {
    //下面几个变量是在解析过程中用到的临时变量,如果要优化的话，可以塞到方法里隐藏起来
    private StringBuffer tokenText = null;   //临时保存token的文本
    private List<Token> tokens = null;       //保存解析出来的Token
    private SimpleToken token = null;        //当前正在解析的Token


    public static void main(String args[]) {
        SimpleLexer lexer = new SimpleLexer();

        String script = "int a = 10;;";
        System.out.println("parse :" + script);
        SimpleTokenReader tokenReader = lexer.tokenize(script);
        dump(tokenReader);

    }


    /**
     * 有限状态机进入初始状态。
     * 这个初始状态其实并不做停留，它马上进入其他状态。
     * 开始解析的时候，进入初始状态；某个Token解析完毕，也进入初始状态，在这里把Token记下来，然后建立一个新的Token。
     * @param ch
     * @return
     */
    public DfaState initToken(char ch) {
        if(tokenText.length()>0) {
            token.setText(tokenText.toString());
            tokens.add(token);
//            System.out.println(token);
            token = new SimpleToken();
            tokenText = new StringBuffer();
        }
        DfaState newState = DfaState.Initial;
        if(isAlpha(ch)) {
            newState = DfaState.Id;
            token.setType(TokenType.Identifier);
            tokenText.append(ch);
        }
        else if(isDigit(ch)) {
            newState = DfaState.IntLiteral;
            token.setType(TokenType.IntLiteral);
            tokenText.append(ch);
        }
        else if (ch == '>') {         //第一个字符是>
            newState = DfaState.GT;
            token.setType(TokenType.GT);
            tokenText.append(ch);
        } else if(ch == '<') {
            newState = DfaState.LT;
            token.setType(TokenType.LT);
            tokenText.append(ch);
        }
        else if (ch == '+') {
            newState = DfaState.Plus;
            token.setType(TokenType.Plus);
            tokenText.append(ch);
        } else if (ch == '-') {
            newState = DfaState.Minus;
            token.setType(TokenType.Minus);
            tokenText.append(ch);
        } else if (ch == '*') {
            newState = DfaState.Star;
            token.setType(TokenType.Star);
            tokenText.append(ch);
        } else if(ch == '%') {
            newState = DfaState.QY;
            token.setType(TokenType.QY);
            tokenText.append(ch);
        } else if(ch == '^') {
            newState = DfaState.CF;
            token.setType(TokenType.CF);
            tokenText.append(ch);
        }

        else if (ch == '/') {
            newState = DfaState.Slash;
            token.setType(TokenType.Slash);
            tokenText.append(ch);
        } else if (ch == ';') {
            newState = DfaState.SemiColon;
            token.setType(TokenType.SemiColon);
            tokenText.append(ch);
        } else if (ch == '(') {
            newState = DfaState.LeftParen;
            token.setType(TokenType.LeftParen);
            tokenText.append(ch);
        } else if (ch == ')') {
            newState = DfaState.RightParen;
            token.setType(TokenType.RightParen);
            tokenText.append(ch);
        } else if (ch == '=') {
            newState = DfaState.Assignment;
            token.setType(TokenType.Assignment);
            tokenText.append(ch);
        } else if (ch == '#') {
            newState = DfaState.Jing;
            token.setType(TokenType.Jing);
            tokenText.append(ch);
        } else if (ch == '"') {
            newState = DfaState.StringBegin;
            token.setType(TokenType.StringBegin);
            tokenText.append(ch);
        } else if (ch == '{') {
            newState = DfaState.ZSs;
            token.setType(TokenType.ZSs);
            tokenText.append(ch);
        }
            else if (ch == '\''){
            newState = DfaState.Charset;
            token.setType(TokenType.Character);
            tokenText.append(ch);
        }
        else {
            newState = DfaState.Initial; // skip all unknown patterns
        }
        return newState;
    }

    /**
     * 解析字符串，形成Token。
     * 这是一个有限状态自动机，在不同的状态中迁移。
     * @param code
     * @return
     */
    public SimpleTokenReader tokenize(String code) {
        boolean flag = false;
        tokens = new ArrayList<Token>();
        CharArrayReader charArrayReader = new CharArrayReader(code.toCharArray());
        int length = code.length();
        tokenText = new StringBuffer();
        token = new SimpleToken();
        int ich = 0;
        char ch = 0;
        DfaState state = DfaState.Initial;
        try{
            while((ich = charArrayReader.read())!=-1) {
                length--;
                ch = (char) ich;
                switch (state) {
                    case Initial:
                        state = initToken(ch);
                        break;
                    case Id:
                        if(isAlpha(ch)||isDigit(ch)) {
                            tokenText.append(ch);
                            if(length==0) {
                                isKeyWork(token,tokenText.toString());
                            }
                        }else {
                            isKeyWork(token,tokenText.toString());
                            state = initToken(ch);
                        }
                        break;
                    case GT:
                        if(ch == '=') {
                            token.setType(TokenType.GE);
                            state = DfaState.GE;
                            tokenText.append(ch);
                        } else if(ch == '>') {
                            token.setType(TokenType.IN);
                            state = DfaState.IN;
                            tokenText.append(ch);
                        }

                        else {
                            state = initToken(ch);
                        }
                        break;
                    case LT:
                        if(ch == '=') {
                            token.setType(TokenType.LE);
                            state = DfaState.LE;
                            tokenText.append(ch);
                        } else if(ch == '<') {
                            token.setType(TokenType.OUT);
                            state = DfaState.OUT;
                            tokenText.append(ch);
                        } else if(isAlpha(ch)) {
                            token.setType(TokenType.KU);
                            state = DfaState.KU;
                            tokenText.append(ch);
                        } else if(ch == '>') {
                            token.setType(TokenType.Not);
                            state = DfaState.Not;
                            tokenText.append(ch);
                        }
                        else {
                            state = initToken(ch);
                        }
                        break;
                    case KU:
                        if(flag) {
                            state = initToken(ch);
                            flag = false;
                        } else {
                            tokenText.append(ch);
                            if (ch == '>') {
                                flag = true;
                            }
                        }
                        break;
                    case Assignment:
                        if(ch == '=') {
                            token.setType(TokenType.EQ);
                            state = DfaState.EQ;
                            tokenText.append(ch);
                        } else {
                            state = initToken(ch);
                        }
                        break;
                    case StringBegin:
                        if(ch =='"') {
                            token.setType(TokenType.StringLiteral);
                            state = DfaState.StringLiteral;
                            tokenText.append(ch);
                        } else {
                            tokenText.append(ch);
                        }
                        break;
                    case ZSs:
                        if (ch == '}') {
                            token.setType(TokenType.ZSe);
                            state = DfaState.ZSe;
                            tokenText.append(ch);
                        } else {
                            tokenText.append(ch);
                        }
                        break;
                    case MULCM_M:
                        if(ch == '*') {
                            tokenText.append(ch);
                        }else if(ch == '/') {
                            tokenText.append(ch);
                            state = DfaState.MULCM;
                        }else {
                            tokenText.append(ch);
                            state = DfaState.MULCM_B;
                        }
                        break;
                    case MULCM_B:
                        tokenText.append(ch);
                        if(ch == '*') {
                            state = DfaState.MULCM_M;
                        }
                        break;
                    case Slash:
                        if(ch =='/') {
                            token.setType(TokenType.Notes);
                            state = DfaState.Notes;
                            tokenText.append(ch);
                        } else if(ch == '*') {
                            token.setType(TokenType.MULCM);
                            state = DfaState.MULCM_B;
                            tokenText.append(ch);
                        }
                        else {
                            state = initToken(ch);
                        }
                        break;
                    case Notes:
                        if (isAlpha(ch)){
                            tokenText.append(ch);
                        } else {
                            state = initToken(ch);
                        }
                        break;
                    case Charset:
                        tokenText.append(ch);
                        if(ch == '\''){
                            state = DfaState.Initial;
                        }
                        break;

                    case Plus:
                            state = initToken(ch);
                        break;
                    case Minus:
                            state = initToken(ch);
                        break;
                    case GE:
                    case LE:
                    case EQ:
                    case IN:
                    case OUT:
                    case Star:
                    case SemiColon:
                    case LeftParen:
                    case Include:
                    case QY:
                    case CF:
                    case And:
                    case Or:
                    case Do:
                    case Until:
                    case Not:
                    case WRITE:
                    case READ:
                    case End:
                    case ZSe:
                    case Jing:
                    case StringLiteral:
                    case MULCM:
                    case RightParen:
                        state = initToken(ch);
                        break;
                    case IntLiteral:
                        if(isDigit(ch)) {
                            tokenText.append(ch);
                        }
                        else {
                            state = initToken(ch);
                        }
                        break;
                    default:
                }
            }
            if (tokenText.length()>0) {
                initToken(ch);
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
        return new SimpleTokenReader(tokens);

    }
    public void isKeyWork(SimpleToken token,String str) {
        Map<String, TokenType> map = KeyWorkMap.map;
        TokenType tokenType = map.get(str);
        if(tokenType!=null) {
            token.setType(tokenType);
        }
    }

    /**
     * 打印所有的Token
     * @param tokenReader
     */
    public static void dump(SimpleTokenReader tokenReader){
        System.out.println("text\ttype");
        Token token = null;
        while ((token= tokenReader.read())!=null){
            System.out.println(token.getText()+"\t\t"+token.getType());
        }
    }
}