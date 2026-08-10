package tester_app.helpers;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FrameResizeListener extends MouseAdapter {
    private final Component frame;
    private Point
        mouseDownCompCoords = null;
    private Dimension currSize = null;

    public FrameResizeListener(Component frame) {
        this.frame = frame;
    }

    public void mouseReleased(MouseEvent e) {
        mouseDownCompCoords = null;
        currSize = null;
    }

    public void mousePressed(MouseEvent e) {
        mouseDownCompCoords = e.getPoint();
        currSize = frame.getSize();
    }

    public void mouseDragged(MouseEvent e) {
        Point
            currCoords = e.getLocationOnScreen();
        int
            x = currCoords.x,
            y = currCoords.y;

        x = currSize.width + (currCoords.x - mouseDownCompCoords.x);

        y = currSize.height + (currCoords.y - mouseDownCompCoords.y);

        frame.setSize(x, y);
    }
}