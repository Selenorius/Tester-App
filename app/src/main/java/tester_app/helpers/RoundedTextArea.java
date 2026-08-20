package tester_app.helpers;

import static tester_app.helpers.Constants.margin;
import static tester_app.helpers.Constants.selectionColor;

import java.awt.Color;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTextArea;

public class RoundedTextArea extends JTextArea {
    public RoundedTextArea(String s) {
        super(s);

        this.setLineWrap(true);
        this.setWrapStyleWord(true);
        this.setBackground(null);
        this.setForeground(Color.GRAY);
        this.setCaretColor(Color.WHITE);
        this.setFont(null);
        this.setMargin(new Insets(margin * 2, margin * 2, margin * 2, margin * 2));
        this.setFocusable(false);

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                setFocusable(false);
                setForeground(Color.GRAY);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                setFocusable(true);
                setForeground(Color.WHITE);
            }
        });
    }

    @Override
    public void scrollRectToVisible(final Rectangle aRect) {}
}
