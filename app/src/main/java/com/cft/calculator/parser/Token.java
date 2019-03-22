package com.cft.calculator.parser;

public abstract class Token {
    public abstract boolean isOperand();

    public abstract Double getValue();

    public abstract Character getCharacter();

    public abstract void print();
}
