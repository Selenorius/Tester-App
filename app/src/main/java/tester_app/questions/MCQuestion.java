package tester_app.questions;

import static tester_app.helpers.Constants.addMargin;
import static tester_app.helpers.Constants.buttonFont;
import static tester_app.helpers.Constants.deleteColor;
import static tester_app.helpers.Constants.editColor;
import static tester_app.helpers.Constants.getScaledDimension;
import static tester_app.helpers.Constants.margin;
import static tester_app.helpers.Constants.next;
import static tester_app.helpers.Constants.styleButton;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import tester_app.Exam;
import tester_app.helpers.RoundedLabel;
import tester_app.helpers.RoundedPanel;
import tester_app.options.ButtonOption;
import tester_app.options.TextOption;

public class MCQuestion extends Question {
    private ArrayList<ButtonOption> options;
    private Boolean ordered;
    private GridBagLayout layout;
    private GridBagConstraints constraints;

    public MCQuestion(Exam exam) {
        score = 0;
        options = new ArrayList<>();
        ordered = false;
        this.exam = exam;
        layout = new GridBagLayout();
        constraints = new GridBagConstraints();

        this.setBackground(exam.getBackground());
        this.setBorderPainted(false);
        this.setLayout(layout);

        inputArea = new RoundedPanel();
        inputArea.setBackground(getBackground().darker().darker());
        inputArea.setBorderColor(getBackground().brighter());
        inputArea.setBorderPainted(false);
        inputArea.setLayout(layout);
        inputArea.setTransparency(true);
        addMargin(inputArea, 0);

        questionTextLabel = new RoundedLabel("<html><center>" + "No question text found" + "<html>", SwingConstants.CENTER);
        questionTextLabel.setBackground(Color.WHITE);
        questionTextLabel.setForeground(Color.WHITE);
        questionTextLabel.setAlignmentX(RoundedLabel.CENTER_ALIGNMENT);
        questionTextLabel.setLayout(layout);
        questionTextLabel.setHorizontalTextPosition(RoundedLabel.CENTER);
        questionTextLabel.setVerticalTextPosition(RoundedLabel.BOTTOM);
        questionTextLabel.setIconTextGap(margin * 3);
        questionTextLabel.setFont(buttonFont);
        questionTextLabel.setPainted(true);
        addMargin(questionTextLabel, margin * 3, margin * 4, margin * 3, margin * 4);

        constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridx = 1;
        constraints.weightx = 0.5;
        constraints.weighty = 0.5;
        constraints.insets = new Insets(margin, margin, margin, margin);

        this.add(questionTextLabel, constraints);

        constraints.insets = new Insets(0, 0, 0, 0);

        this.add(inputArea, constraints);

        constraints = new GridBagConstraints();
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
        o.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                test(o);
            }
        });

        constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 0.5;
        constraints.weighty = 0.5;
        constraints.gridwidth = 1;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new Insets(0, 0, 0, 0);

        int menCount = 0;
        for(Component c : inputArea.getComponents()) {
            constraints.gridx = 2 - (menCount++ % 2);

            layout.setConstraints(c, constraints);
        }

        constraints.gridx = 2 - (inputArea.getComponentCount() % 2);
        constraints.gridwidth = 3 - (inputArea.getComponentCount() % 2);

        options.add(o);
        inputArea.add(o, constraints);

        String
            text = o.getButtonText(),
            image = o.getImagePath();

        styleButton(o, text, new ImageIcon(image).getImage());

        inputArea.revalidate();
    }

    @Override
    public void addOption(TextOption textOption) {
        throw new UnsupportedOperationException("Unimplemented method 'addOption'");
    }

    @Override
    public void shuffle() {
        if(!ordered) {
            Collections.shuffle(options);
        }

        constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 0.5;
        constraints.weighty = 0.5;
        constraints.gridwidth = 1;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new Insets(0, 0, 0, 0);

        int menCount = 0;
        for(ButtonOption butt : options) {
            constraints.gridx = 2 - (menCount % 2);
            constraints.gridy = Math.abs(menCount++ / 2);

            layout.setConstraints(butt, constraints);
        }

        constraints.gridx = (inputArea.getComponentCount() % 2);
        constraints.gridwidth = (inputArea.getComponentCount() % 2) - 1;

        layout.setConstraints(options.getLast(), constraints);
    }

    @Override
    public Test test(ButtonOption o) {
        if(o.isTrue()) {
            options.remove(o);
            o.setEnabled(false);
            o.setBackground(editColor);
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
        } else {
            options.remove(o);
            o.setEnabled(false);
            o.setBackground(deleteColor);

            String
                text = "",
                correctionText = "Correct Answers: ";

            int ansCount = 0;
            for(ButtonOption b : options) {
                if(b.isTrue()) {
                    String s = b.getButtonText();
                
                    text += "- " + s + System.lineSeparator();

                    ++ansCount;
                }
            }

            if(ansCount == 1) {
                correctionText = "Correct Answer: ";
            }

            JOptionPane.showMessageDialog(
                exam,
                correctionText + System.lineSeparator() + System.lineSeparator() +
                text + System.lineSeparator() +
                "Keep trying, you can do it!",
                "Incorrect Answer!",
                JOptionPane.PLAIN_MESSAGE
            );

            next(exam, this);

            return Test.FAIL;
        }
    }
    @Override
    public Test test(String in) {
        throw new UnsupportedOperationException("Unimplemented method 'test'");
    }

    @Override
    public Boolean isOrdered() {
        return ordered;
    }
    @Override
    public JTextArea getTextArea() {
        throw new UnsupportedOperationException("Unimplemented method 'getTextArea'");
    }
    @Override
    public ArrayList<ButtonOption> getButtonOptions() {
        return options;
    }
    @Override
    public ArrayList<TextOption> getTextOptions() {
        throw new UnsupportedOperationException("Unimplemented method 'getTextOptions'");
    }

    @Override
    public void removeOption(TextOption textOption) {
        throw new UnsupportedOperationException("Unimplemented method 'removeOption'");
    }

    @Override
    public void removeOption(ButtonOption buttonOption) {
        options.remove(buttonOption);
        inputArea.remove(buttonOption);
    }

    @Override
    public void setTextOptions(ArrayList<TextOption> options) {
        throw new UnsupportedOperationException("Unimplemented method 'setTextOptions'");
    }

    @Override
    public void setButtonOptions(ArrayList<ButtonOption> options) {
        this.options = options;

        for(Component c : inputArea.getComponents()) {
            if(c.getClass() == ButtonOption.class) {
                inputArea.remove(c);
            }
        }

        for(ButtonOption o : options) {
            o.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    test(o);
                }
            });

            constraints = new GridBagConstraints();
            constraints.fill = GridBagConstraints.BOTH;
            constraints.weightx = 0.5;
            constraints.weighty = 0.5;
            constraints.gridwidth = 1;
            constraints.anchor = GridBagConstraints.CENTER;
            constraints.insets = new Insets(margin * 2, margin * 2, margin * 2, margin * 2);

            int menCount = 0;
            for(Component c : inputArea.getComponents()) {
                constraints.gridx = 2 - (menCount++ % 2);

                layout.setConstraints(c, constraints);
            }

            constraints.gridx = 2 - (inputArea.getComponentCount() % 2);
            constraints.gridwidth = 3 - (inputArea.getComponentCount() % 2);

            inputArea.add(o, constraints);

            String
                text = o.getButtonText(),
                image = o.getImagePath();

            styleButton(o, text, new ImageIcon(image).getImage());

            inputArea.revalidate();
        }
    }

    @Override
    public void setOrdered(Boolean ordered) {
        this.ordered = ordered;
    }

    @Override
    public void adjustImage() {
        if(questionTextLabel.getIcon() != null)  {
            Dimension newSize = getScaledDimension(new Dimension(questionTextLabel.getIcon().getIconWidth(), questionTextLabel.getIcon().getIconHeight()), questionTextLabel.getSize());

            questionTextLabel.setIcon(new ImageIcon(new ImageIcon(getQuestionImage()).getImage().getScaledInstance(newSize.width, newSize.height, Image.SCALE_SMOOTH)));
        }

        for(ButtonOption o : options) {
            if(o.getButtonIcon() != null)  {
                if(o.getButtonIcon().getIconWidth() > 0 && o.getButtonIcon().getIconHeight() > 0) {
                    Dimension newSize = getScaledDimension(new Dimension(o.getButtonIcon().getIconWidth(), o.getButtonIcon().getIconHeight()), o.getSize());
        
                    o.setButtonIcon(new ImageIcon(new ImageIcon(o.getImagePath()).getImage().getScaledInstance(newSize.width, newSize.height, Image.SCALE_SMOOTH)));
                }
            }
        }
    }
}
