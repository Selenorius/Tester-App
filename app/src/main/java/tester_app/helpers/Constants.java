package tester_app.helpers;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public final class Constants {
    private Constants() {}

    //ANSI COLORS
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";

    //APP DATA
    public static final String name = "Tester App";
    public static final String resDir = "app/src/main/resources/";
    public static File root = new File("topics");

    //OPTION BOOLEANS
    public static final List<String>
        mark_true = new ArrayList<String>(List.of("true", "yes", "t", "y")),
        mark_false = new ArrayList<String>(List.of("false", "no", "f", "n"));

    //TAB
    public static final String tab(int i) {
        String s = "";

        while(i > 0) {
            s += "   ";
            --i;
        }

        return s;
    }
    public static final String tab() { return "   "; }

    //READ COMMANDS
    public static final boolean quit(String s) {
        return s.equals("q");
    }
    public static final boolean back(String s) {
        return s.equals("r");
    }
}
