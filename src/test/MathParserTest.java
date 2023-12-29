package test;

import main.model.MathParser;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MathParserTest {

    private final MathParser parser = new MathParser();

    @Test
    void testBinaryAddition() {
        assertEquals(7.0, parser.parseExpression("3+4"));
        assertEquals(9.0, parser.parseExpression("3+4+2"));
    }

    @Test
    void testBinarySubtraction() {
        assertEquals(1.0, parser.parseExpression("5-4"));
        assertEquals(-1.0, parser.parseExpression("3-4"));
        assertEquals(-8.0, parser.parseExpression("3-4-7"));
    }

    @Test
    void testUnarySubtraction() {
        // Simple unary subtraction
        assertEquals(-5, parser.parseExpression("–5"), 0.001);
        // Unary subtraction with addition
        assertEquals(-1, parser.parseExpression("–5 + 4"), 0.001);
        // Unary subtraction with multiplication
        assertEquals(-20, parser.parseExpression("–5×4"), 0.001);
        // Unary subtraction with parentheses
        assertEquals(-9, parser.parseExpression("–(4+5)"), 0.001);
        // Multiple unary operators
        assertEquals(5, parser.parseExpression("–(–5)"), 0.001);
        // Unary subtraction in complex expression
        assertEquals(6.0, parser.parseExpression("3 × (–4 + 6)"), 0.001);
        assertEquals(27.0, parser.parseExpression("–(–(5+4)×3)"), 0.001);
    }

    @Test
    void testMultiplication() {
        assertEquals(20.0, parser.parseExpression("5×4"));
    }

    @Test
    void testDivision() {
        assertEquals(2.0, parser.parseExpression("8÷4"));
        assertEquals(0.75, parser.parseExpression("3÷4"));
    }

    @Test
    void testDecimalHandling() {
        assertEquals(5.5, parser.parseExpression("2.5+3"));
    }

    @Test
    void testParentheses() {
        assertEquals(7.0, parser.parseExpression("(3+4)×(2-1)"));
    }

    @Test
    void testImplicitMultiplication() {
        assertEquals(6.0, parser.parseExpression("(3)(2)"));
        assertEquals(1.8, parser.parseExpression(".6(3)"), 0.001);
    }

    @Test
    void testAllOperators() {
        assertEquals(0.975, parser.parseExpression(".6+3÷4(2)÷(2+4×(2-2))÷2"), 0.001);
        assertEquals(11.25, parser.parseExpression("(5-2)4÷2+7÷(3+1)×(6÷2)"), 0.001);
        assertEquals(9.52857142857, parser.parseExpression("(3.5-1.2)2.8÷1.4+6.9÷(2.1+0.7)(5.4÷2.7)"), 0.001);
        assertEquals(2.25, parser.parseExpression("–(0.5+2-3÷3)÷–(2÷3)"), 0.001);
        assertEquals(-19.257142857142856, parser.parseExpression("–(–3.5-1.2)2.8÷–1.4+6.9÷(–2.1+0.7)(5.4÷2.7)"), 0.001);
        assertEquals(16.0, parser.parseExpression("((8-6)^(0.25÷0.125))^2"), 0.001);
    }

    @Test
    void testInvalidExpression() {
        assertThrows(IllegalArgumentException.class, () -> parser.parseExpression("(3).6"));
    }

    // Add more tests for edge cases and complex expressions
}
