package com.coderpwh.grammar_processor.entry;
/**
 * Token的一个简单实现。只有类型和文本值两个属性。
 */
public class SimpleToken implements Token{
    private TokenType type;
    private String text;

    public void setType(TokenType type) {
        this.type = type;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public TokenType getType() {
        return type;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return "SimpleToken{" +
                "type=" + type +
                ", text='" + text + '\'' +
                '}';
    }
}
