package tester_app.options;

import tester_app.helpers.RoundedButton;

public class ButtonOption extends RoundedButton {
    String text;
    boolean value;

    // CONSTRUCTORS
    public ButtonOption() {
        super("Text missing....");
    }

    // GETTERS
    public boolean isTrue() {
        return value;
    }
    public String getText() {
        return text;
    }

    // SETTERS
    public void setText(String text) {
        this.text = text;
    }
    public void setValue(boolean value) {
        this.value = value;
    }
}
