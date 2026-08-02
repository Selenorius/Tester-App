package tester_app;

import static tester_app.helpers.Constants.addMargin;
import static tester_app.helpers.Constants.buttonBackgroundColor;
import static tester_app.helpers.Constants.margin;
import static tester_app.helpers.Constants.selectionColor;
import static tester_app.helpers.Constants.styleButton;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.JButton;

import tester_app.helpers.RoundedButton;
import tester_app.helpers.RoundedPanel;

public class Topic extends RoundedPanel {
    private RoundedButton topicButton;
    private RoundedPanel fileMenu;

    public Topic(String name, Image icon) {
        super();

        this.setBackground(null);
        this.setBorderPainted(false);

        topicButton = new RoundedButton();
        topicButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                toggle(topicButton);
            }
        });
        styleButton(topicButton, name, icon, JButton.RIGHT);

        fileMenu = new RoundedPanel();
        fileMenu.setBackground(buttonBackgroundColor);
        addMargin(fileMenu, margin);
        fileMenu.setLayout(new BoxLayout(fileMenu, BoxLayout.Y_AXIS));
        fileMenu.setVisible(false);

        this.add(topicButton);
        this.add(fileMenu);

        this.setVisible(true);
    }

    public void loadFiles(final File dir, final Image fileIcon) {
        for (final File f : dir.listFiles()) {
            if (!f.isDirectory()) {
                String
                    fileName = f.getName(),
                    examName;
                examName = fileName.substring(0, fileName.length() - 4);
                JButton fileButton = new RoundedButton();
                fileButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        System.out.println(examName);
                    }
                });
                styleButton(fileButton, examName, fileIcon, JButton.RIGHT);
                fileButton.setBorderPainted(false);
                fileButton.setBackground(null);
                fileMenu.add(fileButton);
            }
        }
    }

    public void toggle(JButton button) {
        if(fileMenu.isVisible()) {
            button.setBackground(buttonBackgroundColor);
            fileMenu.setVisible(false);
        } else {
            button.setBackground(selectionColor);
            fileMenu.setVisible(true);
        }
    }
}
