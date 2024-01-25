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
            if (Character.isWhitespace(c)) continue;

            if (Character.isDigit(c) || c == '.') {
                StringBuilder number = new StringBuilder();
                boolean decimalPointEncountered = false;

                while (i < chars.length && (Character.isDigit(chars[i]) || chars[i] == '.')) {
                    if (chars[i] == '.') {
                        if (decimalPointEncountered) {
                            // More than one decimal point in the number
                            throw new IllegalArgumentException("Invalid number format: " + expression);
                        }
                        decimalPointEncountered = true;
                    }
                    number.append(chars[i++]);
                }

                i--; // Adjust for the next character
                tokens.add(new Token(number.toString(), TokenType.NUMBER));

                // Implicit multiplication after a number
                if (i + 1 < chars.length && chars[i + 1] == '(') {
                    tokens.add(new Token("×", TokenType.OPERATOR));
                }

            } else {
                boolean condition = i + 1 < chars.length && (Character.isDigit(chars[i + 1]) || chars[i + 1] == '(');
                if ("–".indexOf(c) != -1) {
                    boolean correctUnaryUsage = i == 0 || (chars[i - 1] == '(' || "+-×÷^".indexOf(chars[i - 1]) != -1) && condition;

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
                } else if (c == 'e') {
                    tokens.add(new Token(String.valueOf(Math.E), TokenType.NUMBER));
                } else if (c == 'π') {
                    tokens.add(new Token(String.valueOf(Math.PI), TokenType.NUMBER));
                } else if (c == '√') {
                    tokens.add(new Token("√", TokenType.FUNCTION));
                } else if ("+-×÷^".indexOf(c) != -1) {
                    tokens.add(new Token(String.valueOf(c), TokenType.OPERATOR));
                } else if (c == '(') {
                    tokens.add(new Token(String.valueOf(c), TokenType.PARENTHESIS));
                } else if (c == ')') {
                    tokens.add(new Token(String.valueOf(c), TokenType.PARENTHESIS));
                    // Implicit multiplication after a closing parenthesis
                    if (i + 1 < chars.length && chars[i + 1] == '.') {
                        throw new IllegalArgumentException("Invalid expression: ')' directly followed by '.'");
                    }

                    if (condition) {
                        tokens.add(new Token("×", TokenType.OPERATOR));
                    }
                }  else {
                    throw new IllegalArgumentException("Invalid character: " + c);
                }
            }
        }

        return tokens;
    }
}
