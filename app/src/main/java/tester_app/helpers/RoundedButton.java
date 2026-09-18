package tester_app.helpers;

import static tester_app.helpers.Constants.addMargin;
import static tester_app.helpers.Constants.buttonFont;
import static tester_app.helpers.Constants.margin;
import static tester_app.helpers.Constants.selectionColor;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.RenderingHints;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.SwingConstants;

public class RoundedButton extends JButton {
    private int radius;
    private Boolean
        borderPaint,
        borderState,
        isTransparent;
    private Color
        buttonSelectionColor,
        buttonBorderColor;
    private String buttonText;
    
    private RoundedLabel label;
    private GridBagLayout layout;
    private GridBagConstraints constraints;

    public RoundedButton(String text) { 
        super();

        layout = new GridBagLayout();
        constraints = new GridBagConstraints();

        label = new RoundedLabel();
        label.setForeground(Color.WHITE);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);
        label.setFont(buttonFont);
        
        this.setContentAreaFilled(false);
        this.setFocusable(false);
        this.setDoubleBuffered(true);
        this.setOpaque(false);
        this.setLayout(layout);
        addMargin(this, margin * 3);

        this.radius = 10;
        this.borderPaint = false;
        this.borderState = true;
        this.isTransparent = false;
        this.setSize(this.getSize().width + margin, this.getSize().height + margin);

        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.weightx = 0.5;
        constraints.weighty = 0.5;
        constraints.anchor = GridBagConstraints.CENTER;

        this.add(label, constraints);

        setText(text);
    }
    public RoundedButton() { 
        super();

        layout = new GridBagLayout();
        constraints = new GridBagConstraints();

        label = new RoundedLabel();
        label.setForeground(Color.WHITE);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);
        label.setFont(buttonFont);

        this.setContentAreaFilled(false);
        this.setFocusable(false);
        this.setDoubleBuffered(true);
        this.setOpaque(false);
        this.setLayout(layout);
        addMargin(this, margin * 3);

        this.radius = 10;
        this.borderPaint = false;
        this.borderState = true;
        this.isTransparent = false;
        this.setSize(this.getSize().width + margin, this.getSize().height + margin);

        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 0.5;
        constraints.weighty = 0.5;
        constraints.anchor = GridBagConstraints.CENTER;

        this.add(label, constraints);
    }

    // GETTERS
    public Icon getButtonIcon() {
        return label.getIcon();
    }

    public String getButtonText() {
        return buttonText;
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

    public void setTransparency(Boolean isTransparent) {
        this.isTransparent = isTransparent;
    }

    @Override
    public Color getForeground() {
        if(label != null) {
            return label.getForeground();
        }
        return super.getForeground();
    }

    @Override
    public void setForeground(Color color) {
        if(label != null) {
            label.setForeground(color);
        } else {
            super.setForeground(color);
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
        buttonText = text;

        int length = 60;
        String out = null;

        if(text != null) {
            if(text.length() < length) {
                out = text;
            } else {
                String line = null;

                out = "<html><center>";

                while(text.length() > length) {
                    line = text.substring(0, length);

                    if(line.contains(" ")) {
                        out += text.substring(0, line.lastIndexOf(" ") + 1) + "<br>";
                        text = text.substring(line.lastIndexOf(" ") + 1, text.length());
                    } else {
                        out += line + "<br>";
                        text = text.substring(length, text.length());
                    }
                }
                out += text + "</center></html>";
            }
        } else {
            out = "";
        }

        if(label != null) {
            label.setText(out);
        } else {
            super.setText(out);
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
        g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

        if (getModel().isRollover()) {
            setForeground(selectionColor);

            if(buttonSelectionColor != null) {
                g2.setColor(buttonSelectionColor);
                this.borderPaint = false;
            }
        } else {
            setForeground(Color.WHITE);

            if(!this.isSelected()) this.borderPaint = false;
        }

        g2.setColor(getBackground());

        if(isTransparent) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0));
        } else {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
        }
        g2.fillRoundRect(margin, margin, getWidth() - margin * 2, getHeight() - margin * 2, radius, radius); 
        super.paintComponent(g2);
    }

    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

        if (getModel().isRollover()) {
            if(buttonSelectionColor == null ) {
                g2.setColor(selectionColor);
                g2.drawRoundRect(margin, margin, getWidth() - 1 - margin * 2, getHeight() - 1 - margin * 2, radius, radius);
            }
        } else {
            if(borderPaint && borderState) {
                if(buttonSelectionColor == null) {
                    g2.setColor(getBackground().brighter().brighter().brighter());
                } else if(buttonBorderColor != null) {
                    g2.setColor(buttonBorderColor);
                } else {
                    g2.setColor(getBackground());
                }

                if(buttonSelectionColor == null) {
                    g2.drawRoundRect(margin, margin, getWidth() - 1 - margin * 2, getHeight() - 1 - margin * 2, radius, radius);
                }
            }
            
        }
    }

    @Override
    public void setBorderPainted(boolean b) {
        this.borderPaint = b;

        super.setBorderPainted(b);
    }
}
