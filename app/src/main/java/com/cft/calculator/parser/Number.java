package com.cft.calculator.parser;

public class Number extends Token {
    private Double value;

    public Number(Double value) {
        this.value = value;
    }


    @Override
    public boolean isOperand() {
        return true;
    }

    @Override
    public Double getValue() {
        return value;
    }

    @Override
    public Character getCharacter() {
        return null;
    }

    @Override
    public void print() {
        System.out.print(value);
        System.out.print(' ');
    }
}
