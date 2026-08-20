package tester_app.helpers;

import static tester_app.helpers.Constants.margin;

import java.awt.Color;
import java.awt.Insets;
import java.awt.Rectangle;

import javax.swing.JTextArea;

public class RoundedTextArea extends JTextArea {
    public RoundedTextArea(String s) {
        super(s);

        this.setLineWrap(true);
        this.setWrapStyleWord(true);
        this.setBackground(null);
        this.setForeground(Color.WHITE);
        this.setCaretColor(Color.WHITE);
        this.setFont(null);
        this.setMargin(new Insets(margin * 2, margin * 2, margin * 2, margin * 2));
    }

    @Override
    public void scrollRectToVisible(final Rectangle aRect) {}
}
