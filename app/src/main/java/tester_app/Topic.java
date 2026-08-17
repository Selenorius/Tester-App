package tester_app;

import static tester_app.helpers.Constants.backgroundColor;
import static tester_app.helpers.Constants.deleteColor;
import static tester_app.helpers.Constants.editColor;
import static tester_app.helpers.Constants.fieldColor;
import static tester_app.helpers.Constants.margin;
import static tester_app.helpers.Constants.styleButton;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.stream.Stream;

import javax.swing.JOptionPane;
import javax.swing.JRadioButton;

import tester_app.helpers.HamburgerMenu;
import tester_app.helpers.RoundedButton;
import tester_app.helpers.RoundedPanel;
import tester_app.helpers.RoundedTextArea;
import tester_app.options.ButtonOption;
import tester_app.options.TextOption;
import tester_app.questions.MCQuestion;
import tester_app.questions.Question;
import tester_app.questions.TFQuestion;
import tester_app.questions.WQuestion;

public class Topic extends HamburgerMenu {
    private Tester tester;

    public Topic(TopicBuilder builder) {
        super(builder);

        tester = builder.tester;
    }

    public void loadFiles(final File dir, final Image fileIcon) {
        if(dir.isDirectory()) {
            for (final File f : dir.listFiles()) {
                if (!f.isDirectory()) {
                    addExam(f, fileIcon);
                }
            }
        } else {
            addExam(dir, fileIcon);
        }

        HamburgerMenu addButton = new HamburgerMenu.HamburgerMenuBuilder().icon(tester.editorButtonIcon).build();

        addComponent(addButton);
    }

    public void addExam(final File file, final Image fileIcon) {
        String
            fileName = file.getName(),
            examName = fileName.substring(0, fileName.length() - 4);
        RoundedButton
            startButton = new RoundedButton(),
            deleteButton = new RoundedButton();
        HamburgerMenu hamburgerMenu = new HamburgerMenu.HamburgerMenuBuilder().text(examName).icon(fileIcon).build();

        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tester.startExam(file, tester);
            }
        });
        styleButton(startButton, "Start exam");
        hamburgerMenu.addComponent(startButton);
    
        HamburgerMenu editMenu = loadEditMenu(file);
        hamburgerMenu.addComponent(editMenu);

        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int response = JOptionPane.showConfirmDialog(
                    tester,
                    "Are you sure you want to delete this file?",
                    "Warning",
                    JOptionPane.YES_NO_OPTION
                );

                if (response == JOptionPane.YES_OPTION) {
                    try {
                        file.delete();

                        tester.dispose();
                        tester.start();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
        styleButton(deleteButton, "Delete exam");
        deleteButton.setSelectionColor(deleteColor);
        hamburgerMenu.addComponent(deleteButton);

        addComponent(hamburgerMenu);
    }

    private HamburgerMenu loadEditMenu(File file) {
        Exam exam = new Exam(file, tester);
        HamburgerMenu editMenu = new HamburgerMenu.HamburgerMenuBuilder().text("Edit exam").build();
        RoundedButton
            saveButton = new RoundedButton();
        RoundedPanel editPanel;

        ArrayList<Question> questions = exam.getQuestions();
        ArrayList<RoundedTextArea> questionTextAreas = new ArrayList<>();
        ArrayList<RoundedTextArea> optionTextAreas = new ArrayList<>();
        ArrayList<JRadioButton>
            optionRadioButtons = new ArrayList<>(),
            orderedRadioButtons = new ArrayList<>();

        GridBagConstraints constraints = new GridBagConstraints();

        for(Question q : questions) {
            constraints.fill = GridBagConstraints.BOTH;
            constraints.weightx = 0.5;
            constraints.weighty = 0.5;

            editPanel = new RoundedPanel();
            editPanel.setBackground(fieldColor);
            editPanel.setBorderPainted(false);
            editPanel.setLayout(getLayout());

            RoundedPanel textPanel = new RoundedPanel();
            textPanel.setLayout(getLayout());
            textPanel.setBackground(backgroundColor);

            RoundedTextArea textArea = new RoundedTextArea(q.getQuestionText());
            textPanel.add(textArea, constraints);

            constraints.gridx = 1;
            constraints.insets = new Insets(margin, margin, margin, margin);

            editPanel.add(textPanel, constraints);
            questionTextAreas.add(textArea);

            if(q.getClass() == WQuestion.class) {
                ArrayList<TextOption> options = q.getTextOptions();

                for(TextOption o : options) {
                    textPanel = new RoundedPanel();
                    textPanel.setLayout(getLayout());
                    textPanel.setBackground(backgroundColor);

                    RoundedButton deleteButton = new RoundedButton();
                    deleteButton.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            int response = JOptionPane.showConfirmDialog(
                                tester,
                                "Are you sure you want to delete this option?",
                                "Warning",
                                JOptionPane.YES_NO_OPTION
                            );

                            if (response == JOptionPane.YES_OPTION) {
                                q.removeOption(o);

                                exam.setQuestions(questions);
                                exam.saveToFile(file.getName().substring(0, file.getName().length() - 4), file.getParentFile());

                                tester.dispose();
                                tester.start();
                            }
                        }
                    });
                    styleButton(deleteButton, "Delete option");
                    deleteButton.setSelectionColor(deleteColor);

                    constraints.insets = new Insets(0, 0, 0, 0);
                    
                    textArea = new RoundedTextArea(o.getClearText());
                    textPanel.add(textArea, constraints);

                    constraints.fill = GridBagConstraints.HORIZONTAL;
                    constraints.insets = new Insets(0, 0, 0, 0);
                    
                    textPanel.add(deleteButton, constraints);

                    constraints.insets = new Insets(margin, margin, margin, margin);

                    editPanel.add(textPanel, constraints);
                    
                    optionTextAreas.add(textArea);
                }
            } else if(q.getClass() == MCQuestion.class) {
                ArrayList<ButtonOption> options = q.getButtonOptions();

                JRadioButton radioButton = new JRadioButton();
                radioButton.setFocusable(false);
                radioButton.setBackground(null);
                radioButton.setForeground(Color.WHITE);
                radioButton.setText("Ordered");
                radioButton.setSelected(q.isOrdered());
                textPanel.add(radioButton, constraints);

                orderedRadioButtons.add(radioButton);

                for(ButtonOption o : options) {
                    textPanel = new RoundedPanel();
                    textPanel.setLayout(getLayout());
                    textPanel.setBackground(backgroundColor);

                    RoundedButton deleteButton = new RoundedButton();
                    deleteButton.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            int response = JOptionPane.showConfirmDialog(
                                tester,
                                "Are you sure you want to delete this option?",
                                "Warning",
                                JOptionPane.YES_NO_OPTION
                            );

                            if (response == JOptionPane.YES_OPTION) {
                                q.removeOption(o);

                                exam.setQuestions(questions);
                                exam.saveToFile(file.getName().substring(0, file.getName().length() - 4), file.getParentFile());

                                tester.dispose();
                                tester.start();
                            }
                        }
                    });
                    styleButton(deleteButton, "Delete option");
                    deleteButton.setSelectionColor(deleteColor);

                    constraints.insets = new Insets(0, 0, 0, 0);
                    
                    textArea = new RoundedTextArea(o.getText());
                    textPanel.add(textArea, constraints);

                    radioButton = new JRadioButton();
                    radioButton.setFocusable(false);
                    radioButton.setBackground(null);
                    radioButton.setForeground(Color.WHITE);
                    radioButton.setText("True");
                    radioButton.setSelected(o.isTrue());
                    textPanel.add(radioButton, constraints);

                    constraints.fill = GridBagConstraints.HORIZONTAL;
                    constraints.insets = new Insets(0, 0, 0, 0);
                    
                    textPanel.add(deleteButton, constraints);

                    constraints.insets = new Insets(margin, margin, margin, margin);

                    editPanel.add(textPanel, constraints);
                    
                    optionRadioButtons.add(radioButton);
                    optionTextAreas.add(textArea);
                }
            } else {
                textPanel = new RoundedPanel();
                textPanel.setLayout(getLayout());
                textPanel.setBackground(backgroundColor);

                constraints.insets = new Insets(0, 0, 0, 0);
                
                JRadioButton radioButton = new JRadioButton();
                radioButton.setFocusable(false);
                radioButton.setBackground(null);
                radioButton.setForeground(Color.WHITE);
                radioButton.setText("True");
                radioButton.setSelected(q.getButtonOptions().getFirst().isTrue());
                textPanel.add(radioButton, constraints);

                constraints.insets = new Insets(margin, margin, margin, margin);

                editPanel.add(textPanel, constraints);
                
                optionRadioButtons.add(radioButton);
            }

            constraints.fill = GridBagConstraints.HORIZONTAL;
            constraints.insets = new Insets(0, 0, 0, 0);

            if(q.getClass() == WQuestion.class) {
                RoundedButton addButton = new RoundedButton();
                addButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        q.addOption(new TextOption());

                        exam.setQuestions(questions);
                        exam.saveToFile(file.getName().substring(0, file.getName().length() - 4), file.getParentFile());

                        tester.dispose();
                        tester.start();
                    }
                });
                styleButton(addButton, "Add option");
                addButton.setSelectionColor(editColor);

                editPanel.add(addButton, constraints);
            } else if(q.getClass() == MCQuestion.class) {
                RoundedButton addButton = new RoundedButton();
                addButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        q.addOption(new ButtonOption());

                        exam.setQuestions(questions);
                        exam.saveToFile(file.getName().substring(0, file.getName().length() - 4), file.getParentFile());

                        tester.dispose();
                        tester.start();
                    }
                });
                styleButton(addButton, "Add option");
                addButton.setSelectionColor(editColor);

                editPanel.add(addButton, constraints);
            }

            RoundedButton deleteButton = new RoundedButton();
            deleteButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    int response = JOptionPane.showConfirmDialog(
                        tester,
                        "Are you sure you want to delete this question?",
                        "Warning",
                        JOptionPane.YES_NO_OPTION
                    );

                    if (response == JOptionPane.YES_OPTION) {
                        questions.remove(q);

                        exam.setQuestions(questions);
                        exam.saveToFile(file.getName().substring(0, file.getName().length() - 4), file.getParentFile());

                        tester.dispose();
                        tester.start();
                    }
                }
            });
            styleButton(deleteButton, "Delete question");
            deleteButton.setSelectionColor(deleteColor);

            editPanel.add(deleteButton, constraints);
            editMenu.addComponent(editPanel);
        }

        HamburgerMenu addMenu = new HamburgerMenu.HamburgerMenuBuilder().icon(tester.getEditorButtonIcon()).build();

        RoundedButton addWQButton = new RoundedButton();
        addWQButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                WQuestion wQuestion = new WQuestion(exam);
                wQuestion.setQuestionText("New written question");

                questions.add(wQuestion);
                
                exam.setQuestions(questions);
                exam.saveToFile(file.getName().substring(0, file.getName().length() - 4), file.getParentFile());

                tester.dispose();
                tester.start();
            }
        });
        styleButton(addWQButton, "Add written question");
        addMenu.addComponent(addWQButton);

        RoundedButton addMCButton = new RoundedButton();
        addMCButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                MCQuestion mcQuestion = new MCQuestion(exam);
                mcQuestion.setQuestionText("New multiple choice question");

                questions.add(mcQuestion);
                
                exam.setQuestions(questions);
                exam.saveToFile(file.getName().substring(0, file.getName().length() - 4), file.getParentFile());

                tester.dispose();
                tester.start();
            }
        });
        styleButton(addMCButton, "Add multiple choice question");
        addMenu.addComponent(addMCButton);

        RoundedButton addTFButton = new RoundedButton();
        addTFButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                TFQuestion tfQuestion = new TFQuestion(exam);
                tfQuestion.setQuestionText("New true/false question");

                questions.add(tfQuestion);
                
                exam.setQuestions(questions);
                exam.saveToFile(file.getName().substring(0, file.getName().length() - 4), file.getParentFile());

                tester.dispose();
                tester.start();
            }
        });
        styleButton(addTFButton, "Add true/false question");
        addMenu.addComponent(addTFButton);

        editMenu.addComponent(addMenu);

        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int response = JOptionPane.showConfirmDialog(
                    tester,
                    "Are you sure you want to save these changes?",
                    "Warning",
                    JOptionPane.YES_NO_OPTION
                );

                if (response == JOptionPane.YES_OPTION) {
                    int
                        qCount = 0,
                        rCount = 0,
                        oCount = 0,
                        tCount = 0;

                    for(Question q : questions) {
                        q.setQuestionText(questionTextAreas.get(qCount++).getText());

                        if(q.getClass() == WQuestion.class) {
                            ArrayList<TextOption> options = q.getTextOptions();

                            for(TextOption o : options) {
                                String text = optionTextAreas.get(tCount++).getText();
                                Stream<String> lines = text.lines();

                                o.clearText();

                                lines.forEach(line -> {
                                    o.addText(line);
                                });
                            }

                            q.setTextOptions(options);
                        } else if(q.getClass() == MCQuestion.class) {
                            ArrayList<ButtonOption> options = q.getButtonOptions();

                            for(ButtonOption o : options) {
                                o.setText("");

                                o.setText(optionTextAreas.get(tCount++).getText());
                                o.setValue(optionRadioButtons.get(rCount++).isSelected());
                            }

                            q.setOrdered(orderedRadioButtons.get(oCount++).isSelected());
                            q.setButtonOptions(options);
                        } else {
                            ArrayList<ButtonOption> options = q.getButtonOptions();

                            options.getFirst().setValue(optionRadioButtons.get(rCount++).isSelected());

                            q.setButtonOptions(options);
                        }
                    }
                
                    exam.setQuestions(questions);
                    
                    try {
                        if(file.exists()) {
                            file.delete();
                        }
                        exam.saveToFile(file.getName().substring(0, file.getName().length() - 4), file.getParentFile());

                        tester.dispose();
                        tester.start();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
        styleButton(saveButton, "Save changes");
        saveButton.setSelectionColor(editColor);
        editMenu.addComponent(saveButton);
        
        return editMenu;
    }

    public static class TopicBuilder extends HamburgerMenuBuilder {
        protected Tester tester;

        @Override
        public TopicBuilder text(String text) {
            this.text = text;
            return this;
        }

        @Override
        public TopicBuilder icon(Image icon) {
            this.icon = icon;
            return this;
        }

        public TopicBuilder tester(Tester tester) {
            this.tester = tester;
            return this;
        }

        public Topic build() {
            return new Topic(this);
        }
    }
}
