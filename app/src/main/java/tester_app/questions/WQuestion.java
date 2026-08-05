package tester_app.questions;

import static tester_app.helpers.Constants.addMargin;
import static tester_app.helpers.Constants.backgroundColor;
import static tester_app.helpers.Constants.margin;
import static tester_app.helpers.Constants.next;
import static tester_app.helpers.Constants.size;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JTextArea;

import tester_app.Exam;
import tester_app.helpers.RoundedPanel;
import tester_app.options.ButtonOption;
import tester_app.options.TextOption;

public class WQuestion extends Question {
    private ArrayList<TextOption> options;

    private JTextArea textArea;
    
    public WQuestion(Exam exam) {
        score = 0;
        options = new ArrayList<>();
        status = Test.SUCCESS;
        this.exam = exam;

        this.setBackground(null);
        this.setBorderPainted(false);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        textArea = new JTextArea();
        textArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String in = textArea.getText();

                    if(in != null) {
                        textArea.setText("");

                        test(in);
                    }
                }
            }
        });
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setBackground(null);
        textArea.setForeground(Color.WHITE);
        textArea.setCaretColor(Color.WHITE);

        inputArea = new RoundedPanel();
        inputArea.add(textArea);
        inputArea.setBackground(backgroundColor);

        questionTextLabel = new JLabel();
        questionTextLabel.setText("No question text found");
        questionTextLabel.setForeground(Color.WHITE);
        questionTextLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        addMargin(questionTextLabel, margin * 4);
        
        this.add(questionTextLabel);
        this.add(inputArea);

        setInputAreaSize(size.width, size.height);
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

    @Override
    public void shuffle() {
        throw new UnsupportedOperationException("Unimplemented method 'shuffle'");
    }

    @Override
    public Test test(String in) {
        for(TextOption o : options) {
            if(o.isTrue(in)) {
                ++score;

                if(score >= goal) {
                    next(exam, this);

                    return Test.COMPLETION;
                }

                return Test.SUCCESS;
            }
        }

        next(exam, this);

        return Test.FAIL;
    }

    @Override
    public Test test(ButtonOption o) {
        throw new UnsupportedOperationException("Unimplemented method 'test'");
    }

    @Override
    public Boolean isOrdered() {
        throw new UnsupportedOperationException("Unimplemented method 'isOrdered'");
    }

    @Override
    public void setInputAreaSize(int x, int y) {
        x -= 56;
        y -= 56;

        inputArea.setSize(x, y);
        textArea.setSize(x, y);
    }
    @Override
    public void setInputAreaSize(Dimension d) {
        inputArea.setSize(d.width - 56, d.height - 56);
        textArea.setSize(d.width - 56, d.height - 56);
    }
}
