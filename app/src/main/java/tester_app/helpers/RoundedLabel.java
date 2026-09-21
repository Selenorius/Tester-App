package tester_app.helpers;

import static tester_app.helpers.Constants.addMargin;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class RoundedLabel extends JLabel {
    Boolean painted;

    public RoundedLabel(String text, int hor) {
        super("<html><center>" + text + "</html>", hor);

        this.painted = false;

        this.setHorizontalAlignment(hor);
        addMargin(this, 0);
    }
    public RoundedLabel(String text) {
        super("<html><center>" + text + "</html>");

        this.painted = false;

        this.setHorizontalAlignment(SwingConstants.CENTER);
    }
    public RoundedLabel() {
        super();

        this.painted = false;

        this.setHorizontalAlignment(SwingConstants.CENTER);
        addMargin(this, 0);
    }

    // SETTERS
    public void setPainted(Boolean painted) {
        this.painted = painted;

        addMargin(this, 0);
    }

    @Override
    public void setText(String text) {
        super.setText("<html><center>" + text + "</html>");
    }
    public void setText(String text, Boolean hasHTML) {
        super.setText(text);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

        if(painted) {
            int radius = 10;

            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) 0.25));
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
        }
        
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
        
        super.paintComponent(g2);

        g2.dispose();
    }
}
