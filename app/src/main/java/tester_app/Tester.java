package tester_app;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import javax.swing.ImageIcon;

import tester_app.editors.TextEditor;
import tester_app.helpers.ConsoleErrorJFrame;

import static tester_app.helpers.Constants.*;

public class Tester extends ConsoleErrorJFrame {
    private final Dimension size = new Dimension(1280, 720);
    
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
        this.setVisible(true);
    }
}
