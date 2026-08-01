package tester_app.helpers;

import static tester_app.helpers.Constants.borderColor;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

public class RoundedPanel extends JPanel {
    private int radius;
    private Boolean borderPaint;

    public RoundedPanel() {
        this.radius = 10;
        this.borderPaint = true;
        
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
    }

    @Override
    protected void paintBorder(Graphics g) {
        if(borderPaint) {
            g.setColor(borderColor);
            g.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, radius, radius);
        }
    }

    public void setBorderPainted(Boolean val) {
        this.borderPaint = val;
    }
}
