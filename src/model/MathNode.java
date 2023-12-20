package model;

import java.util.Arrays;

public class MathNode {
    String value;
    MathNode argument;
    MathNode left;
    MathNode right;

    public MathNode(String value) {
        this.value = value;
        this.left = null;
        this.right = null;
    }

    public MathNode(String value, MathNode left, MathNode right) {
        this.value = value;
        this.left = left;
        this.right = right;
    }

    private boolean isFunction() {
        // Check if the value represents a function
        return Arrays.asList("sin", "cos", "exp").contains(value);
    }

    // Computes the node
    public float compute(MathNode node) {
        if (isOperator(node.value)) {
            float left = compute(node.left);
            float right = compute(node.right);

            switch (node.value) {
                case "+":
                    return left + right;
                case "-":
                    return left - right;
                case "*":
                    return left * right;
                case "/":
                    if (right == 0) throw new ArithmeticException("Division by zero");
                    return left / right;
                default:
                    throw new IllegalArgumentException("Unknown operator: " + node.value);
            }
        } else {
            // Assuming the node is a number
            return Float.parseFloat(node.value);
        }
    }

    private boolean isOperator(String value) {
        return "+".equals(value) || "-".equals(value) ||
                "*".equals(value) || "/".equals(value);
    }


    // Converts the node back to string for display
    @Override
    public String toString() {
        if (left == null && right == null) {
            return value;
        }
        return "(" + left.toString() + value + right.toString() + ")";
    }
}


