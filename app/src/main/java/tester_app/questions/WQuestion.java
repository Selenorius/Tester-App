package tester_app.questions;

import java.util.ArrayList;

import tester_app.options.ButtonOption;
import tester_app.options.TextOption;

public class WQuestion extends Question {
    private ArrayList<TextOption> options;
    private Boolean ordered;
    
    public WQuestion(Boolean ordered) {
        score = 0;
        options = new ArrayList<>();
        this.ordered = ordered;
    }
    public WQuestion() {
        score = 0;
        options = new ArrayList<>();
        ordered = false;
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
        int count = 0;

        for(@SuppressWarnings("unused") TextOption o : options) {
            ++count;
        }

        goal = count;
    }
}
