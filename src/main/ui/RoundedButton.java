package main.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

class RoundedButton extends JButton {
    private final int cornerRadius;
    private Color borderColor;
    private final Color originalBorderColor;
    private Color currentRainbowColor; // Add this line to store the current color of the rainbow cycle
    private Timer fadeOutTimer;
    private Timer rainbowTimer;
    private final Color hoverBorderColor = new Color(144, 238, 144);
    private float hue = 0.0f; // Hue for the HSB color model

    public RoundedButton(String label, int cornerRadius, Color textColor, Color borderColor) {
        super(label);
        this.cornerRadius = cornerRadius;
        this.originalBorderColor = borderColor;
        this.borderColor = borderColor;

        setContentAreaFilled(false);
        setFocusPainted(false);
        setForeground(textColor);
        setFont(new Font("SansSerif", Font.BOLD, 24));
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (fadeOutTimer != null && fadeOutTimer.isRunning()) {
                    fadeOutTimer.stop();
                }
                RoundedButton.this.borderColor = hoverBorderColor;
                repaint();
            }

            public void mouseExited(MouseEvent e) {
                final int STEPS = 10; // Increase the number of steps
                fadeOutTimer = new Timer(5, null); // Decrease the delay
                fadeOutTimer.addActionListener(new ActionListener() {
                    int step = 0;
                    Color startColor = hoverBorderColor;
                    Color endColor = borderColor;
                    public void actionPerformed(ActionEvent evt) {
                        float ratio = (float)step / (float)STEPS;
                        int red = (int)(startColor.getRed() * (1 - ratio) + endColor.getRed() * ratio);
                        int green = (int)(startColor.getGreen() * (1 - ratio) + endColor.getGreen() * ratio);
                        int blue = (int)(startColor.getBlue() * (1 - ratio) + endColor.getBlue() * ratio);
                        RoundedButton.this.borderColor = new Color(red, green, blue);
                        repaint();
                        step++;
                        if (step > STEPS) {
                            ((Timer)evt.getSource()).stop();
                        }
                    }
                });
                fadeOutTimer.start();
            }
        });
        rainbowTimer = new Timer(100, null);
        rainbowTimer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                hue = (hue + 0.01f) % 1.0f;
                currentRainbowColor = Color.getHSBColor(hue, 1.0f, 1.0f); // Update the current color of the rainbow cycle
                RoundedButton.this.setForeground(currentRainbowColor); // Set the text color to the current color of the rainbow cycle
                repaint();
            }
        });
        rainbowTimer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Adjust shape to accommodate border
        int offset = 1; // Offset for the border
        Shape shape = new RoundRectangle2D.Float(offset, offset, getWidth() - offset * 2, getHeight() - offset * 2, cornerRadius, cornerRadius);
        g2.setColor(getBackground());
        g2.fill(shape);

        // Draw the text
        FontMetrics fm = g2.getFontMetrics();
        Rectangle stringBounds = fm.getStringBounds(this.getText(), g2).getBounds();
        int textX = (getWidth() - stringBounds.width) / 2;
        int textY = (getHeight() - stringBounds.height) / 2 + fm.getAscent();
        g2.setColor(getForeground());
        g2.drawString(getText(), textX, textY);

        g2.dispose();
    }

    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setStroke(new BasicStroke(2));
        g2.setColor(borderColor);
        int offset = 1;
        Shape shape = new RoundRectangle2D.Float(offset, offset, getWidth() - offset * 2 - 1, getHeight() - offset * 2 - 1, cornerRadius, cornerRadius);
        g2.draw(shape);
        g2.dispose();
    }
}
