package tester_app.helpers;

import static tester_app.helpers.Constants.ANSI_PURPLE;
import static tester_app.helpers.Constants.ANSI_RED;
import static tester_app.helpers.Constants.ANSI_RESET;
import static tester_app.helpers.Constants.tab;

import javax.swing.JFrame;

public abstract class ConsoleErrorJFrame extends JFrame implements ConsoleErrorMessage {
   public void consoleErrorMessage(String message) {
        System.out.println(ANSI_PURPLE + this.getClass().getName());
        System.out.print(ANSI_RED + tab());
        System.out.println(message);
        System.out.println(ANSI_RESET);
    }
}
