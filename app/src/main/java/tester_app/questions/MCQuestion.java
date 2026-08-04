package tester_app.questions;

import java.util.ArrayList;

import tester_app.options.ButtonOption;
import tester_app.options.TextOption;

public class MCQuestion extends Question {
    private ArrayList<ButtonOption> options;
    private Boolean ordered;

    public MCQuestion(Boolean ordered) {
        score = 0;
        options = new ArrayList<>();
        this.ordered = ordered;
    }
    public MCQuestion() {
        score = 0;
        options = new ArrayList<>();
        ordered = false;
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
    }

    @Override
    public void addOption(TextOption textOption) {
        throw new UnsupportedOperationException("Unimplemented method 'addOption'");
    }
}
