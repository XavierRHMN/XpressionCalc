package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

class RoundedButton extends JButton {
    private int cornerRadius;
    private Color borderColor;
    private Color originalBorderColor = Color.WHITE;
    private Color hoverBorderColor = new Color(144, 238, 144);

    public RoundedButton(String label, int cornerRadius) {
        super(label);
        this.cornerRadius = cornerRadius;
        setContentAreaFilled(false);
        setFocusPainted(false);
        borderColor = originalBorderColor;

        setFont(new Font("SansSerif", Font.BOLD, 24));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                borderColor = hoverBorderColor;
                setForeground(hoverBorderColor);
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                borderColor = originalBorderColor;
                setForeground(Color.WHITE);
                repaint();
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
