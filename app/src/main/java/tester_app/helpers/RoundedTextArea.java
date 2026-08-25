package tester_app.helpers;

import static tester_app.helpers.Constants.margin;

import java.awt.Color;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTextArea;
import javax.swing.Timer;

public class RoundedTextArea extends JTextArea {
    private final Timer repaintTimer;
    
    public RoundedTextArea(String s) {
        super(s);

        this.setLineWrap(true);
        this.setWrapStyleWord(true);
        this.setBackground(null);
        this.setForeground(Color.GRAY);
        this.setCaretColor(Color.WHITE);
        this.setFont(null);
        this.setMargin(new Insets(margin * 2, margin * 2, margin * 2, margin * 2));
        this.setFocusable(false);
        this.setDoubleBuffered(true);

        repaintTimer = new Timer(50, e -> repaint());
        repaintTimer.setRepeats(false);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                repaintTimer.restart();
            }
        });

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                setFocusable(false);
                setForeground(Color.WHITE.darker());
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                setFocusable(true);
                setForeground(Color.WHITE);
            }
        });
    }

    @Override
    public void scrollRectToVisible(final Rectangle aRect) {}
}
