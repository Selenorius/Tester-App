package tester_app.questions;

import java.awt.Dimension;
import java.awt.Image;

import javax.swing.JLabel;

import tester_app.Exam;
import tester_app.helpers.RoundedPanel;
import tester_app.options.ButtonOption;
import tester_app.options.TextOption;

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
    protected int
        score,
        goal;
    protected Test status;
    protected Exam exam;

    protected RoundedPanel inputArea;
    protected JLabel questionTextLabel;

    public void setInputAreaSize(int x, int y) {
        x -= 56;
        y -= 56;

        inputArea.setSize(x, y);
    }
    public void setInputAreaSize(Dimension d) {
        inputArea.setSize(d.width - 56, d.height - 56);
    }

    // GETTERS
    public String getQuestionText() {
        return questionText;
    }

    public String getAnswerText() {
        return answerText;
    }

    public Image getQuestionImage() {
        return questionImage;
    }

    public Image getAnswerImage() {
        return answerImage;
    }

    public int getGoal() {
        return goal;
    }

    public Test getStatus() {
        return status;
    }

    // SETTERS
    public void setQuestionText(String questionText) {
        this.questionText = questionText;

        if(questionText != null && questionTextLabel != null) {
            questionTextLabel.setText(questionText);
        }
    }

    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }

    public void setQuestionImage(Image questionImage) {
        this.questionImage = questionImage;
    }

    public void setAnswerImage(Image answerImage) {
        this.answerImage = answerImage;
    }

    public void setGoal(int goal) {
        this.goal = goal;
    }

    // ABSTRACT
    public abstract void addOption(TextOption textOption);
    public abstract void addOption(ButtonOption buttonOption);
    
    public abstract void initGoal();

    public abstract void shuffle();

    public abstract Test test(ButtonOption o);
    public abstract Test test(String in);

    public abstract Boolean isOrdered();
}