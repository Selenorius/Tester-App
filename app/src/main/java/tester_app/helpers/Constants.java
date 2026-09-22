package tester_app.helpers;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Container;
import java.io.File;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;
import javax.swing.plaf.basic.BasicScrollBarUI;

import tester_app.Exam;
import tester_app.Topic;
import tester_app.questions.Question;
import tester_app.questions.Question.Test;

public final class Constants {
    public static float opacity = (float) 1;

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
    public static final Font
        textFont = new Font("Verdana", Font.PLAIN, 13),
        buttonFont = new Font("Verdana", Font.BOLD, 13);
    public static final int margin = 6;
    public static final File root = new File("/topics");
    public static final Color
        fieldColor = new Color(0, 0, 0),
        selectionColor = Color.YELLOW,

        backgroundColor = fieldColor.brighter(),
        borderColor = Color.WHITE,

        copyColor = new Color(42, 91, 160),
        pasteColor = new Color(180, 111, 42),
        editColor = new Color(91, 160, 42),
        deleteColor = new Color(180, 71, 42),
        blotBackgroundColor = new Color(184, 28, 84),
        buttonBackgroundColor = new Color(
            backgroundColor.getRed() + 40,
            backgroundColor.getGreen() + 40,
            backgroundColor.getBlue() + 40
        ),

        topicColor = new Color(117, 91, 42),
        examColor = new Color(42, 91, 117),
        editMenuColor = new Color(91, 117, 42),

        wQuestionBackgroundColor = new Color(84, 28, 184).darker().darker(),
        wQuestionBorderColor = wQuestionBackgroundColor,
        tfQuestionBackgroundColor = new Color(28, 184, 84).darker().darker(),
        tfQuestionBorderColor = tfQuestionBackgroundColor,
        mcQuestionBackgroundColor = new Color(184, 28, 84).darker().darker(),
        mcQuestionBorderColor = mcQuestionBackgroundColor;

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

    public static final Color findParentBackground(Component component) {
        if(component != null) {
            if(component.getBackground() == null && component.getParent() != null) {
                findParentBackground(component.getParent());
            }
            return component.getBackground();
        } else {
            return null;
        }
    }

    public static final void styleButton(final RoundedButton button, final String buttonText, final Image buttonIcon, final int hPos, final int vPos) {
        final int inset = margin;
        Color pColor = findParentBackground(button.getParent());

        if(pColor != null) {
            button.setBackground(pColor.brighter());
        } else {
            button.setBackground(buttonBackgroundColor);
        }
        if(buttonIcon != null) {
            button.setButtonIcon(new ImageIcon(buttonIcon));
            button.setIcon(null);
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

    /**
    Adds a margin to a {@Code Component} by creating an {@Code EmptyBorder}.
    @param component
        the {@Code Component} to receive the border
    @param top
        an {@Code Integer} specifying the width of the top, in pixels
    @param left
        an {@Code Integer} specifying the width of the left side, in pixels
    @param bottom
        an {@Code Integer} specifying the width of the  bottom, in pixels
    @param right
        an {@Code Integer} specifying the width of the right side, in pixels
    @return void
    */
    public static final void addMargin(final Component component, final int top, final int left,  final int bottom, final int right) {
        ((JComponent) component).setBorder(BorderFactory.createEmptyBorder(top, left, bottom, right));
    }
    /**
    Adds a margin to a {@Code Component} by creating an {@Code EmptyBorder}.
    @param component
        the {@Code Component} to receive the border
    @param val
        an {@Code Integer} specifying the width of the border, in pixels
    @return {@Code void}
    */
    public static final void addMargin(final Component component, final int val) {
        ((JComponent) component).setBorder(BorderFactory.createEmptyBorder(val, val, val, val));
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
        if(scrollPane.getBackground() == null) {
            Color pColor = findParentBackground(scrollPane.getParent());

            if(pColor != null) {
                scrollPane.setBackground(pColor.brighter());
            } else {
                scrollPane.setBackground(fieldColor);
            }
        }

        addScrollMouseListener(scrollPane);

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

                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));

                g2.setPaint(scrollPane.getBackground());
                g2.fillRect(r.x, r.y, r.width, r.height);

                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));

                g2.setPaint(scrollPane.getBackground().darker());
                g2.fillRoundRect(r.x + 2 + margin, r.y, r.width - 2 - margin, r.height, 10, 10);

                //g2.setPaint(scrollPane.getBackground().darker());
                //g2.drawRoundRect(r.x + 2 + margin, r.y, r.width - 3 - margin, r.height - 1, 10, 10);
            }

            @Override
            protected void paintThumb(Graphics g, JComponent c, Rectangle r) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color color = blotBackgroundColor;
                JScrollBar sb = (JScrollBar)c;

                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));

                if(!sb.isEnabled() || r.width > r.height) {
                    return;
                } else if(isDragging || isThumbRollover()) {
                    int
                        red = color.getRed(),
                        green = color.getGreen(),
                        blue = color.getBlue();

                    color = new Color(
                        red + red / 2 < 256 ? red + red / 2 : 255,
                        green + green / 2 < 256 ? green + green / 2 : 255,
                        blue + blue / 2 < 256 ? blue + blue / 2 : 255
                    );
                }

                g2.setPaint(color);
                g2.fillRoundRect(r.x + 2 + margin, r.y, r.width - 2 - margin, r.height, 10, 10);
                
                g2.setPaint(color);
                g2.drawRoundRect(r.x + 2 + margin, r.y, r.width - 3 - margin, r.height - 1, 10, 10);
                
                g2.dispose();
            }

            @Override
            protected void setThumbBounds(int x, int y, int width, int height) {
                super.setThumbBounds(x, y, width, height);
                scrollbar.repaint();
            }
        });
    }

    public static void addScrollMouseListener(JScrollPane scrollPane) {
        Component object;
        if(
            scrollPane.getViewport().getView().getClass() == JTextArea.class ||
            scrollPane.getViewport().getView().getClass() == RoundedTextArea.class
        ) {
            object = (JTextArea) scrollPane.getViewport().getView();
            ((JTextArea)object).setAutoscrolls(true);
        } else {
            object = (JPanel) scrollPane.getViewport().getView();
            ((JPanel)object).setAutoscrolls(true);
        }

        MouseAdapter mouseAdapter = new MouseAdapter() {
            Point origin;

            @Override
            public void mousePressed(MouseEvent e) {
                origin = new Point(e.getPoint());
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (origin != null) {
                    JViewport viewPort = (JViewport) SwingUtilities.getAncestorOfClass(JViewport.class, object);

                    if (viewPort != null) {
                        int deltaX = origin.x - e.getX();
                        int deltaY = origin.y - e.getY();

                        Rectangle view = viewPort.getViewRect();
                        view.x += deltaX;
                        view.y += deltaY;

                        if(
                            scrollPane.getViewport().getView().getClass() == JTextArea.class ||
                            scrollPane.getViewport().getView().getClass() == RoundedTextArea.class
                        ) {
                            ((JTextArea)object).scrollRectToVisible(view);
                        } else {
                            ((JPanel)object).scrollRectToVisible(view);
                        }
                    }
                }
            }
        };

        object.addMouseListener(mouseAdapter);
        object.addMouseMotionListener(mouseAdapter);
    }

    public static void search(Container container, String s, Boolean... textSort) {
        Boolean sortText;

        if(textSort != null) {
            if(textSort.length > 0) {
                if(textSort[0] == null) {
                    sortText = false;
                } else {
                    sortText = textSort[0];
                }
            } else {
                sortText = false;
            }
        } else {
            sortText = false;
        }

        for(Component c : container.getComponents()) {
            if(
                c.getClass() == HamburgerMenu.class ||
                c.getClass() == Topic.class
            ) {
                if(((HamburgerMenu) c).getText() != null) {
                    if(((HamburgerMenu) c).getText().toLowerCase().contains(s.toLowerCase())) {
                        c.setVisible(true);
                    } else {
                        c.setVisible(false);
                    }
                }
            } else if(c.getClass() == RoundedTextArea.class && sortText) {
                if(((RoundedTextArea) c).getText() != null) {
                    if(((RoundedTextArea) c).getText().toLowerCase().contains(s.toLowerCase())) {
                        c.getParent().setVisible(true);
                    } else {
                        c.getParent().setVisible(false);
                    }
                }
            } else if(c.getClass() == RoundedPanel.class) {
                search((Container) c, s, textSort);
            }
        }
    }

    public static Dimension getScaledDimension(Dimension size, Dimension boundary) {
        int originalWidth = size.width;
        int originalHeight = size.height;
        int boundWidth = boundary.width;
        int newWidth = originalWidth;
        int newHeight = originalHeight;

        newWidth = boundWidth;
        newHeight = (newWidth * originalHeight) / originalWidth;

        return new Dimension(newWidth, newHeight);
    }
}
