package tester_app.helpers;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Insets;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;

import tester_app.Exam;
import tester_app.questions.Question;
import tester_app.questions.Question.Test;

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
    public static final Dimension size = new Dimension(960, 720);
    public static final String name = "Tester App";
    public static final int margin = 4;
    public static final File root = new File("topics");
    public static final Color
        backgroundColor = new Color(50, 50, 50),
        fieldColor = new Color(30, 30, 30),
        borderColor = new Color(90, 90, 90),
        buttonBackgroundColor = new Color(70, 70, 70),
        buttonBorderColor = borderColor,
        selectionColor = new Color(0, 120, 215);

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
        
        button.setBackground(buttonBackgroundColor);
        if(buttonIcon != null) {
            button.setIcon(new ImageIcon(buttonIcon));
        }
        button.setText(buttonText);
        button.setHorizontalTextPosition(hPos);
        button.setVerticalTextPosition(vPos);
        button.setForeground(Color.WHITE);
        if(hPos == JButton.LEFT) {
            button.setMargin(new Insets(inset * 2, inset * 2 + 1 + margin * 2, inset * 2, inset * 2 - 1));
        } else if(hPos == JButton.RIGHT) {
            button.setMargin(new Insets(inset * 2, inset * 2 + 1, inset * 2, inset * 2 - 1 + margin * 2));
        } else {
            button.setMargin(new Insets(inset * 2, inset * 2 + 1, inset * 2, inset * 2 - 1));
        }
    }
    public static final void styleButton(final JButton button, final String buttonText, final Image buttonIcon, final int hPos) {
        styleButton(
            button,
            buttonText,
            buttonIcon,
            hPos,
            JButton.CENTER
        );
    }
    public static final void styleButton(final JButton button, final String buttonText, final Image buttonIcon) {
        styleButton(
            button,
            buttonText,
            buttonIcon,
            JButton.CENTER,
            JButton.BOTTOM
        );
    }
    public static final void styleButton(final JButton button, final String buttonText) {
        styleButton(
            button,
            buttonText,
            null,
            JButton.CENTER,
            JButton.BOTTOM
        );
    }
    public static final void styleButton(final JButton button, final Image buttonIcon) {
        styleButton(
            button,
            null,
            buttonIcon,
            JButton.CENTER,
            JButton.BOTTOM
        );
    }
    public static final void styleButton(final JButton button) {
        styleButton(
            button,
            null,
            null,
            JButton.CENTER,
            JButton.BOTTOM
        );
    }

    public static final void addMargin(final Component c, final int val) {
        ((JComponent) c).setBorder(BorderFactory.createEmptyBorder(val, val, val, val));
    }

    public static final void next(Exam exam, Question currentQuestion) {
        ArrayList<Question> questions = exam.getQuestions();
        int currentIndex = exam.getCurrentIndex();

        if(currentQuestion.getStatus() == Test.COMPLETION) {
            exam.incrementScore();
        }

        if(currentIndex >= questions.size() - 1) {
            exam.finish();
        } else {
            exam.next();
        }
    }
}
