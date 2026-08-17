package tester_app.helpers;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;

public class FrameResizeListener extends MouseAdapter {
    private enum State {
        TOP_LEFT,
        TOP_CENTER,
        TOP_RIGHT,
        CENTER_RIGHT,
        BOTTOM_RIGHT,
        BOTTOM_CENTER,
        BOTTOM_LEFT,
        CENTER_LEFT;
    }

    private final JFrame frame;
    private Point
        mouseDownCompCoords,
        frameCoords;
    private Dimension frameSize;
    private boolean hover = false;
    private State state;

    public FrameResizeListener(JFrame frame) {
        this.frame = frame;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if(hover && frame.getExtendedState() != JFrame.MAXIMIZED_BOTH) {
            Point currCoords = e.getPoint();
            Dimension currFrameSize = frame.getSize();
            int
                xZone = currFrameSize.width / 3,
                yZone = currFrameSize.height / 3;
            Rectangle
                TOP_LEFT = new Rectangle(0, 0, xZone, yZone),
                TOP_CENTER = new Rectangle(xZone, 0, xZone, yZone),
                TOP_RIGHT = new Rectangle(xZone * 2, 0, xZone, yZone),
                CENTER_RIGHT = new Rectangle(xZone * 2, yZone, xZone, yZone),
                BOTTOM_RIGHT = new Rectangle(xZone * 2, yZone * 2, xZone, yZone),
                BOTTOM_CENTER = new Rectangle(xZone, yZone * 2, xZone, yZone),
                BOTTOM_LEFT = new Rectangle(0, yZone * 2, xZone, yZone),
                CENTER_LEFT = new Rectangle(0, yZone, xZone, yZone);

            if(TOP_LEFT.contains(currCoords)) {
                frame.setCursor(new Cursor(Cursor.NW_RESIZE_CURSOR));
                state = State.TOP_LEFT;
            } else if(TOP_CENTER.contains(currCoords)) {
                frame.setCursor(new Cursor(Cursor.N_RESIZE_CURSOR));
                state = State.TOP_CENTER;
            } else if(TOP_RIGHT.contains(currCoords)) {
                frame.setCursor(new Cursor(Cursor.NE_RESIZE_CURSOR));
                state = State.TOP_RIGHT;
            } else if(CENTER_RIGHT.contains(currCoords)) {
                frame.setCursor(new Cursor(Cursor.E_RESIZE_CURSOR));
                state = State.CENTER_RIGHT;
            } else if(BOTTOM_RIGHT.contains(currCoords)) {
                frame.setCursor(new Cursor(Cursor.SE_RESIZE_CURSOR));
                state = State.BOTTOM_RIGHT;
            } else if(BOTTOM_CENTER.contains(currCoords)) {
                frame.setCursor(new Cursor(Cursor.S_RESIZE_CURSOR));
                state = State.BOTTOM_CENTER;
            } else if(BOTTOM_LEFT.contains(currCoords)) {
                frame.setCursor(new Cursor(Cursor.SW_RESIZE_CURSOR));
                state = State.BOTTOM_LEFT;
            } else if(CENTER_LEFT.contains(currCoords)) {
                frame.setCursor(new Cursor(Cursor.W_RESIZE_CURSOR));
                state = State.CENTER_LEFT;
            } else {
                frame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                state = null;
            }
        }
    }

    public void mouseReleased(MouseEvent e) {
        mouseDownCompCoords = null;
        frameSize = null;
        frameCoords = null;
    }

    public void mousePressed(MouseEvent e) {
        mouseDownCompCoords = e.getPoint();
        frameSize = frame.getSize();
        frameCoords = frame.getLocation();
    }

    public void mouseDragged(MouseEvent e) {
        if(frame.getExtendedState() != JFrame.MAXIMIZED_BOTH) {
            Point
                currCoords = e.getPoint(),
                currFrameCoords = e.getLocationOnScreen();
            int
                x = frameCoords.x,
                y = frameCoords.y,
                sizeX = frameSize.width,
                sizeY = frameSize.height;
            
            if(state != null) {
                if(state == State.TOP_LEFT) {
                    if(
                        (
                            frameSize.width > frame.getMinimumSize().width
                        ) || (
                            mouseDownCompCoords.x > currCoords.x
                        )
                    ) {
                        sizeX = frameSize.width - (currCoords.x - mouseDownCompCoords.x);

                        x = currFrameCoords.x - mouseDownCompCoords.x;
                    }
                    if(
                        (
                            frameSize.height > frame.getMinimumSize().height
                        ) || (
                            mouseDownCompCoords.y > currCoords.y
                        )
                    ) {
                        sizeY = frameSize.height - (currCoords.y - mouseDownCompCoords.y);

                        y = currFrameCoords.y - mouseDownCompCoords.y;
                    }

                    frameSize = new Dimension(sizeX, sizeY);
                } else if(state == State.TOP_CENTER) {
                    

                    if(
                        (
                            frameSize.height > frame.getMinimumSize().height
                        ) || (
                            mouseDownCompCoords.y > currCoords.y
                        )
                    ) {
                        sizeY = frameSize.height - (currCoords.y - mouseDownCompCoords.y);
                        
                        y = currFrameCoords.y - mouseDownCompCoords.y;
                    }

                    frameSize = new Dimension(sizeX, sizeY);
                } else if(state == State.TOP_RIGHT) {
                    if(
                        (
                            frameSize.height > frame.getMinimumSize().height
                        ) || (
                            mouseDownCompCoords.y > currCoords.y
                        )
                    ) {
                        sizeY = frameSize.height - (currCoords.y - mouseDownCompCoords.y);

                        y = currFrameCoords.y - mouseDownCompCoords.y;
                    }

                    frameSize = new Dimension(sizeX, sizeY);

                    sizeX = frameSize.width + (currCoords.x - mouseDownCompCoords.x);
                } else if(state == State.CENTER_RIGHT) {
                    sizeX = frameSize.width + (currCoords.x - mouseDownCompCoords.x);
                } else if(state == State.BOTTOM_RIGHT) {
                    sizeX = frameSize.width + (currCoords.x - mouseDownCompCoords.x);
                    sizeY = frameSize.height + (currCoords.y - mouseDownCompCoords.y);
                } else if(state == State.BOTTOM_CENTER) {
                    sizeY = frameSize.height + (currCoords.y - mouseDownCompCoords.y);
                } else if(state == State.BOTTOM_LEFT) {
                    if(
                        (
                            frameSize.width > frame.getMinimumSize().width
                        ) || (
                            mouseDownCompCoords.x > currCoords.x
                        )
                    ) {
                        sizeX = frameSize.width - (currCoords.x - mouseDownCompCoords.x);

                        x = currFrameCoords.x - mouseDownCompCoords.x;
                    }

                    frameSize = new Dimension(sizeX, sizeY);

                    sizeY = frameSize.height + (currCoords.y - mouseDownCompCoords.y);
                } else if(state == State.CENTER_LEFT) {
                    if(
                        (
                            frameSize.width > frame.getMinimumSize().width
                        ) || (
                            mouseDownCompCoords.x > currCoords.x
                        )
                    ) {
                        sizeX = frameSize.width - (currCoords.x - mouseDownCompCoords.x);
                        
                        x = currFrameCoords.x - mouseDownCompCoords.x;
                    }

                    frameSize = new Dimension(sizeX, sizeY);
                }
            }

            frame.setBounds(x, y, sizeX, sizeY);
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        hover = true;
    }

    @Override
    public void mouseExited(MouseEvent e) {
        hover = false;
        frame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }
}