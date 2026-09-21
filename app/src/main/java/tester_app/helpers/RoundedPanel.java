package tester_app.helpers;

import static tester_app.helpers.Constants.addMargin;
import static tester_app.helpers.Constants.backgroundColor;
import static tester_app.helpers.Constants.margin;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.Scrollable;

public class RoundedPanel extends JPanel implements Scrollable {
    private int radius;
    private float opacity;
    private Boolean
        borderPaint,
        isTransparent;
    private ArrayList<Boolean> isHalfRect;
    private Color borderColor;
    private final Image texture;

    public RoundedPanel(Image texture) {
        this.texture = texture;
        this.radius = 10;
        this.opacity = Constants.opacity;
        this.borderPaint = false;
        this.isTransparent = false;
        this.isHalfRect = new ArrayList<>();
        this.isHalfRect.add(false);
        this.isHalfRect.add(false);
        this.isHalfRect.add(false);
        this.isHalfRect.add(false);
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
    }
    public RoundedPanel() {
        this.texture = null;
        this.radius = 10;
        this.opacity = Constants.opacity;
        this.borderPaint = false;
        this.isTransparent = false;
        this.isHalfRect = new ArrayList<>();
        this.isHalfRect.add(false);
        this.isHalfRect.add(false);
        this.isHalfRect.add(false);
        this.isHalfRect.add(false);
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
    }

    // GETTERS
    public Color getBorderColor() {
        return borderColor;
    }

    public float getOpacity() {
        return opacity;
    }

    public ArrayList<Boolean> getIsHalfRect() {
        return isHalfRect;
    }

    // SETTERS
    public void setBorderColor(Color bordeColor) {
        this.borderColor = bordeColor;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public void setBorderPainted(Boolean val) {
        this.borderPaint = val;
    }

    public void setTransparency(Boolean isTransparent) {
        this.isTransparent = isTransparent;
    }

    public void setHalfRect(Boolean nw, Boolean ne, Boolean se, Boolean sw) {
        this.isHalfRect.clear();

        this.isHalfRect.add(nw);
        this.isHalfRect.add(ne);
        this.isHalfRect.add(se);
        this.isHalfRect.add(sw);
    }

    public void setOpacity(double opacity) {
        if((float) opacity <= 1 && (float) opacity > 0) this.opacity = (float) opacity;
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
        
            Area base = new Area(new RoundRectangle2D.Double(x, y, width, height, radius, radius));

            if(isHalfRect.get(0)) {
                Area cut = new Area(new Rectangle2D.Double(x, y, radius, radius));
                base.add(cut);
            }
            if(isHalfRect.get(1)) {
                Area cut = new Area(new Rectangle2D.Double(x + getWidth() - radius, y, radius, radius));
                base.add(cut);
            }
            if(isHalfRect.get(2)) {
                Area cut = new Area(new Rectangle2D.Double(x + getWidth() - radius, y + getHeight() - radius, radius, radius));
                base.add(cut);
            }
            if(isHalfRect.get(3)) {
                Area cut = new Area(new Rectangle2D.Double(x, y + getHeight() - radius, radius, radius));
                base.add(cut);
            }

            g2.setClip(base);

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
            
            int
                width = getSize().width,
                height = getSize().height;
        
            Area base = new Area(new RoundRectangle2D.Double(0, 0, width, height, radius, radius));

            if(isHalfRect.get(0)) {
                Area cut = new Area(new Rectangle2D.Double(0, 0, radius, radius));
                base.add(cut);
            }
            if(isHalfRect.get(1)) {
                Area cut = new Area(new Rectangle2D.Double(getWidth() - radius, 0, radius, radius));
                base.add(cut);
            }
            if(isHalfRect.get(2)) {
                Area cut = new Area(new Rectangle2D.Double(getWidth() - radius, getHeight() - radius, radius, radius));
                base.add(cut);
            }
            if(isHalfRect.get(3)) {
                Area cut = new Area(new Rectangle2D.Double(0, getHeight() - radius, radius, radius));
                base.add(cut);
            }

            g2.fill(base);
        }

        g2.dispose();
    }

    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

        if(borderPaint) {
            g2.setColor(borderColor);
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));

            if(texture != null) {
                int
                    x = getVisibleRect().x,
                    y = getVisibleRect().y,
                    width = getVisibleRect().width,
                    height = getVisibleRect().height;

                Area base = new Area(new RoundRectangle2D.Double(x, y, width, height, radius, radius));

                if(isHalfRect.get(0)) {
                    Area cut = new Area(new Rectangle2D.Double(x, y, radius, radius));
                    base.add(cut);
                }
                if(isHalfRect.get(1)) {
                    Area cut = new Area(new Rectangle2D.Double(x + getWidth() - radius, y, radius, radius));
                    base.add(cut);
                }
                if(isHalfRect.get(2)) {
                    Area cut = new Area(new Rectangle2D.Double(x + getWidth() - radius, y + getHeight() - radius, radius, radius));
                    base.add(cut);
                }
                if(isHalfRect.get(3)) {
                    Area cut = new Area(new Rectangle2D.Double(x, y + getHeight() - radius, radius, radius));
                    base.add(cut);
                }

                g2.draw(base);
            } else {
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
                
                int
                    width = getSize().width,
                    height = getSize().height;
            
                Area base = new Area(new RoundRectangle2D.Double(0, 0, width, height, radius, radius));

                if(isHalfRect.get(0)) {
                    Area cut = new Area(new Rectangle2D.Double(0, 0, radius, radius));
                    base.add(cut);
                }
                if(isHalfRect.get(1)) {
                    Area cut = new Area(new Rectangle2D.Double(getWidth() - radius, 0, radius, radius));
                    base.add(cut);
                }
                if(isHalfRect.get(2)) {
                    Area cut = new Area(new Rectangle2D.Double(getWidth() - radius, getHeight() - radius, radius, radius));
                    base.add(cut);
                }
                if(isHalfRect.get(3)) {
                    Area cut = new Area(new Rectangle2D.Double(0, getHeight() - radius, radius, radius));
                    base.add(cut);
                }

                g2.draw(base);
            }
        }

        g2.dispose();
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
