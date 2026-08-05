package tester_app.questions;

import static tester_app.helpers.Constants.backgroundColor;
import static tester_app.helpers.Constants.next;
import static tester_app.helpers.Constants.size;
import static tester_app.helpers.Constants.styleButton;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.BoxLayout;
import javax.swing.JLabel;

import tester_app.Exam;
import tester_app.helpers.RoundedButton;
import tester_app.helpers.RoundedPanel;
import tester_app.options.ButtonOption;
import tester_app.options.TextOption;

public class MCQuestion extends Question {
    private ArrayList<ButtonOption> options;
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

        this.add(new JLabel(this.questionText));
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
        options.add(o);

        RoundedButton optionButton = new RoundedButton();

        optionButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                test(o);
            }
        });

        styleButton(optionButton, o.getText());

        inputArea.add(optionButton);
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
            ++score;

            if(score >= goal) {
                next(exam, this);

                return Test.COMPLETION;
            }

            return Test.SUCCESS;
        } else {
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
}
