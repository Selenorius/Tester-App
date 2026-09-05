package tester_app.options;

import tester_app.helpers.RoundedButton;

public class ButtonOption extends RoundedButton {
    String imagePath;
    boolean value;

    // CONSTRUCTORS
    public ButtonOption() {
        super();
    }

    // GETTERS
    public boolean isTrue() {
        return value;
    }
    public String getImagePath() {
        return imagePath;
    }

    // SETTERS
    @Override
    public void setText(String text) {
        super.setText(text);
    }
    public void setValue(boolean value) {
        this.value = value;
    }
    public void setImagePath(String path) {
        this.imagePath = path;
    }
}
