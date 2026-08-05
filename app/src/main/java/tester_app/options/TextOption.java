package tester_app.options;

import java.util.ArrayList;

import tester_app.helpers.RoundedPanel;

public class TextOption extends RoundedPanel {
    ArrayList<String> text;

    // CONSTRUCTORS
    public TextOption() {
        text = new ArrayList<>();
    }

     public boolean isTrue(String in) {
        if(in != null && !in.isBlank()) {
            in.toLowerCase();
            in = in.substring(0, in.length() - 1);

            for(String s : text) {
                if(s.equals(in)) {
                    return true;
                }
            }
        }

        return false;
    }

    public void reverseText() {
        text = new ArrayList<>(text.reversed());
    }

    public void addText(String text) {
        String out = text.toLowerCase();

        this.text.add(out);
    }

    // GETTERS
    public ArrayList<String> getText() {
        return text;
    }
}
