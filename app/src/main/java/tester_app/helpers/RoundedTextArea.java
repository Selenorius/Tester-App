package tester_app.helpers;

import static tester_app.helpers.Constants.margin;
import static tester_app.helpers.Constants.selectionColor;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JTextArea;

public class RoundedTextArea extends JTextArea {
    private JLabel label;

    public RoundedTextArea(String s) {
        super();

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
        this.setLayout(new GridLayout());

        label = new JLabel();
        label.setForeground(Color.WHITE);
        this.add(label);
        if(!this.getText().isEmpty()) {
            label.setVisible(false);
        }

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
}
