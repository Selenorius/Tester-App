package tester_app.questions;

import static tester_app.helpers.Constants.addMargin;
import static tester_app.helpers.Constants.deleteColor;
import static tester_app.helpers.Constants.editColor;
import static tester_app.helpers.Constants.margin;
import static tester_app.helpers.Constants.next;
import static tester_app.helpers.Constants.selectionColor;
import static tester_app.helpers.Constants.styleScrollPane;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;

import tester_app.Exam;
import tester_app.helpers.RoundedPanel;
import tester_app.options.ButtonOption;
import tester_app.options.TextOption;

public class WQuestion extends Question {
    private ArrayList<TextOption> options;
    private ArrayList<String> used;
    private Boolean ordered;
    private int
        goal,
        maxScore;

    private JTextArea textArea;
    private JScrollPane scrollPane;
    private GridBagLayout layout;
    private GridBagConstraints constraints;
    
    public WQuestion(Exam exam) {
        layout = new GridBagLayout();
        constraints = new GridBagConstraints();

        goal = 1;
        maxScore = 99;
        ordered = false;
        score = 0;
        options = new ArrayList<>();
        used = new ArrayList<>();
        status = Test.SUCCESS;
        this.exam = exam;

        this.setBorderPainted(false);
        this.setLayout(layout);
        this.addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                adjustImage();
            }

            @Override
            public void componentMoved(ComponentEvent e) {}

            @Override
            public void componentShown(ComponentEvent e) {
                adjustImage();
            }

            @Override
            public void componentHidden(ComponentEvent e) {}
        });

        textArea = new JTextArea();
        textArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                    textArea.setEditable(false);
                    textArea.setText(textArea.getText() + System.lineSeparator());

                    String in = textArea.getText();
                    if(in != null) {
                        test(in);
                    }
                } else if(textArea.getSelectedText() != null) {
                    textArea.setEditable(true);
                    textArea.setText(textArea.getSelectedText());

                    textArea.setSelectionColor(selectionColor);
                    textArea.setSelectedTextColor(Color.BLACK);
                }
            }
        });
        textArea.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                textArea.setEditable(true);

                textArea.setSelectionColor(selectionColor);
                textArea.setSelectedTextColor(Color.BLACK);
            }
        });
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setBackground(getBackground().darker());
        textArea.setForeground(Color.WHITE);
        textArea.setCaretColor(Color.WHITE);
        textArea.setSelectionColor(selectionColor);
        textArea.setSelectedTextColor(Color.BLACK);
        textArea.setFont(null);
        textArea.setMargin(new Insets(margin * 2, margin * 2, margin * 2, margin * 2));
        
        scrollPane = new JScrollPane(textArea);
        scrollPane.setBackground(getBackground().darker());
        scrollPane.setBorder(null);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        styleScrollPane(scrollPane);

        inputArea = new RoundedPanel();
        inputArea.setBackground(getBackground().darker());
        inputArea.add(scrollPane);
        inputArea.setBorderPainted(true);
        inputArea.setLayout(new GridLayout());

        questionTextLabel = new JLabel("<html>" + "No question text found" + "<html>", SwingConstants.CENTER);
        questionTextLabel.setForeground(Color.WHITE);
        questionTextLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        questionTextLabel.setLayout(layout);
        questionTextLabel.setHorizontalTextPosition(JLabel.CENTER);
        questionTextLabel.setVerticalTextPosition(JLabel.BOTTOM);
        questionTextLabel.setIconTextGap(margin * 3);
        addMargin(questionTextLabel, margin * 3);
        
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridx = 1;
        constraints.weightx = 0.5;
        constraints.weighty = 0.5;

        this.add(questionTextLabel, constraints);

        this.add(inputArea, constraints);
    }

    @Override
    public void addOption(TextOption o) {
        options.add(o);
    }

    @Override
    public void addOption(ButtonOption buttonOption) {
        throw new UnsupportedOperationException("Unimplemented method 'addOption'");
    }

    @Override
    public void initGoal() {
        if(ordered) {
            int count = 0;

            for(int i = 0; i < options.size(); ++i) {
                ++count;
            }

            goal = count;
        } else {
            int count = 0;

            if(options.size() > 1) {
                for(int i = 0; i < options.size(); ++i) {
                    ++count;
                }
            } else {
                count = options.get(0).getTexts().size();
            }

            maxScore = count;
        }
    }

    @Override
    public void shuffle() {
        throw new UnsupportedOperationException("Unimplemented method 'shuffle'");
    }

    @Override
    public Test test(String in) {
        for(String u : used) {
            if(in.contains(u)) {
                in = in.replaceFirst(u, "");
            }
        }

        for(String u : used) {
            if(in.trim().equals(u.trim())) {
                textArea.setSelectedTextColor(deleteColor);
                textArea.setSelectionColor(textArea.getBackground());
                textArea.select(0, textArea.getText().length());

                String
                    text = "",
                    correctionText = "Correct Answers: ";
                
                if(options.size() == 1) {
                    if(options.get(0).getTexts().size() == 1) {
                        correctionText = "Correct Answer: ";
                    }
                }

                for(TextOption o : options) {
                    String s = o.getText();

                    if(!text.contains(s)) {
                        text += s + System.lineSeparator();
                    }
                }

                JOptionPane.showMessageDialog(
                    exam,
                    correctionText + System.lineSeparator() + System.lineSeparator() +
                    text +
                    "Keep trying, you can do it!",
                    "Incorrect Answer!",
                    JOptionPane.PLAIN_MESSAGE
                );

                next(exam, this);

                return Test.FAIL;
            }
        }

        if(!options.isEmpty()) {
            if(ordered) {
                TextOption t = options.get(0);

                if(t.isTrue(in)) {
                    textArea.setSelectedTextColor(editColor);
                    textArea.setSelectionColor(textArea.getBackground());
                    textArea.select(0, textArea.getText().length());

                    used.add(in);
                    if(options.size() > 1) {
                        options.remove(t);
                    }
                    ++score;

                    if(score >= goal) {
                        JOptionPane.showMessageDialog(
                            exam,
                            "Great Job!",
                            "Correct Answer!",
                            JOptionPane.PLAIN_MESSAGE
                        );

                        exam.incrementScore();
                        next(exam, this);

                        return Test.COMPLETION;
                    }

                    return Test.SUCCESS;
                }
            } else {
                for(TextOption o : options) {
                    if(o.isTrue(in)) {
                        textArea.setSelectedTextColor(editColor);
                        textArea.setSelectionColor(textArea.getBackground());
                        textArea.select(0, textArea.getText().length());

                        used.add(in);
                        if(options.size() > 1) {
                            options.remove(o);
                        }
                        ++score;

                        if(score >= goal) {
                            JOptionPane.showMessageDialog(
                                exam,
                                "Great Job!",
                                "Correct Answer!",
                                JOptionPane.PLAIN_MESSAGE
                            );

                            exam.incrementScore();
                            next(exam, this);

                            return Test.COMPLETION;
                        }

                        return Test.SUCCESS;
                    }
                }
            }
        }

        textArea.setSelectedTextColor(deleteColor);
        textArea.setSelectionColor(textArea.getBackground());
        textArea.select(0, textArea.getText().length());

        String
            text = "",
            correctionText = "Correct Answers: ";
        
        if(options.size() == 1) {
            if(options.get(0).getTexts().size() == 1) {
                correctionText = "Correct Answer: ";
            }
        }

        for(TextOption o : options) {
            String s = o.getText();

            if(!text.contains(s)) {
                text += s + System.lineSeparator();
            }
        }

        JOptionPane.showMessageDialog(
            exam,
            correctionText + System.lineSeparator() + System.lineSeparator() +
            text +
            "Keep trying, you can do it!",
            "Incorrect Answer!",
            JOptionPane.PLAIN_MESSAGE
        );

        next(exam, this);

        return Test.FAIL;
    }

    // GETTERS
    public int getGoal() {
        return this.goal;
    }

    public int getMaxScore() {
        return Math.max(this.maxScore, 1);
    }

    // SETTERS
    public void setGoal(int goal) {
        this.goal = goal;
    }

    @Override
    public Test test(ButtonOption o) {
        throw new UnsupportedOperationException("Unimplemented method 'test'");
    }

    @Override
    public Boolean isOrdered() {
        return ordered;
    }

    @Override
    public void setInputAreaSize(int x, int y) {
        x -= 56;
        y -= 56;

        questionTextLabel.setPreferredSize(new Dimension(x, y / 4));
        inputArea.setMaximumSize(new Dimension(x, y));
        scrollPane.setPreferredSize(new Dimension(x - margin, y - y / 4 - margin * 5));
    }
    @Override
    public void setInputAreaSize(Dimension d) {
        questionTextLabel.setPreferredSize(new Dimension(d.width - 56, d.height / 4 - 56));
        inputArea.setMaximumSize(new Dimension(d.width - 56, d.height - 56));
        scrollPane.setPreferredSize(new Dimension(d.width - 56 - margin, d.height - 56 - d.height / 4 - margin * 5));
    }

    @Override
    public JTextArea getTextArea() {
        return textArea;
    }

    @Override
    public ArrayList<ButtonOption> getButtonOptions() {
        throw new UnsupportedOperationException("Unimplemented method 'getButtonOptions'");
    }

    @Override
    public ArrayList<TextOption> getTextOptions() {
        return options;
    }

    @Override
    public void removeOption(TextOption textOption) {
        options.remove(textOption);
    }

    @Override
    public void removeOption(ButtonOption buttonOption) {
        throw new UnsupportedOperationException("Unimplemented method 'removeOption'");
    }

    @Override
    public void setTextOptions(ArrayList<TextOption> options) {
        this.options = options;
    }

    @Override
    public void setButtonOptions(ArrayList<ButtonOption> options) {
        throw new UnsupportedOperationException("Unimplemented method 'setButtonOptions'");
    }

    @Override
    public void setOrdered(Boolean ordered) {
        this.ordered = ordered;
    }
}
