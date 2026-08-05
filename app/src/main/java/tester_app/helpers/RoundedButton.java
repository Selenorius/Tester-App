package tester_app.helpers;

import static tester_app.helpers.Constants.buttonBorderColor;
import static tester_app.helpers.Constants.margin;
import static tester_app.helpers.Constants.selectionColor;

import java.awt.Graphics;

import javax.swing.JButton;

public class RoundedButton extends JButton {
    private int radius;
    private Boolean borderPaint;

    public RoundedButton(String text) { 
        super(text);
        setContentAreaFilled(false); 

        this.radius = 10;
        this.borderPaint = true;
        this.setSize(this.getSize().width + margin, this.getSize().height + margin);
    }
    public RoundedButton() { 
        super();
        setContentAreaFilled(false); 

        this.radius = 10;
        this.borderPaint = true;
        this.setSize(this.getSize().width + margin, this.getSize().height + margin);
    }

    @Override
    protected void paintComponent(Graphics g) { 
        if (getModel().isRollover()) { 
            g.setColor(selectionColor);
            this.borderPaint = false;
        } else { 
            g.setColor(getBackground());
            if(!this.isSelected()) this.borderPaint = true;
        }
        g.fillRoundRect(margin, margin, getWidth() - margin * 2, getHeight() - margin * 2, radius, radius); 
        super.paintComponent(g); 
    }

    @Override
    protected void paintBorder(Graphics g) { 
        if(borderPaint) {
            g.setColor(buttonBorderColor);
            g.drawRoundRect(margin, margin, getWidth() - 1 - margin * 2, getHeight() - 1 - margin * 2, radius, radius);
        }
    }

    @Override
    public void setBorderPainted(boolean b) {
        this.borderPaint = b;

        super.setBorderPainted(b);
    }
}
