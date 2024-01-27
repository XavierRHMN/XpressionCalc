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
    private Timer fadeOutTimer;
    private final Color hoverBorderColor = new Color(144, 238, 144);

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
                setForeground(hoverBorderColor); // Set the text color to green
                repaint();
            }

            public void mouseExited(MouseEvent e) {
                final int STEPS = 10;
                fadeOutTimer = new Timer(20, null);
                fadeOutTimer.addActionListener(new ActionListener() {
                    int step = 0;
                    public void actionPerformed(ActionEvent evt) {
                        float linearRatio = (float)step / (float)STEPS;
                        // Apply ease-out interpolation
                        float ratio = (float)Math.pow(linearRatio, 0.67);
            
                        int red = (int)(hoverBorderColor.getRed() * (1 - ratio) + originalBorderColor.getRed() * ratio);
                        int green = (int)(hoverBorderColor.getGreen() * (1 - ratio) + originalBorderColor.getGreen() * ratio);
                        int blue = (int)(hoverBorderColor.getBlue() * (1 - ratio) + originalBorderColor.getBlue() * ratio);
                        RoundedButton.this.borderColor = new Color(red, green, blue);
            
                        // Fade out the text color from green to white
                        int textRed = (int)(hoverBorderColor.getRed() * (1 - ratio) + Color.WHITE.getRed() * ratio);
                        int textGreen = (int)(hoverBorderColor.getGreen() * (1 - ratio) + Color.WHITE.getGreen() * ratio);
                        int textBlue = (int)(hoverBorderColor.getBlue() * (1 - ratio) + Color.WHITE.getBlue() * ratio);
                        Color mixedTextColor = new Color(textRed, textGreen, textBlue);
                        setForeground(mixedTextColor); // Change the text color
            
                        repaint();
                        step++;
                        if (step > STEPS) {
                            fadeOutTimer.stop();
                        }
                    }
                });
                fadeOutTimer.start();
            }
        });
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
