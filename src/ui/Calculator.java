package ui;

import model.MathNode;
import model.MathParser;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Map;
import java.util.HashMap;


public class Calculator extends JFrame {
    private JTextField displayField;
    private JPanel buttonPanel;
    private boolean calculationPerformed; // Flag to indicate if calculation was performed

    public Calculator() {
        setIconImage(loadIconImage("calculator.jpg")); // Set custom icon

        initDisplayField();
        initButtonPanel();
        layoutComponents();

        setSize(300, 400);
        setTitle("Calculator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setLocationRelativeTo(null); // Center the frame on the screen
    }

    private Image loadIconImage(String path) {
        ImageIcon icon = new ImageIcon(path);
        return icon.getImage();
    }


    private void initDisplayField() {
        displayField = new JTextField();
        displayField.setHorizontalAlignment(JTextField.CENTER);
        displayField.setEditable(false);
        displayField.setFont(new Font("SansSerif", Font.BOLD, 20));
        displayField.setBackground(Color.BLACK);
        displayField.setForeground(Color.WHITE);
        displayField.setBorder(new EmptyBorder(10, 10, 10, 10)); // Set padding around the display field
    }

    private void initButtonPanel() {
        Map<String, Color> buttonColors = defineButtonColors();


        buttonPanel = new JPanel(new BorderLayout(10, 10)); // Use BorderLayout
        buttonPanel.setBackground(Color.BLACK);
        buttonPanel.setBorder(new EmptyBorder(10, 10, 10, 10)); // Add padding around the panel

        JPanel gridPanel = new JPanel(new GridLayout(5, 3, 10, 10)); // 5 rows, 3 cols, 10px gaps
        gridPanel.setBackground(Color.BLACK);

        String[] buttons = {
                "1", "2", "3",
                "4", "5", "6",
                "7", "8", "9",
                "0", "+", "-",
                "×", "÷", "DEL"
        };

        for (String buttonText : buttons) {
            RoundedButton button = new RoundedButton(buttonText, 20); // Use RoundedButton with corner radius
            button.setForeground(Color.WHITE); // Text color
            button.setBackground(Color.BLACK); // Button color
            button.addActionListener(e -> buttonClicked(e.getActionCommand()));
            gridPanel.add(button);
        }

        buttonPanel.add(gridPanel, BorderLayout.CENTER);
        buttonPanel.add(createButton("="), BorderLayout.SOUTH); // Add "=" button to the bottom
    }

    private Map<String, Color> defineButtonColors() {
        Map<String, Color> buttonColors = new HashMap<>();
        buttonColors.put("1", Color.RED);
        buttonColors.put("2", Color.BLUE);
        buttonColors.put("3", Color.GREEN);
        // ... Add colors for other buttons ...
        return buttonColors;
    }

    private boolean isOperator(String buttonText) {
        return buttonText.equals("+") || buttonText.equals("-") ||
                buttonText.equals("*") || buttonText.equals("/") ||
                buttonText.equals("DEL") || buttonText.equals("=");
    }

    private JButton createButton(String buttonText) {
        RoundedButton button = new RoundedButton(buttonText, 20); // Use RoundedButton with corner radius
        button.setForeground(Color.WHITE); // Text color
        button.setBackground(Color.BLACK); // Button color
        button.setFont(new Font("SansSerif", Font.BOLD, 20)); // Increase font size
        button.setMargin(new Insets(5, 5, 5, 5)); // Set button margins
        button.addActionListener(e -> buttonClicked(e.getActionCommand()));
        return button;
    }

    private void buttonClicked(String buttonText) {
        if (buttonText.equals("=")) {
            try {
                performCalculation();
            } catch (Exception e) {
                displayField.setText("ERROR");
            }
        } else if (buttonText.equals("DEL")) {
            resetDisplayField();
            handleDelete();
        } else {
            resetDisplayField();
            handleInput(buttonText);
        }
    }

    private void resetDisplayField() {
        if (calculationPerformed || displayField.getText().equals("ERROR")) {
            displayField.setText("");
        }
        calculationPerformed = false;
    }

    private void performCalculation() {
        String userInput = displayField.getText();
        MathNode expression = new MathParser().parseExpression(userInput);
        float result = expression.compute(expression);

        // Check if the result is an integer
        if (result == (int) result) {
            displayField.setText(String.valueOf((int) result));
        } else {
            displayField.setText(String.valueOf(result));
        }
        calculationPerformed = true;
    }

    private void handleDelete() {
        String currentText = displayField.getText();
        if (!currentText.isEmpty()) {
            displayField.setText(currentText.substring(0, currentText.length() - 1));
        }
    }

    private void handleInput(String buttonText) {
        displayField.setText(displayField.getText() + buttonText);
    }

    private void layoutComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(0, 0)); // Main panel with BorderLayout
        mainPanel.setBorder(new EmptyBorder(0, 0, 0, 0)); // Remove padding

        mainPanel.add(displayField, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        setContentPane(mainPanel); // Set the main panel as the content pane
    }
}
