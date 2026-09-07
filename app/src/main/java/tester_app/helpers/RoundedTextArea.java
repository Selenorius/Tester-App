package tester_app.helpers;

import static tester_app.helpers.Constants.buttonFont;
import static tester_app.helpers.Constants.margin;
import static tester_app.helpers.Constants.selectionColor;
import static tester_app.helpers.Constants.textFont;

import java.awt.Color;
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

import javax.swing.JTextArea;

public class RoundedTextArea extends JTextArea {
    private RoundedLabel label;
    private GridBagLayout layout;
    private GridBagConstraints constraints;

    public RoundedTextArea(String s) {
        super();

        layout = new GridBagLayout();
        constraints = new GridBagConstraints();

        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 0.5;
        constraints.weighty = 0.5;

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

        label = new RoundedLabel();
        label.setForeground(Color.WHITE.darker());
        this.add(label, constraints);
        if(!this.getText().isEmpty()) {
            label.setVisible(false);
        }
        label.setFont(buttonFont);

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
    public String getLabel() {
        return label.getText();
    }

    // SETTERS
    public void setLabel(String text) {
        label.setText(text);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        
        super.paintComponent(g2);
    }

    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

        super.paintBorder(g2);
    }
}
