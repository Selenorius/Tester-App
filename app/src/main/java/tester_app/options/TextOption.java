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
        in.toLowerCase();

        return text.contains(in);
    }

    public void addText(String text) {
        this.text.add(text);
    }
}
