package tester_app.questions;

import static tester_app.helpers.Constants.addMargin;
import static tester_app.helpers.Constants.deleteColor;
import static tester_app.helpers.Constants.editColor;
import static tester_app.helpers.Constants.margin;
import static tester_app.helpers.Constants.fieldColor;
import static tester_app.helpers.Constants.next;
import static tester_app.helpers.Constants.styleButton;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import tester_app.Exam;
import tester_app.helpers.RoundedPanel;
import tester_app.options.ButtonOption;
import tester_app.options.TextOption;

public class MCQuestion extends Question {
    private ArrayList<ButtonOption>
        options;
    private Boolean ordered;
    private GridBagLayout layout;
    private GridBagConstraints constraints;

    public MCQuestion(Exam exam) {
        score = 0;
        options = new ArrayList<>();
        ordered = false;
        this.exam = exam;
        layout = new GridBagLayout();
        constraints = new GridBagConstraints();

        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridx = 1;
        constraints.weightx = 0.5;
        constraints.weighty = 0.5;

        this.setBackground(fieldColor.darker());
        this.setBorderColor(fieldColor.darker());
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

        inputArea = new RoundedPanel();
        inputArea.setBackground(getBackground().brighter());
        inputArea.setBorderColor(getBackground().brighter().brighter());
        inputArea.setBorderPainted(true);
        inputArea.setLayout(layout);

        questionTextLabel = new JLabel("<html>" + "No question text found" + "<html>", SwingConstants.CENTER);
        questionTextLabel.setForeground(Color.WHITE);
        questionTextLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        questionTextLabel.setLayout(layout);
        questionTextLabel.setHorizontalTextPosition(JLabel.CENTER);
        questionTextLabel.setVerticalTextPosition(JLabel.BOTTOM);
        questionTextLabel.setHorizontalTextPosition(JLabel.CENTER);
        questionTextLabel.setIconTextGap(margin * 3);
        addMargin(questionTextLabel, margin * 3);

        this.add(questionTextLabel, constraints);

        this.add(inputArea, constraints);
    }

    @Override
    public void initGoal() {
        int count = 0;

        for(ButtonOption o : options) {
            if(o.isTrue()) ++count;
        }

        goal = count;
    }

    @Override
    public void addOption(ButtonOption o) {
        o.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                test(o);
            }
        });

        constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 0.5;
        constraints.weighty = 0.5;
        constraints.gridwidth = 1;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new Insets(margin * 2, margin * 2, margin * 2, margin * 2);

        int menCount = 0;
        for(Component c : inputArea.getComponents()) {
            constraints.gridx = 2 - (menCount++ % 2);

            layout.setConstraints(c, constraints);
        }

        constraints.gridx = 2 - (inputArea.getComponentCount() % 2);
        constraints.gridwidth = 3 - (inputArea.getComponentCount() % 2);

        options.add(o);
        inputArea.add(o, constraints);

        String
            text = o.getText(),
            image = o.getImagePath();
        if(text != null && image != null) {
            if(text.isBlank()) {
                styleButton(o, new ImageIcon(image).getImage());
            } else if(image.isBlank()) {
                styleButton(o, text);
            } else {
                styleButton(o, text.substring(0, text.length()), new ImageIcon(image).getImage());
            }
        } else if(text != null) {
            if(!text.isBlank()) {
                styleButton(o, text);
            } else {
                styleButton(o);
            }
        } else if(image != null) {
            if(!image.isBlank()) {
                styleButton(o, new ImageIcon(image).getImage());
            } else {
                styleButton(o);
            }
        } else {
            styleButton(o);
        }

        inputArea.revalidate();
    }

    @Override
    public void addOption(TextOption textOption) {
        throw new UnsupportedOperationException("Unimplemented method 'addOption'");
    }

    @Override
    public void shuffle() {
        if(!ordered) {
            Collections.shuffle(options);
        }

        constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 0.5;
        constraints.weighty = 0.5;
        constraints.gridwidth = 1;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new Insets(margin * 2, margin * 2, margin * 2, margin * 2);

        int menCount = 0;
        for(ButtonOption butt : options) {
            constraints.gridx = 2 - (menCount % 2);
            constraints.gridy = Math.abs(menCount++ / 2);

            layout.setConstraints(butt, constraints);
        }

        constraints.gridx = (inputArea.getComponentCount() % 2);
        constraints.gridwidth = (inputArea.getComponentCount() % 2) - 1;

        layout.setConstraints(options.getLast(), constraints);
    }

    @Override
    public Test test(ButtonOption o) {
        if(o.isTrue()) {
            options.remove(o);
            o.setEnabled(false);
            o.setBackground(editColor);
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
        } else {
            options.remove(o);
            o.setEnabled(false);
            o.setBackground(deleteColor);

            String
                text = "",
                correctionText = "Correct Answers: ";
        
            if(options.size() == 1) {
                correctionText = "Correct Answer: ";
            }

            for(ButtonOption b : options) {
                if(b.isTrue()) {
                    String s = b.getText();
                
                    if(!text.contains(s)) {
                        text += "- " + s + System.lineSeparator();
                    }
                }
            }

            JOptionPane.showMessageDialog(
                exam,
                correctionText + System.lineSeparator() + System.lineSeparator() +
                text + System.lineSeparator() +
                "Keep trying, you can do it!",
                "Incorrect Answer!",
                JOptionPane.PLAIN_MESSAGE
            );

            next(exam, this);

            return Test.FAIL;
        }
    }
    @Override
    public Test test(String in) {
        throw new UnsupportedOperationException("Unimplemented method 'test'");
    }

    @Override
    public Boolean isOrdered() {
        return ordered;
    }
    @Override
    public JTextArea getTextArea() {
        throw new UnsupportedOperationException("Unimplemented method 'getTextArea'");
    }
    @Override
    public ArrayList<ButtonOption> getButtonOptions() {
        return options;
    }
    @Override
    public ArrayList<TextOption> getTextOptions() {
        throw new UnsupportedOperationException("Unimplemented method 'getTextOptions'");
    }

    @Override
    public void removeOption(TextOption textOption) {
        throw new UnsupportedOperationException("Unimplemented method 'removeOption'");
    }

    @Override
    public void removeOption(ButtonOption buttonOption) {
        options.remove(buttonOption);
    }

    @Override
    public void setTextOptions(ArrayList<TextOption> options) {
        throw new UnsupportedOperationException("Unimplemented method 'setTextOptions'");
    }

    @Override
    public void setButtonOptions(ArrayList<ButtonOption> options) {
        this.options = options;
    }

    @Override
    public void setOrdered(Boolean ordered) {
        this.ordered = ordered;
    }

    @Override
    public void adjustImage() {
        if(questionTextLabel.getIcon() != null)  {
            if(questionTextLabel.getWidth() - margin * 6 < questionTextLabel.getIcon().getIconWidth()) {
                questionTextLabel.setIcon(new ImageIcon(new ImageIcon(getQuestionImage()).getImage().getScaledInstance(questionTextLabel.getWidth() - margin * 6, questionTextLabel.getHeight() - margin * 6, Image.SCALE_SMOOTH)));
            }
        }

        for(ButtonOption o : options) {
            if(o.getIcon() != null)  {
                if(o.getWidth() - margin * 6 < o.getIcon().getIconWidth()) {
                    o.setIcon(new ImageIcon(new ImageIcon(o.getImagePath()).getImage().getScaledInstance(o.getWidth() - margin * 6, o.getHeight() - margin * 6, Image.SCALE_SMOOTH)));
                }
            }
        }
    }
}
