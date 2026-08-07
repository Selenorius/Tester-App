package tester_app;

import static tester_app.helpers.Constants.deleteColor;
import static tester_app.helpers.Constants.editColor;
import static tester_app.helpers.Constants.styleButton;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JOptionPane;

import tester_app.helpers.HamburgerMenu;
import tester_app.helpers.RoundedButton;

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
    }

    public void addExam(final File file, final Image fileIcon) {
        String
            fileName = file.getName(),
            examName = fileName.substring(0, fileName.length() - 4);
        RoundedButton
            startButton = new RoundedButton(),
            editButton = new RoundedButton(),
            deleteButton = new RoundedButton();
        HamburgerMenu hamburgerMenu = new HamburgerMenu.HamburgerMenuBuilder().text(examName).icon(fileIcon).build();

        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tester.startExam(file, tester);
            }
        });
        styleButton(startButton, "Start exam");
        hamburgerMenu.addComponent(startButton);
        
        editButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int response = JOptionPane.showConfirmDialog(
                    tester,
                    "Are you sure you want to save this exam?",
                    "Warning",
                    JOptionPane.YES_NO_OPTION
                );

                if (response == JOptionPane.YES_OPTION) {
                    try {
                        Exam exam = new Exam(file, tester);

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
        styleButton(editButton, "Edit exam");
        editButton.setSelectionColor(editColor);
        hamburgerMenu.addComponent(editButton);

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
