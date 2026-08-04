package tester_app.questions;

import java.util.ArrayList;

import tester_app.options.ButtonOption;
import tester_app.options.TextOption;

public class TFQuestion extends Question {
    private ArrayList<ButtonOption> options;

    public TFQuestion() {
        score = 0;
        options = new ArrayList<>();
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
}
