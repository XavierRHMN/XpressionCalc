package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class MathParser {

    enum TokenType {
        NUMBER, OPERATOR, VARIABLE, PARENTHESIS
    }
    static class Token {
        String value;
        TokenType type;

        public Token(String value, TokenType type) {
            this.value = value;
            this.type = type;
        }
    }

    // Tokenize the expression
    private List<Token> tokenize(String expression) {
        List<Token> tokens = new ArrayList<>();
        char[] chars = expression.toCharArray();

        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if (Character.isWhitespace(c)) continue;

            if (Character.isDigit(c)) {
                StringBuilder number = new StringBuilder();
                while (i < chars.length && Character.isDigit(chars[i])) {
                    number.append(chars[i++]);
                }
                i--; // Adjust for the next character
                tokens.add(new Token(number.toString(), TokenType.NUMBER));
            } else if (c == 'x') {
                tokens.add(new Token(String.valueOf(c), TokenType.VARIABLE));
            } else if ("+-×÷^".indexOf(c) != -1) {
                tokens.add(new Token(String.valueOf(c), TokenType.OPERATOR));
            }  else if ("()".indexOf(c) != -1) {
                tokens.add(new Token(String.valueOf(c), TokenType.PARENTHESIS));
            } else {
                throw new IllegalArgumentException("Invalid character: " + c);
            }
        }

        return tokens;
    }



    // if last item in operator stack has >= precedence than first item in input, then last item in operator
    // stack is pushed to output and first item in input is pushed to operator stack, otherwise the first item
    //  in input is pushed to operator stack

    // if an open bracket is first in the input it is pushed to operator stack, otherwise if a closing
    // bracket is first in the input then it will continually push each item from the operatorStack to the output
    // until it reaches the closing bracket in the operator stack, which is then popped
    public MathNode parseExpression(String expression) {
        List<Token> input = tokenize(expression);
        Stack<Token> operatorStack = new Stack<>();
        Stack<MathNode> output = new Stack<>();

        for (Token token : input) {
            switch (token.type) {
                case NUMBER:
                    output.push(new MathNode(token.value));
                    break;
                case OPERATOR:
                    while (!operatorStack.isEmpty() && getPrecedence(operatorStack.peek().value) >= getPrecedence(token.value)) {
                        processOperator(operatorStack, output);
                    }
                    operatorStack.push(token);
                    break;
                case PARENTHESIS:
                    if (token.value.equals("(")) {
                        operatorStack.push(token);
                    } else {
                        if (token.value.equals(")")) {
                            while (!operatorStack.isEmpty() && !operatorStack.peek().value.equals("(")) {
                                processOperator(operatorStack, output);
                            }
                            if (operatorStack.isEmpty()) {
                                throw new IllegalStateException("Mismatched parentheses in expression");
                            }
                            operatorStack.pop(); // Pop the opening parenthesis
                        }
                    }
                    break;
            }
        }

        while (!operatorStack.isEmpty()) {
            processOperator(operatorStack, output);
        }

        return output.pop();
    }

    private void processOperator(Stack<Token> operatorStack, Stack<MathNode> output) {

//        if (output.size() < 2) {
//            throw new IllegalStateException("Insufficient operands for the operator");
//        }

        Token operator = operatorStack.pop();
        MathNode rightOperand = output.pop();
        MathNode leftOperand = output.pop();
        output.push(new MathNode(operator.value, leftOperand, rightOperand));
    }

    // gets the precedence of current operator
    private int getPrecedence(String operator) {
        switch (operator) {
            case "+":
            case "-":
                return 1;
            case "×":
            case "÷":
                return 2;
            case "^":
                return 3;
            case "(":
            case ")":
                return 10;
            default:
                throw new IllegalArgumentException("Unknown operator: " + operator);
        }
    }
}

