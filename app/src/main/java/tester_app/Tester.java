package tester_app;

import static tester_app.helpers.Constants.ANSI_CYAN;
import static tester_app.helpers.Constants.ANSI_RESET;
import static tester_app.helpers.Constants.back;
import static tester_app.helpers.Constants.name;
import static tester_app.helpers.Constants.quit;
import static tester_app.helpers.Constants.resDir;
import static tester_app.helpers.Constants.root;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenuBar;

import tester_app.editors.TextEditor;
import tester_app.helpers.ConsoleErrorJFrame;

public class Tester extends ConsoleErrorJFrame {
    private final Dimension size = new Dimension(1280, 720);
    private JMenuBar buttonMenu;
    
    public Tester() {}

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

    private Image loadIcon(final String path) {
        try {
            BufferedImage source = ImageIO.read(new FileImageInputStream(new File(path)));
            if(source == null) {
                consoleErrorMessage(
                    "loadIcon",
                    "Source cannot be null",
                    "Source path: " + path
                );

                return new BufferedImage(64, 64, Image.SCALE_FAST);
            }

            Image icon = new ImageIcon(source).getImage();
            if(icon == null) {
                consoleErrorMessage(
                    "loadIcon",
                    "Image cannot be null",
                    "Image source: " + source
                );

                return new BufferedImage(64, 64, Image.SCALE_FAST);
            }

            return icon;
        } catch (Exception e) {
            consoleErrorMessage("loadIcon", e.getMessage());

            return new BufferedImage(64, 64, Image.SCALE_FAST);
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
                buttonMenu.add(dirButton);
                loadDir(f);
            }
        }
    }

    private void dirButtonAction(final File dir) {
        buttonMenu.setVisible(false);

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
                this.add(fileButton);
            }
        }
    }

    public void startEditor() {
        new TextEditor(this);
    }

    public void start() {
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setTitle(name);
        this.setIconImage(loadIcon(resDir + "tester_appx96.png"));
        this.setMinimumSize(size);
        this.setSize(size);
        this.setLayout(new FlowLayout());
        this.setLocationRelativeTo(null);

        //MEMORY LEAK HERE
        buttonMenu = new JMenuBar();
        loadDir(root);
        this.add(buttonMenu);

        this.setVisible(true);
    }
}
