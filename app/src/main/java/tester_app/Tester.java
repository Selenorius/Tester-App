package tester_app;

import static tester_app.helpers.Constants.ANSI_CYAN;
import static tester_app.helpers.Constants.ANSI_RESET;
import static tester_app.helpers.Constants.back;
import static tester_app.helpers.Constants.backgroundColor;
import static tester_app.helpers.Constants.name;
import static tester_app.helpers.Constants.quit;
import static tester_app.helpers.Constants.root;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;
import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import tester_app.editors.TextEditor;
import tester_app.helpers.ConsoleErrorJFrame;

public class Tester extends ConsoleErrorJFrame {
    private final Dimension size = new Dimension(1280, 720);
    private JScrollPane dirScrollPane, fileScrollPane;
    private JPanel dirMenu, fileMenu;
    private JButton backButton, editorButton;
    private final Image
        icon = loadIcon("/tester_appx96.png"),
        dirButtonIcon = loadIcon("/dirButtonx32.png"),
        backButtonIcon = loadIcon("/backButtonx32.png"),
        fileButtonIcon = loadIcon("/fileButtonx32.png"),
        editorButtonIcon = loadIcon("/editorButtonx32.png");
    private final Font
        buttonFont = new Font("Courier New", Font.PLAIN, 18);
    private String settingsFile = "settings.txt";
    
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

                dirScrollPane.setPreferredSize(testerSize);
                fileScrollPane.setPreferredSize(testerSize);
                dirMenu.setPreferredSize(testerSize);
                fileMenu.setPreferredSize(testerSize);
            }
        });
        this.addWindowStateListener(new WindowStateListener() {
            @Override
            public void windowStateChanged(WindowEvent e) {
                Dimension testerSize = getTesterSize();

                dirScrollPane.setPreferredSize(testerSize);
                fileScrollPane.setPreferredSize(testerSize);
                dirMenu.setPreferredSize(testerSize);
                fileMenu.setPreferredSize(testerSize);
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

        dirMenu = new JPanel();
        dirMenu.setBackground(backgroundColor);
        dirMenu.setLayout(new GridLayout());

        fileMenu = new JPanel();
        fileMenu.setBackground(backgroundColor);
        fileMenu.setLayout(new GridLayout());

        dirScrollPane = new JScrollPane(dirMenu);
        dirScrollPane.setSize(getTesterSize());
        dirScrollPane.setBackground(null);
        dirScrollPane.setBorder(null);
        dirScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        dirScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        fileScrollPane = new JScrollPane(fileMenu);
        fileScrollPane.setSize(getTesterSize());
        fileScrollPane.setBackground(null);
        fileScrollPane.setBorder(null);
        fileScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        fileScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        editorButton = new JButton("Editor");
        editorButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startEditor();
                dispose();
            }
        });
        styleButton(editorButton, editorButtonIcon);

        backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fileScrollPane.setVisible(false);
                backButton.setVisible(false);
                dirScrollPane.setVisible(true);
                editorButton.setVisible(true);
            }
        });
        styleButton(backButton, backButtonIcon);
        backButton.setVisible(false);

        this.add(dirScrollPane);
        this.add(fileScrollPane);
        this.add(editorButton);
        this.add(backButton);

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
        for (final File f : dir.listFiles()) {
            if (f.isDirectory()) {
                JButton dirButton = new JButton(f.getName());
                dirButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        dirButtonAction(f);
                    }
                });
                styleButton(dirButton, dirButtonIcon);
                dirMenu.add(dirButton);
                loadDir(f);
            }
        }
    }

    private void dirButtonAction(final File dir) {
        dirScrollPane.setVisible(false);
        editorButton.setVisible(false);
        backButton.setVisible(true);
        fileMenu.removeAll();

        for (final File f : dir.listFiles()) {
            if (!f.isDirectory()) {
                String
                    fileName = f.getName(),
                    examName;
                examName = fileName.substring(0, fileName.length() - 4);
                JButton fileButton = new JButton(examName);
                fileButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        System.out.println(examName);
                    }
                });
                styleButton(fileButton, fileButtonIcon);
                fileMenu.add(fileButton);
            }
        }
        fileScrollPane.setVisible(true);
    }

    private void styleButton(JButton button, Image buttonIcon) {
        button.setBackground(null);
        button.setFont(buttonFont);
        button.setIcon(new ImageIcon(buttonIcon));
    }

    private Dimension getTesterSize() {
        return new Dimension(this.getSize().width - 28, this.getSize().height - 72 - 23);
    }

    public void startEditor() {
        new TextEditor(this);
    }

    public void start() {
        dirMenu.removeAll();
        loadDir(root);

        fileScrollPane.setVisible(false);
        
        this.setVisible(true);
    }
}
