package tester_app.helpers;

import static tester_app.helpers.Constants.addMargin;
import static tester_app.helpers.Constants.blotBackgroundColor;
import static tester_app.helpers.Constants.buttonFont;
import static tester_app.helpers.Constants.margin;
import static tester_app.helpers.Constants.search;
import static tester_app.helpers.Constants.styleButton;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

import javax.swing.SwingConstants;

import tester_app.Topic;

public class HamburgerMenu extends RoundedPanel implements Comparable<HamburgerMenu> {
    private RoundedButton
        menuButton,
        empty;
    private RoundedPanel
        menu,
        blot;
    private RoundedTextArea searchPanel;
    private Component parent;
    private GridBagLayout layout;
    private GridBagConstraints constraints;
    private RoundedLabel
        text,
        size;

    private int
        radius,
        menuSize,
        blotOffset,
        hPos;
    private Color
        selectionColor,
        borderColor;
    private String buttonText;
    private Boolean
        isGrid,
        hasSearch,
        borderPainted,
        nw,
        ne,
        se,
        sw;

    public HamburgerMenu(HamburgerMenuBuilder builder) {
        super();

        this.blotOffset = 0;
        this.menuSize = 0;
        this.radius = 10;
        this.isGrid = false;
        this.borderPainted = false;
        if(builder.hasSearch != null) {
            this.hasSearch = builder.hasSearch;
        } else {
            this.hasSearch = false;
        }
        this.selectionColor = Constants.selectionColor;
        if(builder.text != null) {
            this.buttonText = "   " + builder.text + "   ";
        }
        this.parent = builder.parent;

        layout = new GridBagLayout();
        constraints = new GridBagConstraints();
        
        this.setLayout(layout);
        this.setSize(this.getSize().width + margin, this.getSize().height + margin);
        this.setOpacity((float) 0.25);
        addMargin(this, 0);

        menuButton = new RoundedButton();
        menuButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                toggle();
            }
        });
        addMargin(menuButton, (int) (margin * 2.5), margin, (int) (margin * 2.5), margin);

        blot = new RoundedPanel();
        blot.setBackground(blotBackgroundColor);
        blot.setBorderColor(blotBackgroundColor.brighter().brighter().brighter());
        blot.setBorderPainted(false);
        blot.setRadius(4);
        blot.setOpacity(1);
        addMargin(blot, 0);

        size = new RoundedLabel("0");
        size.setForeground(Color.WHITE);
        size.setFont(buttonFont);
        blot.add(size);

        menu = new RoundedPanel();
        menu.setBackground(null);
        menu.setBorderPainted(false);
        menu.setVisible(false);
        menu.setLayout(layout);
        menu.setTransparency(true);
        addMargin(menu, 0, margin * -1, 0, margin * -1);

        empty = new RoundedButton();
        empty.setLayout(layout);

        text = new RoundedLabel("Nothing here...");
        text.setForeground(Color.WHITE);
        empty.add(text);

        constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.weightx = 2;
        constraints.weighty = 2;

        this.add(menu, constraints);
        addComponent(empty);

        constraints.fill = GridBagConstraints.NONE;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.NORTHEAST;
        constraints.insets = new Insets(0, 0, 0, 0);

        this.add(blot, constraints);

        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 0.5;
        constraints.weighty = 0.5;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new Insets(0, 0, 0, 0);

        this.add(menuButton, constraints);

        if(parent != null) {
            ((Container) parent).add(this);
        }

        if(builder.textHPos != null) {
            styleButton(menuButton, buttonText, builder.icon, builder.textHPos);
            this.hPos = builder.textHPos;
        } else {
            styleButton(menuButton, buttonText, builder.icon);
            this.hPos = SwingConstants.CENTER;
        }

        if(isEmpty()) {
            empty.setVisible(true);
        }

        constraints = new GridBagConstraints();

        if(builder.parent != null) {
            if(builder.parent.getBackground() != null) {
                int
                    red = builder.parent.getBackground().getRed(),
                    green = builder.parent.getBackground().getGreen(),
                    blue = builder.parent.getBackground().getBlue();

                this.setBackground(
                    new Color(
                        red - red / 4 > 0 ? red - red / 4 : 0,
                        green - green / 4 > 0 ? green - green / 4 : 0,
                        blue - blue / 4 > 0 ? blue - blue / 4 : 0
                    )
                );
                menuButton.setBackground(builder.parent.getBackground().brighter());
                empty.setBackground(builder.parent.getBackground().darker());
                empty.setSelectionColor(builder.parent.getBackground().darker());
            } else {
                this.setBackground(null);
                menuButton.setBackground(null);
                empty.setBackground(null);
                empty.setSelectionColor(null);
            }
        } else {
            this.setBackground(null);
            menuButton.setBackground(null);
            empty.setBackground(null);
            empty.setSelectionColor(null);
        }
        this.borderColor = getBackground();

        searchPanel = new RoundedTextArea(menu);
        searchPanel.setPlaceholder("Search...");
        searchPanel.setHalfRect(true, false, false, true);
        if(menuButton.getBackground() != null) {
            searchPanel.setBackground(menuButton.getBackground());
            searchPanel.setBorderColor(menuButton.getBackground());
        } else {
            searchPanel.setBackground(menuButton.getBackground());
            searchPanel.setBorderColor(menuButton.getBackground());
        }
        searchPanel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                    e.consume();
                    menu.requestFocus();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if(e.getKeyCode() != KeyEvent.VK_ENTER) {
                    search(menu, searchPanel.getText(), builder.hasTextSearch);
                }
            }
        });

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.NONE;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets((int) (margin * 0.5), 0, (int) (margin * 0.5), 0);

        menuButton.add(searchPanel, constraints);

        nw = menuButton.getHalfRect().get(0);
        ne = menuButton.getHalfRect().get(1);
        se = menuButton.getHalfRect().get(2);
        sw = menuButton.getHalfRect().get(3);

        searchPanel.setVisible(false);

        this.setVisible(true);
    }

    public void toggle() {
        if(isExtended()) {
            addMargin(this, 0);
            
            searchPanel.setVisible(false);
            menuButton.setHalfRect(nw, ne, se, sw);
            menuButton.setHorizontalTextPosition(hPos);
            menuButton.setHorizontalIconAlignment(SwingConstants.CENTER);
            menuButton.setIconTextGap(margin);
            menu.setVisible(false);
            blot.setVisible(true);
        } else {
            addMargin(this, 0);

            if(isEmpty()) {
                empty.setVisible(true);
            } else {
                empty.setVisible(false);
            }

            blot.setVisible(false);
            menu.setVisible(true);
            menuButton.setHorizontalIconAlignment(SwingConstants.LEFT);
            menuButton.setHorizontalTextPosition(SwingConstants.RIGHT);
            menuButton.setIconTextGap((int) (margin * 1.75));
            menuButton.setHalfRect(false, false, true, true);
            if(menuButton.getBackground() != null) {
                searchPanel.setBackground(menuButton.getBackground().darker());
                searchPanel.setBorderColor(menuButton.getBackground().brighter().brighter().brighter());
            } else {
                searchPanel.setBackground(menuButton.getBackground());
                searchPanel.setBorderColor(menuButton.getBackground());
            }
            if(hasSearch) {
                searchPanel.setVisible(true);
            }
        }
    }
    public void toggle(Boolean value) {
        if(value != isExtended()) {
            toggle();
        }
    }

    public Boolean isExtended() {
        return menu.isVisible();
    }

    public void clearMenu() {
        menu.removeAll();
    }
    
    public Boolean isEmpty() {
        return menu.getComponents().length <= 1;
    }

    public void addComponent(Component component, int anchor, int fill, double weightx, double weighty, int top, int left, int bottom, int right) {
        constraints.fill = fill;
        constraints.weightx = weightx;
        constraints.weighty = weighty;
        constraints.anchor = anchor;

        if(isGrid) {
            int menCount = 0;

            constraints.gridwidth = 1;
            constraints.anchor = GridBagConstraints.WEST;

            for(Component c : menu.getComponents()) {
                constraints.gridx = 2 - (menCount++ % 2);
                if(constraints.anchor == GridBagConstraints.WEST) {
                    constraints.anchor = GridBagConstraints.EAST;
                } else {
                    constraints.anchor = GridBagConstraints.WEST;
                }

                layout.setConstraints(c, constraints);
            }

            constraints.gridx = 2 - (getMenuSize() % 2);
            constraints.gridwidth = 3 - (getMenuSize() % 2);
        } else {
            constraints.gridwidth = 1;
            constraints.gridx = 1;
        }
        
        if(
            component.getClass() == RoundedButton.class ||
            component.getClass() == HamburgerMenu.class ||
            component.getClass() == Topic.class
        ) {
            constraints.insets = new Insets(top, left, bottom, margin + right);
        } else {
            constraints.insets = new Insets(margin + top, margin + left, margin + bottom, margin * 2 + right);
        }

        menu.add(component, constraints);

        menuSize = menu.getComponentCount() - 1 - blotOffset;
        size.setText("" + menuSize);

        menu.revalidate();
    }
    public void addComponent(Component c, int anchor, int fill, double weightx, double weighty, int insets) {
        addComponent(c, anchor, fill, weightx, weighty, insets, insets, insets, insets);
    }
    public void addComponent(Component c, int anchor, int fill, double weightx, double weighty) {
        addComponent(c, anchor, fill, weightx, weighty, 0);
    }
    public void addComponent(Component c, int anchor, int fill) {
        addComponent(c, anchor, fill, 0.5, 0.5);
    }
    public void addComponent(Component c, int anchor) {
        addComponent(c, anchor, GridBagConstraints.BOTH);
    }
    public void addComponent(Component c) {
        addComponent(c, GridBagConstraints.CENTER);
    }

    public void replace(int index, Component c) {
        if(index < menu.getComponents().length) {
            menu.getComponents()[index] = c;
        }
    }

    // GETTERS
    public Color getSelectionColor() {
        return selectionColor;
    }

    public Color getBorderColor() {
        return borderColor;
    }

    public RoundedPanel getMenu() {
        return menu;
    }

    public int getMenuSize() {
        return menu.getComponentCount();
    }

    public int getBlotOffset() {
        return blotOffset;
    }

    public String getText() {
        if(buttonText != null) {
            return buttonText.trim();
        }
        return buttonText;
    }

    public Boolean getIsGrid() {
        return isGrid;
    }

    // SETTERS
    public void setSelectionColor(Color selectionColor) {
        this.menuButton.setSelectionColor(selectionColor);
        this.selectionColor = selectionColor;
    }

    public void setBackground(Color backgroundColor) {
        super.setBackground(backgroundColor);
        if(this.menu != null) {
            this.menu.setBackground(backgroundColor);
        }
        if(backgroundColor != null && empty != null) {
            empty.setBackground(backgroundColor.darker());
            empty.setSelectionColor(backgroundColor.darker());
        } else if(empty != null) {
            empty.setBackground(backgroundColor);
            empty.setSelectionColor(backgroundColor);
        }
    }
    
    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
    }

    public void setButtonColor(Color buttonColor) {
        if(menuButton != null) {
            this.menuButton.setBackground(buttonColor);
            this.searchPanel.setBackground(buttonColor);
        }
    }
    
    public void setMenu(RoundedPanel menu) {
        this.menu = menu;
    }

    public void setBlotOffset(int offset) {
        this.blotOffset = offset;
    }

    public void setText(String text) {
        this.buttonText = "   " + text + "   ";
    }

    public void setGrid(Boolean isGrid) {
        this.isGrid = isGrid;
    }

    public void setBorderPainted(Boolean borderPainted) {
        this.borderPainted = borderPainted;
    }

    public void setButtonOpacity(double opacity) {
        if((float) opacity <= 1 && (float) opacity > 0) menuButton.setOpacity((float) opacity);
    }

    @Override
    public void setHalfRect(Boolean nw, Boolean ne, Boolean se, Boolean sw) {
        //super.setHalfRect(nw, ne, se, sw);
        menuButton.setHalfRect(nw, ne, se, sw);

        this.nw = menuButton.getHalfRect().get(0);
        this.ne = menuButton.getHalfRect().get(1);
        this.se = menuButton.getHalfRect().get(2);
        this.sw = menuButton.getHalfRect().get(3);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

        if(!isExtended()) {
            g2.setColor(null);
        } else {
            g2.setColor(getBackground());
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, getOpacity()));
        }

        int
            width = getSize().width,
            height = getSize().height;
        
        Area base = new Area(new RoundRectangle2D.Double(margin, margin, width - margin * 2, height - margin * 2, radius, radius));

        if(getIsHalfRect().get(0)) {
            Area cut = new Area(new Rectangle2D.Double(margin, margin, radius, radius));
            base.add(cut);
        }
        if(getIsHalfRect().get(1)) {
            Area cut = new Area(new Rectangle2D.Double(getWidth() - margin - radius, margin, radius, radius));
            base.add(cut);
        }
        if(getIsHalfRect().get(2)) {
            Area cut = new Area(new Rectangle2D.Double(getWidth() - margin - radius, getHeight() - margin - radius, radius, radius));
            base.add(cut);
        }
        if(getIsHalfRect().get(3)) {
            Area cut = new Area(new Rectangle2D.Double(margin, getHeight() - margin - radius, radius, radius));
            base.add(cut);
        }

        g2.fill(base);

        g2.dispose();
    }

    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

        if(isExtended() && borderPainted) {
            g2.setColor(borderColor);

            int
                width = getSize().width,
                height = getSize().height;
            
            Area base = new Area(new RoundRectangle2D.Double(margin, margin, width - 1 - margin * 2, height - 1 - margin * 2, radius, radius));

            if(getIsHalfRect().get(0)) {
                Area cut = new Area(new Rectangle2D.Double(margin, margin, radius, radius));
                base.add(cut);
            }
            if(getIsHalfRect().get(1)) {
                Area cut = new Area(new Rectangle2D.Double(getWidth() - margin - radius - 1, margin, radius, radius));
                base.add(cut);
            }
            if(getIsHalfRect().get(2)) {
                Area cut = new Area(new Rectangle2D.Double(getWidth() - margin - radius - 1, getHeight() - margin - radius - 1, radius, radius));
                base.add(cut);
            }
            if(getIsHalfRect().get(3)) {
                Area cut = new Area(new Rectangle2D.Double(margin, getHeight() - margin - radius - 1, radius, radius));
                base.add(cut);
            }

            g2.draw(base);
        }

        g2.dispose();
    }

    @Override
    public int compareTo(HamburgerMenu ham) {
        if(this.getText() == null) {
            return -999999;
        } else if(ham.getText() == null) {
            return 999999;
        }
        return this.getText().compareTo(ham.getText());
    }

    public static class HamburgerMenuBuilder {
        public String text;
        public Image icon;
        public Component parent;
        public Integer textHPos;
        public Boolean
            hasSearch,
            hasTextSearch;

        public HamburgerMenuBuilder text(String text) {
            this.text = text;
            return this;
        }

        public HamburgerMenuBuilder icon(Image icon) {
            this.icon = icon;
            return this;
        }

        public HamburgerMenuBuilder parent(Component parent) {
            this.parent = parent;
            return this;
        }

        public HamburgerMenuBuilder textHPos(Integer textHPos) {
            this.textHPos = textHPos;
            return this;
        }

        public HamburgerMenuBuilder hasSearch(Boolean hasSearch) {
            this.hasSearch = hasSearch;
            return this;
        }

        public HamburgerMenuBuilder hasTextSearch(Boolean hasTextSearch) {
            this.hasTextSearch = hasTextSearch;
            return this;
        }

        public HamburgerMenu build() {
            return new HamburgerMenu(this);
        }
    }
}
