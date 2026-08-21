package tester_app.helpers;

import static tester_app.helpers.Constants.margin;
import static tester_app.helpers.Constants.selectionColor;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JButton;

public class RoundedButton extends JButton {
    private int radius;
    private Boolean
        borderPaint,
        borderState;
    private Color
        buttonSelectionColor,
        buttonBorderColor;

    public RoundedButton(String text) { 
        super(text);
        
        setContentAreaFilled(false);
        setFocusable(false);

        this.radius = 10;
        this.borderPaint = true;
        this.borderState = true;
        this.setSize(this.getSize().width + margin, this.getSize().height + margin);
    }
    public RoundedButton() { 
        super();

        setContentAreaFilled(false);
        setFocusable(false);

        this.radius = 10;
        this.borderPaint = true;
        this.borderState = true;
        this.setSize(this.getSize().width + margin, this.getSize().height + margin);
    }
    
    // SETTERS
    public void setSelectionColor(Color color) {
        buttonSelectionColor = color;
    }

    public void setBorder(Boolean borderState) {
        this.borderState = borderState;
    }

    public void setBorderColor(Color buttonBorderColor) {
        this.buttonBorderColor = buttonBorderColor;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (getModel().isRollover()) {
            setForeground(selectionColor);

            if(buttonSelectionColor != null) {
                g2.setColor(buttonSelectionColor);
                this.borderPaint = false;
            } else {
                g2.setColor(new Color(
                    getBackground().getBlue() - 10 > 0 ? getBackground().getRed() - 10 : 0,
                    getBackground().getBlue() - 10 > 0 ? getBackground().getGreen() - 10 : 0,
                    getBackground().getBlue() - 10 > 0 ? getBackground().getBlue() - 10 : 0
                ));
            }
        } else {
            setForeground(Color.WHITE);
            
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

        if(borderPaint || borderState) {
            if (getModel().isRollover()) {
                if(buttonSelectionColor == null ) {
                    if(buttonBorderColor != null) {
                        g2.setColor(new Color(
                            buttonBorderColor.getRed() - 10 > 0 ? buttonBorderColor.getRed() - 10 : 0,
                            buttonBorderColor.getRed() - 10 > 0 ? buttonBorderColor.getGreen() - 10 : 0,
                            buttonBorderColor.getRed() - 10 > 0 ? buttonBorderColor.getBlue() - 10 : 0
                        ));
                    } else {
                        g2.setColor(new Color(
                            getBackground().brighter().getRed() - 10,
                            getBackground().brighter().getGreen() - 10,
                            getBackground().brighter().getBlue() - 10
                        ));
                    }
                }
            } else {
                if(buttonSelectionColor == null) {
                    g2.setColor(getBackground().brighter());
                } else if(buttonBorderColor != null) {
                    g2.setColor(buttonBorderColor);
                } else {
                    g2.setColor(getBackground());
                }
            }
            if(buttonSelectionColor == null) {
                g2.drawRoundRect(margin, margin, getWidth() - 1 - margin * 2, getHeight() - 1 - margin * 2, radius, radius);
            }
        }
    }

    @Override
    public void setBorderPainted(boolean b) {
        this.borderPaint = b;

        super.setBorderPainted(b);
    }
}
