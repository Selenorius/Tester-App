package tester_app.helpers;

import static tester_app.helpers.Constants.textFont;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class ExamTimer extends RoundedPanel {
    private long
        start,
        timer;
    private Boolean run;

    private RoundedLabel label;

    public ExamTimer() {
        super();

        run = true;
        start = System.currentTimeMillis();

        label  = new RoundedLabel();
        label.setForeground(Color.WHITE);
        label.setFont(textFont);

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

    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

        super.paintBorder(g2);
    }
}
