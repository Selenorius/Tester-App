package tester_app.helpers;

import static tester_app.helpers.Constants.fieldColor;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JLabel;

public class ExamTimer extends RoundedPanel {
    private long
        start,
        timer;
    private Boolean run;

    private JLabel label;

    public ExamTimer() {
        super();

        if(this.getParent() != null) {
            if(this.getParent().getBackground() != null) {
                this.setBackground(this.getParent().getBackground().darker());
            }
            else {
                this.setBackground(this.getParent().getBackground());
            }
        } else {
            this.setBackground(fieldColor);
        }

        run = true;
        start = System.currentTimeMillis();

        label  = new JLabel();
        label.setForeground(Color.WHITE);

        this.add(label);
    }

    public String getTime() {
        long minutes = (timer / 1000) / 60;
        long seconds = (timer / 1000) % 60;

        if(seconds >= 10) {
            return (minutes + ":" + seconds);
        }
        return (minutes + ":0" + seconds);
    }

    public void stop() {
        run = false;
    }

    public void resume() {
        run = true;
    }

    public void restart() {
        start = System.currentTimeMillis();
        run = true;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if(run) {
            timer = System.currentTimeMillis() - start;

            long minutes = (timer / 1000) / 60;
            long seconds = (timer / 1000) % 60;

            if(seconds >= 10) {
                label.setText(minutes + ":" + seconds);
            } else {
                label.setText(minutes + ":0" + seconds);
            }

            repaint();
        }
    }
}
