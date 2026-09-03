package tester_app.helpers;

import static tester_app.helpers.Constants.addMargin;
import static tester_app.helpers.Constants.backgroundColor;
import static tester_app.helpers.Constants.margin;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JPanel;
import javax.swing.Scrollable;
import javax.swing.Timer;

public class RoundedPanel extends JPanel implements Scrollable {
    private int
        radius,
        textureOffsetX,
        textureOffsetY;
    private Boolean
        borderPaint,
        textured;
    private Color borderColor;
    private final Timer repaintTimer;

    public RoundedPanel() {
        this.radius = 10;
        this.borderPaint = true;
        this.textured = false;
        this.textureOffsetX = 0;
        this.textureOffsetY = 0;
        this.setOpaque(false);
        if(this.getParent() != null) {
            if(this.getParent().getBackground() != null) {
                this.setBackground(this.getParent().getBackground().brighter());
            }
            else {
                this.setBackground(this.getParent().getBackground());
            }
        } else {
            this.setBackground(backgroundColor);
        }
        if(getBackground() != null) {
            this.borderColor = getBackground().brighter();
        }
        else {
            this.borderColor = Constants.borderColor;
        }
        this.setDoubleBuffered(true);
        addMargin(this, margin);

        repaintTimer = new Timer(50, e -> repaint());
        repaintTimer.setRepeats(false);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                repaintTimer.restart();
            }
        });
        
        setOpaque(false);
    }

    // GETTERS
    public Color getBorderColor() {
        return borderColor;
    }

    // SETTERS
    public void setBorderColor(Color bordeColor) {
        this.borderColor = bordeColor;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public void setTextureOffsetX(int offset) {
        this.textureOffsetX = offset;
    }

    public void setTextureOffsetY(int offset) {
        this.textureOffsetY = offset;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);

        if(textured) {
            int
                hStep = 16,
                vStep = 16;

            g2.setColor(getBackground().brighter());

            for(int w = hStep - textureOffsetX; w < getWidth() - radius / 2 + 2; w += hStep) {
                for(int h = vStep - textureOffsetY; h < getHeight() - radius / 2 + 2; h += vStep) {
                    g2.drawLine(w, h, w - hStep, h);
                    g2.drawLine(w, h, w, h - vStep);
                    g2.drawLine(w, h, w + hStep, h);
                    g2.drawLine(w, h, w, h + vStep);
                }
            }
        }
    }

    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if(borderPaint) {
            g2.setColor(borderColor);
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
        }
    }

    public void setBorderPainted(Boolean val) {
        this.borderPaint = val;
    }

    public void setTextured(Boolean val) {
        this.textured = val;
    }

    @Override
    public Dimension getPreferredScrollableViewportSize() {
        return this.getPreferredSize();
    }

    @Override
    public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
        return 4;
    }

    @Override
    public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
        return 1;
    }

    @Override
    public boolean getScrollableTracksViewportWidth() {
        return true;
    }

    @Override
    public boolean getScrollableTracksViewportHeight() {
        if(this.getMinimumSize().height < this.getParent().getSize().height) {
            return true;
        }
        return false;
    }
}
