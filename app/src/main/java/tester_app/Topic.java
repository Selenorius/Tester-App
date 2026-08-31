package tester_app;

import static tester_app.helpers.Constants.copyColor;
import static tester_app.helpers.Constants.deleteColor;
import static tester_app.helpers.Constants.editBackgroundColor;
import static tester_app.helpers.Constants.editBorderColor;
import static tester_app.helpers.Constants.editColor;
import static tester_app.helpers.Constants.examAddBackgroundColor;
import static tester_app.helpers.Constants.examAddBorderColor;
import static tester_app.helpers.Constants.examBackgroundColor;
import static tester_app.helpers.Constants.examBorderColor;
import static tester_app.helpers.Constants.fieldColor;
import static tester_app.helpers.Constants.margin;
import static tester_app.helpers.Constants.pasteColor;
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
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.stream.Stream;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import tester_app.helpers.DirectoryRestrictedFileSystemView;
import tester_app.helpers.HamburgerMenu;
import tester_app.helpers.RoundedButton;
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

    public Topic(TopicBuilder builder) {
        super(builder);

        tester = builder.tester;
        titled = true;

        this.setBackground(topicBackgroundColor);
        this.setBorderColor(topicBorderColor);
        this.setBlotOffset(2);
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
        titleArea.setToolTipText("Click to change the name of this topic");
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
            addHam.setBackground(examBackgroundColor);
            addHam.setBorderColor(examBorderColor);
            addHam.setIsGrid(true);
            addHam.addComponent(addButton);
            addHam.addComponent(pasteButton);
            addHam.addComponent(deleteButton);
            addHam.addComponent(copyButton);

            addComponent(addHam);

            styleButton(addButton, "Add exam");
            addButton.setBackground(editColor);

            styleButton(pasteButton, "Paste exam");
            pasteButton.setBackground(pasteColor);

            styleButton(copyButton, "Copy topic");
            copyButton.setBackground(copyColor);

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
        HamburgerMenu examMenu = new HamburgerMenu.HamburgerMenuBuilder().parent(this.getMenu()).text(examName).icon(fileIcon).build();
        examMenu.setBackground(examBackgroundColor);
        examMenu.setBorderColor(examBorderColor);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 0.5;

        RoundedPanel titlePanel = new RoundedPanel();
        titlePanel.setBackground(fieldColor);
        titlePanel.setBorderColor(examAddBorderColor);
        titlePanel.setBorderPainted(true);
        titlePanel.setLayout(getLayout());

        HamburgerMenu editMenu = loadEditMenu(file, examMenu);

        RoundedTextArea titleArea = new RoundedTextArea(examName);
        titleArea.setToolTipText("Click to change the name of this exam");
        titleArea.setLabel("Enter exam name...");
        titleArea.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                if(e != null) {
                    String fileTitle = titleArea.getText();

                    if(fileTitle.length() <= 30) {
                        Path oldDirPath = Paths.get(file.getPath());
                        Path newDirPath = Paths.get(file.getParentFile().getPath(), fileTitle + ".txt");

                        try {
                            Files.move(oldDirPath, newDirPath, StandardCopyOption.REPLACE_EXISTING);
                            examMenu.setText(fileTitle);
                            editMenu.setText("Edit " + fileTitle);
                        } catch (Exception e1) {
                            System.out.println("Error renaming exam: " + e1.getMessage());
                            e1.printStackTrace();
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
    
        examMenu.addComponent(editMenu);

        examMenu.setBlotOffset(6 - editMenu.getMenuSize());

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
        addHam.setBackground(editBackgroundColor);
        addHam.setBorderColor(editBorderColor);
        addHam.setIsGrid(true);
        addHam.addComponent(deleteButton);
        addHam.addComponent(copyButton);

        examMenu.addComponent(addHam);

        styleButton(copyButton, "Copy exam");
        copyButton.setBackground(copyColor);

        styleButton(deleteButton, "Delete exam");
        deleteButton.setBackground(deleteColor);

        addComponent(examMenu);
    }

    private HamburgerMenu loadEditMenu(File file, Component parent) {
        Exam exam = new Exam(file, tester);
        HamburgerMenu
            editMenu = new HamburgerMenu.HamburgerMenuBuilder().parent(parent).text("Edit " + exam.getName()).build(),
            editPanel;

        ArrayList<Question> questions = exam.getQuestions();
        ArrayList<RoundedTextArea>
            questionTextAreas = new ArrayList<>(),
            optionTextAreas = new ArrayList<>();
        ArrayList<JRadioButton>
            optionRadioButtons = new ArrayList<>(),
            orderedRadioButtons = new ArrayList<>();
        ArrayList<RoundedSpinner> goalSpinners = new ArrayList<>();

        GridBagConstraints constraints = new GridBagConstraints();

        editMenu.setBackground(editBackgroundColor);
        editMenu.setBorderColor(editBorderColor);
        editMenu.setBlotOffset(1);

        for(Question q : questions) {
            constraints = new GridBagConstraints();

            constraints.fill = GridBagConstraints.BOTH;
            constraints.weightx = 0.5;
            constraints.weighty = 0.5;
            constraints.insets = new Insets(margin * 2, margin * 2, margin * 2, margin * 2);

            String qText = q.getQuestionText();
            if(qText.length() > 20) {
                qText = qText.substring(0, 20) + "...";
            }

            editPanel = new HamburgerMenu.HamburgerMenuBuilder().parent(parent).text(qText).build();
            editPanel.setBackground(examAddBackgroundColor);
            editPanel.setBorderColor(examAddBorderColor);
            editPanel.setBlotOffset(2);

            HamburgerMenu addHam = new HamburgerMenu.HamburgerMenuBuilder().parent(editPanel).icon(tester.getEditorButtonIcon()).build();
            addHam.setBackground(editBackgroundColor);
            addHam.setBorderColor(editBorderColor);
            addHam.setIsGrid(true);

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

            RoundedButton copyButton = new RoundedButton();
            deleteButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    copy(q);
                }
            });

            constraints.insets = new Insets(margin, margin, margin, margin);

            addHam.addComponent(deleteButton);
            addHam.addComponent(copyButton);

            styleButton(deleteButton, "Delete question");
            deleteButton.setBackground(deleteColor);

            RoundedPanel textPanel = new RoundedPanel();
            textPanel.setLayout(getLayout());
            textPanel.setBackground(fieldColor);
            textPanel.setBorderColor(examAddBorderColor);
            textPanel.setBorderPainted(true);

            RoundedButton deleteImageButton = new RoundedButton();
            deleteImageButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    q.setQuestionImage("");

                    saveExam(file, questions, questionTextAreas, optionTextAreas, optionRadioButtons, orderedRadioButtons, goalSpinners);

                    tester.reset();
                }
            });
            styleButton(deleteImageButton, "Delete image");
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

                    saveExam(file, questions, questionTextAreas, optionTextAreas, optionRadioButtons, orderedRadioButtons, goalSpinners);

                    tester.reset();
                }
            });

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
                styleButton(imageButton, "Add image");
            }
            textPanel.add(imageButton, constraints);

            constraints.gridy = 1;

            textPanel.add(deleteImageButton, constraints);

            constraints = new GridBagConstraints();
            constraints.fill = GridBagConstraints.BOTH;
            constraints.weightx = 0.5;
            constraints.weighty = 0.5;
            constraints.insets = new Insets(margin * 2, margin * 2, margin * 2, margin * 2);
            constraints.gridx = 1;

            RoundedTextArea textArea = new RoundedTextArea(q.getQuestionText());
            textArea.setLabel("Enter question...");
            textArea.setToolTipText("Click to change the question text");
            textArea.addKeyListener(new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent e) {
                    if(e.getKeyCode() != KeyEvent.VK_ENTER) {
                        saveExam(file, questions, questionTextAreas, optionTextAreas, optionRadioButtons, orderedRadioButtons, goalSpinners);
                    }
                }
            });
            textPanel.add(textArea, constraints);

            constraints.gridx = 1;
            constraints.insets = new Insets(margin * 2, margin * 2, margin * 2, margin * 2);

            editPanel.addComponent(textPanel);
            questionTextAreas.add(textArea);

            if(q.getClass() == WQuestion.class) {
                ArrayList<TextOption> options = q.getTextOptions();

                RoundedSpinner goalSpinner = new RoundedSpinner(new SpinnerNumberModel(q.getGoal(), 1, ((WQuestion) q).getMaxScore(), 1), "Answer");
                goalSpinner.setToolTipText("Determines how many correct answers are needed to pass");
                if((Integer) goalSpinner.getValue() > 1) {
                    goalSpinner.setText("Answers");
                }
                goalSpinner.addChangeListener(new ChangeListener() {
                    @Override
                    public void stateChanged(ChangeEvent e) {
                        if((Integer) goalSpinner.getValue() == 1) {
                            goalSpinner.setText("Answer");
                        } else {
                            goalSpinner.setText("Answers");
                        }

                        saveExam(file, questions, questionTextAreas, optionTextAreas, optionRadioButtons, orderedRadioButtons, goalSpinners);
                    }
                });
                if(q.isOrdered()) {
                    goalSpinner.setVisible(false);
                }

                JRadioButton radioButton = new JRadioButton();
                radioButton.setToolTipText("When selected, answers must be given in order");
                radioButton.setFocusable(false);
                radioButton.setBackground(null);
                radioButton.setForeground(Color.WHITE);
                radioButton.setText("Ordered");
                radioButton.setSelected(q.isOrdered());
                radioButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if( radioButton.isSelected()) {
                            goalSpinner.setVisible(false);
                        } else {
                            goalSpinner.setVisible(true);
                        }

                        saveExam(file, questions, questionTextAreas, optionTextAreas, optionRadioButtons, orderedRadioButtons, goalSpinners);
                    }
                });

                textPanel.add(radioButton, constraints);
                orderedRadioButtons.add(radioButton);

                constraints.insets = new Insets(margin * 3, margin * 3, margin * 3, margin * 3);

                textPanel.add(goalSpinner, constraints);
                goalSpinners.add(goalSpinner);

                constraints.insets = new Insets(margin * 2, margin * 2, margin * 2, margin * 2);

                for(TextOption o : options) {
                    textPanel = new RoundedPanel();
                    textPanel.setLayout(getLayout());
                    textPanel.setBackground(fieldColor);
                    textPanel.setBorderColor(examAddBorderColor);
                    textPanel.setBorderPainted(true);

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
                                exam.saveToFile(file.getName().substring(0, file.getName().length() - 4), file.getParentFile());

                                tester.reset();
                            }
                        }
                    });

                    constraints.insets = new Insets(margin * 2, margin * 2, margin * 2, margin * 2);
                    
                    textArea = new RoundedTextArea(o.getClearText());
                    textArea.setToolTipText("Click to change the answer text");
                    textArea.setLabel("Enter answer...");
                    textArea.addKeyListener(new KeyAdapter() {
                        @Override
                        public void keyReleased(KeyEvent e) {
                            if(e.getKeyCode() != KeyEvent.VK_ENTER) {
                                saveExam(file, questions, questionTextAreas, optionTextAreas, optionRadioButtons, orderedRadioButtons, goalSpinners);
                            }
                        }
                    });
                    textPanel.add(textArea, constraints);

                    constraints.fill = GridBagConstraints.HORIZONTAL;
                    constraints.insets = new Insets(margin * 2, margin * 2, margin * 2, margin * 2);
                    
                    textPanel.add(deleteButton, constraints);

                    styleButton(deleteButton, "Delete answer");
                    deleteButton.setBackground(deleteColor);

                    constraints.insets = new Insets(margin * 2, margin * 2, margin * 2, margin * 2);

                    editPanel.addComponent(textPanel);
                    
                    optionTextAreas.add(textArea);
                }
            } else if(q.getClass() == MCQuestion.class) {
                ArrayList<ButtonOption> options = q.getButtonOptions();

                JRadioButton radioButton = new JRadioButton();
                radioButton.setToolTipText("When selected, answers must be given in order");
                radioButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        saveExam(file, questions, questionTextAreas, optionTextAreas, optionRadioButtons, orderedRadioButtons, goalSpinners);
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
                    constraints = new GridBagConstraints();

                    constraints.fill = GridBagConstraints.BOTH;
                    constraints.weightx = 0.5;
                    constraints.weighty = 0.5;
                    constraints.insets = new Insets(margin * 2, margin * 2, margin * 2, margin * 2);

                    textPanel = new RoundedPanel();
                    textPanel.setLayout(getLayout());
                    textPanel.setBackground(fieldColor);
                    textPanel.setBorderColor(examAddBorderColor);
                    textPanel.setBorderPainted(true);

                    RoundedButton deleteImagePathButton = new RoundedButton();
                    deleteImagePathButton.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            o.setImagePath("");

                            saveExam(file, questions, questionTextAreas, optionTextAreas, optionRadioButtons, orderedRadioButtons, goalSpinners);

                            tester.reset();
                        }
                    });
                    styleButton(deleteImagePathButton, "Delete image");
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

                            saveExam(file, questions, questionTextAreas, optionTextAreas, optionRadioButtons, orderedRadioButtons, goalSpinners);

                            tester.reset();
                        }
                    });

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
                        styleButton(imageButton, "Add image");
                    }
                    textPanel.add(imageButton, constraints);

                    constraints.gridy = 1;

                    textPanel.add(deleteImagePathButton, constraints);

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
                                exam.saveToFile(file.getName().substring(0, file.getName().length() - 4), file.getParentFile());

                                tester.reset();
                            }
                        }
                    });

                    constraints.insets = new Insets(margin * 2, margin * 2, margin * 2, margin * 2);
                    
                    textArea = new RoundedTextArea(o.getText());
                    textArea.setToolTipText("Click to change the answer text");
                    textArea.setLabel("Enter answer...");
                    textArea.addKeyListener(new KeyAdapter() {
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
                                saveExam(file, questions, questionTextAreas, optionTextAreas, optionRadioButtons, orderedRadioButtons, goalSpinners);
                            }
                        }
                    });
                    textPanel.add(textArea, constraints);

                    radioButton = new JRadioButton();
                    radioButton.setToolTipText("Is this answer true?");
                    radioButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            saveExam(file, questions, questionTextAreas, optionTextAreas, optionRadioButtons, orderedRadioButtons, goalSpinners);
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

                    styleButton(deleteButton, "Delete answer");
                    deleteButton.setBackground(deleteColor);

                    constraints.insets = new Insets(margin * 2, margin * 2, margin * 2, margin * 2);

                    editPanel.addComponent(textPanel);
                    
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
                radioButton.setToolTipText("Is the question text true?");
                radioButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        saveExam(file, questions, questionTextAreas, optionTextAreas, optionRadioButtons, orderedRadioButtons, goalSpinners);
                    }
                });
                radioButton.setFocusable(false);
                radioButton.setBackground(null);
                radioButton.setForeground(Color.WHITE);
                radioButton.setText("True");
                radioButton.setSelected(q.getButtonOptions().getFirst().isTrue());
                textPanel.add(radioButton, constraints);

                constraints.insets = new Insets(margin * 2, margin * 2, margin * 2, margin * 2);

                editPanel.addComponent(textPanel);
                
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
                        exam.saveToFile(file.getName().substring(0, file.getName().length() - 4), file.getParentFile());

                        tester.reset();
                    }
                });

                constraints.insets = new Insets(margin, margin, margin, margin);

                addHam.addComponent(addButton);

                styleButton(addButton, "Add answer");
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

                addHam.addComponent(addButton);

                styleButton(addButton, "Add answer");
                addButton.setBackground(editColor);
            }

            editPanel.addComponent(addHam);

            editMenu.addComponent(editPanel);
        }

        HamburgerMenu addMenu = new HamburgerMenu.HamburgerMenuBuilder().parent(editMenu).icon(tester.getEditorButtonIcon()).build();
        addMenu.setBackground(examAddBackgroundColor);
        addMenu.setBorderColor(examAddBorderColor);
        addMenu.setBorderPainted(true);

        RoundedButton addWQButton = new RoundedButton();
        addWQButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                WQuestion wQuestion = new WQuestion(exam);
                wQuestion.addOption(new TextOption());

                questions.add(wQuestion);
                
                saveExam(file, questions, questionTextAreas, optionTextAreas, optionRadioButtons, orderedRadioButtons, goalSpinners);
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
                mcQuestion.addOption(new ButtonOption());

                questions.add(mcQuestion);
                
                saveExam(file, questions, questionTextAreas, optionTextAreas, optionRadioButtons, orderedRadioButtons, goalSpinners);
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

                ButtonOption trueOption = new ButtonOption();
                trueOption.setText("True");
                trueOption.setValue(true);
                tfQuestion.addOption(trueOption);

                ButtonOption falseOption = new ButtonOption();
                falseOption.setText("False");
                falseOption.setValue(false);
                tfQuestion.addOption(falseOption);

                questions.add(tfQuestion);

                saveExam(file, questions, questionTextAreas, optionTextAreas, optionRadioButtons, orderedRadioButtons, goalSpinners);
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
        ArrayList<JRadioButton> orderedRadioButtons,
        ArrayList<RoundedSpinner> goalSpinners
    ) {
        Exam exam = new Exam(file, tester);
        int
            qCount = 0,
            rCount = 0,
            oCount = 0,
            tCount = 0,
            sCount = 0;

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

    public void paste(File dir) {
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
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public void paste(Exam exam) {
        exam.addQuestion(tester.getCopiedQuestion());
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
