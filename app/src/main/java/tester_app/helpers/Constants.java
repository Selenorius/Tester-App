package tester_app.helpers;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.plaf.basic.BasicScrollBarUI;

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
        backgroundColor = new Color(10, 36, 27),
        fieldColor = new Color(16, 20, 17),
        borderColor = new Color(20, 72, 54),
        buttonBackgroundColor = new Color(16, 60, 45),
        buttonBorderColor = new Color(32, 120, 90),
        selectionColor = new Color(15,130,215),
        editColor = new Color(15, 191, 62),
        deleteColor = new Color(208, 15, 0),
        topicBackgroundColor = new Color(11, 40, 30),
        topicBorderColor = new Color(22, 80, 60),
        examBackgroundColor = new Color(12, 44, 33),
        examBorderColor = new Color(24, 88, 66),
        editBackgroundColor = new Color(13, 48, 36),
        editBorderColor = new Color(26, 96, 72),
        examAddBackgroundColor = new Color(14, 52, 39),
        examAddBorderColor = new Color(28, 104, 78);

    //OPTION BOOLEANS
    public static final List<String>
        mark_true = new ArrayList<String>(List.of("true", "yes", "t", "y")),
        mark_false = new ArrayList<String>(List.of("false", "no", "f", "n"));

    //TAB
    public static final String tab(int i) {
        String s = "";

        while(i > 0) {
            s += "    ";
            --i;
        }

        return s;
    }
    public static final String tab() { return "    "; }

    //READ COMMANDS
    public static final boolean quit(final String s) {
        return s.equals("q");
    }
    public static final boolean back(final String s) {
        return s.equals("r");
    }

    public static final void styleButton(final RoundedButton button, final String buttonText, final Image buttonIcon, final int hPos, final int vPos) {
        final int inset = 4;
        
        button.setBackground(buttonBackgroundColor);
        button.setBorderColor(buttonBackgroundColor);
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
    public static final void styleButton(final RoundedButton button, final String buttonText, final Image buttonIcon, final int hPos) {
        styleButton(
            button,
            buttonText,
            buttonIcon,
            hPos,
            JButton.CENTER
        );
    }
    public static final void styleButton(final RoundedButton button, final String buttonText, final Image buttonIcon) {
        styleButton(
            button,
            buttonText,
            buttonIcon,
            JButton.CENTER,
            JButton.BOTTOM
        );
    }
    public static final void styleButton(final RoundedButton button, final String buttonText) {
        styleButton(
            button,
            buttonText,
            null,
            JButton.CENTER,
            JButton.BOTTOM
        );
    }
    public static final void styleButton(final RoundedButton button, final Image buttonIcon) {
        styleButton(
            button,
            null,
            buttonIcon,
            JButton.CENTER,
            JButton.BOTTOM
        );
    }
    public static final void styleButton(final RoundedButton button) {
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

    public static void styleScrollPane(JScrollPane scrollPane) {
        scrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            private final Dimension d = new Dimension();

            @Override protected JButton createDecreaseButton(int orientation) {
                return new RoundedButton() {
                    @Override public Dimension getPreferredSize() {
                        return d;
                    }
                };
            }

            @Override protected JButton createIncreaseButton(int orientation) {
                return new RoundedButton() {
                    @Override public Dimension getPreferredSize() {
                        return d;
                    }
                };
            }

            @Override
            protected void paintTrack(Graphics g, JComponent c, Rectangle r) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setPaint(fieldColor);
                g2.fillRect(r.x, r.y, r.width, r.height);

                g2.setPaint(backgroundColor);
                g2.fillRoundRect(r.x + 2 + margin, r.y, r.width - 2 - margin, r.height, 10, 10);

                g2.setPaint(borderColor);
                g2.drawRoundRect(r.x + 2 + margin, r.y, r.width - 3 - margin, r.height - 1, 10, 10);
            }

            @Override
            protected void paintThumb(Graphics g, JComponent c, Rectangle r) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color color = buttonBackgroundColor;
                JScrollBar sb = (JScrollBar)c;

                if(!sb.isEnabled() || r.width > r.height) {
                    return;
                } else if(isDragging) {
                    color = selectionColor;
                } else if(isThumbRollover()) {
                    color = selectionColor;
                }

                g2.setPaint(color);
                g2.fillRoundRect(r.x + 2 + margin, r.y, r.width - 2 - margin, r.height, 10, 10);
                
                if(!isThumbRollover()) {
                    color = buttonBackgroundColor;

                    g2.setPaint(buttonBorderColor);
                    g2.drawRoundRect(r.x + 2 + margin, r.y, r.width - 3 - margin, r.height - 1, 10, 10);
                }
                
                g2.dispose();
            }

            @Override
            protected void setThumbBounds(int x, int y, int width, int height) {
                super.setThumbBounds(x, y, width, height);
                scrollbar.repaint();
            }
        });
    }
}
