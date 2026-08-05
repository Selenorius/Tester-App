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
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;

import tester_app.Exam;
import tester_app.helpers.RoundedPanel;
import tester_app.options.ButtonOption;
import tester_app.options.TextOption;

public class WQuestion extends Question {
    private ArrayList<TextOption>
        options;

    private JTextArea textArea;
    private JScrollPane scrollPane;
    
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
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String in = textArea.getText();

                    if(in != null) {
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
        textArea.setFont(null);

        scrollPane = new JScrollPane(textArea);
        scrollPane.setBorder(null);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        inputArea = new RoundedPanel();
        inputArea.add(scrollPane);
        inputArea.setBackground(backgroundColor);

        questionTextLabel = new JLabel("<html>" + "No question text found" + "<html>", SwingConstants.CENTER);
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

        for(int i = 0; i < options.size(); ++i) {
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
                options.remove(o);
                ++score;

                if(score >= goal) {
                    JOptionPane.showMessageDialog(
                        exam,
                        "Great Job!",
                        "Correct Answer!",
                        JOptionPane.PLAIN_MESSAGE
                    );

                    exam.incrementScore();
                    next(exam, this);

                    return Test.COMPLETION;
                }

                return Test.SUCCESS;
            }
        }

        String text = "";
        for(TextOption o : options) {
            String s = o.getText();

            if(!text.contains(s)) {
                text += s + System.lineSeparator();
            }
        }

        JOptionPane.showMessageDialog(
            exam,
            ("Correct Answers: " + System.lineSeparator() + System.lineSeparator() +
            text +
            "Keep trying, you can do it!"),
            "Incorrect Answer!",
            JOptionPane.PLAIN_MESSAGE
        );

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

        questionTextLabel.setPreferredSize(new Dimension(x, y / 4));
        inputArea.setMaximumSize(new Dimension(x, y));
        scrollPane.setPreferredSize(new Dimension(x - margin, y - y / 4 - margin * 5));
    }
    @Override
    public void setInputAreaSize(Dimension d) {
        questionTextLabel.setPreferredSize(new Dimension(d.width - 56, d.height / 4 - 56));
        inputArea.setMaximumSize(new Dimension(d.width - 56, d.height - 56));
        scrollPane.setPreferredSize(new Dimension(d.width - 56 - margin, d.height - 56 - d.height / 4 - margin * 5));
    }

    @Override
    public JTextArea getTextArea() {
        return textArea;
    }
}
