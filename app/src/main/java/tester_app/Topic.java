package tester_app;

import static tester_app.helpers.Constants.borderColor;
import static tester_app.helpers.Constants.buttonFont;
import static tester_app.helpers.Constants.copyColor;
import static tester_app.helpers.Constants.deleteColor;
import static tester_app.helpers.Constants.editColor;
import static tester_app.helpers.Constants.margin;
import static tester_app.helpers.Constants.mcQuestionBackgroundColor;
import static tester_app.helpers.Constants.mcQuestionBorderColor;
import static tester_app.helpers.Constants.name;
import static tester_app.helpers.Constants.pasteColor;
import static tester_app.helpers.Constants.selectionColor;
import static tester_app.helpers.Constants.styleButton;
import static tester_app.helpers.Constants.tfQuestionBackgroundColor;
import static tester_app.helpers.Constants.tfQuestionBorderColor;
import static tester_app.helpers.Constants.wQuestionBackgroundColor;
import static tester_app.helpers.Constants.wQuestionBorderColor;

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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.stream.Stream;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import tester_app.helpers.DirectoryRestrictedFileSystemView;
import tester_app.helpers.HamburgerMenu;
import tester_app.helpers.RoundedButton;
import tester_app.helpers.RoundedLabel;
import tester_app.helpers.RoundedPanel;
import tester_app.helpers.RoundedSpinner;
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
    private ArrayList<Exam> exams;

    public Topic(TopicBuilder builder) {
        super(builder);

        tester = builder.tester;
        titled = true;
        exams = new ArrayList<>();

        this.setBlotOffset(2);
    }

    public void loadFiles(final File dir, final Image fileIcon) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 0.5;

        RoundedPanel untitledPanel = new RoundedPanel();
        untitledPanel.setBackground(null);
        untitledPanel.setVisible(false);

        RoundedTextArea titlePanel = new RoundedTextArea(dir.getName(), getMenu());
        titlePanel.setToolTipText("Click to change the name of this topic");
        if(getBackground() != null) {
            titlePanel.setBackground(getBackground().darker());
        } else {
            titlePanel.setBackground(getBackground());
        }
        titlePanel.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                if(e != null) {
                    String fileTitle = titlePanel.getText();

                    if(fileTitle.length() <= 30) {
                        String
                            oldPath = dir.getPath(),
                            newPath = dir.getParentFile().getPath() + "/" + fileTitle;

                        if(oldPath != newPath) {
                            Path oldDirPath = Paths.get(oldPath);
                            Path newDirPath = Paths.get(newPath);

                            try {
                                Files.move(oldDirPath, newDirPath, StandardCopyOption.REPLACE_EXISTING);
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
        titlePanel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(titlePanel.getText().length() <= 30) {
                    titlePanel.setForeground(Color.WHITE);
                } else {
                    titlePanel.setForeground(deleteColor);
                }

                if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                    e.consume();
                    tester.requestFocus();
                }
            }
        });

        constraints.weightx = 0;
        constraints.gridx = 0;
        constraints.gridy = 0;
        
        if(titled) {
            addComponent(titlePanel, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, 0);
        } else {
            addComponent(untitledPanel);
            setBlotOffset(getMenuSize() / 2);
        }

        loadDir(dir);
        
        if(dir.isDirectory()) {
            for (final File f : dir.listFiles()) {
                if (!f.isDirectory()) {
                    addExam(f, fileIcon);
                }
            }
        } else {
            addExam(dir, fileIcon);
        }

        RoundedButton addTopicButton = new RoundedButton();
        addTopicButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                File file = new File(dir.getPath() + "/New topic");
                if(!file.exists()) {
                    file.mkdir();
                    addComponent(new Topic.TopicBuilder().text("New topic").hasSearch(true).build());

                    tester.reset();
                }
            }
        });

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

        RoundedButton pasteButton = new RoundedButton();
        pasteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                paste(dir);
            }
        });

        RoundedButton copyButton = new RoundedButton();
        copyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                copy(dir);
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
            HamburgerMenu addHam = new HamburgerMenu.HamburgerMenuBuilder().parent(this).icon(tester.getEditorButtonIcon()).build();
            addHam.setGrid(true);
            addHam.addComponent(copyButton);
            addHam.addComponent(pasteButton);
            addHam.addComponent(addTopicButton);
            addHam.addComponent(addButton);
            addHam.addComponent(deleteButton);

            addComponent(addHam);

            styleButton(addTopicButton, "Add topic", tester.getAddIcon(), JButton.RIGHT);
            addTopicButton.setBackground(editColor);

            styleButton(addButton, "Add exam", tester.getAddIcon(), JButton.RIGHT);
            addButton.setBackground(editColor);

            styleButton(pasteButton, "Paste exam", tester.getPasteIcon(), JButton.RIGHT);
            pasteButton.setBackground(pasteColor);

            styleButton(copyButton, "Copy topic", tester.getCopyIcon(), JButton.RIGHT);
            copyButton.setBackground(copyColor);

            styleButton(deleteButton, "Delete topic", tester.getDeleteIcon(), JButton.RIGHT);
            deleteButton.setBackground(deleteColor);
        }
    }

    public void loadDir(final File dir) {
        try {
            File[] files = dir.listFiles();

            if(files != null) {
                if(files.length > 0) {
                    for (final File f : files) {
                        if (f.isDirectory()) {
                            Topic dirTopic = new Topic.TopicBuilder().parent(this).text(f.getName()).icon(tester.getDirButtonIcon()).hasSearch(true).tester(tester).build();

                            dirTopic.loadFiles(f, tester.getFileButtonIcon());

                            GridBagConstraints constraints = new GridBagConstraints();
                            constraints.fill = GridBagConstraints.HORIZONTAL;
                            constraints.gridx = 1;
                            constraints.weightx = 0.5;
                            constraints.insets = new Insets(margin, margin, margin, margin);

                            getMenu().add(dirTopic, constraints);
                        }
                    }
                }
            }
        } catch(Exception e) {
            RoundedLabel fail = new RoundedLabel("Failed to read directory!" + System.lineSeparator() + "If the directory is protected, try running " + name + " as administrator.");
            fail.setForeground(Color.WHITE);
            fail.setFont(buttonFont);

            addComponent(fail);

            e.printStackTrace();
        }
    }

    public void addExam(final File file, final Image fileIcon) {
        Exam exam = new Exam(file, tester);
        String
            fileName = file.getName(),
            examName = fileName.substring(0, fileName.length() - 4);
        RoundedButton
            startButton = new RoundedButton(),
            deleteButton = new RoundedButton();
        HamburgerMenu examMenu = new HamburgerMenu.HamburgerMenuBuilder().parent(this.getMenu()).text(examName).icon(fileIcon).build();

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 0.5;

        loadEditMenu(exam, examMenu);

        RoundedTextArea titlePanel = new RoundedTextArea(examName, examMenu);
        titlePanel.setToolTipText("Click to change the name of this exam");
        titlePanel.setPlaceholder("Enter exam name...");
        if(examMenu.getBackground() != null) {
            titlePanel.setBackground(examMenu.getBackground().darker());
        } else {
            titlePanel.setBackground(examMenu.getBackground());
        }
        titlePanel.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                if(e != null) {
                    String fileTitle = titlePanel.getText();

                    if(fileTitle.length() <= 30) {
                        Path oldDirPath = Paths.get(file.getPath());
                        Path newDirPath = Paths.get(file.getParentFile().getPath(), fileTitle + ".txt");

                        try {
                            Files.move(oldDirPath, newDirPath, StandardCopyOption.REPLACE_EXISTING);
                            examMenu.setText(fileTitle);
                            exam.getEditMenu().setText("Edit " + fileTitle);
                        } catch (Exception e1) {
                            System.out.println("Error renaming exam: " + e1.getMessage());
                            e1.printStackTrace();
                        }
                    }

                    tester.reset();
                }
            }
        });
        titlePanel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(titlePanel.getText().length() <= 30) {
                    titlePanel.setForeground(Color.WHITE);
                } else {
                    titlePanel.setForeground(deleteColor);
                }

                if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                    e.consume();
                    tester.requestFocus();
                }
            }
        });
        
        examMenu.addComponent(titlePanel, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, 0);

        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(new Exam(file, tester).getQuestions().size() > 0) {
                    tester.startExam(file, tester);
                }
            }
        });
        examMenu.addComponent(startButton);
        styleButton(startButton, "Start exam");
    
        examMenu.addComponent(exam.getEditMenu());

        examMenu.setBlotOffset(6 - exam.getEditMenu().getMenuSize());

        RoundedButton copyButton = new RoundedButton();
        copyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                copy(file, examMenu);
            }
        });

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
                        deleteExam(examMenu);

                        tester.reset();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });

        HamburgerMenu addHam = new HamburgerMenu.HamburgerMenuBuilder().parent(examMenu).icon(tester.getEditorButtonIcon()).build();
        addHam.setGrid(true);
        addHam.addComponent(copyButton);
        addHam.addComponent(deleteButton);

        examMenu.addComponent(addHam);

        styleButton(copyButton, "Copy exam", tester.getCopyIcon(), JButton.RIGHT);
        copyButton.setBackground(copyColor);

        styleButton(deleteButton, "Delete exam", tester.getDeleteIcon(), JButton.RIGHT);
        deleteButton.setBackground(deleteColor);

        addComponent(examMenu);

        exams.add(exam);
    }

    private void loadEditMenu(Exam exam, Component parent) {
        HamburgerMenu editMenu = new HamburgerMenu.HamburgerMenuBuilder().parent(parent).icon(tester.getEditIcon()).textHPos(JButton.RIGHT).text("Edit " + exam.getName()).hasSearch(true).build();

        ArrayList<Question> questions = exam.getQuestions();
        ArrayList<JTextArea>
            questionTextAreas = new ArrayList<>(),
            optionTextAreas = new ArrayList<>();
        ArrayList<JRadioButton>
            optionRadioButtons = new ArrayList<>(),
            orderedRadioButtons = new ArrayList<>();
        ArrayList<RoundedSpinner> goalSpinners = new ArrayList<>();

        GridBagConstraints constraints = new GridBagConstraints();

        editMenu.setBlotOffset(1);

        for(Question q : questions) {
            HamburgerMenu editPanel;

            constraints = new GridBagConstraints();
            constraints.fill = GridBagConstraints.BOTH;
            constraints.weightx = 0.5;
            constraints.weighty = 0.5;
            constraints.insets = new Insets(margin * 2, margin * 2, margin * 2, margin * 2);

            String qText = q.getQuestionText();
            if(qText != null) {
                if(qText == "") {
                    qText = "New " + q.getClass().getSimpleName();
                }
            } else {
                qText = "New " + q.getClass().getSimpleName();
            }
            
            if(q.getClass() == TFQuestion.class) {
                editPanel = new HamburgerMenu.HamburgerMenuBuilder().parent(parent).icon(tester.getTfIcon()).text(qText).hasSearch(true).build();
                editPanel.setBackground(tfQuestionBackgroundColor);
                editPanel.setBorderColor(tfQuestionBorderColor);
                editPanel.setButtonColor(tfQuestionBackgroundColor.brighter());
            } else if(q.getClass() == MCQuestion.class) {
                editPanel = new HamburgerMenu.HamburgerMenuBuilder().parent(parent).icon(tester.getMcIcon()).text(qText).hasSearch(true).build();
                editPanel.setBackground(mcQuestionBackgroundColor);
                editPanel.setBorderColor(mcQuestionBorderColor);
                editPanel.setButtonColor(mcQuestionBackgroundColor.brighter());
            } else {
                editPanel = new HamburgerMenu.HamburgerMenuBuilder().parent(parent).icon(tester.getWqIcon()).text(qText).hasSearch(true).build();
                editPanel.setBackground(wQuestionBackgroundColor);
                editPanel.setBorderColor(wQuestionBorderColor);
                editPanel.setButtonColor(wQuestionBackgroundColor.brighter());
            }
            editPanel.setBlotOffset(2);
            editPanel.setOpacity(1);

            HamburgerMenu addHam = new HamburgerMenu.HamburgerMenuBuilder().parent(editPanel).icon(tester.getEditorButtonIcon()).build();
            addHam.setBackground(editPanel.getBackground().darker());
            addHam.setBorderColor(editPanel.getBorderColor());
            addHam.setButtonColor(editPanel.getBackground().brighter());
            addHam.setGrid(true);
            addHam.setOpacity(1);

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
                        exam.saveToFile(exam.getExamFile().getName().substring(0, exam.getExamFile().getName().length() - 4), exam.getExamFile().getParentFile());

                        tester.reset();
                    }
                }
            });

            RoundedButton copyButton = new RoundedButton();
            copyButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    copy(q);
                }
            });

            constraints.insets = new Insets(margin * 2, margin * 2, margin * 2, margin * 2);

            addHam.addComponent(copyButton);
            addHam.addComponent(deleteButton);

            styleButton(deleteButton, "Delete question", tester.getDeleteIcon(), JButton.RIGHT);
            deleteButton.setBackground(deleteColor);

            styleButton(copyButton, "Copy question", tester.getCopyIcon(), JButton.RIGHT);
            copyButton.setBackground(copyColor);

            RoundedPanel textPanel = new RoundedPanel();
            textPanel.setLayout(getLayout());
            textPanel.setBackground(editPanel.getBackground().darker());
            textPanel.setBorderColor(editPanel.getBorderColor());
            textPanel.setOpacity(1);

            RoundedButton deleteImageButton = new RoundedButton();
            deleteImageButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    q.setQuestionImage("");

                    saveExam(exam);

                    tester.reset();
                }
            });
            styleButton(deleteImageButton, "Delete image", tester.getDeleteIcon(), JButton.RIGHT);
            deleteImageButton.setBackground(deleteColor);
            
            RoundedButton imageButton = new RoundedButton();
            imageButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    JFileChooser fileChooser = new JFileChooser(new DirectoryRestrictedFileSystemView());
                    fileChooser.setCurrentDirectory(new File("./resources"));
                    FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", "png", "jpg");
                    fileChooser.setFileFilter(filter);

                    int response = fileChooser.showOpenDialog(null);

                    if(response == JFileChooser.APPROVE_OPTION) {
                        q.setQuestionImage(fileChooser.getSelectedFile().getAbsolutePath());
                    }

                    if(q.getQuestionImage() == null) {
                        deleteImageButton.setVisible(false);
                    } else if(q.getQuestionImage().isBlank()) {
                        deleteImageButton.setVisible(false);
                    } else {
                        deleteImageButton.setVisible(true);
                    }

                    saveExam(exam);

                    tester.reset();
                }
            });

            constraints.weightx = 0.1;
            constraints.weighty = 0.1;

            textPanel.add(imageButton, constraints);
            String questionImage = q.getQuestionImage();
            if(
                questionImage != null &&
                !questionImage.isBlank() &&
                (questionImage.contains("/") || questionImage.contains("\\"))
            ) {
                if(questionImage.contains("/")) {
                    styleButton(imageButton, questionImage.substring(questionImage.lastIndexOf("/") + 1));
                } else {
                    styleButton(imageButton, questionImage.substring(questionImage.lastIndexOf("\\") + 1));
                }
            } else {
                styleButton(imageButton, "Add image", tester.getAddIcon(), JButton.RIGHT);
            }
            imageButton.setBackground(textPanel.getBackground().brighter());

            constraints.gridy = 1;

            textPanel.add(deleteImageButton, constraints);

            constraints = new GridBagConstraints();
            constraints.fill = GridBagConstraints.BOTH;
            constraints.gridx = 1;
            constraints.gridy = 0;
            constraints.weightx = 0.5;
            constraints.weighty = 0.5;
            constraints.insets = new Insets(margin * 3, margin * 3, margin * 3, margin * 3);

            RoundedTextArea textArea = new RoundedTextArea(q.getQuestionText(), textPanel);
            textArea.setPlaceholder("Enter question...");
            textArea.setToolTipText("Click to change the question text");
            textArea.addKeyListener(new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent e) {
                    if(e.getKeyCode() != KeyEvent.VK_ENTER) {
                        String text = textArea.getText();

                        editPanel.setText(text);
                        q.setQuestionText(text);

                        saveExam(exam);

                        editPanel.revalidate();
                    }
                }
            });
            textArea.setOpacity(1);

            textPanel.add(textArea, constraints);

            editPanel.addComponent(textPanel);
            questionTextAreas.add(textArea);

            if(q.getClass() == WQuestion.class) {
                constraints = new GridBagConstraints();
                constraints.fill = GridBagConstraints.BOTH;
                constraints.weightx = 0.5;
                constraints.weighty = 0.5;
                constraints.insets = new Insets(margin * 2, margin * 2, margin * 2, margin * 2);

                ArrayList<TextOption> options = q.getTextOptions();

                int
                    goal = q.getGoal(),
                    maxScore = ((WQuestion) q).getMaxScore();

                RoundedSpinner goalSpinner = new RoundedSpinner(new SpinnerNumberModel(goal <= maxScore ? goal : maxScore, 1, maxScore, 1), "Answer");
                goalSpinner.setToolTipText("Determines how many correct answers are needed to pass");
                if((Integer) goalSpinner.getValue() > 1) {
                    goalSpinner.setText("Answers");
                }
                goalSpinner.addChangeListener(new ChangeListener() {
                    @Override
                    public void stateChanged(ChangeEvent e) {
                        int value = (int) goalSpinner.getValue();

                        if((Integer) value == 1) {
                            goalSpinner.setText("Answer");
                        } else {
                            goalSpinner.setText("Answers");
                        }

                        q.setGoal(value);

                        saveExam(exam);
                    }
                });
                if(q.isOrdered()) {
                    goalSpinner.setVisible(false);
                }
                goalSpinner.setBackground(textPanel.getBackground().brighter());
                goalSpinner.setBorderColor(textPanel.getBackground().brighter().brighter().brighter().brighter());

                JRadioButton radioButton = new JRadioButton();
                radioButton.setOpaque(false);
                radioButton.setToolTipText("When selected, answers must be given in order");
                radioButton.setFocusable(false);
                radioButton.setBackground(null);
                radioButton.setForeground(Color.WHITE);
                radioButton.setText("Ordered");
                radioButton.setSelected(q.isOrdered());
                radioButton.setIconTextGap(margin * 2);
                radioButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Boolean ordered = radioButton.isSelected();

                        if(ordered) {
                            goalSpinner.setVisible(false);
                        } else {
                            goalSpinner.setVisible(true);
                        }

                        q.setOrdered(ordered);

                        saveExam(exam);
                    }
                });
                radioButton.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseExited(MouseEvent e) {
                        radioButton.setForeground(Color.WHITE);
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        radioButton.setForeground(selectionColor);
                    }
                });

                constraints.insets = new Insets(margin * 4, margin * 4, margin * 4, margin * 4);
                constraints.weightx = 0.1;
                constraints.gridx = 1;
                constraints.gridy = 1;

                textPanel.add(radioButton, constraints);
                orderedRadioButtons.add(radioButton);

                constraints.weightx = 0.5;
                constraints.gridx = 0;
                constraints.gridwidth = 2;

                constraints.insets = new Insets(margin * 3, margin * 3, margin * 3, margin * 3);
                constraints.gridy = 2;

                textPanel.add(goalSpinner, constraints);
                goalSpinners.add(goalSpinner);

                for(TextOption o : options) {
                    constraints = new GridBagConstraints();
                    constraints.fill = GridBagConstraints.BOTH;
                    constraints.weightx = 0.5;
                    constraints.weighty = 0.5;
                    constraints.insets = new Insets(margin * 2, margin * 2, margin * 2, margin * 2);

                    RoundedPanel optionTextPanel = new RoundedPanel();
                    optionTextPanel.setLayout(getLayout());
                    optionTextPanel.setBackground(editPanel.getBackground().darker().darker());
                    optionTextPanel.setBorderColor(editPanel.getBorderColor());
                    optionTextPanel.setOpacity(1);

                    deleteButton = new RoundedButton();
                    deleteButton.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            int response = JOptionPane.showConfirmDialog(
                                tester,
                                "Are you sure you want to delete this answer?",
                                "Warning",
                                JOptionPane.YES_NO_OPTION
                            );

                            if (response == JOptionPane.YES_OPTION) {
                                q.removeOption(o);

                                exam.setQuestions(questions);
                                exam.saveToFile(exam.getExamFile().getName().substring(0, exam.getExamFile().getName().length() - 4), exam.getExamFile().getParentFile());

                                tester.reset();
                            }
                        }
                    });

                    constraints.insets = new Insets(margin * 3, margin * 3, margin * 3, margin * 3);
                    
                    RoundedTextArea optionTextArea = new RoundedTextArea(o.getClearText(), optionTextPanel);
                    optionTextArea.setToolTipText("Click to change the answer text");
                    optionTextArea.setPlaceholder("Enter answer...");
                    optionTextArea.setOpacity(1);
                    optionTextArea.addKeyListener(new KeyAdapter() {
                        @Override
                        public void keyReleased(KeyEvent e) {
                            if(e.getKeyCode() != KeyEvent.VK_ENTER) {
                                String text = optionTextArea.getText();
                                Stream<String> lines = text.lines();

                                o.clearText();

                                lines.forEach(line -> {
                                    o.addText(line);
                                });

                                q.initGoal();
                                
                                int
                                    goal = q.getGoal(),
                                    maxScore = ((WQuestion) q).getMaxScore();

                                goalSpinner.setModel(new SpinnerNumberModel(goal <= maxScore ? goal : maxScore, 1, maxScore, 1));
                                goalSpinner.style("Answer");
                                goalSpinner.setToolTipText("Determines how many correct answers are needed to pass");
                                if((Integer) goalSpinner.getValue() > 1) {
                                    goalSpinner.setText("Answers");
                                }
                                goalSpinner.addChangeListener(new ChangeListener() {
                                    @Override
                                    public void stateChanged(ChangeEvent e) {
                                        int value = (int) goalSpinner.getValue();

                                        if((Integer) value == 1) {
                                            goalSpinner.setText("Answer");
                                        } else {
                                            goalSpinner.setText("Answers");
                                        }

                                        q.setGoal(value);

                                        saveExam(exam);
                                    }
                                });
                                if(q.isOrdered()) {
                                    goalSpinner.setVisible(false);
                                }
                                goalSpinner.setBackground(textPanel.getBackground().brighter());
                                goalSpinner.setBorderColor(textPanel.getBackground().brighter().brighter());

                                saveExam(exam);
                            }
                        }
                    });
                    optionTextPanel.add(optionTextArea, constraints);

                    constraints.fill = GridBagConstraints.HORIZONTAL;
                    constraints.insets = new Insets(margin * 2, margin * 2, margin * 2, margin * 2);
                    constraints.gridy = 1;
                    
                    optionTextPanel.add(deleteButton, constraints);

                    styleButton(deleteButton, "Delete answer", tester.getDeleteIcon(), JButton.RIGHT);
                    deleteButton.setBackground(deleteColor);

                    constraints.insets = new Insets(margin * 2, margin * 2, margin * 2, margin * 2);

                    editPanel.addComponent(optionTextPanel);
                    
                    optionTextAreas.add(optionTextArea);
                }
            } else if(q.getClass() == MCQuestion.class) {
                constraints = new GridBagConstraints();
                constraints.fill = GridBagConstraints.BOTH;
                constraints.weightx = 0.5;
                constraints.weighty = 0.5;
                constraints.insets = new Insets(margin * 2, margin * 2, margin * 2, margin * 2);

                ArrayList<ButtonOption> options = q.getButtonOptions();

                JRadioButton radioButton = new JRadioButton();
                radioButton.setOpaque(false);
                radioButton.setToolTipText("When selected, answers must be given in order");
                radioButton.setIconTextGap(margin * 2);
                radioButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        q.setOrdered(radioButton.isSelected());

                        saveExam(exam);
                    }
                });
                radioButton.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseExited(MouseEvent e) {
                        radioButton.setForeground(Color.WHITE);
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        radioButton.setForeground(selectionColor);
                    }
                });
                radioButton.setFocusable(false);
                radioButton.setBackground(null);
                radioButton.setForeground(Color.WHITE);
                radioButton.setText("Ordered");
                radioButton.setSelected(q.isOrdered());

                constraints.insets = new Insets(margin * 4, margin * 4, margin * 4, margin * 4);
                constraints.weightx = 0.1;
                constraints.gridx = 1;
                constraints.gridy = 1;

                textPanel.add(radioButton, constraints);
                orderedRadioButtons.add(radioButton);

                constraints.weightx = 0.5;

                for(ButtonOption o : options) {
                    constraints = new GridBagConstraints();
                    constraints.fill = GridBagConstraints.BOTH;
                    constraints.weightx = 0.5;
                    constraints.weighty = 0.5;
                    constraints.insets = new Insets(margin * 2, margin * 2, margin * 2, margin * 2);

                    RoundedPanel optionTextPanel = new RoundedPanel();
                    optionTextPanel.setLayout(getLayout());
                    optionTextPanel.setBackground(editPanel.getBackground().darker().darker());
                    optionTextPanel.setBorderColor(editPanel.getBorderColor());
                    optionTextPanel.setOpacity(1);

                    RoundedButton deleteImagePathButton = new RoundedButton();
                    deleteImagePathButton.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            o.setImagePath("");

                            saveExam(exam);

                            tester.reset();
                        }
                    });
                    styleButton(deleteImagePathButton, "Delete image", tester.getDeleteIcon(), JButton.RIGHT);
                    deleteImagePathButton.setBackground(deleteColor);

                    imageButton = new RoundedButton();
                    imageButton.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            JFileChooser fileChooser = new JFileChooser(new DirectoryRestrictedFileSystemView());
                            fileChooser.setCurrentDirectory(new File("./resources"));
                            FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", "png", "jpg");
                            fileChooser.setFileFilter(filter);

                            int response = fileChooser.showOpenDialog(null);

                            if(response == JFileChooser.APPROVE_OPTION) {
                                o.setImagePath(fileChooser.getSelectedFile().getAbsolutePath());
                            }

                            saveExam(exam);

                            tester.reset();
                        }
                    });

                    constraints.weightx = 0.1;
                    constraints.weighty = 0.1;

                    optionTextPanel.add(imageButton, constraints);
                    String imagePath = o.getImagePath();
                    if(
                        imagePath != null &&
                        !imagePath.isBlank() &&
                        (imagePath.contains("/") || imagePath.contains("\\"))
                    ) {
                        if(imagePath.contains("/")) {
                            styleButton(imageButton, imagePath.substring(imagePath.lastIndexOf("/") + 1));
                        } else {
                            styleButton(imageButton, imagePath.substring(imagePath.lastIndexOf("\\") + 1));
                        }
                    } else {
                        styleButton(imageButton, "Add image", tester.getAddIcon(), JButton.RIGHT);
                    }
                    imageButton.setBackground(textPanel.getBackground().brighter());

                    constraints.gridy = 1;

                    optionTextPanel.add(deleteImagePathButton, constraints);

                    constraints = new GridBagConstraints();
                    constraints.fill = GridBagConstraints.BOTH;
                    constraints.weightx = 0.5;
                    constraints.weighty = 0.5;
                    constraints.insets = new Insets(margin * 2, margin * 2, margin * 2, margin * 2);
                    constraints.gridx = 1;

                    deleteButton = new RoundedButton();
                    deleteButton.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            int response = JOptionPane.showConfirmDialog(
                                tester,
                                "Are you sure you want to delete this answer?",
                                "Warning",
                                JOptionPane.YES_NO_OPTION
                            );

                            if (response == JOptionPane.YES_OPTION) {
                                q.removeOption(o);

                                exam.setQuestions(questions);
                                exam.saveToFile(exam.getExamFile().getName().substring(0, exam.getExamFile().getName().length() - 4), exam.getExamFile().getParentFile());

                                tester.reset();
                            }
                        }
                    });

                    constraints.insets = new Insets(margin * 3, margin * 3, margin * 3, margin * 3);
                    
                    RoundedTextArea optionTextArea = new RoundedTextArea(o.getButtonText(), optionTextPanel);
                    optionTextArea.setToolTipText("Click to change the answer text");
                    optionTextArea.setPlaceholder("Enter answer...");
                    optionTextArea.setOpacity(1);
                    optionTextArea.addKeyListener(new KeyAdapter() {
                        @Override
                        public void keyPressed(KeyEvent e) {
                            if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                                e.consume();
                                tester.requestFocus();
                            }
                        }

                        @Override
                        public void keyReleased(KeyEvent e) {
                            if(e.getKeyCode() != KeyEvent.VK_ENTER) { 
                                String text = optionTextArea.getText();
                                o.setText(text);

                                saveExam(exam);
                            }
                        }
                    });

                    constraints.weightx = 0.1;

                    optionTextPanel.add(optionTextArea, constraints);

                    JRadioButton optionRadioButton = new JRadioButton();
                    optionRadioButton.setOpaque(false);
                    optionRadioButton.setToolTipText("Is this answer true?");
                    optionRadioButton.setIconTextGap(margin * 2);
                    optionRadioButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            o.setValue(optionRadioButton.isSelected());

                            saveExam(exam);
                        }
                    });
                    optionRadioButton.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseExited(MouseEvent e) {
                            optionRadioButton.setForeground(Color.WHITE);
                        }

                        @Override
                        public void mouseEntered(MouseEvent e) {
                            optionRadioButton.setForeground(selectionColor);
                        }
                    });
                    optionRadioButton.setFocusable(false);
                    optionRadioButton.setBackground(null);
                    optionRadioButton.setForeground(Color.WHITE);
                    optionRadioButton.setText("True");
                    optionRadioButton.setSelected(o.isTrue());

                    constraints.insets = new Insets(margin * 4, margin * 4, margin * 4, margin * 4);

                    optionTextPanel.add(optionRadioButton, constraints);

                    constraints.fill = GridBagConstraints.HORIZONTAL;
                    constraints.insets = new Insets(margin * 2, margin * 2, margin * 2, margin * 2);
                    constraints.weightx = 0.5;
                    constraints.gridx = 0;
                    constraints.gridwidth = 2;
                    
                    optionTextPanel.add(deleteButton, constraints);

                    styleButton(deleteButton, "Delete answer", tester.getDeleteIcon(), JButton.RIGHT);
                    deleteButton.setBackground(deleteColor);

                    constraints.insets = new Insets(margin * 2, margin * 2, margin * 2, margin * 2);

                    editPanel.addComponent(optionTextPanel);
                    
                    optionRadioButtons.add(optionRadioButton);
                    optionTextAreas.add(optionTextArea);
                }
            } else {
                constraints = new GridBagConstraints();
                constraints.fill = GridBagConstraints.BOTH;
                constraints.weightx = 0.1;
                constraints.weighty = 0.5;
                constraints.gridx = 1;
                constraints.gridy = 1;
                constraints.insets = new Insets(margin * 4, margin * 4, margin * 4, margin * 4);
                
                JRadioButton radioButton = new JRadioButton();
                radioButton.setOpaque(false);
                radioButton.setToolTipText("Is the question text true?");
                radioButton.setIconTextGap(margin * 2);
                radioButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        ArrayList<ButtonOption> options = q.getButtonOptions();

                        options.get(0).setValue(radioButton.isSelected());
                        options.get(1).setValue(!radioButton.isSelected());

                        saveExam(exam);
                    }
                });
                radioButton.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseExited(MouseEvent e) {
                        radioButton.setForeground(Color.WHITE);
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        radioButton.setForeground(selectionColor);
                    }
                });
                radioButton.setFocusable(false);
                radioButton.setBackground(null);
                radioButton.setForeground(Color.WHITE);
                radioButton.setText("True");
                radioButton.setSelected(q.getButtonOptions().getFirst().isTrue());
                
                textPanel.add(radioButton, constraints);
                
                optionRadioButtons.add(radioButton);
            }

            constraints.fill = GridBagConstraints.HORIZONTAL;
            constraints.insets = new Insets(margin * 2, margin * 2, margin * 2, margin * 2);

            if(q.getClass() == WQuestion.class) {
                RoundedButton addButton = new RoundedButton();
                addButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        TextOption option = new TextOption();
                        q.addOption(option);

                        exam.setQuestions(questions);
                        exam.saveToFile(exam.getExamFile().getName().substring(0, exam.getExamFile().getName().length() - 4), exam.getExamFile().getParentFile());

                        tester.reset();
                    }
                });

                constraints.insets = new Insets(margin, margin, margin, margin);

                addHam.addComponent(addButton);

                styleButton(addButton, "Add answer", tester.getAddIcon(), JButton.RIGHT);
                addButton.setBackground(editColor);
            } else if(q.getClass() == MCQuestion.class) {
                RoundedButton addButton = new RoundedButton();
                addButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        q.addOption(new ButtonOption());

                        exam.setQuestions(questions);
                        exam.saveToFile(exam.getExamFile().getName().substring(0, exam.getExamFile().getName().length() - 4), exam.getExamFile().getParentFile());

                        tester.reset();
                    }
                });

                constraints.insets = new Insets(margin, margin, margin, margin);

                addHam.addComponent(addButton);

                styleButton(addButton, "Add answer", tester.getAddIcon(), JButton.RIGHT);
                addButton.setBackground(editColor);
            }

            editPanel.addComponent(addHam);

            editMenu.addComponent(editPanel);
        }

        HamburgerMenu addMenu = new HamburgerMenu.HamburgerMenuBuilder().parent(editMenu).icon(tester.getEditorButtonIcon()).build();
        addMenu.setGrid(true);

        RoundedButton addWQButton = new RoundedButton();
        addWQButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                WQuestion wQuestion = new WQuestion(exam);
                wQuestion.addOption(new TextOption());

                questions.add(wQuestion);
                
                saveExam(exam);
                tester.reset();
            }
        });

        addMenu.addComponent(addWQButton);

        styleButton(addWQButton, "Add written question", tester.getAddIcon(), JButton.RIGHT);
        addWQButton.setBackground(editColor);

        RoundedButton addMCButton = new RoundedButton();
        addMCButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                MCQuestion mcQuestion = new MCQuestion(exam);
                mcQuestion.addOption(new ButtonOption());

                questions.add(mcQuestion);
                
                saveExam(exam);
                tester.reset();
            }
        });

        addMenu.addComponent(addMCButton);

        styleButton(addMCButton, "Add multiple choice question", tester.getAddIcon(), JButton.RIGHT);
        addMCButton.setBackground(editColor);

        RoundedButton addTFButton = new RoundedButton();
        addTFButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                TFQuestion tfQuestion = new TFQuestion(exam);

                ButtonOption trueOption = new ButtonOption();
                trueOption.setText("True");
                trueOption.setValue(true);
                tfQuestion.addOption(trueOption);

                ButtonOption falseOption = new ButtonOption();
                falseOption.setText("False");
                falseOption.setValue(false);
                tfQuestion.addOption(falseOption);

                questions.add(tfQuestion);

                saveExam(exam);
                tester.reset();
            }
        });
    
        addMenu.addComponent(addTFButton);

        styleButton(addTFButton, "Add true/false question", tester.getAddIcon(), JButton.RIGHT);
        addTFButton.setBackground(editColor);

        RoundedButton pasteButton = new RoundedButton();
        pasteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                questions.add(tester.getCopiedQuestion());

                saveExam(exam);
                tester.reset();
            }
        });

        addMenu.addComponent(pasteButton);

        styleButton(pasteButton, "Paste question", tester.getPasteIcon(), JButton.RIGHT);
        pasteButton.setBackground(pasteColor);

        editMenu.addComponent(addMenu);
        
        exam.setEditMenu(editMenu);
        exam.setQuestions(questions);
        exam.setOptionTextAreas(optionTextAreas);
        exam.setOptionRadioButtons(optionRadioButtons);
        exam.setOrderedRadioButtons(orderedRadioButtons);
        exam.setGoalSpinners(goalSpinners);
    }

    private void saveExam(Exam exam) {
        int
            qCount = 0,
            rCount = 0,
            oCount = 0,
            tCount = 0,
            sCount = 0;
        ArrayList<Question> questions = exam.getQuestions();
        ArrayList<JTextArea>
            questionTextAreas = exam.getQuestionTextAreas(),
            optionTextAreas = exam.getOptionTextAreas();
        ArrayList<JRadioButton>
            optionRadioButtons = exam.getOptionRadioButtons(),
            orderedRadioButtons = exam.getOrderedRadioButtons();
        ArrayList<RoundedSpinner> goalSpinners = exam.getGoalSpinners();

        for(Question q : questions) {
            if(qCount < questionTextAreas.size()) {
                q.setQuestionText(questionTextAreas.get(qCount++).getText());

                if(q.getClass() == WQuestion.class) {
                    if(oCount < orderedRadioButtons.size()) {
                        ArrayList<TextOption> options = q.getTextOptions();

                        for(TextOption o : options) {
                            if(tCount < optionTextAreas.size()) {
                                String text = optionTextAreas.get(tCount++).getText();
                                Stream<String> lines = text.lines();


                                o.clearText();

                                lines.forEach(line -> {
                                    o.addText(line);
                                });

                                q.initGoal();
                            }
                        }

                        q.setOrdered(orderedRadioButtons.get(oCount++).isSelected());
                        if(!q.isOrdered()) {
                            q.setGoal((Integer) goalSpinners.get(sCount++).getValue());
                        } else {
                            ++sCount;
                        }
                        q.setTextOptions(options);
                    }
                } else if(q.getClass() == MCQuestion.class) {
                    if(oCount < orderedRadioButtons.size()) {
                        ArrayList<ButtonOption> options = q.getButtonOptions();

                        for(ButtonOption o : options) {
                            if(
                                tCount < optionTextAreas.size() &&
                                rCount < optionRadioButtons.size()
                            ) {
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
        exam.setOptionTextAreas(optionTextAreas);
        exam.setOptionRadioButtons(optionRadioButtons);
        exam.setOrderedRadioButtons(orderedRadioButtons);
        exam.setGoalSpinners(goalSpinners);

        exam.getEditMenu().revalidate();
        
        try {
            File file = exam.getExamFile();

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

    public void copy(File file, Component component) {
        tester.setCopiedComponent(component);
        tester.setCopiedFile(file);
    }
    public void copy(File file) {
        tester.setCopiedComponent(this);
        tester.setCopiedFile(file);
    }
    public void copy(Question question) {
        tester.setCopiedQuestion(question);
    }

    public boolean paste(File dir) {
        if(
            tester.getCopiedComponent() != null &&
            tester.getCopiedFile() != null
        ) {
            if(tester.getCopiedComponent().getClass() == HamburgerMenu.class) {
                HamburgerMenu copy = (HamburgerMenu) tester.getCopiedComponent();

                for(Component c : this.getMenu().getComponents()) {
                    if(c.getClass() == HamburgerMenu.class) {
                        if(((HamburgerMenu) c).getText() != null) {
                            if(((HamburgerMenu) c).getText().equals(copy.getText())) {
                                (copy).setText(copy.getText() + " - Copy");
                            }
                        }
                    }
                }

                File file = new File(dir.getPath() + "/" + copy.getText() + ".txt");
                Path oldDirPath = Paths.get(tester.getCopiedFile().getPath());
                Path newDirPath = Paths.get(file.getPath());

                try {
                    Files.copy(oldDirPath, newDirPath, StandardCopyOption.REPLACE_EXISTING);
                    addComponent(copy);

                    tester.reset();
                    return true;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
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

        @Override
        public TopicBuilder hasSearch(Boolean hasSearch) {
            this.hasSearch = hasSearch;
            return this;
        }

        @Override
        public TopicBuilder textHPos(Integer textHPos) {
            this.textHPos = textHPos;
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
