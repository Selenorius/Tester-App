package tester_app.helpers;

import static tester_app.helpers.Constants.fieldColor;
import static tester_app.helpers.Constants.margin;

import java.awt.Color;
import java.awt.Insets;

import javax.swing.JTextArea;

public class RoundedTextArea extends JTextArea {
    public RoundedTextArea(String s) {
        super(s);

        this.setLineWrap(true);
        this.setWrapStyleWord(true);
        this.setBackground(fieldColor);
        this.setForeground(Color.WHITE);
        this.setCaretColor(Color.WHITE);
        this.setFont(null);
        this.setMargin(new Insets(margin * 2, margin * 2, margin * 2, margin * 2));
    }
}
