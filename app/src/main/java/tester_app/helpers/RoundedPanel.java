package tester_app.helpers;

import static tester_app.helpers.Constants.addMargin;
import static tester_app.helpers.Constants.backgroundColor;
import static tester_app.helpers.Constants.margin;
import static tester_app.helpers.Constants.opacity;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JPanel;
import javax.swing.Scrollable;
import javax.swing.Timer;

public class RoundedPanel extends JPanel implements Scrollable {
    private int radius;
    private Boolean
        borderPaint,
        isTransparent;
    private Color borderColor;
    private final Timer repaintTimer;
    private final Image texture;

    public RoundedPanel(Image texture) {
        this.texture = texture;
        this.radius = 10;
        this.borderPaint = false;
        this.isTransparent = false;
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
            this.borderColor = getBackground().brighter().brighter().brighter();
        } else {
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
    }
    public RoundedPanel() {
        this.texture = null;
        this.radius = 10;
        this.borderPaint = false;
        this.isTransparent = false;
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
            this.borderColor = getBackground().brighter().brighter().brighter();
        } else {
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

    public void setTransparency(Boolean isTransparent) {
        this.isTransparent = isTransparent;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

        super.paintComponent(g2);
        
        if(texture != null) {
            int
                wStep = 32,
                hStep = 32,
                x = getVisibleRect().x,
                y = getVisibleRect().y,
                width = getVisibleRect().width,
                height = getVisibleRect().height;
            
            g2.setClip(new RoundRectangle2D.Double(x, y, width, height, radius, radius));

            for(int w = 0; w < getWidth(); w += wStep) {
                for(int h = 0; h < getHeight(); h += hStep) {
                    g2.drawImage(texture, w, h, null);
                }
            }
        } else {
            if(isTransparent) {
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0));
            } else {
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
            }
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
        }
    }

    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

        if(borderPaint) {
            g2.setColor(borderColor);

            if(texture != null) {
                int
                    x = getVisibleRect().x,
                    y = getVisibleRect().y,
                    width = getVisibleRect().width,
                    height = getVisibleRect().height;

                g2.drawRoundRect(x, y, width, height, radius, radius);
            } else {
                g2.drawRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            }
        }
    }

    public void setBorderPainted(Boolean val) {
        this.borderPaint = val;
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
