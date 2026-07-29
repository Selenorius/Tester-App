package tester_app;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.Scanner;

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

    public void startEditor() {
        new TextEditor(this);
    }

    public void start() {
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setTitle(name);
        this.setMinimumSize(size);
        this.setLayout(new FlowLayout());
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
}
