package tester_app.questions;

import static tester_app.helpers.Constants.backgroundColor;
import static tester_app.helpers.Constants.next;
import static tester_app.helpers.Constants.size;
import static tester_app.helpers.Constants.styleButton;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JLabel;

import tester_app.Exam;
import tester_app.helpers.RoundedButton;
import tester_app.helpers.RoundedPanel;
import tester_app.options.ButtonOption;
import tester_app.options.TextOption;

public class TFQuestion extends Question {
    private ArrayList<ButtonOption> options;

    public TFQuestion(Exam exam) {
        score = 0;
        options = new ArrayList<>();
        status = Test.SUCCESS;
        this.exam = exam;

        this.setBackground(null);
        this.setBorderPainted(false);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        inputArea = new RoundedPanel();
        inputArea.setBackground(backgroundColor);
        inputArea.setLayout(new GridLayout());

        this.add(new JLabel(this.questionText));
        this.add(inputArea);

        setInputAreaSize(size.width, size.height);
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
        throw new UnsupportedOperationException("Unimplemented method 'isOrdered'");
    }
}
