package main.model;

import java.util.ArrayList;
import java.util.List;

public class MathTokenizer {

    // Tokenize the expression
    public List<Token> tokenize(String expression) {
        List<Token> tokens = new ArrayList<>();
        char[] chars = expression.toCharArray();

        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if (Character.isDigit(c) || c == '.') {
                i = tokenizeNumber(chars, i, tokens);
            } else {
                i = tokenizeOperatorFuncOrParenthesis(chars, i, tokens, expression);
            }
        }
        return tokens;
    }

    private int tokenizeNumber(char[] chars, int startIndex, List<Token> tokens) {
        StringBuilder number = new StringBuilder();
        boolean decimalPointEncountered = false;

        while (startIndex < chars.length && (Character.isDigit(chars[startIndex]) || chars[startIndex] == '.')) {
            if (chars[startIndex] == '.') {
                if (decimalPointEncountered) {
                    // More than one decimal point in the number
                    throw new IllegalArgumentException("Invalid number format:" + chars.toString());
                }
                decimalPointEncountered = true;
            }
            number.append(chars[startIndex++]);
        }

        startIndex--; // Adjust for the next character
        tokens.add(new Token(number.toString(), TokenType.NUMBER));

        // Implicit multiplication after a number
        if (startIndex + 1 < chars.length) {
            char nextChar = chars[startIndex + 1];
            if (nextChar == '(' || nextChar == 'e' || nextChar == 'π' || nextChar == '√' || (nextChar == 'l' && (chars[startIndex + 2] == 'n' || chars[startIndex + 2] == 'o'))) {
                tokens.add(new Token("×", TokenType.OPERATOR));
            }
        }

        return startIndex;
    }

    private int tokenizeOperatorFuncOrParenthesis(char[] chars, int startIndex, List<Token> tokens, String expression) {
        char c = chars[startIndex];
        boolean condition = startIndex + 1 < chars.length && (Character.isDigit(chars[startIndex + 1])
                || chars[startIndex + 1] == '(' || chars[startIndex + 1] == 'e' || chars[startIndex + 1] == 'π'
                || chars[startIndex + 1] == '√' || (chars[startIndex + 1] == 'l' && (chars[startIndex + 2] == 'n'
                || chars[startIndex + 2] == 'o')));

        if ("–".indexOf(c) != -1) {
            boolean correctUnaryUsage = startIndex == 0 || (chars[startIndex - 1] == '(' || "+-×÷^".indexOf(chars[startIndex - 1]) != -1) && condition;

            if (!correctUnaryUsage) {
                throw new IllegalArgumentException("Invalid use of en dash: " + expression);
            } else {
                // Correct usage for unary negation
                tokens.add(new Token("(", TokenType.PARENTHESIS));
                tokens.add(new Token("0", TokenType.NUMBER));
                tokens.add(new Token("-", TokenType.OPERATOR));
                tokens.add(new Token("1", TokenType.NUMBER));
                tokens.add(new Token(")", TokenType.PARENTHESIS));
                tokens.add(new Token("*", TokenType.OPERATOR));
            }
        } else if (c == ')') {
            tokens.add(new Token(")", TokenType.PARENTHESIS));
            // If the next character is a period without a digit in between, throw a syntax error
            if (startIndex + 1 < chars.length && chars[startIndex + 1] == '.') {
                throw new IllegalArgumentException("Syntax Error: Parenthesis followed by a period without a digit in between");
            }
            // If the next character is a digit, a constant, or a parenthesis, add a multiplication operator
            if (startIndex + 1 < chars.length && (Character.isDigit(chars[startIndex + 1]) || chars[startIndex + 1] == 'e' || chars[startIndex + 1] == 'π' || chars[startIndex + 1] == '(' || chars[startIndex + 1] == '√')) {
                tokens.add(new Token("×", TokenType.OPERATOR));
            }
        } else if (c == 'e') {
            // If the last token is also a constant, throw a syntax error
            if (!tokens.isEmpty() && tokens.getLast().getType() == TokenType.NUMBER) {
                throw new IllegalArgumentException("Syntax Error: Two constants without an operator in between");
            }
            tokens.add(new Token(String.valueOf(Math.E), TokenType.NUMBER));
            // If the next character is a digit or a parenthesis, add a multiplication operator
            if (startIndex + 1 < chars.length && (Character.isDigit(chars[startIndex + 1]) || chars[startIndex + 1] == '(')) {
                tokens.add(new Token("×", TokenType.OPERATOR));
            }
        } else if (c == 'π') {
            // If the last token is also a constant, throw a syntax error
            if (!tokens.isEmpty() && tokens.getLast().getType() == TokenType.NUMBER) {
                throw new IllegalArgumentException("Syntax Error: Two constants without an operator in between");
            }
            tokens.add(new Token(String.valueOf(Math.PI), TokenType.NUMBER));
            // If the next character is a digit or a parenthesis, add a multiplication operator
            if (startIndex + 1 < chars.length && (Character.isDigit(chars[startIndex + 1]) || chars[startIndex + 1] == '(')) {
                tokens.add(new Token("×", TokenType.OPERATOR));
            }
        } else if (c == '√') {
            tokens.add(new Token("√", TokenType.FUNCTION));
        } else if (expression.startsWith("ln", startIndex)) {
            tokens.add(new Token("ln", TokenType.FUNCTION));
            startIndex += 1; // Skip past "ln"
        } else if (expression.startsWith("log", startIndex)) {
            tokens.add(new Token("log", TokenType.FUNCTION));
            startIndex += 2; // Skip past "log"
        } else if ("+-×÷^".indexOf(c) != -1) {
            tokens.add(new Token(String.valueOf(c), TokenType.OPERATOR));
        } else if (c == '(') {
            tokens.add(new Token(String.valueOf(c), TokenType.PARENTHESIS));
        } else {
            throw new IllegalArgumentException("Invalid character: " + c);
        }

        return startIndex;
    }
}
