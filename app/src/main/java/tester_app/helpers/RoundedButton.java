package tester_app.helpers;

import static tester_app.helpers.Constants.buttonBorderColor;
import static tester_app.helpers.Constants.margin;
import static tester_app.helpers.Constants.selectionColor;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JButton;

public class RoundedButton extends JButton {
    private int radius;
    private Boolean borderPaint;
    private Color
        rButtonBorderColor,
        rButtonSelectionColor;

    public RoundedButton(String text) { 
        super(text);
        
        setContentAreaFilled(false);
        setFocusable(false);

        this.radius = 10;
        this.borderPaint = true;
        this.rButtonBorderColor = buttonBorderColor;
        this.rButtonSelectionColor = selectionColor;
        this.setSize(this.getSize().width + margin, this.getSize().height + margin);
    }
    public RoundedButton() { 
        super();

        setContentAreaFilled(false);
        setFocusable(false);

        this.radius = 10;
        this.borderPaint = true;
        this.rButtonBorderColor = buttonBorderColor;
        this.rButtonSelectionColor = selectionColor;
        this.setSize(this.getSize().width + margin, this.getSize().height + margin);
    }
    
    // SETTERS
    public void setBorderColor(Color color) {
        rButtonBorderColor = color;
    }

    public void setSelectionColor(Color color) {
        rButtonSelectionColor = color;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (getModel().isRollover()) { 
            g2.setColor(rButtonSelectionColor);
            this.borderPaint = false;
        } else { 
            g2.setColor(getBackground());
            if(!this.isSelected()) this.borderPaint = true;
        }
        g2.fillRoundRect(margin, margin, getWidth() - margin * 2, getHeight() - margin * 2, radius, radius); 
        super.paintComponent(g2);
    }

    @Override
    protected void paintBorder(Graphics g) { 
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if(borderPaint) {
            g2.setColor(rButtonBorderColor);
            g2.drawRoundRect(margin, margin, getWidth() - 1 - margin * 2, getHeight() - 1 - margin * 2, radius, radius);
        }
    }

    @Override
    public void setBorderPainted(boolean b) {
        this.borderPaint = b;

        super.setBorderPainted(b);
    }
}
