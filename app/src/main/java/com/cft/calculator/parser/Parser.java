package com.cft.calculator.parser;

import java.util.ArrayList;
import java.util.Stack;

public class Parser {
    private ArrayList<Token> expression;

    public Parser(String input) throws Exception {
        input = fixDiv(input);
        parenthesesCheck(input);
        this.expression = new ArrayList<>();

        for (int i = 0; i < input.length(); ++i) {
            char cur = input.charAt(i);
            switch (cur) {
                case '+':
                    checkForOperands();
                    expression.add(new Signifier(cur));
                    break;
                case '-':
                    if (size() != 0 && last().isOperand()) {
                        expression.add(new Signifier(cur));
                        break;
                    }
                    expression.add(new Signifier('!'));
                    break;
                case '*':
                    checkForOperands();
                    expression.add(new Signifier(cur));
                    break;
                case '/':
                    checkForOperands();
                    expression.add(new Signifier(cur));
                    break;
                case '?':
                    checkForOperands();
                    expression.add(new Signifier(cur));
                    break;
                case ':':
                    checkForOperands();
                    expression.add(new Signifier(cur));
                    break;
                case '(':
                    expression.add(new Signifier(cur));
                    break;
                case ')':
                    checkForOperands();
                    if (size() != 0 && areEqual(last(), '(')) {
                        expression.remove(size() - 1);
                        break;
                    }
                    expression.add(new Signifier(cur));
                    break;

                default:
                    int l = i;
                    for (; i < input.length(); ++i) {
                        if (i == input.length() - 1) {
                            break;
                        }
                        if (!Character.isDigit(input.charAt(i+1)) && !(input.charAt(i+1) == '.')) {
                            break;
                        }
                    }
                    expression.add(new Number(Double.parseDouble(input.substring(l, i + 1))));
            }
        }
    }

    public Double parse() throws Exception {
        return parse(0, size(), "ternary");
    }

    private Double parse(int left, int right, String priority) throws Exception {
        if (left >= right) {
            throw new Exception("Missing operand");
        }

        while (hasBorderParentheses(left, right)) {
            ++left;
            --right;
        }

        switch (priority) {
            case "ternary":
                int question = findLeastNestedAndLowestPriorityOperator(left, right);
                if (question != -1 && areEqual(expression.get(question), '?')) {
                    int colon = findColon(question, right);
                    if (parse(left, question, "ternary") != 0) {
                        return parse(question + 1, colon, "ternary");
                    }
                    return parse(colon + 1, right, "ternary");
                } else {
                    return parse(left, right, "arithmetical");
                }

            case "arithmetical":
                int operator = findLeastNestedAndLowestPriorityOperator(left, right);
                if (operator != -1) {
                    switch (expression.get(operator).getCharacter()) {
                        case '+':
                            return parse(left, operator, "ternary") +
                                    parse(operator + 1, right,"ternary");
                        case '-':
                            return parse(left, operator, "ternary") -
                                    parse(operator + 1, right,"ternary");
                        case '*':
                            return parse(left, operator, "ternary") *
                                    parse(operator + 1, right, "ternary");
                        case '/':
                            return parse(left, operator,"ternary") /
                                    parse(operator + 1, right, "ternary");
                    }
                } else {
                    return parse(left, right, "number");
                }

            case "number":
                Token cur = expression.get(left);
                if (cur.getCharacter() != null && cur.getCharacter() == '!') {
                    return -1 * expression.get(left + 1).getValue();
                }
                return cur.getValue();

            default:
                throw new Exception("Unknown error, thank god it didn't crash");
        }
    }

    private int findLeastNestedAndLowestPriorityOperator(int left, int right) {
        ArrayList<Operator> operators = new ArrayList<>();
        int balance = 0;
        for (int i = left; i < right; ++i) {
            Token token = expression.get(i);
            if (token.getCharacter() == null) {
                continue;
            }
            switch (token.getCharacter()) {
                case '(':
                    balance++;
                    break;
                case ')':
                    balance--;
                    break;
                case '+':
                    operators.add(new Operator(balance, 2, i));
                    break;
                case '-':
                    operators.add(new Operator(balance, 2, i));
                    break;
                case '*':
                    operators.add(new Operator(balance, 3, i));
                    break;
                case '/':
                    operators.add(new Operator(balance, 3, i));
                    break;
                case '?':
                    operators.add(new Operator(balance, 1, i));
                    break;
            }
        }
        Operator min = new Operator(Integer.MAX_VALUE, 4, 0);
        for (Operator operator : operators) {
            if (operator.bracketsBefore < min.bracketsBefore) {
                min = operator;
                continue;
            }
            if (operator.bracketsBefore == min.bracketsBefore) {
                if (operator.priority < min.priority) {
                    min = operator;
                    continue;
                }
                if (operator.priority == min.priority) {
                    if (operator.index > min.index) {
                        min = operator;
                    }
                }
            }
        }
        if (min.priority != 4) {
            return min.index;
        } else {
            return -1;
        }
    }

    private int findColon(int left, int right) throws Exception {
        for (int i = left; i < right; ++i) {
            if (areEqual(expression.get(i), ':')) {
                return i;
            }
        }
        throw new Exception("Missing colon");
    }

    private String fixDiv(String input) {
        StringBuilder s = new StringBuilder(input);
        for (int i = 0; i < s.length(); ++i) {
            if (s.charAt(i) == '÷') {
                s.setCharAt(i, '/');
            }
        }
        return s.toString();
    }

    private void parenthesesCheck(String s) throws Exception {
        Stack<Character> stack = new Stack<>();
        for (char c : s.toCharArray()) {
            if (c == '(') {
                stack.push(c);
            }
            if (c == ')') {
                if (stack.peek() == '(') {
                    stack.pop();
                } else {
                    stack.push(c);
                }
            }
        }
        if (!stack.isEmpty()) {
            throw new Exception("Missing parentheses");
        }
    }

    private int size() {
        return expression.size();
    }

    private Token last() {
        return expression.get(size() - 1);
    }

    private void checkForOperands() throws Exception {
        if (size() == 0 || !last().isOperand()) {
            throw new Exception("Missing operand");
        }
    }

    private boolean areEqual(Token token, char c) {
        return token.getCharacter() != null && token.getCharacter() == c;
    }

    private boolean hasBorderParentheses(int left, int right) throws Exception {
        if (!areEqual(expression.get(left), '(')) {
            return false;
        }

        Stack<Token> stack = new Stack<>();
        stack.add(new Signifier('('));

        for (int i = left + 1; i < right; ++i) {
            Token token = expression.get(i);
            if (areEqual(token, '(')) {
                stack.push(token);
            }
            if (areEqual(token, ')')) {
                if (areEqual(stack.peek(), '(')) {
                    stack.pop();
                } else {
                    stack.push(token);
                }
            }
            if (stack.isEmpty()) {
                return i == right - 1;
            }
        }
        throw new Exception("Missing parentheses");
    }
}