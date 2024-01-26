package main.model;

import java.util.List;
import java.util.Stack;

public class MathParser {

    private void checkParenthesesBalance(String expression) {
        int balance = 0;
        for (char c : expression.toCharArray()) {
            if (c == '(') {
                balance++;
            } else if (c == ')') {
                balance--;
                if (balance < 0) {
                    // Found a closing parenthesis without a matching opening parenthesis
                    throw new IllegalArgumentException("Mismatched parentheses in expression: " + expression);
                }
            }
        }

        if (balance != 0) {
            // Opening parentheses not matched by closing parentheses
            throw new IllegalArgumentException("Mismatched parentheses in expression: " + expression);
        }
    }

    // if last item in operator stack has >= precedence than first item in input, then last item in operator
    // stack is pushed to output and first item in input is pushed to operator stack, otherwise the first item
    //  in input is pushed to operator stack

    // if an open bracket is first in the input it is pushed to operator stack, otherwise if a closing
    // bracket is first in the input then it will continually push each item from the operatorStack to the output
    // until it reaches the closing bracket in the operator stack, which is then popped
    public double parseExpression(String expression) {
        MathTokenizer tokenizedExpression = new MathTokenizer();
        List<Token> tokens = tokenizedExpression.tokenize(expression);
        Stack<Token> operatorStack = new Stack<>();
        Stack<Double> operandStack = new Stack<>();

        checkParenthesesBalance(expression);

        for (Token token : tokens) {
            switch (token.getType()) {
                case NUMBER:
                    operandStack.push(Double.parseDouble(token.getValue()));
                    break;
                case OPERATOR:
                    while (!operatorStack.isEmpty() && getPrecedence(operatorStack.peek()) >= getPrecedence(token)) {
                        processOperator(operatorStack.pop(), operandStack);
                    }
                    operatorStack.push(token);
                    break;
                case PARENTHESIS:
                    if (token.getValue().equals("(")) {
                        operatorStack.push(token);
                    } else {
                        while (!operatorStack.isEmpty() && !operatorStack.peek().getValue().equals("(")) {
                            processOperator(operatorStack.pop(), operandStack);
                        }
                        operatorStack.pop(); // Pop the '(' from the stack
                    }
                    break;
                case FUNCTION:
                    if (token.getValue().equals("√") || token.getValue().equals("ln") || token.getValue().equals("log")) {
                        operatorStack.push(token);
                    }
                    break;
            }
        }

        while (!operatorStack.isEmpty()) {
            processOperator(operatorStack.pop(), operandStack);
        }

        return operandStack.pop();
    }

    private void processOperator(Token operator, Stack<Double> operandStack) {
        if (operandStack.isEmpty()) {
            throw new IllegalArgumentException("No operand");
        }

        if (operator.getValue().equals("√")) {
            double operand = operandStack.pop();
            operandStack.push(Math.sqrt(operand));
        } else if (operator.getValue().equals("ln")) {
            double operand = operandStack.pop();
            if (operand <= 0) {
                throw new ArithmeticException("Argument of ln must be positive");
            }
            operandStack.push(Math.log(operand));
        } else if (operator.getValue().equals("log")) {
            double operand = operandStack.pop();
            if (operand <= 0) {
                throw new ArithmeticException("Argument of log must be positive");
            }
            operandStack.push(Math.log10(operand));
        } else {
            if (operandStack.size() < 2) {
                throw new IllegalArgumentException("Not enough operands for operator");
            }
            double rightOperand = operandStack.pop();
            double leftOperand = operandStack.pop();
            switch (operator.getValue()) {
                case "+":
                    operandStack.push(leftOperand + rightOperand);
                    break;
                case "-":
                    operandStack.push(leftOperand - rightOperand);
                    break;
                case "×":
                case "*":
                    operandStack.push(leftOperand * rightOperand);
                    break;
                case "÷":
                    if (rightOperand == 0) {
                        throw new ArithmeticException("Division by zero");
                    }
                    operandStack.push(leftOperand / rightOperand);
                    break;
                case "^":
                operandStack.push(Math.pow(leftOperand, rightOperand));
                break;
            }
        }
    }

    // gets the precedence of current operator
    private int getPrecedence(Token token) {
        if (token.getType() == TokenType.PARENTHESIS) return 0;

        switch (token.getValue()) {
            case "+":
            case "-":
                return 1;
            case "×":
            case "÷":
                return 2;
            case "^":
                return 4;
            case "*":
                return 3;
            case "√":
            case "ln":
            case "log":
                return 4;
            default:
                throw new IllegalArgumentException("Unknown operator: " + token.getValue());
        }
    }
}

