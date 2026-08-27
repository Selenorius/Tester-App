package tester_app.questions;

import static tester_app.helpers.Constants.addMargin;
import static tester_app.helpers.Constants.examAddBorderColor;
import static tester_app.helpers.Constants.fieldColor;
import static tester_app.helpers.Constants.margin;
import static tester_app.helpers.Constants.next;
import static tester_app.helpers.Constants.styleButton;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import tester_app.Exam;
import tester_app.helpers.RoundedPanel;
import tester_app.options.ButtonOption;
import tester_app.options.TextOption;

public class TFQuestion extends Question {
    private ArrayList<ButtonOption> options;
    private GridBagLayout layout;
    private GridBagConstraints constraints;

    public TFQuestion(Exam exam) {
        score = 0;
        options = new ArrayList<>();
        status = Test.SUCCESS;
        this.exam = exam;

        layout = new GridBagLayout();
        constraints = new GridBagConstraints();

        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridx = 1;
        constraints.weightx = 0.5;
        constraints.weighty = 0.5;

        this.setBackground(null);
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
        inputArea.setBackground(fieldColor);
        inputArea.setBorderColor(examAddBorderColor);
        inputArea.setBorderPainted(true);
        inputArea.setLayout(layout);

        questionTextLabel = new JLabel("<html>" + "No question text found" + "<html>", SwingConstants.CENTER);
        questionTextLabel.setForeground(Color.WHITE);
        questionTextLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        questionTextLabel.setLayout(layout);
        questionTextLabel.setHorizontalTextPosition(JLabel.CENTER);
        questionTextLabel.setVerticalTextPosition(JLabel.BOTTOM);
        questionTextLabel.setIconTextGap(margin * 3);
        addMargin(questionTextLabel, margin * 3);

        this.add(questionTextLabel, constraints);

        this.add(inputArea, constraints);

        constraints = new GridBagConstraints();
    }

    // GETTERS
    public Boolean isTrue() {
        return options.getFirst().isTrue();
    }

    @Override
    public void addOption(ButtonOption o) {
        o.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                test(o);
            }
        });

        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridy = 1;
        constraints.weightx = 0.5;
        constraints.weighty = 0.5;
        constraints.insets = new Insets(margin * 2, margin * 2, margin * 2, margin * 2);

        options.add(o);
        inputArea.add(o, constraints);

        String text = o.getText();
        if(text != null && !text.isBlank()) {
            styleButton(o, text.substring(0, text.length()));
        }
    }

    @Override
    public void addOption(TextOption textOption) {
        throw new UnsupportedOperationException("Unimplemented method 'addOption'");
    }

    @Override
    public void initGoal() {
        throw new UnsupportedOperationException("Unimplemented method 'initGoal'");
    }

    @Override
    public void shuffle() {
        throw new UnsupportedOperationException("Unimplemented method 'shuffle'");
    }
    
    @Override
    public Test test(ButtonOption o) {
        if(o.isTrue()) {
            options.remove(o);
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

            ArrayList<ButtonOption> answers = new ArrayList<>();

            for(ButtonOption n : options) {
                if(n.isTrue()) {
                    answers.add(n);
                }
            }

            JOptionPane.showMessageDialog(
                exam,
                "Correct Answer: " + System.lineSeparator() + System.lineSeparator() +
                "- " + options.get(0).getText() + System.lineSeparator() + System.lineSeparator() +
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
        throw new UnsupportedOperationException("Unimplemented method 'isOrdered'");
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
        throw new UnsupportedOperationException("Unimplemented method 'setOrdered'");
    }
}
