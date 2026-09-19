package tester_app.helpers;

import static tester_app.helpers.Constants.buttonFont;
import static tester_app.helpers.Constants.fieldColor;
import static tester_app.helpers.Constants.margin;
import static tester_app.helpers.Constants.selectionColor;
import static tester_app.helpers.Constants.textFont;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;

import javax.swing.JTextArea;

public class RoundedTextArea extends JTextArea {
    private RoundedLabel label;
    private GridBagLayout layout;
    private GridBagConstraints constraints;
    private Component parent;

    private int radius;
    private Color borderColor;
    private Boolean borderPainted;
    private ArrayList<Boolean> isHalfRect;
    private float opacity;

    public RoundedTextArea(String s, Component parent) {
        super();

        layout = new GridBagLayout();
        constraints = new GridBagConstraints();

        if(s != null) {
            if(!s.equals("null")) {
                this.setText(s);
            }
        }

        this.setLineWrap(true);
        this.setWrapStyleWord(true);
        this.setBackground(null);
        this.setForeground(Color.WHITE);
        this.setCaretColor(Color.WHITE);
        this.setFont(null);
        this.setMargin(new Insets(margin * 2, margin * 2, margin * 2, margin * 2));
        this.setFocusable(false);
        this.setDoubleBuffered(true);
        this.setSelectionColor(selectionColor);
        this.setSelectedTextColor(Color.BLACK);
        this.setLayout(layout);
        this.setOpaque(false);
        this.setFont(textFont);

        this.radius = 10;
        this.borderPainted = false;
        this.isHalfRect = new ArrayList<>();
        this.isHalfRect.add(false);
        this.isHalfRect.add(false);
        this.isHalfRect.add(false);
        this.isHalfRect.add(false);
        this.opacity = 1;
        this.parent = parent;

        if(this.parent != null) {
            if(this.parent.getBackground() != null) {
                this.setBackground(this.parent.getBackground());
                this.setBorderColor(this.getBackground().brighter().brighter().brighter());
            } else {
                this.setBackground(fieldColor);
                this.setBorderColor(this.getBackground().brighter().brighter().brighter());
            }
        } else {
            this.setBackground(fieldColor);
            this.setBorderColor(this.getBackground().brighter().brighter().brighter());
        }

        label = new RoundedLabel();
        label.setForeground(Color.WHITE.darker());
        if(!this.getText().isEmpty()) {
            label.setVisible(false);
        }
        label.setFont(buttonFont);

        constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.NONE;
        constraints.weightx = 0.5;
        constraints.weighty = 0.5;
        constraints.anchor = GridBagConstraints.NORTHWEST;

        this.add(label, constraints);

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                setFocusable(false);
                setForeground(Color.WHITE);

                if(getText().isEmpty()) {
                    label.setVisible(true);
                } else {
                    label.setVisible(false);
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                setFocusable(true);
                setForeground(selectionColor);
            }
        });

        this.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                label.setVisible(false);
                setForeground(Color.WHITE);
            }

            @Override
            public void focusLost(FocusEvent e) {}
        });
    }
    public RoundedTextArea(String s) {
        super();

        layout = new GridBagLayout();
        constraints = new GridBagConstraints();

        if(s != null) {
            if(!s.equals("null")) {
                this.setText(s);
            }
        }

        this.setLineWrap(true);
        this.setWrapStyleWord(true);
        this.setBackground(null);
        this.setForeground(Color.WHITE);
        this.setCaretColor(Color.WHITE);
        this.setFont(null);
        this.setMargin(new Insets(margin * 2, margin * 2, margin * 2, margin * 2));
        this.setFocusable(false);
        this.setDoubleBuffered(true);
        this.setSelectionColor(selectionColor);
        this.setSelectedTextColor(Color.BLACK);
        this.setLayout(layout);
        this.setOpaque(false);
        this.setFont(textFont);

        this.radius = 10;
        this.borderPainted = false;
        this.isHalfRect = new ArrayList<>();
        this.isHalfRect.add(false);
        this.isHalfRect.add(false);
        this.isHalfRect.add(false);
        this.isHalfRect.add(false);
        this.opacity = 1;

        if(this.parent != null) {
            if(this.parent.getBackground() != null) {
                this.setBackground(this.parent.getBackground());
                this.setBorderColor(this.getBackground().brighter().brighter().brighter());
            } else {
                this.setBackground(fieldColor);
                this.setBorderColor(this.getBackground().brighter().brighter().brighter());
            }
        } else {
            this.setBackground(fieldColor);
            this.setBorderColor(this.getBackground().brighter().brighter().brighter());
        }

        label = new RoundedLabel();
        label.setForeground(Color.WHITE.darker());
        if(!this.getText().isEmpty()) {
            label.setVisible(false);
        }
        label.setFont(buttonFont);

        constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.NONE;
        constraints.weightx = 0.5;
        constraints.weighty = 0.5;
        constraints.anchor = GridBagConstraints.NORTHWEST;

        this.add(label, constraints);

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                setFocusable(false);
                setForeground(Color.WHITE);

                if(getText().isEmpty()) {
                    label.setVisible(true);
                } else {
                    label.setVisible(false);
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                setFocusable(true);
                setForeground(selectionColor);
            }
        });

        this.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                label.setVisible(false);
                setForeground(Color.WHITE);
            }

            @Override
            public void focusLost(FocusEvent e) {}
        });
    }
    public RoundedTextArea(Component parent) {
        super();

        layout = new GridBagLayout();
        constraints = new GridBagConstraints();

        this.setLineWrap(true);
        this.setWrapStyleWord(true);
        this.setBackground(null);
        this.setForeground(Color.WHITE);
        this.setCaretColor(Color.WHITE);
        this.setFont(null);
        this.setMargin(new Insets(margin * 2, margin * 2, margin * 2, margin * 2));
        this.setFocusable(false);
        this.setDoubleBuffered(true);
        this.setSelectionColor(selectionColor);
        this.setSelectedTextColor(Color.BLACK);
        this.setLayout(layout);
        this.setOpaque(false);
        this.setFont(textFont);

        this.radius = 10;
        this.borderPainted = false;
        this.isHalfRect = new ArrayList<>();
        this.isHalfRect.add(false);
        this.isHalfRect.add(false);
        this.isHalfRect.add(false);
        this.isHalfRect.add(false);
        this.opacity = 1;
        this.parent = parent;

        if(this.parent != null) {
            if(this.parent.getBackground() != null) {
                this.setBackground(this.parent.getBackground());
                this.setBorderColor(this.getBackground().brighter().brighter().brighter());
            } else {
                this.setBackground(fieldColor);
                this.setBorderColor(this.getBackground().brighter().brighter().brighter());
            }
        } else {
            this.setBackground(fieldColor);
            this.setBorderColor(this.getBackground().brighter().brighter().brighter());
        }

        label = new RoundedLabel();
        label.setForeground(Color.WHITE.darker());
        if(!this.getText().isEmpty()) {
            label.setVisible(false);
        }
        label.setFont(buttonFont);

        constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.NONE;
        constraints.weightx = 0.5;
        constraints.weighty = 0.5;
        constraints.anchor = GridBagConstraints.NORTHWEST;

        this.add(label, constraints);

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                setFocusable(false);
                setForeground(Color.WHITE);

                if(getText().isEmpty()) {
                    label.setVisible(true);
                } else {
                    label.setVisible(false);
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                setFocusable(true);
                setForeground(selectionColor);
            }
        });

        this.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                label.setVisible(false);
                setForeground(Color.WHITE);
            }

            @Override
            public void focusLost(FocusEvent e) {}
        });
    }
    public RoundedTextArea() {
        super();

        layout = new GridBagLayout();
        constraints = new GridBagConstraints();

        this.setLineWrap(true);
        this.setWrapStyleWord(true);
        this.setBackground(null);
        this.setForeground(Color.WHITE);
        this.setCaretColor(Color.WHITE);
        this.setFont(null);
        this.setMargin(new Insets(margin * 2, margin * 2, margin * 2, margin * 2));
        this.setFocusable(false);
        this.setDoubleBuffered(true);
        this.setSelectionColor(selectionColor);
        this.setSelectedTextColor(Color.BLACK);
        this.setLayout(layout);
        this.setOpaque(false);
        this.setFont(textFont);

        this.radius = 10;
        this.borderPainted = false;
        this.isHalfRect = new ArrayList<>();
        this.isHalfRect.add(false);
        this.isHalfRect.add(false);
        this.isHalfRect.add(false);
        this.isHalfRect.add(false);
        this.opacity = 1;

        if(this.parent != null) {
            if(this.parent.getBackground() != null) {
                this.setBackground(this.parent.getBackground());
                this.setBorderColor(this.getBackground().brighter().brighter().brighter());
            } else {
                this.setBackground(fieldColor);
                this.setBorderColor(this.getBackground().brighter().brighter().brighter());
            }
        } else {
            this.setBackground(fieldColor);
            this.setBorderColor(this.getBackground().brighter().brighter().brighter());
        }

        label = new RoundedLabel();
        label.setForeground(Color.WHITE.darker());
        if(!this.getText().isEmpty()) {
            label.setVisible(false);
        }
        label.setFont(buttonFont);

        constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.NONE;
        constraints.weightx = 0.5;
        constraints.weighty = 0.5;
        constraints.anchor = GridBagConstraints.NORTHWEST;

        this.add(label, constraints);

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                setFocusable(false);
                setForeground(Color.WHITE);

                if(getText().isEmpty()) {
                    label.setVisible(true);
                } else {
                    label.setVisible(false);
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                setFocusable(true);
                setForeground(selectionColor);
            }
        });

        this.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                label.setVisible(false);
                setForeground(Color.WHITE);
            }

            @Override
            public void focusLost(FocusEvent e) {}
        });
    }

    @Override
    public void scrollRectToVisible(final Rectangle aRect) {}

    // GETTERS
    public String getPlaceholder() {
        return label.getText();
    }

    // SETTERS
    public void setPlaceholder(String text) {
        label.setText(text);
    }

    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
    }

    public void setBorderPainted(Boolean borderPainted) {
        this.borderPainted = borderPainted;
    }

    public void setHalfRect(Boolean nw, Boolean ne, Boolean se, Boolean sw) {
        this.isHalfRect.clear();

        this.isHalfRect.add(nw);
        this.isHalfRect.add(ne);
        this.isHalfRect.add(se);
        this.isHalfRect.add(sw);
    }

    public void setOpacity(double opacity) {
        if((float) opacity <= 1 && (float) opacity > 0) this.opacity = (float) opacity;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
        g2.setColor(getBackground());
        
        int
            width = getSize().width,
            height = getSize().height;
    
        Area base = new Area(new RoundRectangle2D.Double(0, 0, width, height, radius, radius));

        if(isHalfRect.get(0)) {
            Area cut = new Area(new Rectangle2D.Double(0, 0, radius, radius));
            base.add(cut);
        }
        if(isHalfRect.get(1)) {
            Area cut = new Area(new Rectangle2D.Double(getWidth() - radius, 0, radius, radius));
            base.add(cut);
        }
        if(isHalfRect.get(2)) {
            Area cut = new Area(new Rectangle2D.Double(getWidth() - radius, getHeight() - radius, radius, radius));
            base.add(cut);
        }
        if(isHalfRect.get(3)) {
            Area cut = new Area(new Rectangle2D.Double(0, getHeight() - radius, radius, radius));
            base.add(cut);
        }

        g2.fill(base);

        super.paintComponent(g2);
    }

    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

        if(borderPainted) {
            g2.setColor(borderColor);
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
            
            int
                width = getSize().width,
                height = getSize().height;
        
            Area base = new Area(new RoundRectangle2D.Double(0, 0, width - 1, height - 1, radius, radius));

            if(isHalfRect.get(0)) {
                Area cut = new Area(new Rectangle2D.Double(0, 0, radius, radius));
                base.add(cut);
            }
            if(isHalfRect.get(1)) {
                Area cut = new Area(new Rectangle2D.Double(getWidth() - radius - 1, 0, radius, radius));
                base.add(cut);
            }
            if(isHalfRect.get(2)) {
                Area cut = new Area(new Rectangle2D.Double(getWidth() - radius - 1, getHeight() - radius - 1, radius, radius));
                base.add(cut);
            }
            if(isHalfRect.get(3)) {
                Area cut = new Area(new Rectangle2D.Double(0, getHeight() - radius - 1, radius, radius));
                base.add(cut);
            }

            g2.draw(base);
        }
    }
}
