package tester_app.questions;

import java.awt.Image;

import tester_app.helpers.RoundedPanel;
import tester_app.options.Option;

public abstract class Question extends RoundedPanel {
    public static enum Test {
        FAIL,
        SUCCESS,
        COMPLETION
    }

    protected String
        questionText,
        answerText;
    protected Image
        questionImage,
        answerImage;
        protected Option[] options;
    protected int
        score,
        goal;
    protected RoundedPanel inputArea;

    protected int getGoal() {
        int count = 0;

        for(Option o : options) {
            if(o.isTrue()) ++count;
        }

        return count;
    }

    public Test test(Option o) {
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
}
