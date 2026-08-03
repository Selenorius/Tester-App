package tester_app.options;

import java.util.ArrayList;
import java.util.List;

import tester_app.helpers.RoundedButton;

public class ButtonOption extends RoundedButton {
    String text, answer, tip;
    List<String> altText;
    boolean value;

    //CONSTRUCTORS
    public ButtonOption(String tex, String ans, String t, List<String> altTex, boolean val) {
        text = tex;
        answer = ans;
        tip = t;
        altText = altTex;
        value = val;
    }
    public ButtonOption(String tex, List<String> altTex, boolean val) {
        text = tex;
        altText = altTex;
        value = val;
    }
    public ButtonOption(String tex, String ans, String t, boolean val) {
        text = tex;
        answer = ans;
        tip = t;
        value = val;
    }
    public ButtonOption(String tex, boolean val) {
        text = tex;
        value = val;
    }
    public ButtonOption() {}

    //GETTERS
    String text() { return text; }
    List<String> allText() {
        List<String> allText = new ArrayList<>();
        
        if(altText != null) allText = altText;
        allText.add(text);

        return allText;
    }
    public boolean isTrue() { return value; }

    //  GETTERS
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
    
    //DISPLAYS
    void display() { if(text != null) System.out.println(text); }
    void answer() { if(answer != null) System.out.println(answer); }
    void tip() { if(tip != null) System.out.println(tip); }
}
