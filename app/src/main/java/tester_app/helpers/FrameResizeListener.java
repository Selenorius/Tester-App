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
                xZone = currFrameSize.width / 6,
                yZone = currFrameSize.height / 6;
            Rectangle
                TOP_LEFT = new Rectangle(0, 0, xZone, yZone),
                TOP_CENTER = new Rectangle(xZone, 0, xZone * 4, yZone),
                TOP_RIGHT = new Rectangle(xZone * 5, 0, xZone, yZone),
                CENTER_RIGHT = new Rectangle(xZone * 5, yZone, xZone, yZone * 4),
                BOTTOM_RIGHT = new Rectangle(xZone * 5, yZone * 5, xZone, yZone),
                BOTTOM_CENTER = new Rectangle(xZone, yZone * 5, xZone * 4, yZone),
                BOTTOM_LEFT = new Rectangle(0, yZone * 5, xZone, yZone),
                CENTER_LEFT = new Rectangle(0, yZone, xZone, yZone * 4);

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
                currFrameCoords = e.getLocationOnScreen(),
                change = new Point(
                    currCoords.x - mouseDownCompCoords.x,
                    currCoords.y - mouseDownCompCoords.y
                );
            int
                x = frameCoords.x,
                y = frameCoords.y,
                sizeX = frameSize.width,
                sizeY = frameSize.height;

            if(state != null) {
                if(state == State.TOP_LEFT) {
                    if(frameSize.width - change.x > frame.getMinimumSize().width) {
                        sizeX -= change.x;
                        frameSize.width = sizeX;

                        x = currFrameCoords.x - mouseDownCompCoords.x;
                        frameCoords.x = x;
                    }
                    if(frameSize.height - change.y > frame.getMinimumSize().height) {
                        sizeY -= change.y;
                        frameSize.height = sizeY;

                        y = currFrameCoords.y - mouseDownCompCoords.y;
                        frameCoords.y = y;
                    }
                } else if(state == State.TOP_CENTER) {
                    if(frameSize.height - change.y > frame.getMinimumSize().height) {
                        sizeY -= change.y;
                        frameSize.height = sizeY;
                        
                        y = currFrameCoords.y - mouseDownCompCoords.y;
                        frameCoords.y = y;
                    }
                } else if(state == State.TOP_RIGHT) {
                    if(frameSize.height - change.y > frame.getMinimumSize().height) {
                        sizeY -= change.y;
                        frameSize.height = sizeY;

                        y = currFrameCoords.y - mouseDownCompCoords.y;
                        frameCoords.y = y;
                    }

                    sizeX += change.x;
                } else if(state == State.CENTER_RIGHT) {
                    sizeX += change.x;
                } else if(state == State.BOTTOM_RIGHT) {
                    sizeX += change.x;
                    sizeY += change.y;
                } else if(state == State.BOTTOM_CENTER) {
                    sizeY += change.y;
                } else if(state == State.BOTTOM_LEFT) {
                    if(frameSize.width - change.x > frame.getMinimumSize().width) {
                        sizeX -= change.x;
                        frameSize.width = sizeX;

                        x = currFrameCoords.x - mouseDownCompCoords.x;
                        frameCoords.x = x;
                    }

                    sizeY += currCoords.y - mouseDownCompCoords.y;
                } else if(state == State.CENTER_LEFT) {
                    if(frameSize.width - change.x > frame.getMinimumSize().width) {
                        sizeX -= change.x;
                        frameSize.width = sizeX;
                        
                        x = currFrameCoords.x - mouseDownCompCoords.x;
                        frameCoords.x = x;
                    }
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