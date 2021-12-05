package com.coderpwh.grammar_processor.service;

import com.coderpwh.grammar_processor.entry.*;

/**
 * 一个简单的语法解析器。
 * 能够解析简单的表达式、变量声明和初始化语句、赋值语句。
 * 它支持的语法规则为：
 *
 * programm -> intDeclare | expressionStatement | assignmentStatement
 * intDeclare -> 'int' Id ( = additive) ';'
 * expressionStatement -> addtive ';'
 * addtive -> multiplicative ( (+ | -) multiplicative)*
 * multiplicative -> primary ( (* | /) primary)*
 * primary -> IntLiteral | Id | (additive)
 *
 */

public class SimpleParser {
    public static void main(String[] args) throws Exception {
        String script = "int b = 5; int c = 2+3; c = b+a;";
        SimpleParser parser = new SimpleParser();
        SimpleASTNode node = parser.parse(script);
        parser.dumpAST(node,"");

    }
    /**
     * 解析脚本，并返回根节点
     * @param code
     * @return
     * @throws Exception
     */
    public SimpleASTNode parse(String code) throws Exception {
        SimpleLexer lexer = new SimpleLexer();
        TokenReader tokens = lexer.tokenize(code);
        SimpleASTNode rootNode = prog(tokens);
        return rootNode;
    }
    /**
     * 语法解析：根节点
     * @return
     * @throws Exception
     */
    public SimpleASTNode prog(TokenReader tokens) throws Exception {
        SimpleASTNode node = new SimpleASTNode(ASTNodeType.Programm, "pwc");

        while (tokens.peek() != null) {
            SimpleASTNode child = intDeclare(tokens);

            if (child == null) {
                child = expressionStatement(tokens);
            }

            if (child == null) {
                child = assignmentStatement(tokens);
            }

            if (child != null) {
                node.addChild(child);
            } else {
                throw new Exception("unknown statement");
            }
        }

        return node;

    }

    /**
     * 整型变量声明，如：
     * int a;
     * int b = 2*3;
     *
     * @return
     * @throws Exception
     */
    public SimpleASTNode intDeclare(TokenReader tokens) throws Exception {
        SimpleASTNode node = null;
        Token token = tokens.peek();
        if (token != null && token.getType() == TokenType.Int) {
            token = tokens.read();
            if (tokens.peek().getType() == TokenType.Identifier) {
                token = tokens.read();
                node = new SimpleASTNode(ASTNodeType.IntDeclaration, token.getText());
                token = tokens.peek();
                if (token != null && token.getType() == TokenType.Assignment) {
                    tokens.read();  //取出等号
                    SimpleASTNode child = additive(tokens);
                    if (child == null) {
                        throw new Exception("invalide variable initialization, expecting an expression");
                    }
                    else{
                        node.addChild(child);
                    }
                }
            } else {
                throw new Exception("variable name expected");
            }

            if (node != null) {
                token = tokens.peek();
                if (token != null && token.getType() == TokenType.SemiColon) {
                    tokens.read();
                } else {
                    System.out.println(token);
                    throw new Exception("invalid statement, expecting semicolon");
                }
            }
        }
        return node;
    }

    /**
     * assignmentStatement -> id = additive;
     * @param tokens
     * @return
     */
    public SimpleASTNode assignmentStatement(TokenReader tokens) throws Exception {
        SimpleASTNode node = null;
        Token token = tokens.peek();    //预读，看看下面是不是标识符
        if (token != null && token.getType() == TokenType.Identifier) {
            token = tokens.read();      //读入标识符
            node = new SimpleASTNode(ASTNodeType.AssignmentStmt, token.getText());
            token = tokens.peek();      //预读，看看下面是不是等号
            if (token != null && token.getType() == TokenType.Assignment) {
                tokens.read();          //取出等号
                SimpleASTNode child = additive(tokens);
                if (child == null) {    //出错，等号右面没有一个合法的表达式
                    throw new Exception("invalide assignment statement, expecting an expression");
                }
                else{
                    node.addChild(child);   //添加子节点
                    token = tokens.peek();  //预读，看看后面是不是分号
                    if (token != null && token.getType() == TokenType.SemiColon) {
                        tokens.read();      //消耗掉这个分号

                    } else {                //报错，缺少分号
                        throw new Exception("invalid statement, expecting semicolon");
                    }
                }
            }
            else {
                tokens.unread();            //回溯，吐出之前消化掉的标识符
                node = null;
            }
        }
        return node;
    }

    /**
     * expressionStatement -> addtive ';'
     * @param tokens
     * @return
     */
    public SimpleASTNode expressionStatement(TokenReader tokens) throws Exception {
        int pos = tokens.getPosition();
        SimpleASTNode node = additive(tokens);
        if (node != null) {
            Token token = tokens.peek();
            if (token != null && token.getType() == TokenType.SemiColon) {
                tokens.read();
            } else {
                node = null;
                tokens.setPosition(pos); // 回溯
            }
        }
        return node;  //直接返回子节点，简化了AST。

    }

    /**
     * primary -> IntLiteral | Id | (additive)
     * @param tokens
     * @return
     */
    public SimpleASTNode primary(TokenReader tokens) throws Exception {
        SimpleASTNode node = null;
        Token token = tokens.peek();
        if (token!=null) {
            if(token.getType() == TokenType.IntLiteral) {
                tokens.read();
                node = new SimpleASTNode(ASTNodeType.IntLiteral, token.getText());
            }
            else if(token.getType() == TokenType.Identifier) {
                tokens.read();
                node = new SimpleASTNode(ASTNodeType.Identifier, token.getText());
            } else if(token.getType() == TokenType.LeftParen) {
                tokens.read();
                node = additive(tokens);
                if(node != null) {
                    token = tokens.peek();
                    if (token!=null && token.getType() == TokenType.RightParen) {
                        tokens.read();
                    }else {
                        throw new Exception("expecting right parenthesis");
                    }
                }else {
                    throw new Exception("expecting an additive expression inside parenthesis");
                }
            }
        }
        return node;
    }


    /**
     * addtive -> multiplicative ( (+ | -) multiplicative)*
     * @param tokens
     * @return
     */
    public SimpleASTNode additive(TokenReader tokens) throws Exception {
        SimpleASTNode child1 = multiplicative(tokens);
        SimpleASTNode node = child1;
        if (child1!=null) {
            while(true) {
                Token token = tokens.peek();
                if(token!=null && (token.getType() == TokenType.Plus || token.getType() == TokenType.Minus)) {
                    tokens.read();
                    SimpleASTNode child2 = multiplicative(tokens);
                    node = new SimpleASTNode(ASTNodeType.Additive,token.getText());
                    node.addChild(child1);
                    node.addChild(child2);
                    child1 = node;
                }else {
                    break;
                }
            }
        }
        return node;
    }

    /**
     * multiplicative -> primary ( (* | /) primary)*
     * @param tokens
     * @return
     */
    public SimpleASTNode multiplicative(TokenReader tokens) throws Exception {
        SimpleASTNode child1 = primary(tokens);
        SimpleASTNode node = child1;
        Token token = tokens.peek();
        if(child1!=null && token!=null) {
            if(token.getType() == TokenType.Star || token.getType() == TokenType.Slash) {
                tokens.read();
                SimpleASTNode child2 = multiplicative(tokens);
                if(child2!=null){
                    node = new SimpleASTNode(ASTNodeType.Multiplicative,token.getText());
                    node.addChild(child1);
                    node.addChild(child2);
                }else {
                    throw new Exception("invalid multiplicative expression, expecting the right part.");
                }
            }
        }
        return node;
    }

    /**
     * 打印输出AST的树状结构
     * @param node
     * @param indent 缩进字符，由tab组成，每一级多一个tab
     */
    public void dumpAST(ASTNode node, String indent) {
        System.out.println(indent + node.getType() + " " + node.getText());
        for (ASTNode child : node.getChildren()) {
            dumpAST(child, indent + "\t");
        }
    }
}
