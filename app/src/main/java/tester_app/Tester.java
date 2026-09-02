package tester_app;

import static tester_app.helpers.Constants.addMargin;
import static tester_app.helpers.Constants.deleteColor;
import static tester_app.helpers.Constants.editColor;
import static tester_app.helpers.Constants.fieldColor;
import static tester_app.helpers.Constants.margin;
import static tester_app.helpers.Constants.name;
import static tester_app.helpers.Constants.pasteColor;
import static tester_app.helpers.Constants.root;
import static tester_app.helpers.Constants.size;
import static tester_app.helpers.Constants.styleButton;
import static tester_app.helpers.Constants.styleScrollPane;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import tester_app.helpers.ConsoleErrorJFrame;
import tester_app.helpers.FrameDragListener;
import tester_app.helpers.FrameResizeListener;
import tester_app.helpers.HamburgerMenu;
import tester_app.helpers.RoundedButton;
import tester_app.helpers.RoundedMenuBar;
import tester_app.helpers.RoundedPanel;
import tester_app.questions.Question;

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
    private Component copiedComponent;
    private File copiedFile;
    private Question copiedQuestion;

    private int
        windowstate,
        prevWindowstate,
        sCount;
    protected ArrayList<HamburgerMenu> extendedStates;
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
        extendedStates = new ArrayList<>();
        
        this.getContentPane().setIgnoreRepaint(false);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setTitle(name);
        this.setIconImage(icon);
        this.setMinimumSize(size);
        this.setSize(size);
        this.setLayout(layout);
        this.setLocationRelativeTo(null);
        this.setUndecorated(true);
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e1) {
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

        RoundedButton backButton = new RoundedButton();
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        RoundedPanel space = new RoundedPanel();
        space.setBackground(null);
        space.setBorderPainted(false);

        menuBar = new RoundedMenuBar();
        menuBar.setBorderPainted(false);
        menuBar.setBackground(getBackground());
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

        styleButton(minButton, "Min");
        styleButton(maxButton, "Max");
        styleButton(backButton, "Exit");
        backButton.setBackground(deleteColor);

        dirMenu = new RoundedPanel();
        dirMenu.setBorderPainted(true);
        dirMenu.setLayout(layout);

        scrollPane = new JScrollPane(dirMenu);
        scrollPane.getViewport().setBackground(fieldColor);
        scrollPane.setBorder(null);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(64);
        scrollPane.setViewportView(dirMenu);
        styleScrollPane(scrollPane);

        uncategorized = new Topic.TopicBuilder().parent(dirMenu).text("Uncategorized").icon(dirButtonIcon).tester(this).build();
        uncategorized.setTitled(false);
        uncategorized.setBlotOffset(0);

        RoundedButton addTopicButton = new RoundedButton();
        addTopicButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                File file = new File(root.getPath() + "/New topic");
                if(!file.exists()) {
                    file.mkdir();
                    addComponent(new Topic.TopicBuilder().text("New topic").build());

                    addButton.toggle(false);
                    reset();
                }
            }
        });

        RoundedButton addExamButton = new RoundedButton();
        addExamButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    File file = new File(root.getPath() + "/New exam.txt");
                    if(!file.exists()) {
                        file.createNewFile();
                        HamburgerMenu newExam = new HamburgerMenu.HamburgerMenuBuilder().text("New exam").build();
                        newExam.addComponent(new HamburgerMenu.HamburgerMenuBuilder().build());
                        uncategorized.addComponent(newExam);
                        
                        addButton.toggle(false);
                        reset();
                    }
                } catch (IOException e1) {
                    consoleErrorMessage(e1);
                }
            }
        });

        RoundedButton pasteButton = new RoundedButton();
        pasteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(paste()) {
                    addButton.toggle(false);
                }
            }
        });

        addButton = new HamburgerMenu.HamburgerMenuBuilder().parent(dirMenu).icon(editorButtonIcon).build();
        addButton.setBorderPainted(true);
        
        addButton.addComponent(addTopicButton);
        addButton.addComponent(addExamButton);
        addButton.addComponent(pasteButton);

        styleButton(addTopicButton, "Create new topic", dirButtonIcon);
        addTopicButton.setBackground(editColor);

        styleButton(addExamButton, "Create new exam", fileButtonIcon);
        addExamButton.setBackground(editColor);

        styleButton(pasteButton, "Paste");
        pasteButton.setBackground(pasteColor);

        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 0.5;
        constraints.weighty = 0.5;
        constraints.insets = new Insets(0, margin * 2, margin * 2, margin * 2);

        this.setJMenuBar(menuBar);
        this.add(scrollPane, constraints);

        constraints.insets = new Insets(margin * 2, margin * 2, margin * 2, margin * 2);

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

    private void saveExtendedStates(Component component) {
        if(((Container) component).getComponentCount() > 0) {
            for(Component c : ((Container) component).getComponents()) {
                if(c.getClass() == HamburgerMenu.class) {
                    if(c != addButton) {
                        extendedStates.add(((HamburgerMenu) c));
                    }

                    saveExtendedStates(((HamburgerMenu) c).getMenu());
                } else if(c.getClass() == Topic.class) {
                    if(c != uncategorized) {
                        extendedStates.add(((Topic) c));
                    }

                    saveExtendedStates(((Topic) c).getMenu());
                }
            }
        }
    }

    private void loadExtendedStates(Component component) {
        if(
            ((Container) component).getComponentCount() > 0 &&
            extendedStates != null
        ) {
            for(Component c : ((Container) component).getComponents()) {
                try {
                    if(
                        c.getClass() == Topic.class ||
                        c.getClass() == HamburgerMenu.class
                    ) {
                        if(
                            c != uncategorized &&
                            c != addButton &&
                            sCount < extendedStates.size()
                        ) {
                            HamburgerMenu ham = extendedStates.get(sCount);

                            //System.out.println(sCount + ". " + ((HamburgerMenu) c).getText() + ": " + ham.isExtended());

                            if(ham.getText() != null) {
                                if(ham.getText().equals(((HamburgerMenu) c).getText())) {
                                    if(ham.isExtended()) {
                                        ((HamburgerMenu) c).toggle();
                                    }

                                    //System.out.println(ANSI_GREEN + "HAM EQUALS" + ANSI_RESET);
                                } else {
                                    for(HamburgerMenu h : extendedStates) {
                                        if(h.getText() != null) {
                                            if(h.getText().equals(((HamburgerMenu) c).getText())) {
                                                if(h.isExtended()) {
                                                    ((HamburgerMenu) c).toggle();
                                                }

                                                //System.out.println(ANSI_GREEN + "H EQUALS" + ANSI_RESET);
                                            }
                                        }
                                    }
                                }
                            } else {
                                if(ham.isExtended()) {
                                    ((HamburgerMenu) c).toggle();
                                }

                                //System.out.println(ANSI_GREEN + "DEFAULTED" + ANSI_RESET);
                            }

                            ++sCount;
                        }

                        loadExtendedStates(((HamburgerMenu) c).getMenu());
                    }
                } catch(Exception e) {
                    consoleErrorMessage(e);
                }
            }
        }
    }

    private void loadDir(final File dir) {
        File[] files = dir.listFiles();

        if(files.length != 0) {
            for (final File f : files) {
                if (f.isDirectory()) {
                    Topic dirTopic = new Topic.TopicBuilder().parent(dirMenu).text(f.getName()).icon(dirButtonIcon).tester(this).build();

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

    public void startExam(File f, Tester tester) {
        new Exam(f, tester).start();
    }

    public void reset() {
        sCount = 0;
        extendedStates.clear();

        saveExtendedStates(dirMenu);

        //System.out.println(getExtendedStates());

        uncategorized.clearMenu();
        dirMenu.removeAll();
        loadDir(root);
        
        if(uncategorized.isEmpty()) {
            uncategorized.setVisible(false);
        } else {
            uncategorized.setVisible(true);
        }

        loadExtendedStates(dirMenu);

        revalidate();
        repaint();
    }

    public void start() {
        sCount = 0;
        extendedStates.clear();

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

    public boolean paste() {
        if(
            copiedComponent != null &&
            copiedFile != null
        ) {
            for(Component c : dirMenu.getComponents()) {
                if(
                    c.getClass() == HamburgerMenu.class ||
                    c.getClass() == Topic.class
                ) {
                    if(((HamburgerMenu) c).getText() != null) {
                        if(((HamburgerMenu) c).getText().equals(((HamburgerMenu) copiedComponent).getText())) {
                            ((HamburgerMenu) copiedComponent).setText(((HamburgerMenu) copiedComponent).getText() + " - Copy");
                        }
                    }
                }
            }

            Path oldDirPath = Paths.get(copiedFile.getPath());
            Path newDirPath = Paths.get(root.getPath(), ((HamburgerMenu) copiedComponent).getText());

            if(copiedComponent.getClass() == HamburgerMenu.class) {
                newDirPath = Paths.get(root.getPath(), ((HamburgerMenu) copiedComponent).getText() + ".txt");

                uncategorized.addComponent(copiedComponent);
            } else {
                addComponent(copiedComponent);
            }

            try {
                Files.copy(oldDirPath, newDirPath);
                if(copiedComponent.getClass() == Topic.class) {
                    for(String f : copiedFile.list()) {
                        try {
                            Files.copy(Paths.get(copiedFile.getPath(), f), Paths.get(root.getPath(), ((HamburgerMenu) copiedComponent).getText(), f));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                reset();
                return true;
            } catch (IOException e) {
                consoleErrorMessage(e);
            }
        }
        return false;
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

    public String getExtendedStates() {
        String out = "";
        int
            count = 0,
            extended = 0;

        for(HamburgerMenu h : extendedStates) {
            out += count++ + ". " + h.getText() + ": " + h.isExtended() + System.lineSeparator();
            if(h.isExtended()) {
                ++extended;
            }
        }

        return out + System.lineSeparator() + extendedStates.size() + ":" + extended;
    }

    public File getCopiedFile() {
        return this.copiedFile;
    }

    public Component getCopiedComponent() {
        return this.copiedComponent;
    }

    public Question getCopiedQuestion() {
        return copiedQuestion;
    }

    // SETTERS
    public void setCopiedComponent(Component component) {
        this.copiedComponent =  component;
    }

    public void setCopiedFile(File file) {
        this.copiedFile = file;
    }

    public void setCopiedQuestion(Question copiedQuestion) {
        this.copiedQuestion = copiedQuestion;
    }
}
