package tester_app.questions;

import static tester_app.helpers.Constants.addMargin;
import static tester_app.helpers.Constants.fieldColor;
import static tester_app.helpers.Constants.margin;
import static tester_app.helpers.Constants.next;
import static tester_app.helpers.Constants.size;
import static tester_app.helpers.Constants.styleButton;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
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

        this.setBackground(null);
        this.setBorderPainted(false);
        this.setLayout(layout);

        inputArea = new RoundedPanel();
        inputArea.setBackground(fieldColor);
        inputArea.setBorderPainted(false);
        inputArea.setLayout(layout);

        questionTextLabel = new JLabel("<html>" + "No question text found" + "<html>", SwingConstants.CENTER);
        questionTextLabel.setForeground(Color.WHITE);
        questionTextLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        addMargin(questionTextLabel, margin * 4);

        this.add(questionTextLabel, constraints);
        this.add(inputArea, constraints);

        setInputAreaSize(size.width - 28, size.height - 28);
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

        String text = o.getText();
        if(text != null && !text.isBlank()) {
            styleButton(o, text.substring(0, text.length() - 1));
        }

        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridx = 1;
        constraints.weightx = 0.5;
        constraints.weighty = 0.5;

        options.add(o);
        inputArea.add(o, constraints);
    }

    @Override
    public void addOption(TextOption textOption) {
        throw new UnsupportedOperationException("Unimplemented method 'addOption'");
    }

    @Override
    public void shuffle() {
        Collections.shuffle(options);

        inputArea.removeAll();

        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridx = 1;
        constraints.weightx = 0.5;
        constraints.weighty = 0.5;

        for(ButtonOption o : options) {
            inputArea.add(o, constraints);
        }
    }

    @Override
    public Test test(ButtonOption o) {
        if(o.isTrue()) {
            options.remove(o);
            inputArea.remove(o);
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
            inputArea.remove(o);

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
}
