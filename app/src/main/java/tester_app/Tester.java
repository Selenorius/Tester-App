package tester_app;

import static tester_app.helpers.Constants.addMargin;
import static tester_app.helpers.Constants.buttonFont;
import static tester_app.helpers.Constants.deleteColor;
import static tester_app.helpers.Constants.editColor;
import static tester_app.helpers.Constants.fieldColor;
import static tester_app.helpers.Constants.margin;
import static tester_app.helpers.Constants.name;
import static tester_app.helpers.Constants.pasteColor;
import static tester_app.helpers.Constants.root;
import static tester_app.helpers.Constants.search;
import static tester_app.helpers.Constants.size;
import static tester_app.helpers.Constants.styleButton;
import static tester_app.helpers.Constants.styleScrollPane;
import static tester_app.helpers.Constants.topicColor;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
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
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import tester_app.helpers.ConsoleErrorJFrame;
import tester_app.helpers.FrameDragListener;
import tester_app.helpers.FrameResizeListener;
import tester_app.helpers.HamburgerMenu;
import tester_app.helpers.RoundedButton;
import tester_app.helpers.RoundedLabel;
import tester_app.helpers.RoundedMenuBar;
import tester_app.helpers.RoundedPanel;
import tester_app.helpers.RoundedTextArea;
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
    private RoundedLabel iconLabel;
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
        backButtonIcon = loadIcon("/backButtonx32.png"),
        returnButtonIcon = loadIcon("/returnButtonx32.png"),
        minButtonIcon = loadIcon("/minButtonx32.png"),
        maxButtonIcon = loadIcon("/maxButtonx32.png"),
        wqIcon = loadIcon("/WQ_iconx32.png"),
        mcIcon = loadIcon("/MC_iconx32.png"),
        tfIcon = loadIcon("/TF_iconx32.png"),
        editIcon = loadIcon("/edit_iconx32.png"),
        copyIcon = loadIcon("/copy_iconx32.png"),
        deleteIcon = loadIcon("/delete_iconx32.png"),
        addIcon = loadIcon("/add_iconx32.png"),
        pasteIcon = loadIcon("/paste_iconx32.png"),
        resetIcon = loadIcon("/backButtonx32.png");
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
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
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

                System.exit(0);
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

        menuBar = new RoundedMenuBar();
        menuBar.setBackground(getBackground());
        menuBar.setLayout(layout);
        menuBar.add(Box.createHorizontalGlue());
        FrameDragListener frameDragListener = new FrameDragListener(this);
        menuBar.addMouseListener(frameDragListener);
        menuBar.addMouseMotionListener(frameDragListener);
        addMargin(menuBar, margin);

        iconLabel = new RoundedLabel("Tester App", SwingConstants.LEFT);
        iconLabel.setIcon(new ImageIcon(icon.getScaledInstance(16, 16,  java.awt.Image.SCALE_SMOOTH)));
        iconLabel.setForeground(Color.WHITE);
        iconLabel.setFont(new Font(buttonFont.getFamily(), buttonFont.getStyle(), 11));
        iconLabel.setIconTextGap(margin * 2);

        titleMenu = new RoundedPanel();
        titleMenu.setBackground(null);
        titleMenu.setLayout(layout);
        
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 0.5;
        constraints.anchor = GridBagConstraints.WEST;

        titleMenu.add(iconLabel, constraints);

        constraints.weightx = 10;
        constraints.insets = new Insets(0, 0, 0, 0);
        constraints.anchor = GridBagConstraints.WEST;

        menuBar.add(titleMenu, constraints);

        constraints.weightx = 0;
        constraints.anchor = GridBagConstraints.EAST;

        menuBar.add(minButton, constraints);
        menuBar.add(maxButton, constraints);
        menuBar.add(backButton, constraints);

        styleButton(minButton, minButtonIcon.getScaledInstance(10, 10, Image.SCALE_SMOOTH));
        styleButton(maxButton, maxButtonIcon.getScaledInstance(10, 10, Image.SCALE_SMOOTH));
        styleButton(backButton, backButtonIcon.getScaledInstance(10, 10, Image.SCALE_SMOOTH));
        minButton.setBackground(getBackground());
        maxButton.setBackground(getBackground());
        backButton.setBackground(getBackground());
        backButton.setSelectionColor(deleteColor);

        addMargin(minButton, margin * 3, margin, margin * 3, margin);
        addMargin(maxButton, margin * 3, margin, margin * 3, margin);
        addMargin(backButton, margin * 3, margin, margin * 3, margin);

        dirMenu = new RoundedPanel(loadIcon("/texture.png"));
        dirMenu.setBackground(getBackground().darker());
        dirMenu.setBorderColor(getBackground().darker());
        dirMenu.setBorderPainted(true);
        dirMenu.setLayout(layout);
        addMargin(dirMenu, 0);

        scrollPane = new JScrollPane(dirMenu);
        scrollPane.setBackground(getBackground());
        scrollPane.getViewport().setBackground(getBackground());
        scrollPane.setBorder(null);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(64);
        scrollPane.setViewportView(dirMenu);
        styleScrollPane(scrollPane);

        uncategorized = new Topic.TopicBuilder().parent(dirMenu).text("Uncategorized").icon(dirButtonIcon).hasSearch(true).tester(this).build();
        uncategorized.setTitled(false);
        uncategorized.setBlotOffset(0);
        uncategorized.setBackground(topicColor);

        RoundedButton addTopicButton = new RoundedButton();
        addTopicButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                File file = new File(root.getPath() + "/New topic");
                if(!file.exists()) {
                    file.mkdir();
                    addComponent(new Topic.TopicBuilder().text("New topic").hasSearch(true).build());

                    addButton.toggle(false);
                    reset();
                }
            }
        });
        addTopicButton.setHalfRect(true, false, false, true);

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
        addExamButton.setHalfRect(false, true, true, false);

        RoundedButton pasteButton = new RoundedButton();
        pasteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(paste()) {
                    addButton.toggle(false);
                }
            }
        });
        pasteButton.setHalfRect(true, true, false, false);

        addButton = new HamburgerMenu.HamburgerMenuBuilder().parent(dirMenu).icon(editorButtonIcon).build();
        addButton.setGrid(true);
        
        addButton.addComponent(addTopicButton);
        addButton.addComponent(addExamButton);
        addButton.addComponent(pasteButton);

        styleButton(addTopicButton, "Create new topic", dirButtonIcon);
        addTopicButton.setBackground(editColor);

        styleButton(addExamButton, "Create new exam", fileButtonIcon);
        addExamButton.setBackground(editColor);

        styleButton(pasteButton, "Paste", pasteIcon, JButton.RIGHT);
        pasteButton.setBackground(pasteColor);

        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 0.5;
        constraints.weighty = 0.5;
        constraints.insets = new Insets(0, margin * 2, margin * 2, margin * 2);

        this.setJMenuBar(menuBar);
        this.add(scrollPane, constraints);

        constraints.insets = new Insets(margin * 2, margin * 2, margin * 2, margin * 2);

        this.setVisible(true);
        this.pack();
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
        RoundedTextArea searchPanel = new RoundedTextArea(dirMenu);
        if(dirMenu.getBackground() != null) {
            searchPanel.setBackground(dirMenu.getBackground().darker());
        } else {
            searchPanel.setBackground(dirMenu.getBackground());
        }
        searchPanel.setPlaceholder("Search...");
        searchPanel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                    e.consume();
                    dirMenu.requestFocus();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if(e.getKeyCode() != KeyEvent.VK_ENTER) {
                    String text = searchPanel.getText();

                    search(dirMenu, text);

                    if(uncategorized.isEmpty()) {
                        uncategorized.setVisible(false);
                    } else if(uncategorized.getText().toLowerCase().contains(text.toLowerCase())) {
                        uncategorized.setVisible(true);
                    }
                }
            }
        });

        constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.weightx = 0.5;
        constraints.anchor = GridBagConstraints.NORTH;
        constraints.insets = new Insets(margin * 4, margin * 4, margin * 2, margin * 4);

        dirMenu.add(searchPanel, constraints);
        
        try {
            File[] files = dir.listFiles();

            if(files.length != 0) {
                for (final File f : files) {
                    if (f.isDirectory()) {
                        Topic dirTopic = new Topic.TopicBuilder().parent(dirMenu).text(f.getName()).icon(dirButtonIcon).hasSearch(true).tester(this).build();
                        dirTopic.setBackground(topicColor);

                        dirTopic.loadFiles(f, fileButtonIcon);

                        addComponent(dirTopic, GridBagConstraints.CENTER, 0, 0);
                    } else if(f.getParentFile().compareTo(root) == 0) {
                        uncategorized.loadFiles(f, fileButtonIcon);
                    }
                }
            }
            
            if(uncategorized.getMenuSize() > 0) {
                ((HamburgerMenu) uncategorized.getMenu().getComponents()[uncategorized.getMenu().getComponentCount() - 1]).setHalfRect(true, true, false, false);
            }
                
            addComponent(uncategorized);
            addComponent(addButton, GridBagConstraints.SOUTH);

            constraints = new GridBagConstraints();
            constraints.fill = GridBagConstraints.HORIZONTAL;
            constraints.gridx = 1;
            constraints.weightx = 0.5;
            constraints.weighty = 0.5;
            constraints.anchor = GridBagConstraints.SOUTH;
            constraints.insets = new Insets(margin, margin * 3, margin * 3, margin * 3);

            layout.setConstraints(addButton, constraints);
        } catch(Exception e) {
            RoundedLabel fail = new RoundedLabel("Failed to read directory!" + System.lineSeparator() + "If the directory is protected, try running " + name + " as administrator.");
            fail.setForeground(Color.WHITE);
            fail.setFont(buttonFont);

            addComponent(fail);

            consoleErrorMessage(e);
        }
    }

    private void addComponent(Component c, int anchor, double weightx, double weighty) {
        constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 1;
        constraints.weightx = weightx;
        constraints.weighty = weighty;
        constraints.anchor = anchor;
        constraints.insets = new Insets(margin, margin * 3, margin, margin * 3);

        dirMenu.add(c, constraints);
    }
    private void addComponent(Component c, int anchor) {
        addComponent(c, anchor, 0.5, 0.5);
    }
    private void addComponent(Component c) {
        addComponent(c, GridBagConstraints.NORTH);
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
    public Image getReturnButtonIcon() {
        return returnButtonIcon;
    }
    public Image getMinButtonIcon() {
        return minButtonIcon;
    }
    public Image getMaxButtonIcon() {
        return maxButtonIcon;
    }

    public Image getWqIcon() {
        return wqIcon;
    }

    public Image getMcIcon() {
        return mcIcon;
    }

    public Image getTfIcon() {
        return tfIcon;
    }

    public Image getEditIcon() {
        return editIcon;
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

    public Image getCopyIcon() {
        return copyIcon;
    }

    public Image getDeleteIcon() {
        return deleteIcon;
    }

    public Image getAddIcon() {
        return addIcon;
    }

    public Image getPasteIcon() {
        return pasteIcon;
    }

    public Image getResetIcon() {
        return resetIcon;
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
