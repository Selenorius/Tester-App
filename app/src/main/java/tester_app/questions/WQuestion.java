package tester_app.questions;

import static tester_app.helpers.Constants.backgroundColor;
import static tester_app.helpers.Constants.size;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JTextArea;

import tester_app.helpers.RoundedPanel;
import tester_app.options.ButtonOption;
import tester_app.options.TextOption;

public class WQuestion extends Question {
    private ArrayList<TextOption> options;

    private JTextArea textArea;
    
    public WQuestion() {
        score = 0;
        options = new ArrayList<>();

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
        textArea.setBackground(backgroundColor);
        textArea.setForeground(Color.WHITE);
        textArea.setCaretColor(Color.WHITE);

        inputArea = new RoundedPanel();
        inputArea.add(textArea);
        inputArea.setBackground(null);
        inputArea.setBorderPainted(false);
        
        this.add(new JLabel(this.questionText));
        this.add(inputArea);

        setInputAreaSize(size.width - 28, size.height - 28);
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
                    return Test.COMPLETION;
                }

                return Test.SUCCESS;
            }
        }

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
        inputArea.setSize(x, y);
        textArea.setSize(x - 28, y - 28);
    }
    @Override
    public void setInputAreaSize(Dimension d) {
        inputArea.setSize(d);
        textArea.setSize(d);
    }
}
