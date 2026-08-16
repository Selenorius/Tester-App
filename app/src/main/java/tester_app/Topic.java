package tester_app;

import static tester_app.helpers.Constants.deleteColor;
import static tester_app.helpers.Constants.editColor;
import static tester_app.helpers.Constants.fieldColor;
import static tester_app.helpers.Constants.margin;
import static tester_app.helpers.Constants.styleButton;

import java.awt.GridBagConstraints;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import tester_app.helpers.HamburgerMenu;
import tester_app.helpers.RoundedButton;
import tester_app.helpers.RoundedPanel;
import tester_app.helpers.RoundedTextArea;
import tester_app.questions.MCQuestion;
import tester_app.questions.Question;
import tester_app.questions.TFQuestion;
import tester_app.questions.WQuestion;

public class Topic extends HamburgerMenu {
    private Tester tester;
    private ArrayList<HamburgerMenu> editMenus;

    public Topic(TopicBuilder builder) {
        super(builder);

        tester = builder.tester;
        editMenus = new ArrayList<>();
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
        ArrayList<RoundedTextArea> textAreas = new ArrayList<>();

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 0.5;
        constraints.weighty = 0.5;
        constraints.insets = new Insets(margin * 2, margin * 2, margin * 2, margin * 2);

        for(Question q : questions) {
            RoundedTextArea textArea = new RoundedTextArea(q.getQuestionText());
            textAreas.add(textArea);

            editPanel = new RoundedPanel();
            editPanel.setBackground(fieldColor);
            editPanel.setBorderPainted(false);
            editPanel.setLayout(getLayout());

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

            editPanel.add(textArea, constraints);
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
                    int i = 0;

                    for(Question q : questions) {
                        q.setQuestionText(textAreas.get(i++).getText());
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

        editMenus.add(editMenu);
        
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
