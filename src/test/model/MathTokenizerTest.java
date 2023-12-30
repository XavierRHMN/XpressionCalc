package test.model;

import main.model.MathTokenizer;
import main.model.Token;
import main.model.TokenType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MathTokenizerTest {

    private final MathTokenizer tokenizer = new MathTokenizer();

    @Test
    void testDecimalNumbers() {
        List<Token> tokens = tokenizer.tokenize("2.5+3.0");
        assertEquals(3, tokens.size());
        assertEquals(TokenType.NUMBER, tokens.get(0).getType());
        assertEquals("2.5", tokens.get(0).getValue());
        assertEquals(TokenType.OPERATOR, tokens.get(1).getType());
        assertEquals("+", tokens.get(1).getValue());
        assertEquals(TokenType.NUMBER, tokens.get(2).getType());
        assertEquals("3.0", tokens.get(2).getValue());
    }

    @Test
    void testOperators() {
        List<Token> tokens = tokenizer.tokenize("5×4-3÷2+2^2");
        assertEquals(11, tokens.size());
        assertEquals(TokenType.NUMBER, tokens.get(0).getType());
        assertEquals("5", tokens.get(0).getValue());
        assertEquals(TokenType.OPERATOR, tokens.get(1).getType());
        assertEquals("×", tokens.get(1).getValue());
        assertEquals(TokenType.NUMBER, tokens.get(2).getType());
        assertEquals("4", tokens.get(2).getValue());
        assertEquals(TokenType.OPERATOR, tokens.get(3).getType());
        assertEquals("-", tokens.get(3).getValue());
        assertEquals(TokenType.NUMBER, tokens.get(4).getType());
        assertEquals("3", tokens.get(4).getValue());
        assertEquals(TokenType.OPERATOR, tokens.get(5).getType());
        assertEquals("÷", tokens.get(5).getValue());
        assertEquals(TokenType.NUMBER, tokens.get(6).getType());
        assertEquals("2", tokens.get(6).getValue());
        assertEquals(TokenType.OPERATOR, tokens.get(7).getType());
        assertEquals("+", tokens.get(7).getValue());
        assertEquals(TokenType.NUMBER, tokens.get(8).getType());
        assertEquals("2", tokens.get(8).getValue());
        assertEquals(TokenType.OPERATOR, tokens.get(9).getType());
        assertEquals("^", tokens.get(9).getValue());
        assertEquals(TokenType.NUMBER, tokens.get(10).getType());
        assertEquals("2", tokens.get(10).getValue());
    }


    @Test
    void testParentheses() {
        List<Token> tokens = tokenizer.tokenize("(3+4)");
        assertEquals(5, tokens.size());
        assertEquals(TokenType.PARENTHESIS, tokens.get(0).getType());
        assertEquals("(", tokens.get(0).getValue());
        assertEquals(TokenType.PARENTHESIS, tokens.get(4).getType());
        assertEquals(")", tokens.get(4).getValue());
    }

    @Test
    void testUnaryNegation() {
        List<Token> tokens = tokenizer.tokenize("–3");
        assertEquals(TokenType.PARENTHESIS, tokens.get(0).getType());
        assertEquals("(", tokens.get(0).getValue());
        assertEquals(TokenType.NUMBER, tokens.get(1).getType());
        assertEquals("0", tokens.get(1).getValue());
        assertEquals(TokenType.OPERATOR, tokens.get(2).getType());
        assertEquals("-", tokens.get(2).getValue());
        assertEquals(TokenType.NUMBER, tokens.get(3).getType());
        assertEquals("1", tokens.get(3).getValue());
        assertEquals(TokenType.PARENTHESIS, tokens.get(4).getType());
        assertEquals(")", tokens.get(4).getValue());
        assertEquals(TokenType.OPERATOR, tokens.get(5).getType());
        assertEquals("*", tokens.get(5).getValue());
        assertEquals(TokenType.NUMBER, tokens.get(6).getType());
        assertEquals("3", tokens.get(6).getValue());
    }

    @Test
    void testImplicitMultiplication() {
        List<Token> tokens = tokenizer.tokenize("3(2)");
        assertEquals(TokenType.OPERATOR, tokens.get(1).getType());
        assertEquals("×", tokens.get(1).getValue());
    }

    @Test
    void testInvalidInput() {
        assertThrows(IllegalArgumentException.class, () -> tokenizer.tokenize("2..3"));
        assertThrows(IllegalArgumentException.class, () -> tokenizer.tokenize("5_4"));
    }
}
