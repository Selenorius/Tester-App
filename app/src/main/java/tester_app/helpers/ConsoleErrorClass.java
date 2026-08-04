package tester_app.helpers;

import static tester_app.helpers.Constants.ANSI_PURPLE;
import static tester_app.helpers.Constants.ANSI_RED;
import static tester_app.helpers.Constants.ANSI_RESET;
import static tester_app.helpers.Constants.tab;

public abstract class ConsoleErrorClass implements ConsoleErrorMessage {
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
    public void consoleErrorMessage(Exception... exception) {
        System.out.println(ANSI_PURPLE + this.getClass().getName());
        System.out.print(tab() + ANSI_RED);
        for(Exception e : exception) {
            e.printStackTrace();
        }
        System.out.println(ANSI_RESET);
    }
}
