package ui;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

class RoundedButton extends JButton {
    private int cornerRadius;

    public RoundedButton(String label, int cornerRadius) {
        super(label);
        this.cornerRadius = cornerRadius;
        setContentAreaFilled(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        setBorder(new LineBorder(Color.WHITE, 2)); // Set the white border
        g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius));
        g2.setColor(getForeground());
        g2.drawString(getText(), getWidth() / 2 - g.getFontMetrics().stringWidth(getText()) / 2,
                getHeight() / 2 + g.getFontMetrics().getAscent() / 2 - 2);
        g2.dispose();
    }

    @Override
    protected void paintBorder(Graphics g) {
        // Optional: Implement custom border painting if needed
    }
}

