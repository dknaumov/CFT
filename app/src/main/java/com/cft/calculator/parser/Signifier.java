package com.cft.calculator.parser;

public class Signifier extends Token {
    private Character symbol;

    public Signifier(Character symbol) {
        this.symbol = symbol;
    }

    @Override
    public boolean isOperand() {
        return symbol == ')';
    }

    @Override
    public Double getValue() {
        return null;
    }

    @Override
    public Character getCharacter() {
        return symbol;
    }

    @Override
    public void print() {
        System.out.print(symbol);
        System.out.print(' ');
    }
}
