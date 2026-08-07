package tester_app;

import static tester_app.helpers.Constants.styleButton;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

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
                    String
                        fileName = f.getName(),
                        examName = fileName.substring(0, fileName.length() - 4);
                    RoundedButton
                        startButton = new RoundedButton(),
                        editButton = new RoundedButton(),
                        deleteButton = new RoundedButton();

                    startButton.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            tester.startExam(f, tester);
                        }
                    });
                    styleButton(startButton, "Start exam");
                    styleButton(editButton, "Edit exam");
                    styleButton(deleteButton, "Delete exam");

                    HamburgerMenu hamburgerMenu = new HamburgerMenu.HamburgerMenuBuilder().text(examName).icon(fileIcon).build();
                    hamburgerMenu.addComponent(startButton);
                    hamburgerMenu.addComponent(editButton);
                    hamburgerMenu.addComponent(deleteButton);

                    addComponent(hamburgerMenu);
                }
            }
        } else {
            String
                fileName = dir.getName(),
                examName = fileName.substring(0, fileName.length() - 4);
            RoundedButton
                startButton = new RoundedButton(),
                editButton = new RoundedButton(),
                deleteButton = new RoundedButton();

            startButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    tester.startExam(dir, tester);
                }
            });
            styleButton(startButton, "Start exam");
            styleButton(editButton, "Edit exam");
            styleButton(deleteButton, "Delete exam");

            HamburgerMenu hamburgerMenu = new HamburgerMenu.HamburgerMenuBuilder().text(examName).icon(fileIcon).build();
            hamburgerMenu.addComponent(startButton);
            hamburgerMenu.addComponent(editButton);
            hamburgerMenu.addComponent(deleteButton);

            addComponent(hamburgerMenu);
        }
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
