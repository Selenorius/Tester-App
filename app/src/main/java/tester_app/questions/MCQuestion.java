package tester_app.questions;

import static tester_app.helpers.Constants.addMargin;
import static tester_app.helpers.Constants.backgroundColor;
import static tester_app.helpers.Constants.margin;
import static tester_app.helpers.Constants.next;
import static tester_app.helpers.Constants.size;
import static tester_app.helpers.Constants.styleButton;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import tester_app.Exam;
import tester_app.helpers.RoundedButton;
import tester_app.helpers.RoundedPanel;
import tester_app.options.ButtonOption;
import tester_app.options.TextOption;

public class MCQuestion extends Question {
    private ArrayList<ButtonOption>
        options;
    private Boolean ordered;

    public MCQuestion(Boolean ordered) {
        score = 0;
        options = new ArrayList<>();
        this.ordered = ordered;
        status = Test.SUCCESS;

        this.setBackground(null);
        this.setBorderPainted(false);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        inputArea = new RoundedPanel();
        inputArea.setBackground(backgroundColor);
        inputArea.setLayout(new GridLayout());

        questionTextLabel = new JLabel("<html>" + "No question text found" + "<html>", SwingConstants.CENTER);
        questionTextLabel.setForeground(Color.WHITE);
        questionTextLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        addMargin(questionTextLabel, margin * 4);

        for(ButtonOption o : options) {
            RoundedButton optionButton = new RoundedButton();

            styleButton(optionButton, o.getText());

            inputArea.add(optionButton);
        }

        this.add(new JLabel(this.questionText));
        this.add(inputArea);

        setInputAreaSize(size.width, size.height);
    }
    public MCQuestion(Exam exam) {
        score = 0;
        options = new ArrayList<>();
        ordered = false;
        this.exam = exam;

        this.setBackground(null);
        this.setBorderPainted(false);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        inputArea = new RoundedPanel();
        inputArea.setBackground(null);
        inputArea.setBorderPainted(false);
        inputArea.setLayout(new GridLayout());

        questionTextLabel = new JLabel("<html>" + "No question text found" + "<html>", SwingConstants.CENTER);
        questionTextLabel.setForeground(Color.WHITE);
        questionTextLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        addMargin(questionTextLabel, margin * 4);

        this.add(questionTextLabel);
        this.add(inputArea);

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

        options.add(o);
        inputArea.add(o);
    }

    @Override
    public void addOption(TextOption textOption) {
        throw new UnsupportedOperationException("Unimplemented method 'addOption'");
    }

    @Override
    public void shuffle() {
        Collections.shuffle(options);
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

            String
                text = "",
                correctionText = "Correct Answers: ";
        
            if(options.size() == 1) {
                correctionText = "Correct Answer: ";
            }

            for(ButtonOption b : options) {
                String s = b.getText();

                if(!text.contains(s)) {
                    text += s + System.lineSeparator();
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
}
