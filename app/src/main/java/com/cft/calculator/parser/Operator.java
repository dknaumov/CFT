package com.cft.calculator.parser;

public class Operator {
    public int bracketsBefore;
    public int priority;
    public int index;

    Operator(int bracketsBefore,int priority, int index) {
        this.bracketsBefore = bracketsBefore;
        this.priority = priority;
        this.index = index;
    }
}
