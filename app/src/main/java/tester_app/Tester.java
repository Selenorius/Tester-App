package tester_app;

import static tester_app.helpers.Constants.addMargin;
import static tester_app.helpers.Constants.backgroundColor;
import static tester_app.helpers.Constants.deleteColor;
import static tester_app.helpers.Constants.editColor;
import static tester_app.helpers.Constants.examAddBorderColor;
import static tester_app.helpers.Constants.fieldColor;
import static tester_app.helpers.Constants.margin;
import static tester_app.helpers.Constants.name;
import static tester_app.helpers.Constants.root;
import static tester_app.helpers.Constants.size;
import static tester_app.helpers.Constants.styleButton;
import static tester_app.helpers.Constants.styleScrollPane;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import tester_app.editors.TextEditor;
import tester_app.helpers.ConsoleErrorJFrame;
import tester_app.helpers.FrameDragListener;
import tester_app.helpers.FrameResizeListener;
import tester_app.helpers.HamburgerMenu;
import tester_app.helpers.RoundedButton;
import tester_app.helpers.RoundedMenuBar;
import tester_app.helpers.RoundedPanel;

public class Tester extends ConsoleErrorJFrame {
    private JScrollPane scrollPane;
    protected RoundedPanel
        dirMenu,
        titleMenu;
    private HamburgerMenu addButton;
    private Topic uncategorized;
    private GridBagLayout layout;
    private GridBagConstraints constraints;
    private RoundedMenuBar menuBar;
    private JLabel
        iconLabel,
        titleLabel;

    private int
        windowstate,
        prevWindowstate;

    protected final Image
        icon = loadIcon("/tester_appx96.png"),
        dirButtonIcon = loadIcon("/dirButtonx32.png"),
        fileButtonIcon = loadIcon("/fileButtonx32.png"),
        editorButtonIcon = loadIcon("/editorButtonx32.png"),
        backButtonIcon = loadIcon("/backButtonx32.png");
    private final String settingsFile = "settings.txt";
    
    public Tester() {
        if(!root.exists()) {
            root.mkdir();
        }

        layout = new GridBagLayout();
        constraints = new GridBagConstraints();
        
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setTitle(name);
        this.setIconImage(icon);
        this.setMinimumSize(size);
        this.setSize(size);
        this.setLayout(layout);
        this.setLocationRelativeTo(null);
        this.setUndecorated(true);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | UnsupportedLookAndFeelException e1) {
            consoleErrorMessage("UIManager.setLookAndFeel", e1.getMessage());
        }
        this.getContentPane().setBackground(fieldColor);
        this.addWindowStateListener(new WindowStateListener() {
            @Override
            public void windowStateChanged(WindowEvent e) {
                if(windowstate == ICONIFIED && windowstate != getExtendedState()) {
                    setExtendedState(prevWindowstate);
                } else if(windowstate != getExtendedState()) {
                    setExtendedState(windowstate);
                }
            }
        });
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                File settings = new File(settingsFile);

                if(settings.isFile()) {
                    try(Scanner settingsFileIn = new Scanner(settings)) {
                        String data = settingsFileIn.nextLine();
                        if(data != "") {
                            windowstate = Integer.parseInt(data);
                            setExtendedState(windowstate);
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
            public void windowClosed(WindowEvent e) {
                updateSettings();
            }
        });
        FrameResizeListener frameResizeListener = new FrameResizeListener(this);
        this.addMouseListener(frameResizeListener);
        this.addMouseMotionListener(frameResizeListener);
        this.getContentPane().setBackground(fieldColor);
        this.setBackground(fieldColor);

        RoundedButton minButton = new RoundedButton();
        minButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                prevWindowstate = getExtendedState();
                windowstate = ICONIFIED;
                setExtendedState(ICONIFIED);
            }
        });
        styleButton(minButton, "Min");

        RoundedButton maxButton = new RoundedButton();
        maxButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(getExtendedState() == NORMAL) {
                    windowstate = MAXIMIZED_BOTH;
                    setExtendedState(MAXIMIZED_BOTH);
                } else {
                    windowstate = NORMAL;
                    setExtendedState(NORMAL);
                }
            }
        });
        styleButton(maxButton, "Max");

        RoundedButton backButton = new RoundedButton();
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        styleButton(backButton, "Exit");
        backButton.setSelectionColor(deleteColor);

        RoundedPanel space = new RoundedPanel();
        space.setBackground(null);
        space.setBorderPainted(false);

        menuBar = new RoundedMenuBar();
        menuBar.setBorderPainted(false);
        menuBar.setBackground(fieldColor);
        menuBar.setLayout(layout);
        menuBar.add(Box.createHorizontalGlue());
        FrameDragListener frameDragListener = new FrameDragListener(this);
        menuBar.addMouseListener(frameDragListener);
        menuBar.addMouseMotionListener(frameDragListener);
        addMargin(menuBar, margin);

        iconLabel = new JLabel();
        iconLabel.setIcon(new ImageIcon(icon.getScaledInstance(16, 16,  java.awt.Image.SCALE_SMOOTH)));

        titleLabel = new JLabel("Tester App");
        titleLabel.setForeground(Color.WHITE);

        titleMenu = new RoundedPanel();
        titleMenu.setBackground(null);
        titleMenu.setBorderPainted(false);
        titleMenu.setLayout(layout);
        
        constraints.insets = new Insets(0, 0, 0, margin * 2);

        titleMenu.add(iconLabel, constraints);
        titleMenu.add(titleLabel, constraints);

        menuBar.add(titleMenu);

        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 10;
        constraints.weighty = 0.5;
        constraints.insets = new Insets(0, 0, 0, 0);

        menuBar.add(space, constraints);

        constraints.weightx = 0.5;

        menuBar.add(minButton, constraints);
        menuBar.add(maxButton, constraints);
        menuBar.add(backButton, constraints);

        uncategorized = new Topic.TopicBuilder().text("Uncategorized").icon(dirButtonIcon).tester(this).build();

        dirMenu = new RoundedPanel();
        dirMenu.setBackground(backgroundColor);
        dirMenu.setLayout(layout);

        scrollPane = new JScrollPane(dirMenu);
        scrollPane.getViewport().setBackground(fieldColor);
        scrollPane.setBorder(null);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(64);
        styleScrollPane(scrollPane);

        RoundedButton addTopicButton = new RoundedButton();
        addTopicButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new File(root.getPath() + "/New topic").mkdir();

                dispose();
                start();
            }
        });
        styleButton(addTopicButton, "Create new topic", dirButtonIcon);
        addTopicButton.setSelectionColor(editColor);

        RoundedButton addExamButton = new RoundedButton();
        addExamButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    new File(root.getPath() + "/New exam.txt").createNewFile();
                } catch (IOException e1) {
                    consoleErrorMessage(e1);
                }

                dispose();
                start();
            }
        });
        styleButton(addExamButton, "Create new exam", fileButtonIcon);
        addExamButton.setSelectionColor(editColor);

        addButton = new HamburgerMenu.HamburgerMenuBuilder().icon(editorButtonIcon).build();
        addButton.setBackground(fieldColor);
        addButton.setBorderColor(examAddBorderColor);
        addButton.setBorderPainted(true);
        addButton.addComponent(addTopicButton);
        addButton.addComponent(addExamButton);

        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 0.5;
        constraints.weighty = 0.5;
        constraints.insets = new Insets(margin * 2, margin * 2, margin * 2, margin * 2);

        this.setJMenuBar(menuBar);
        this.add(scrollPane, constraints);

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

        if(files.length != 0) {
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

    // GETTERS
    public Image getIcon() {
        return icon;
    }

    public Image getDirButtonIcon() {
        return dirButtonIcon;
    }

    public Image getFileButtonIcon() {
        return fileButtonIcon;
    }

    public Image getEditorButtonIcon() {
        return editorButtonIcon;
    }

    public Image getBackButtonIcon() {
        return backButtonIcon;
    }
}
