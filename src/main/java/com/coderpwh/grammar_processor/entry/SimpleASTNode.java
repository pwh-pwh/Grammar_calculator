package com.coderpwh.grammar_processor.entry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SimpleASTNode implements ASTNode{
    SimpleASTNode parent = null;
    List<ASTNode> children = new ArrayList<ASTNode>();
    List<ASTNode> readonlyChildren = Collections.unmodifiableList(children);
    ASTNodeType nodeType = null;
    String text = null;


    public SimpleASTNode(ASTNodeType nodeType, String text) {
        this.nodeType = nodeType;
        this.text = text;
    }

    @Override
    public ASTNode getParent() {
        return parent;
    }

    @Override
    public List<ASTNode> getChildren() {
        return readonlyChildren;
    }

    @Override
    public ASTNodeType getType() {
        return nodeType;
    }

    @Override
    public String getText() {
        return text;
    }

    public void addChild(SimpleASTNode child) {
        children.add(child);
        child.parent = this;
    }

}
