package tester_app;

import static tester_app.helpers.Constants.backgroundColor;
import static tester_app.helpers.Constants.fieldColor;
import static tester_app.helpers.Constants.name;
import static tester_app.helpers.Constants.root;
import static tester_app.helpers.Constants.styleScrollPane;
import static tester_app.helpers.Constants.size;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;
import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import tester_app.editors.TextEditor;
import tester_app.helpers.ConsoleErrorJFrame;
import tester_app.helpers.HamburgerMenu;
import tester_app.helpers.RoundedPanel;

public class Tester extends ConsoleErrorJFrame {
    private JScrollPane scrollPane;
    protected RoundedPanel dirMenu;
    private HamburgerMenu addButton;
    private Topic uncategorized;
    protected final Image
        icon = loadIcon("/tester_appx96.png"),
        dirButtonIcon = loadIcon("/dirButtonx32.png"),
        fileButtonIcon = loadIcon("/fileButtonx32.png"),
        editorButtonIcon = loadIcon("/editorButtonx32.png");
    private final String settingsFile = "settings.txt";
    private GridBagLayout layout;
    private GridBagConstraints constraints;
    
    public Tester() {
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setTitle(name);
        this.setIconImage(icon);
        this.setMinimumSize(size);
        this.setSize(size);
        this.setLayout(new FlowLayout());
        this.setLocationRelativeTo(null);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | UnsupportedLookAndFeelException e1) {
            consoleErrorMessage("UIManager.setLookAndFeel", e1.getMessage());
        }
        this.getContentPane().setBackground(backgroundColor);
        this.getContentPane().addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                Dimension testerSize = getTesterSize();

                scrollPane.setPreferredSize(testerSize);
            }
        });
        this.addWindowStateListener(new WindowStateListener() {
            @Override
            public void windowStateChanged(WindowEvent e) {
                Dimension testerSize = getTesterSize();

                scrollPane.setPreferredSize(testerSize);
            }
        });
        this.addWindowListener(new WindowListener() {

            @Override
            public void windowOpened(WindowEvent e) {
                File settings = new File(settingsFile);

                if(settings.isFile()) {
                    try(Scanner settingsFileIn = new Scanner(settings)) {
                        String data = settingsFileIn.nextLine();
                        if(data != "") {
                            setExtendedState(Integer.parseInt(data));
                        }
                    } catch (Exception e1) {
                        consoleErrorMessage("windowOpened.Scanner", e1.getMessage());
                    }
                }

                if(!root.exists()) {
                    root.mkdir();
                }
            }

            @Override
            public void windowClosing(WindowEvent e) {}

            @Override
            public void windowClosed(WindowEvent e) {
                updateSettings();
            }

            @Override
            public void windowIconified(WindowEvent e) {}

            @Override
            public void windowDeiconified(WindowEvent e) {}

            @Override
            public void windowActivated(WindowEvent e) {}

            @Override
            public void windowDeactivated(WindowEvent e) {}
            
        });

        uncategorized = new Topic.TopicBuilder().text("Uncategorized").icon(dirButtonIcon).tester(this).build();

        layout = new GridBagLayout();
        constraints = new GridBagConstraints();

        dirMenu = new RoundedPanel();
        dirMenu.setBackground(fieldColor);
        dirMenu.setLayout(layout);

        scrollPane = new JScrollPane(dirMenu);
        scrollPane.getViewport().setBackground(backgroundColor);
        scrollPane.setBorder(null);
        scrollPane.setSize(getTesterSize());
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        styleScrollPane(scrollPane);

        HamburgerMenu addTopicButton = new HamburgerMenu.HamburgerMenuBuilder().text("Create new topic").icon(dirButtonIcon).build();

        HamburgerMenu addExamButton = new HamburgerMenu.HamburgerMenuBuilder().text("Create new exam").icon(fileButtonIcon).build();

        addButton = new HamburgerMenu.HamburgerMenuBuilder().icon(editorButtonIcon).build();
        addButton.addComponent(addTopicButton);
        addButton.addComponent(addExamButton);

        this.add(scrollPane);

        this.setVisible(true);
    }

    private void updateSettings() {
        try {
            FileWriter settingsFileOut = new FileWriter(settingsFile);
            settingsFileOut.write(
                getExtendedState() + System.lineSeparator()
            );
            settingsFileOut.close();
        } catch (Exception e1) {
            consoleErrorMessage("updateSettings", e1.getMessage());
        }
    }

    private void loadDir(final File dir) {
        File[] files = dir.listFiles();
        RoundedPanel empty = new RoundedPanel();
        JLabel text = new JLabel("No exams found.");

        text.setForeground(Color.WHITE);

        empty.add(text);
        empty.setBackground(null);
        empty.setBorderPainted(false);

        if(files.length == 0) {
            if(dir == root) {
                addComponent(empty);
            }
        } else {
            for (final File f : files) {
                if (f.isDirectory()) {
                    Topic dirTopic = new Topic.TopicBuilder().text(f.getName()).icon(dirButtonIcon).tester(this).build();

                    dirTopic.loadFiles(f, fileButtonIcon);

                    addComponent(dirTopic);
                    loadDir(f);
                } else if(f.getParentFile().compareTo(root) == 0) {
                    uncategorized.loadFiles(f, fileButtonIcon);
                }
            }
        }
        
        addComponent(uncategorized);
        addComponent(addButton, GridBagConstraints.SOUTH);
    }

    private void addComponent(Component c, int anchor) {
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridx = 1;
        constraints.weightx = 0.5;
        constraints.weighty = 0.5;
        constraints.anchor = anchor;

        dirMenu.add(c, constraints);
    }
    private void addComponent(Component c) {
        addComponent(c, GridBagConstraints.CENTER);
    }

    private Dimension getTesterSize() {
        return new Dimension(this.getSize().width - 28, this.getSize().height - 50);
    }

    public void startEditor() {
        new TextEditor(this);
        this.dispose();
    }

    public void startExam(File f, Tester tester) {
        new Exam(f, tester).start();
    
        this.dispose();
    }

    public void start() {
        uncategorized.clearMenu();
        dirMenu.removeAll();
        loadDir(root);
        
        if(uncategorized.isEmpty()) {
            uncategorized.setVisible(false);
        } else {
            uncategorized.setVisible(true);
        }
        
        this.setVisible(true);
    }
}
