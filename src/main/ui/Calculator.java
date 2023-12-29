package main.ui;

import main.model.MathParser;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.Map;


public class Calculator extends JFrame {
    private JTextField displayField;
    private JPanel buttonPanel;
    private boolean calculationPerformed; // Flag to indicate if calculation was performed

    public Calculator() {
        setIconImage(loadIconImage()); // Set custom icon

        initDisplayField();
        initButtonPanel();
        layoutComponents();

        setSize(250, 420);
        setTitle("Calculator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setLocationRelativeTo(null); // Center the frame on the screen
    }

    private Image loadIconImage() {
        ImageIcon icon = new ImageIcon("calculator.jpg");
        return icon.getImage();
    }


    private void initDisplayField() {
        displayField = new JTextField();
        displayField.setHorizontalAlignment(JTextField.CENTER);
        displayField.setEditable(false);
        displayField.setFont(new Font("Lucida Sans", Font.BOLD, 20));
        displayField.setBackground(Color.BLACK);
        displayField.setForeground(Color.WHITE);
        displayField.setBorder(new EmptyBorder(10, 10, 10, 10)); // Set padding around the display field
    }

    private void initButtonPanel() {
        Map<String, Color> buttonColors = defineButtonColors();

        buttonPanel = new JPanel(new BorderLayout(10, 10)); // Use BorderLayout
        buttonPanel.setBackground(Color.BLACK);
        buttonPanel.setBorder(new EmptyBorder(10, 10, 10, 10)); // Add padding around the panel

        JPanel gridPanel = new JPanel(new GridLayout(5, 4, 10, 10)); // 5 rows, 3 cols, 10px gaps
        gridPanel.setBackground(Color.BLACK);

        String[] buttons = {
            "+", "-", "×", "÷",
            "7", "8", "9", "(",
            "4", "5", "6", ")",
            "1", "2", "3", "^",
            "0", ".", "(–)", "CE"
        };

        for (String buttonText : buttons) {
            Color buttonColor = buttonColors.getOrDefault(buttonText, Color.WHITE);
            // set text colour and border colour for all buttons except '='
            RoundedButton button = new RoundedButton(buttonText, 20, Color.WHITE, buttonColor);
            button.setBackground(Color.BLACK); // Button color
            button.setFont(new Font("Lucida Sans", Font.BOLD, 20)); // Set the custom font here
            button.addActionListener(e -> buttonClicked(e.getActionCommand()));
            gridPanel.add(button);
        }

        buttonPanel.add(gridPanel, BorderLayout.CENTER);
        buttonPanel.add(createEqualsButton(), BorderLayout.SOUTH); // Add "=" button to the bottom
    }

    private Map<String, Color> defineButtonColors() {
        Map<String, Color> buttonColors = new HashMap<>();

        Color red = new Color(255, 75, 89);
        Color lightRed = new Color(255, 89, 89);
        Color redOrange = new Color(250, 110, 91);
        Color lightOrange = new Color(255, 130, 102);
        Color OrangeYellow = new Color(255, 165, 102);
        Color Yellow = new Color(255, 190, 102);

//        String[] buttons = {
//                "+", "-", "×", "÷",
//                "7", "8", "9", "(",
//                "4", "5", "6", ")",
//                "1", "2", "3", "^",
//                "0", ".", "(–)", "CE"
//        };

        buttonColors.put("0", lightRed);
        buttonColors.put(".", lightRed);
        buttonColors.put("(–)", lightRed);
        buttonColors.put("CE", lightRed);

        buttonColors.put("1", redOrange);
        buttonColors.put("2", redOrange);
        buttonColors.put("3", redOrange);
        buttonColors.put("^", redOrange);

        buttonColors.put("4", lightOrange);
        buttonColors.put("5", lightOrange);
        buttonColors.put("6", lightOrange);
        buttonColors.put(")", lightOrange);

        buttonColors.put("7", OrangeYellow);
        buttonColors.put("8", OrangeYellow);
        buttonColors.put("9", OrangeYellow);
        buttonColors.put("(", OrangeYellow);

        buttonColors.put("+", Yellow);
        buttonColors.put("-", Yellow);
        buttonColors.put("×", Yellow);
        buttonColors.put("÷", Yellow);

        buttonColors.put("=", red);
        return buttonColors;
    }

    private JButton createEqualsButton() {
        Color buttonColor = defineButtonColors().getOrDefault("=", Color.WHITE);
        // Set text colour and border colour for '=' button
        RoundedButton button = new RoundedButton("=", 20, Color.WHITE, buttonColor);
        button.setBackground(Color.BLACK); // Button color
//        button.setFont(new Font("SansSerif", Font.BOLD, 20)); // Increase font size
         button.setFont(new Font("Lucida Sans", Font.BOLD, 20)); // Set the custom font here
        button.setMargin(new Insets(5, 5, 5, 5)); // Set button margins
        button.addActionListener(e -> buttonClicked(e.getActionCommand()));
        return button;
    }

    private void buttonClicked(String buttonText) {
        if (buttonText.equals("=")) {
            try {
                performCalculation();
            } catch (IllegalArgumentException e) {
                displayField.setText("SYNTAX ERROR");
            } catch (ArithmeticException e) {
                displayField.setText("ARITHMETIC ERROR");
            } catch (EmptyStackException e) {

            }
        } else if (buttonText.equals("CE")) {
            resetDisplayField();
            handleDelete();
        } else if (buttonText.equals("(–)")) {
            handleInput("–");
        } else {
            resetDisplayField();
            handleInput(buttonText);
        }
    }

    private void resetDisplayField() {
        if (calculationPerformed || displayField.getText().equals("SYNTAX ERROR") ||
                displayField.getText().equals("ARITHMETIC ERROR")) {
            displayField.setText("");
        }
        calculationPerformed = false;
    }

    private void performCalculation() {
        String userInput = displayField.getText();
        double result = new MathParser().parseExpression(userInput);

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
