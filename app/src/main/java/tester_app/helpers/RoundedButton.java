package tester_app.helpers;

import static tester_app.helpers.Constants.buttonBorderColor;
import static tester_app.helpers.Constants.selectionColor;

import java.awt.Graphics;

import javax.swing.JButton;

public class RoundedButton extends JButton {
    private int radius;
    private Boolean borderPaint;

    public RoundedButton() { 
        super(); 
        setContentAreaFilled(false); 

        this.radius = 10;
        this.borderPaint = true;
    }

    @Override
    protected void paintComponent(Graphics g) { 
        if (getModel().isRollover()) { 
            g.setColor(selectionColor); 
        } else { 
            g.setColor(getBackground()); 
        }
        g.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius); 
        super.paintComponent(g); 
    }

    @Override
    protected void paintBorder(Graphics g) { 
        if(borderPaint) {
            g.setColor(buttonBorderColor);
            g.drawRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
        }
    }

    @Override
    public void setBorderPainted(boolean b) {
        this.borderPaint = b;

        super.setBorderPainted(b);
    }
}
