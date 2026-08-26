package tester_app.options;

import tester_app.helpers.RoundedButton;

public class ButtonOption extends RoundedButton {
    String
    text,
    imagePath;
    boolean value;

    // CONSTRUCTORS
    public ButtonOption() {
        super();
    }

    // GETTERS
    public boolean isTrue() {
        return value;
    }
    public String getText() {
        if(text == null) {
            return "";
        }
        return text;
    }
    public String getImagePath() {
        return imagePath;
    }

    // SETTERS
    public void setText(String text) {
        if(text == null) {
            this.text = "";
        } else {
            this.text = text;
        }
    }
    public void setValue(boolean value) {
        this.value = value;
    }
    public void setImagePath(String path) {
        this.imagePath = path;
    }
}
