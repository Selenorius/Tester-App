package tester_app.helpers;

import java.awt.Color;
import java.awt.Component;
import java.awt.Image;
import java.awt.Insets;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;

public final class Constants {
    private Constants() {}

    //ANSI COLORS
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";

    //APP DATA
    public static final String name = "Tester App";
    public static final int margin = 4;
    public static final File root = new File("topics");
    public static final Color
        backgroundColor = new Color(25, 23, 20),
        fieldColor = new Color(20, 18, 15),
        borderColor = new Color(130, 128, 125),
        buttonColor = new Color(30, 28, 25),
        selectionColor = new Color(25, 135, 175);

    //OPTION BOOLEANS
    public static final List<String>
        mark_true = new ArrayList<String>(List.of("true", "yes", "t", "y")),
        mark_false = new ArrayList<String>(List.of("false", "no", "f", "n"));

    //TAB
    public static final String tab(int i) {
        String s = "";

        while(i > 0) {
            s += "   ";
            --i;
        }

        return s;
    }
    public static final String tab() { return "   "; }

    //READ COMMANDS
    public static final boolean quit(final String s) {
        return s.equals("q");
    }
    public static final boolean back(final String s) {
        return s.equals("r");
    }

    public static final void styleButton(final JButton button, final String buttonText, final Image buttonIcon, final int hPos, final int vPos) {
        final int inset = 4;

        button.setBackground(buttonColor);
        button.setIcon(new ImageIcon(buttonIcon));
        button.setHorizontalTextPosition(hPos);
        button.setVerticalTextPosition(vPos);
        button.setText(buttonText);
        button.setForeground(Color.WHITE);
        button.setMargin(new Insets(inset, inset, inset, inset));
    }
    public static final void styleButton(final JButton button, final String buttonText, final Image buttonIcon, final int hPos) {
        final int inset = 4;

        button.setBackground(buttonColor);
        button.setIcon(new ImageIcon(buttonIcon));
        button.setHorizontalTextPosition(hPos);
        button.setVerticalTextPosition(JButton.CENTER);
        button.setText(buttonText);
        button.setForeground(Color.WHITE);
        button.setMargin(new Insets(inset, inset, inset, inset));
    }
    public static final void styleButton(final JButton button, final String buttonText, final Image buttonIcon) {
        final int inset = 4;
        
        button.setBackground(buttonColor);
        button.setIcon(new ImageIcon(buttonIcon));
        button.setHorizontalTextPosition(JButton.CENTER);
        button.setVerticalTextPosition(JButton.BOTTOM);
        button.setText(buttonText);
        button.setForeground(Color.WHITE);
        button.setMargin(new Insets(inset, inset, inset, inset));
    }

    public static final void addMargin(final Component c, final int val) {
        ((JComponent) c).setBorder(BorderFactory.createEmptyBorder(val, val, val, val));
    }
}
