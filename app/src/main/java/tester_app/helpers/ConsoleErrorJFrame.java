package tester_app.helpers;

import static tester_app.helpers.Constants.ANSI_PURPLE;
import static tester_app.helpers.Constants.ANSI_RED;
import static tester_app.helpers.Constants.ANSI_RESET;
import static tester_app.helpers.Constants.tab;

import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

import tester_app.Tester;

public abstract class ConsoleErrorJFrame extends JFrame implements ConsoleErrorMessage, LoadIcon {
    @Override
    public void consoleErrorMessage(String... message) {
        Boolean once = true;
        String out = "";
        for(String s : message) {
            if(once) {
                out += tab() + s;
                once = false;
            } else {
                out += System.lineSeparator() + tab() + s;
            }
        }

        System.out.println(ANSI_PURPLE + this.getClass().getName());
        System.out.print(ANSI_RED);
        System.out.println(out);
        System.out.println(ANSI_RESET);
    }

    @Override
    public Image loadIcon(final String name) {
        try {
            BufferedImage source = ImageIO.read(ConsoleErrorJFrame.class.getResource(name));
            if(source == null) {
                consoleErrorMessage(
                    "loadIcon",
                    "Source cannot be null",
                    "Source: " + name
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
}
