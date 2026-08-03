package tester_app.questions;

import java.util.ArrayList;

import tester_app.options.ButtonOption;
import tester_app.options.TextOption;

public class WQuestion extends Question {
    private ArrayList<TextOption> options;
    
    public WQuestion() {
        options = new ArrayList<>();
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
        throw new UnsupportedOperationException("Unimplemented method 'initGoal'");
    }
}
