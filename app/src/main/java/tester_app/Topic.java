package tester_app;

import static tester_app.helpers.Constants.addMargin;
import static tester_app.helpers.Constants.backgroundColor;
import static tester_app.helpers.Constants.borderColor;
import static tester_app.helpers.Constants.margin;
import static tester_app.helpers.Constants.selectionColor;
import static tester_app.helpers.Constants.styleButton;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;

import tester_app.helpers.RoundedButton;
import tester_app.helpers.RoundedPanel;

public class Topic extends RoundedPanel {
    private RoundedButton topicButton;
    private RoundedPanel fileMenu;
    private Tester tester;
    private GridBagLayout layout;
    private GridBagConstraints constraints;
    private int radius;

    public Topic(String name, Image icon, Tester tester) {
        super();

        this.radius = 10;
        this.tester = tester;
        layout = new GridBagLayout();
        constraints = new GridBagConstraints();

        this.setBackground(backgroundColor);
        this.setBorderPainted(false);
        this.setLayout(layout);
        this.setSize(this.getSize().width + margin, this.getSize().height + margin);
        addMargin(this, 0);

        topicButton = new RoundedButton();
        topicButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                toggle(topicButton);
            }
        });
        styleButton(topicButton, name, icon, JButton.RIGHT);

        fileMenu = new RoundedPanel();
        fileMenu.setBackground(null);
        fileMenu.setBorderPainted(false);
        fileMenu.setVisible(false);
        fileMenu.setLayout(layout);

        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridx = 1;
        constraints.weightx = 0.5;
        constraints.weighty = 0.5;

        this.add(topicButton, constraints);
        this.add(fileMenu, constraints);

        this.setVisible(true);
    }

    public void loadFiles(final File dir, final Image fileIcon) {
        if(dir.isDirectory()) {
            for (final File f : dir.listFiles()) {
                if (!f.isDirectory()) {
                    String
                        fileName = f.getName(),
                        examName = fileName.substring(0, fileName.length() - 4);
                    RoundedButton fileButton = new RoundedButton();

                    fileButton.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            tester.startExam(f, tester);
                        }
                    });
                    styleButton(fileButton, examName, fileIcon, JButton.RIGHT);

                    addComponent(fileButton);
                }
            }
        } else {
            String
                fileName = dir.getName(),
                examName = fileName.substring(0, fileName.length() - 4);
            RoundedButton fileButton = new RoundedButton();

            fileButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    tester.startExam(dir, tester);
                }
            });
            styleButton(fileButton, examName, fileIcon, JButton.RIGHT);

            addComponent(fileButton);
        }
    }

    public void toggle(RoundedButton button) {
        if(fileMenu.isVisible()) {
            addMargin(this, 0);

            fileMenu.setVisible(false);
        } else {
            addMargin(this, margin * 2);

            fileMenu.setVisible(true);
        }
    }

    public void clearFileMenu() {
        fileMenu.removeAll();
    }
    public Boolean isEmpty() {
        return fileMenu.getComponents().length == 0;
    }

    private void addComponent(Component c, int anchor) {
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridx = 1;
        constraints.weightx = 0.5;
        constraints.weighty = 0.5;
        constraints.anchor = anchor;

        fileMenu.add(c, constraints);
    }
    private void addComponent(Component c) {
        addComponent(c, GridBagConstraints.CENTER);
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(getBackground());
        g.fillRoundRect(margin, margin, getWidth() - margin * 2, getHeight() - margin * 2, radius, radius);
    }

    @Override
    protected void paintBorder(Graphics g) {
        if(fileMenu.isVisible()) {
            g.setColor(selectionColor);
        } else {
            g.setColor(borderColor);
        }

        g.drawRoundRect(margin, margin, getWidth() - 1 - margin * 2, getHeight() - 1 - margin * 2, radius, radius);
    }
}
