package test.model;

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
        assertEquals(0.0, parser.parseExpression("–3+3"));
        assertEquals(10.0, parser.parseExpression("5+2+3"));
    }


    @Test
    void testBinarySubtraction() {
        assertEquals(1.0, parser.parseExpression("5-4"));
        assertEquals(-1.0, parser.parseExpression("3-4"));
        assertEquals(-8.0, parser.parseExpression("3-4-7"));
        assertEquals(0.0, parser.parseExpression("5-2-3"));
    }

    @Test
    void testUnarySubtraction() {
        assertEquals(-5, parser.parseExpression("–5"), 0.001);
        assertEquals(-1, parser.parseExpression("–5+4"), 0.001);
        assertEquals(-20, parser.parseExpression("–5×4"), 0.001);
        assertEquals(-9, parser.parseExpression("–(4+5)"), 0.001);
        assertEquals(5, parser.parseExpression("–(–5)"), 0.001);
        assertEquals(6.0, parser.parseExpression("3×(–4+6)"), 0.001);
        assertEquals(27.0, parser.parseExpression("–(–(5+4)×3)"), 0.001);
    }

    @Test
    void testMultiplication() {
        assertEquals(20.0, parser.parseExpression("5×4"));
        assertEquals(0.0, parser.parseExpression("0×4"));
        assertEquals(-15.0, parser.parseExpression("–3×5"));
        assertEquals(12.0, parser.parseExpression("3×4×1"));
        assertEquals(-36.0, parser.parseExpression("3×4×–3"));
    }


    @Test
    void testDivision() {
        assertEquals(2.0, parser.parseExpression("8÷4"));
        assertEquals(0.75, parser.parseExpression("3÷4"));
        assertEquals(-2.0, parser.parseExpression("–8÷4"));
        assertEquals(0.5, parser.parseExpression("1÷2"));
        assertThrows(ArithmeticException.class, () -> parser.parseExpression("10÷0"));
    }


    @Test
    void testDecimalHandling() {
        assertEquals(5.5, parser.parseExpression("2.5+3"));
        assertEquals(7.3, parser.parseExpression("4.1+3.2"));
        assertEquals(-2.56, parser.parseExpression("–1.28×2"));
        assertEquals(3.333333333, parser.parseExpression("10÷3"), 0.000000001);
    }


    @Test
    void testParentheses() {
        assertEquals(7.0, parser.parseExpression("(3+4)×(2-1)"));
        assertEquals(14.0, parser.parseExpression("(3+4)×(2)"));
        assertEquals(21.0, parser.parseExpression("3×(7-1+1)"));
        assertEquals(-4.0, parser.parseExpression("–(3+1)"));
    }


    @Test
    void testImplicitMultiplication() {
        assertEquals(6.0, parser.parseExpression("(3)(2)"));
        assertEquals(1.8, parser.parseExpression(".6(3)"), 0.001);
        assertEquals(-6.0, parser.parseExpression("–(3)(2)"));
        assertEquals(9.0, parser.parseExpression("(3)(3)"));
    }

    @Test
    void testEulerMultiplication() {
        assertEquals(Math.E * 5, parser.parseExpression("e5"), 0.001);
        assertEquals(5 * Math.E, parser.parseExpression("5e"), 0.001);
        assertEquals(Math.E * 5 * 2, parser.parseExpression("5e×2"), 0.001);
        assertEquals(Math.E * 5 + 2, parser.parseExpression("5e+2"), 0.001);
    }

    @Test
    void testPi() {
        assertEquals(Math.PI * 2, parser.parseExpression("2π"), 0.001);
        assertEquals(Math.PI * 2, parser.parseExpression("π2"), 0.001);
        assertEquals(Math.PI * 2, parser.parseExpression("π×2"), 0.001);
    }

    @Test
    void testLogFunction() {
        assertEquals(1.0, parser.parseExpression("log(10)"), 0.001);
        assertEquals(2.0, parser.parseExpression("log(100)"), 0.001);
        assertEquals(0.0, parser.parseExpression("log(1)"), 0.001);
        assertEquals(3.0, parser.parseExpression("2+log(10)"), 0.001);
        assertEquals(1.0, parser.parseExpression("log(100)-log(10)"), 0.001);
        assertEquals(0.5, parser.parseExpression("log(10)÷log(100)"), 0.001);
    }

    @Test
    void testLnFunction() {
    assertEquals(3.30258509299, parser.parseExpression("1+ln(10)"), 0.001);
    assertEquals(1.60943791243, parser.parseExpression("ln(10)-ln(2)"), 0.001);
    assertEquals(3.32192809489, parser.parseExpression("ln(10)÷ln(2)"), 0.001);
    }

    @Test
    void testSqrtFunction() {
        assertEquals(3.0, parser.parseExpression("√9"));
        assertEquals(1.41421356237, parser.parseExpression("√2"), 0.001);
        assertEquals(4.0, parser.parseExpression("2+√4"), 0.001);
        assertEquals(4.0, parser.parseExpression("2√4"), 0.001);
        assertEquals(5.0, parser.parseExpression("√25+√0"), 0.001);
        assertEquals(5.41421356237, parser.parseExpression("√2+2×√4"), 0.001);
        assertEquals(3.0, parser.parseExpression("√(2+7.5-0.5)"));
        assertEquals(2.0, parser.parseExpression("√(2^√4)"));
    }

    @Test
    void testAllOperators() {
        assertEquals(0.975, parser.parseExpression(".6+3÷4(2)÷(2+4×(2-2))÷2"), 0.001);
        assertEquals(11.25, parser.parseExpression("(5-2)4÷2+7÷(3+1)×(6÷2)"), 0.001);
        assertEquals(9.52857142857, parser.parseExpression("(3.5-1.2)2.8÷1.4+6.9÷(2.1+0.7)(5.4÷2.7)"), 0.001);
        assertEquals(2.25, parser.parseExpression("–(0.5+2-3÷3)÷–(2÷3)"), 0.001);
        assertEquals(-19.257142857142856, parser.parseExpression("–(–3.5-1.2)2.8÷–1.4+6.9÷(–2.1+0.7)(5.4÷2.7)"), 0.001);
        assertEquals(16.0, parser.parseExpression("((8-6)^(0.25÷0.125))^2"), 0.001);

        assertEquals(Math.E * Math.PI + Math.sqrt(4), parser.parseExpression("e×π+√4"), 0.001);
        assertEquals(Math.log10(100) * Math.log(Math.E), parser.parseExpression("log(100)×ln(e)"), 0.001);
        assertEquals(Math.sqrt(Math.log10(1000)), parser.parseExpression("√(log(1000))"), 0.001);
        assertEquals(Math.log(Math.E) / Math.log10(100), parser.parseExpression("ln(e)÷log(100)"), 0.001);
        assertEquals(Math.pow(Math.E, Math.PI), parser.parseExpression("e^π"), 0.001);
    }

    @Test
    void testInvalidDecimalExpression() {
        assertThrows(IllegalArgumentException.class, () -> parser.parseExpression("(3).6"));
    }

    @Test
    void testInvalidUnarySubtract() {
        assertThrows(IllegalArgumentException.class,() -> parser.parseExpression("–(–3)–(3)"));
    }

    @Test
    void testMismatchedParentheses() {
        assertThrows(IllegalArgumentException.class, () -> parser.parseExpression("(2 + 3"));
        assertThrows(IllegalArgumentException.class, () -> parser.parseExpression("2 + 3)"));
        assertThrows(IllegalArgumentException.class, () -> parser.parseExpression("((2 + 3"));
        assertThrows(IllegalArgumentException.class, () -> parser.parseExpression("2 + 3))"));
    }


    @Test
    void testIncorrectOperatorUsage() {
        assertThrows(IllegalArgumentException.class, () -> parser.parseExpression("2++3"));
        assertThrows(IllegalArgumentException.class, () -> parser.parseExpression("2+÷3"));
    }

    @Test
    void testInvalidSyntax() {
        assertThrows(IllegalArgumentException.class, () -> parser.parseExpression("×2+3"));
        assertThrows(IllegalArgumentException.class, () -> parser.parseExpression("2+"));
    }

    @Test
    void testInvalidNumberFormats() {
        assertThrows(IllegalArgumentException.class, () -> parser.parseExpression("2..5+3"));
        assertThrows(IllegalArgumentException.class, () -> parser.parseExpression("2.3.4+5"));
    }

    @Test
    void testInvalidSqrtFunction() {
        assertThrows(IllegalArgumentException.class, () -> parser.parseExpression("√-4"));
    }
}
