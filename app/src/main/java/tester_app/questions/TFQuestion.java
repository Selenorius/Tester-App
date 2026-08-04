package tester_app.questions;

import static tester_app.helpers.Constants.size;
import static tester_app.helpers.Constants.styleButton;

import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JLabel;

import tester_app.helpers.RoundedButton;
import tester_app.helpers.RoundedPanel;
import tester_app.options.ButtonOption;
import tester_app.options.TextOption;

public class TFQuestion extends Question {
    private ArrayList<ButtonOption> options;

    public TFQuestion() {
        score = 0;
        options = new ArrayList<>();

        this.setBackground(null);
        this.setBorderPainted(false);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        inputArea = new RoundedPanel();
        inputArea.setBackground(null);
        inputArea.setBorderPainted(false);
        inputArea.setLayout(new GridLayout());

        for(ButtonOption o : options) {
            RoundedButton optionButton = new RoundedButton();

            styleButton(optionButton, o.getText());

            inputArea.add(optionButton);
        }

        this.add(new JLabel(this.questionText));
        this.add(inputArea);

        setInputAreaSize(size.width - 28, size.height - 28);
    }

    @Override
    public void addOption(ButtonOption o) {
        options.add(o);
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
                return Test.COMPLETION;
            }

            return Test.SUCCESS;
        } else {
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
