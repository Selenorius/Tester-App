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
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenuBar;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import tester_app.editors.TextEditor;
import tester_app.helpers.ConsoleErrorJFrame;
import tester_app.helpers.LoadIcon;

public class Tester extends ConsoleErrorJFrame {
    private final Dimension size = new Dimension(1280, 720);
    private JMenuBar dirMenu, fileMenu;
    private final Image
        icon = loadIcon("/tester_appx96.png"),
        dirButtonIcon = loadIcon("/dirButtonx32.png"),
        backButtonIcon = loadIcon("/backButtonx32.png"),
        fileButtonIcon = loadIcon("/fileButtonx32.png"),
        editorButtonIcon = loadIcon("/editorButtonx32.png");
    
    public Tester() {
        dirMenu = new JMenuBar();
        dirMenu.setBackground(backgroundColor);
        dirMenu.setBorderPainted(false);

        fileMenu = new JMenuBar();
        fileMenu.setBackground(backgroundColor);
        fileMenu.setBorderPainted(false);
    }

    //READ
    public int read() {
        System.out.print(ANSI_CYAN + "Your answer: " + ANSI_RESET);
        Scanner in = new Scanner(System.in);
        String s = in.nextLine();

        in.close();

        s.toLowerCase();
        s.trim();

        if(back(s)) {
            System.out.println();
            return 0;
        } else if(quit(s)) {
            System.out.println();
            return -1;
        }
        System.out.println();

        return 1;
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
                dirButton.setBackground(backgroundColor);
                dirButton.setIcon(new ImageIcon(dirButtonIcon));
                dirMenu.add(dirButton);
                loadDir(f);
            }
        }
    }

    private void dirButtonAction(final File dir) {
        dirMenu.setVisible(false);
        fileMenu.removeAll();

        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fileMenu.setVisible(false);
                dirMenu.setVisible(true);
            }
        });
        backButton.setBackground(backgroundColor);
        backButton.setIcon(new ImageIcon(backButtonIcon));
        fileMenu.add(backButton);

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
                fileButton.setBackground(backgroundColor);
                fileButton.setIcon(new ImageIcon(fileButtonIcon));
                fileMenu.add(fileButton);
            }
        }
        fileMenu.setVisible(true);
    }

    public void startEditor() {
        new TextEditor(this);
    }

    public void start() {
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

        this.add(dirMenu);

        fileMenu.setVisible(false);
        this.add(fileMenu);

        dirMenu.removeAll();
        JButton editorButton = new JButton("Editor");
        editorButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startEditor();
                dispose();
            }
        });
        editorButton.setBackground(backgroundColor);
        editorButton.setIcon(new ImageIcon(editorButtonIcon));
        dirMenu.add(editorButton);
        loadDir(root);

        this.setVisible(true);
    }
}
