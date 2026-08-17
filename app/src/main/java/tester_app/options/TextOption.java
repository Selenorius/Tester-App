package tester_app.options;

import static tester_app.helpers.Constants.tab;

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
            in = in.toLowerCase();
            
            for(String s : text) {
                if(in.contains(s.toLowerCase())) {
                    return true;
                }
            }
        }

        return false;
    }

    public void addText(String text) {
        String out = text;

        this.text.add(out);
    }

    // GETTERS
    public String getText() {
        String out = "";

        for(String s : text) {
            if(s != null) {
                out += "- " + s + System.lineSeparator();
            }
        }

        return out;
    }

    public String getProperText() {
        String out = "";

        for(String s : text) {
            if(s != null) {
                out += tab(4) + "\"" + s + "\"" + System.lineSeparator();
            }
        }

        return out;
    }

    public String getClearText() {
        String out = "";
        Boolean once = true;

        for(String s : text) {
            if(s != null) {
                if(once) {
                    out += s;
                    once = false;
                } else {
                    out += System.lineSeparator() + s;
                }
            }
        }

        return out;
    }

    public ArrayList<String> getTexts() {
        return text;
    }
}
