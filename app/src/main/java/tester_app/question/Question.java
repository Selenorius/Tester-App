package tester_app.question;

import javax.swing.JTextField;

import tester_app.helpers.RoundedPanel;

public abstract class Question extends RoundedPanel {
    protected JTextField textField = new JTextField();
    protected RoundedPanel buttonMenu = new RoundedPanel();

    public void ask() {
        
    }
}
