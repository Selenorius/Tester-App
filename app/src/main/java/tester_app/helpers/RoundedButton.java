package tester_app.helpers;

import static tester_app.helpers.Constants.margin;
import static tester_app.helpers.Constants.selectionColor;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.RenderingHints;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class RoundedButton extends JButton {
    private int radius;
    private Boolean
        borderPaint,
        borderState;
    private Color
        buttonSelectionColor,
        buttonBorderColor;
    
    private JLabel label;
    private GridBagLayout layout;
    private GridBagConstraints constraints;

    public RoundedButton(String text) { 
        super();

        layout = new GridBagLayout();
        constraints = new GridBagConstraints();

        label = new JLabel(text);
        label.setForeground(Color.WHITE);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);
        
        this.setContentAreaFilled(false);
        this.setFocusable(false);
        this.setDoubleBuffered(true);
        this.setOpaque(false);
        this.setLayout(layout);

        this.radius = 10;
        this.borderPaint = true;
        this.borderState = true;
        this.setSize(this.getSize().width + margin, this.getSize().height + margin);

        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 0.5;
        constraints.weighty = 0.5;
        constraints.anchor = GridBagConstraints.CENTER;

        this.add(label, constraints);
    }
    public RoundedButton() { 
        super();

        layout = new GridBagLayout();
        constraints = new GridBagConstraints();

        label = new JLabel();
        label.setForeground(Color.WHITE);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);

        this.setContentAreaFilled(false);
        this.setFocusable(false);
        this.setDoubleBuffered(true);
        this.setOpaque(false);
        this.setLayout(layout);

        this.radius = 10;
        this.borderPaint = true;
        this.borderState = true;
        this.setSize(this.getSize().width + margin, this.getSize().height + margin);

        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 0.5;
        constraints.weighty = 0.5;
        constraints.anchor = GridBagConstraints.CENTER;

        this.add(label, constraints);
    }

    // GETTERS
    public Icon getButtonIcon() {
        return label.getIcon();
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

    public void setButtonIcon(Icon defaultIcon) {
        if(label != null) {
            label.setIcon(defaultIcon);
        }
    }

    @Override
    public void setIconTextGap(int iconTextGap) {
        if(label != null) {
            label.setIconTextGap(iconTextGap);
        } else {
            super.setIconTextGap(iconTextGap);
        }
    }

    @Override
    public void setText(String text) {
        if(label != null) {
            label.setText(text);
        } else {
            super.setText(text);
        }
    }

    @Override
    public void setHorizontalTextPosition(int textPosition) {
        if(label != null) {
            label.setHorizontalTextPosition(textPosition);
        } else {
            super.setHorizontalTextPosition(textPosition);
        }
    }

    @Override
    public void setVerticalTextPosition(int textPosition) {
        if(label != null) {
            label.setVerticalTextPosition(textPosition);
        } else {
            super.setVerticalTextPosition(textPosition);
        }
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
                    getBackground().getRed() - getBackground().getRed() / 6 > 0 ? getBackground().getRed() - getBackground().getRed() / 6 : 0,
                    getBackground().getGreen() - getBackground().getGreen() / 6 > 0 ? getBackground().getGreen() - getBackground().getGreen() / 6 : 0,
                    getBackground().getBlue() - getBackground().getBlue() / 6 > 0 ? getBackground().getBlue() - getBackground().getBlue() / 6 : 0
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
                            buttonBorderColor.getRed() - buttonBorderColor.getRed() / 6 > 0 ? buttonBorderColor.getRed() - buttonBorderColor.getRed() / 6 : 0,
                            buttonBorderColor.getGreen() - buttonBorderColor.getGreen() / 6 > 0 ? buttonBorderColor.getGreen() - buttonBorderColor.getGreen() / 6 : 0,
                            buttonBorderColor.getBlue() - buttonBorderColor.getBlue() / 6 > 0 ? buttonBorderColor.getBlue() - buttonBorderColor.getBlue() / 6 : 0
                        ));
                    } else {
                        g2.setColor(new Color(
                            getBackground().brighter().getRed() - getBackground().brighter().getRed() / 6 > 0 ? getBackground().brighter().getRed() - getBackground().brighter().getRed() / 6 : 0,
                            getBackground().brighter().getGreen() - getBackground().brighter().getGreen() / 6 > 0 ? getBackground().brighter().getGreen() - getBackground().brighter().getGreen() / 6 : 0,
                            getBackground().brighter().getBlue() - getBackground().brighter().getBlue() / 6 > 0 ? getBackground().brighter().getBlue() - getBackground().brighter().getBlue() / 6 : 0
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
