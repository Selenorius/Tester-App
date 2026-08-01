package tester_app;

import static tester_app.helpers.Constants.addMargin;
import static tester_app.helpers.Constants.backgroundColor;
import static tester_app.helpers.Constants.fieldColor;
import static tester_app.helpers.Constants.margin;
import static tester_app.helpers.Constants.name;
import static tester_app.helpers.Constants.root;
import static tester_app.helpers.Constants.styleButton;

import java.awt.Dimension;
import java.awt.FlowLayout;
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

import javax.swing.BoxLayout;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import tester_app.editors.TextEditor;
import tester_app.helpers.ConsoleErrorJFrame;
import tester_app.helpers.RoundedButton;
import tester_app.helpers.RoundedPanel;

public class Tester extends ConsoleErrorJFrame {
    private final Dimension size = new Dimension(1280, 720);
    private JScrollPane scrollPane;
    private RoundedPanel dirMenu;
    private RoundedButton editorButton;
    private final Image
        icon = loadIcon("/tester_appx96.png"),
        dirButtonIcon = loadIcon("/dirButtonx32.png"),
        fileButtonIcon = loadIcon("/fileButtonx32.png"),
        editorButtonIcon = loadIcon("/editorButtonx32.png");
    private final String settingsFile = "settings.txt";
    
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

        dirMenu = new RoundedPanel();
        dirMenu.setBackground(fieldColor);
        addMargin(dirMenu, margin);
        dirMenu.setLayout(new BoxLayout(dirMenu, BoxLayout.Y_AXIS));

        scrollPane = new JScrollPane(dirMenu);
        scrollPane.getViewport().setBackground(backgroundColor);
        scrollPane.setBorder(null);
        scrollPane.setSize(getTesterSize());
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        editorButton = new RoundedButton();
        editorButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startEditor();
                dispose();
            }
        });
        styleButton(editorButton, "Editor", editorButtonIcon);

        this.add(scrollPane);
        this.add(editorButton);

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
                Topic dirTopic = new Topic(f.getName(), dirButtonIcon);
                dirTopic.loadFiles(f, fileButtonIcon);
                dirMenu.add(dirTopic);
                loadDir(f);
            }
        }
    }

    private Dimension getTesterSize() {
        return new Dimension(this.getSize().width - 28, this.getSize().height - 72 - 23 - 22);
    }

    public void startEditor() {
        new TextEditor(this);
    }

    public void start() {
        dirMenu.removeAll();
        loadDir(root);
        
        this.setVisible(true);
    }
}
