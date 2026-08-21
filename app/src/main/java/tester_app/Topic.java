package tester_app;

import static tester_app.helpers.Constants.addMargin;
import static tester_app.helpers.Constants.deleteColor;
import static tester_app.helpers.Constants.editBackgroundColor;
import static tester_app.helpers.Constants.editBorderColor;
import static tester_app.helpers.Constants.editColor;
import static tester_app.helpers.Constants.examAddBorderColor;
import static tester_app.helpers.Constants.examBackgroundColor;
import static tester_app.helpers.Constants.examBorderColor;
import static tester_app.helpers.Constants.fieldColor;
import static tester_app.helpers.Constants.margin;
import static tester_app.helpers.Constants.styleButton;
import static tester_app.helpers.Constants.topicBackgroundColor;
import static tester_app.helpers.Constants.topicBorderColor;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    private boolean titled;

    public Topic(TopicBuilder builder) {
        super(builder);

        tester = builder.tester;
        titled = true;

        this.setBackground(topicBackgroundColor);
        this.setBorderColor(topicBorderColor);
        this.setBlotOffset(3);
    }

    public void loadFiles(final File dir, final Image fileIcon) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 0.5;

        RoundedPanel titlePanel = new RoundedPanel();
        titlePanel.setBackground(fieldColor);
        titlePanel.setBorderColor(examAddBorderColor);
        titlePanel.setBorderPainted(true);
        titlePanel.setLayout(getLayout());

        RoundedPanel untitledPanel = new RoundedPanel();
        untitledPanel.setBackground(null);
        untitledPanel.setBorderPainted(false);

        RoundedTextArea titleArea = new RoundedTextArea(dir.getName());
        titleArea.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                if(e != null) {
                    String fileTitle = titleArea.getText();

                    if(fileTitle.length() <= 30) {
                        String
                            oldPath = dir.getPath(),
                            newPath = dir.getParentFile().getPath() + "/" + fileTitle;

                        if(oldPath != newPath) {
                            Path oldDirPath = Paths.get(oldPath);
                            Path newDirPath = Paths.get(newPath);

                            try {
                                Files.move(oldDirPath, newDirPath);
                                setText(fileTitle);
                            } catch (Exception e1) {
                                System.out.println("Error renaming topic: " + e1.getMessage());
                                e1.printStackTrace();
                            }
                        }
                    }

                    tester.reset();
                }
            }
        });
        titleArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(titleArea.getText().length() <= 30) {
                    titleArea.setForeground(Color.WHITE);
                } else {
                    titleArea.setForeground(deleteColor);
                }

                if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                    e.consume();
                    tester.requestFocus();
                }
            }
        });
        titlePanel.add(titleArea, constraints);

        constraints.weightx = 0;
        constraints.gridx = 0;
        constraints.gridy = 0;
        
        if(titled) {
            addComponent(titlePanel, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, 0);
        } else {
            getMenu().add(untitledPanel, constraints);
            setBlotOffset(getMenuSize() / 2);
        }
        
        if(dir.isDirectory()) {
            for (final File f : dir.listFiles()) {
                if (!f.isDirectory()) {
                    addExam(f, fileIcon);
                }
            }
        } else {
            addExam(dir, fileIcon);
        }

        RoundedButton addButton = new RoundedButton();
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    File file = new File(dir.getPath() + "/New exam.txt");
                    if(!file.exists()) {
                        file.createNewFile();
                        addExam(file, fileIcon);

                        tester.reset();
                    }
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });

        RoundedButton deleteButton = new RoundedButton();
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int response = JOptionPane.showConfirmDialog(
                    tester,
                    "Are you sure you want to delete this topic?",
                    "Warning",
                    JOptionPane.YES_NO_OPTION
                );

                if (response == JOptionPane.YES_OPTION) {
                    try {
                        dir.delete();
                        deleteTopic();

                        tester.reset();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });

        if(titled) {
            addComponent(addButton);
            addComponent(deleteButton);

            styleButton(addButton, "Add exam");
            addButton.setBackground(editColor);

            styleButton(deleteButton, "Delete topic");
            deleteButton.setBackground(deleteColor);
        }
    }

    public void addExam(final File file, final Image fileIcon) {
        String
            fileName = file.getName(),
            examName = fileName.substring(0, fileName.length() - 4);
        RoundedButton
            startButton = new RoundedButton(),
            deleteButton = new RoundedButton();
        HamburgerMenu hamburgerMenu = new HamburgerMenu.HamburgerMenuBuilder().parent(this.getMenu()).text(examName).icon(fileIcon).build();
        hamburgerMenu.setBackground(examBackgroundColor);
        hamburgerMenu.setBorderColor(examBorderColor);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 0.5;

        RoundedPanel titlePanel = new RoundedPanel();
        titlePanel.setBackground(fieldColor);
        titlePanel.setBorderColor(examAddBorderColor);
        titlePanel.setBorderPainted(true);
        titlePanel.setLayout(getLayout());

        RoundedTextArea titleArea = new RoundedTextArea(examName);
        titleArea.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                if(e != null) {
                    String fileTitle = titleArea.getText();

                    if(fileTitle.length() <= 30) {
                        String
                            oldPath = file.getPath(),
                            newPath = file.getParentFile().getPath() + "/" + fileTitle + ".txt";

                        if(oldPath != newPath) {
                            Path oldDirPath = Paths.get(oldPath);
                            Path newDirPath = Paths.get(newPath);

                            try {
                                Files.move(oldDirPath, newDirPath);
                                hamburgerMenu.setText(fileTitle);
                            } catch (Exception e1) {
                                System.out.println("Error renaming exam: " + e1.getMessage());
                                e1.printStackTrace();
                            }
                        }
                    }

                    tester.reset();
                }
            }
        });
        titleArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(titleArea.getText().length() <= 30) {
                    titleArea.setForeground(Color.WHITE);
                } else {
                    titleArea.setForeground(deleteColor);
                }

                if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                    e.consume();
                    tester.requestFocus();
                }
            }
        });
        titlePanel.add(titleArea, constraints);
        
        hamburgerMenu.addComponent(titlePanel, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, 0);

        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tester.startExam(file, tester);
            }
        });
        hamburgerMenu.addComponent(startButton);
        styleButton(startButton, "Start exam");
    
        HamburgerMenu editMenu = loadEditMenu(file, hamburgerMenu);
        hamburgerMenu.addComponent(editMenu);

        hamburgerMenu.setBlotOffset(6 - editMenu.getMenuSize());

        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int response = JOptionPane.showConfirmDialog(
                    tester,
                    "Are you sure you want to delete this exam?",
                    "Warning",
                    JOptionPane.YES_NO_OPTION
                );

                if (response == JOptionPane.YES_OPTION) {
                    try {
                        file.delete();
                        deleteExam(hamburgerMenu);

                        tester.reset();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
        hamburgerMenu.addComponent(deleteButton);

        styleButton(deleteButton, "Delete exam");
        deleteButton.setBackground(deleteColor);

        addComponent(hamburgerMenu);
    }

    private HamburgerMenu loadEditMenu(File file, Component parent) {
        Exam exam = new Exam(file, tester);
        HamburgerMenu editMenu = new HamburgerMenu.HamburgerMenuBuilder().parent(parent).text("Edit exam").build();
        RoundedPanel editPanel;

        ArrayList<Question> questions = exam.getQuestions();
        ArrayList<RoundedTextArea>
            questionTextAreas = new ArrayList<>(),
            optionTextAreas = new ArrayList<>();
        ArrayList<JRadioButton>
            optionRadioButtons = new ArrayList<>(),
            orderedRadioButtons = new ArrayList<>();

        GridBagConstraints constraints = new GridBagConstraints();

        editMenu.setBackground(editBackgroundColor);
        editMenu.setBorderColor(editBorderColor);
        editMenu.setBlotOffset(1);

        for(Question q : questions) {
            constraints.fill = GridBagConstraints.BOTH;
            constraints.weightx = 0.5;
            constraints.weighty = 0.5;
            constraints.insets = new Insets(margin * 2, margin * 2, margin * 2, margin * 2);

            editPanel = new RoundedPanel();
            editPanel.setBackground(fieldColor);
            editPanel.setBorderColor(examAddBorderColor);
            editPanel.setBorderPainted(true);
            editPanel.setLayout(getLayout());
            addMargin(editPanel, margin * 2);

            RoundedPanel textPanel = new RoundedPanel();
            textPanel.setLayout(getLayout());
            textPanel.setBackground(fieldColor);
            textPanel.setBorderColor(examAddBorderColor);
            textPanel.setBorderPainted(true);

            RoundedTextArea textArea = new RoundedTextArea(q.getQuestionText());
            textArea.addKeyListener(new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent e) {
                    if(e.getKeyCode() != KeyEvent.VK_ENTER) {
                        saveExam(file, questions, questionTextAreas, optionTextAreas, optionRadioButtons, orderedRadioButtons);
                    }
                }
            });
            textPanel.add(textArea, constraints);

            constraints.gridx = 1;
            constraints.insets = new Insets(margin * 2, margin * 2, margin * 2, margin * 2);

            editPanel.add(textPanel, constraints);
            questionTextAreas.add(textArea);

            if(q.getClass() == WQuestion.class) {
                ArrayList<TextOption> options = q.getTextOptions();

                for(TextOption o : options) {
                    textPanel = new RoundedPanel();
                    textPanel.setLayout(getLayout());
                    textPanel.setBackground(fieldColor);
                    textPanel.setBorderColor(examAddBorderColor);
                    textPanel.setBorderPainted(true);

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

                                tester.reset();
                            }
                        }
                    });

                    constraints.insets = new Insets(margin * 2, margin * 2, margin * 2, margin * 2);
                    
                    textArea = new RoundedTextArea(o.getClearText());
                    textArea.addKeyListener(new KeyAdapter() {
                        @Override
                        public void keyReleased(KeyEvent e) {
                            if(e.getKeyCode() != KeyEvent.VK_ENTER) {
                                saveExam(file, questions, questionTextAreas, optionTextAreas, optionRadioButtons, orderedRadioButtons);
                            }
                        }
                    });
                    textPanel.add(textArea, constraints);

                    constraints.fill = GridBagConstraints.HORIZONTAL;
                    constraints.insets = new Insets(margin * 2, margin * 2, margin * 2, margin * 2);
                    
                    textPanel.add(deleteButton, constraints);

                    styleButton(deleteButton, "Delete option");
                    deleteButton.setBackground(deleteColor);

                    constraints.insets = new Insets(margin * 2, margin * 2, margin * 2, margin * 2);

                    editPanel.add(textPanel, constraints);
                    
                    optionTextAreas.add(textArea);
                }
            } else if(q.getClass() == MCQuestion.class) {
                ArrayList<ButtonOption> options = q.getButtonOptions();

                JRadioButton radioButton = new JRadioButton();
                radioButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        saveExam(file, questions, questionTextAreas, optionTextAreas, optionRadioButtons, orderedRadioButtons);
                    }
                });
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
                    textPanel.setBackground(fieldColor);
                    textPanel.setBorderColor(examAddBorderColor);
                    textPanel.setBorderPainted(true);

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

                                tester.reset();
                            }
                        }
                    });

                    constraints.insets = new Insets(margin * 2, margin * 2, margin * 2, margin * 2);
                    
                    textArea = new RoundedTextArea(o.getText());
                    textArea.addKeyListener(new KeyAdapter() {
                        @Override
                        public void keyReleased(KeyEvent e) {
                            if(e.getKeyCode() != KeyEvent.VK_ENTER) {
                                saveExam(file, questions, questionTextAreas, optionTextAreas, optionRadioButtons, orderedRadioButtons);
                            }
                        }
                    });
                    textPanel.add(textArea, constraints);

                    radioButton = new JRadioButton();
                    radioButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            saveExam(file, questions, questionTextAreas, optionTextAreas, optionRadioButtons, orderedRadioButtons);
                        }
                    });
                    radioButton.setFocusable(false);
                    radioButton.setBackground(null);
                    radioButton.setForeground(Color.WHITE);
                    radioButton.setText("True");
                    radioButton.setSelected(o.isTrue());
                    textPanel.add(radioButton, constraints);

                    constraints.fill = GridBagConstraints.HORIZONTAL;
                    constraints.insets = new Insets(margin * 2, margin * 2, margin * 2, margin * 2);
                    
                    textPanel.add(deleteButton, constraints);

                    styleButton(deleteButton, "Delete option");
                    deleteButton.setBackground(deleteColor);

                    constraints.insets = new Insets(margin * 2, margin * 2, margin * 2, margin * 2);

                    editPanel.add(textPanel, constraints);
                    
                    optionRadioButtons.add(radioButton);
                    optionTextAreas.add(textArea);
                }
            } else {
                textPanel = new RoundedPanel();
                textPanel.setLayout(getLayout());
                textPanel.setBackground(fieldColor);
                textPanel.setBorderColor(examAddBorderColor);
                textPanel.setBorderPainted(true);

                constraints.insets = new Insets(margin * 2, margin * 2, margin * 2, margin * 2);
                
                JRadioButton radioButton = new JRadioButton();
                radioButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        saveExam(file, questions, questionTextAreas, optionTextAreas, optionRadioButtons, orderedRadioButtons);
                    }
                });
                radioButton.setFocusable(false);
                radioButton.setBackground(null);
                radioButton.setForeground(Color.WHITE);
                radioButton.setText("True");
                radioButton.setSelected(q.getButtonOptions().getFirst().isTrue());
                textPanel.add(radioButton, constraints);

                constraints.insets = new Insets(margin * 2, margin * 2, margin * 2, margin * 2);

                editPanel.add(textPanel, constraints);
                
                optionRadioButtons.add(radioButton);
            }

            constraints.fill = GridBagConstraints.HORIZONTAL;
            constraints.insets = new Insets(margin * 2, margin * 2, margin * 2, margin * 2);

            if(q.getClass() == WQuestion.class) {
                RoundedButton addButton = new RoundedButton();
                addButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        TextOption option = new TextOption();
                        option.addText("Text missing...");
                        q.addOption(option);

                        exam.setQuestions(questions);
                        exam.saveToFile(file.getName().substring(0, file.getName().length() - 4), file.getParentFile());

                        tester.reset();
                    }
                });

                constraints.insets = new Insets(margin, margin, margin, margin);

                editPanel.add(addButton, constraints);

                styleButton(addButton, "Add option");
                addButton.setBackground(editColor);
            } else if(q.getClass() == MCQuestion.class) {
                RoundedButton addButton = new RoundedButton();
                addButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        q.addOption(new ButtonOption());

                        exam.setQuestions(questions);
                        exam.saveToFile(file.getName().substring(0, file.getName().length() - 4), file.getParentFile());

                        tester.reset();
                    }
                });

                constraints.insets = new Insets(margin, margin, margin, margin);

                editPanel.add(addButton, constraints);

                styleButton(addButton, "Add option");
                addButton.setBackground(editColor);
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

                        tester.reset();
                    }
                }
            });

            constraints.insets = new Insets(margin, margin, margin, margin);

            editPanel.add(deleteButton, constraints);

            styleButton(deleteButton, "Delete question");
            deleteButton.setBackground(deleteColor);

            editMenu.addComponent(editPanel);
        }

        HamburgerMenu addMenu = new HamburgerMenu.HamburgerMenuBuilder().parent(editMenu).icon(tester.getEditorButtonIcon()).build();
        addMenu.setBackground(fieldColor);
        addMenu.setBorderColor(examAddBorderColor);
        addMenu.setBorderPainted(true);

        RoundedButton addWQButton = new RoundedButton();
        addWQButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                WQuestion wQuestion = new WQuestion(exam);
                wQuestion.setQuestionText("New written question");

                questions.add(wQuestion);
                
                saveExam(file, questions, questionTextAreas, optionTextAreas, optionRadioButtons, orderedRadioButtons);
                tester.reset();
            }
        });

        addMenu.addComponent(addWQButton);

        styleButton(addWQButton, "Add written question");
        addWQButton.setBackground(editColor);

        RoundedButton addMCButton = new RoundedButton();
        addMCButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                MCQuestion mcQuestion = new MCQuestion(exam);
                mcQuestion.setQuestionText("New multiple choice question");

                questions.add(mcQuestion);
                
                saveExam(file, questions, questionTextAreas, optionTextAreas, optionRadioButtons, orderedRadioButtons);
                tester.reset();
            }
        });

        addMenu.addComponent(addMCButton);

        styleButton(addMCButton, "Add multiple choice question");
        addMCButton.setBackground(editColor);

        RoundedButton addTFButton = new RoundedButton();
        addTFButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                TFQuestion tfQuestion = new TFQuestion(exam);
                tfQuestion.setQuestionText("New true/false question");

                ButtonOption trueOption = new ButtonOption();
                trueOption.setText("True");
                trueOption.setValue(true);
                tfQuestion.addOption(trueOption);

                ButtonOption falseOption = new ButtonOption();
                falseOption.setText("False");
                falseOption.setValue(false);
                tfQuestion.addOption(falseOption);

                questions.add(tfQuestion);

                saveExam(file, questions, questionTextAreas, optionTextAreas, optionRadioButtons, orderedRadioButtons);
                tester.reset();
            }
        });
    
        addMenu.addComponent(addTFButton);

        styleButton(addTFButton, "Add true/false question");
        addTFButton.setBackground(editColor);

        editMenu.addComponent(addMenu);
        
        return editMenu;
    }

    private void saveExam(
        File file,
        ArrayList<Question> questions,
        ArrayList<RoundedTextArea> questionTextAreas,
        ArrayList<RoundedTextArea> optionTextAreas,
        ArrayList<JRadioButton> optionRadioButtons,
        ArrayList<JRadioButton> orderedRadioButtons
    ) {
        Exam exam = new Exam(file, tester);
        int
            qCount = 0,
            rCount = 0,
            oCount = 0,
            tCount = 0;

        for(Question q : questions) {
            if(qCount < questionTextAreas.size()) {
                q.setQuestionText(questionTextAreas.get(qCount++).getText());

                if(q.getClass() == WQuestion.class) {
                    ArrayList<TextOption> options = q.getTextOptions();

                    for(TextOption o : options) {
                        if(tCount < optionTextAreas.size()) {
                            String text = optionTextAreas.get(tCount++).getText();
                            Stream<String> lines = text.lines();

                            o.clearText();

                            lines.forEach(line -> {
                                o.addText(line);
                            });
                        }
                    }

                    q.setTextOptions(options);
                } else if(q.getClass() == MCQuestion.class) {
                    if(oCount < orderedRadioButtons.size()) {
                        ArrayList<ButtonOption> options = q.getButtonOptions();

                        for(ButtonOption o : options) {
                            if(
                                tCount < optionTextAreas.size() &&
                                rCount < optionRadioButtons.size()
                            ) {
                                o.setText("");

                                o.setText(optionTextAreas.get(tCount++).getText());
                                o.setValue(optionRadioButtons.get(rCount++).isSelected());
                            }
                        }

                        q.setOrdered(orderedRadioButtons.get(oCount++).isSelected());
                        q.setButtonOptions(options);
                    }
                } else {
                    if(rCount < optionRadioButtons.size()) {
                        ArrayList<ButtonOption> options = q.getButtonOptions();
                        Boolean value = optionRadioButtons.get(rCount++).isSelected();

                        options.getFirst().setValue(value);
                        options.getLast().setValue(!value);

                        q.setButtonOptions(options);
                    }
                }
            }
        }
    
        exam.setQuestions(questions);
        
        try {
            if(file.exists()) {
                file.delete();
            }
            exam.saveToFile(file.getName().substring(0, file.getName().length() - 4), file.getParentFile());
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    public void deleteTopic() {
        tester.dirMenu.remove(this);
    }

    public void deleteExam(HamburgerMenu menu) {
        getMenu().remove(menu);
    }

    // SETTERS
    public void setTitled(Boolean titled) {
        this.titled = titled;
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

        @Override
        public TopicBuilder parent(Component parent) {
            this.parent = parent;
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
