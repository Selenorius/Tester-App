package tester_app.helpers;

import static tester_app.helpers.Constants.addMargin;
import static tester_app.helpers.Constants.blotBackgroundColor;
import static tester_app.helpers.Constants.margin;
import static tester_app.helpers.Constants.styleButton;

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

import javax.swing.JLabel;

public class HamburgerMenu extends RoundedPanel implements Comparable<HamburgerMenu> {
    private RoundedButton
        menuButton,
        empty;
    private RoundedPanel
        menu,
        blot;
    private Component parent;
    private GridBagLayout layout;
    private GridBagConstraints constraints;
    private int
        radius,
        menuSize,
        blotOffset;
    private JLabel
        text,
        size;
    private Color
        buttonColor,
        selectionColor,
        borderColor;
    private String buttonText;
    private Boolean isGrid;

    public HamburgerMenu(HamburgerMenuBuilder builder) {
        super();

        this.blotOffset = 0;
        this.menuSize = 0;
        this.radius = 10;
        this.isGrid = false;
        this.selectionColor = Constants.selectionColor;
        if(builder.parent != null) {
            if(builder.parent.getBackground() != null) {
                this.borderColor = builder.parent.getBackground().brighter();
            }
        } else {
            this.borderColor = null;
        }
        if(builder.text != null) {
            this.buttonText = "   " + builder.text + "   ";
        }
        this.parent = builder.parent;

        layout = new GridBagLayout();
        constraints = new GridBagConstraints();

        this.setBorderPainted(false);
        this.setLayout(layout);
        this.setSize(this.getSize().width + margin, this.getSize().height + margin);
        addMargin(this, 0);

        text = new JLabel("Nothing here...");

        menuButton = new RoundedButton();
        menuButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                toggle();
            }
        });

        blot = new RoundedPanel();
        blot.setBackground(blotBackgroundColor);
        blot.setBorderColor(blotBackgroundColor.brighter());
        blot.setBorderPainted(true);
        blot.setRadius(4);
        addMargin(blot, 0);

        size  = new JLabel("0");
        size.setForeground(Color.WHITE);
        blot.add(size);

        menu = new RoundedPanel();
        menu.setBackground(null);
        menu.setBorderPainted(false);
        menu.setVisible(false);
        menu.setLayout(layout);
        addMargin(menu, 0);

        empty = new RoundedButton();
        empty.setLayout(layout);

        text.setForeground(Color.WHITE);
        empty.add(text);

        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridx = 2;
        constraints.weightx = 2;

        this.add(menu, constraints);
        addComponent(empty);

        constraints.fill = GridBagConstraints.NONE;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 0.001;
        constraints.weighty = 0.001;
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
        } else {
            styleButton(menuButton, buttonText, builder.icon);
        }

        if(isEmpty()) {
            empty.setVisible(true);
        }

        constraints = new GridBagConstraints();

        if(builder.parent != null) {
            if(builder.parent.getBackground() != null) {
                this.setBackground(builder.parent.getBackground().darker());
                menuButton.setBackground(builder.parent.getBackground().brighter());
                empty.setBackground(builder.parent.getBackground().darker());
                empty.setSelectionColor(builder.parent.getBackground().darker());
            } else {
                this.setBackground(null);
                empty.setBackground(null);
                empty.setSelectionColor(null);
            }
        } else {
            this.setBackground(null);
            empty.setBackground(null);
            empty.setSelectionColor(null);
        }

        buttonColor = menuButton.getBackground();

        this.setVisible(true);
    }

    public void toggle() {
        if(isExtended()) {
            addMargin(this, 0);

            menuButton.setSelectionColor(null);
            menuButton.setBackground(buttonColor);
            menuButton.setText(buttonText);
            menu.setVisible(false);
            blot.setVisible(true);
        } else {
            addMargin(this, margin * 3);

            if(isEmpty()) {
                empty.setVisible(true);
            } else {
                empty.setVisible(false);
            }

            blot.setVisible(false);
            menu.setVisible(true);
            if(menuButton.getButtonIcon() != null) {
                menuButton.setText("");
            }
            menuButton.setBackground(menu.getBackground());
            menuButton.setSelectionColor(menu.getBackground());
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

    public void addComponent(Component component, int anchor, int fill, double weighty) {
        constraints.fill = fill;
        if(isGrid) {
            int menCount = 0;

            constraints.gridwidth = 1;

            for(Component c : menu.getComponents()) {
                constraints.gridx = 2 - (menCount++ % 2);

                layout.setConstraints(c, constraints);
            }

            constraints.gridx = 2 - (getMenuSize() % 2);
            constraints.gridwidth = 3 - (getMenuSize() % 2);
        } else {
            constraints.gridwidth = 1;
            constraints.gridx = 1;
        }
        constraints.weightx = 0.5;
        constraints.weighty = weighty;
        constraints.anchor = anchor;
        if(component.getClass() == RoundedButton.class || component.getClass() == HamburgerMenu.class) {
            constraints.insets = new Insets(margin * 1, margin * 1, margin * 1, margin * 1);
        } else {
            constraints.insets = new Insets(margin * 2, margin * 2, margin * 2, margin * 2);
        }

        menu.add(component, constraints);

        menuSize = menu.getComponentCount() - 1 - blotOffset;
        size.setText("" + menuSize);

        menu.revalidate();
    }
    public void addComponent(Component c, int anchor, int fill) {
        addComponent(c, GridBagConstraints.CENTER, GridBagConstraints.BOTH, 0.5);
    }
    public void addComponent(Component c, int anchor) {
        addComponent(c, GridBagConstraints.CENTER, GridBagConstraints.BOTH);
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
        this.buttonColor = buttonColor;
        if(menuButton != null) {
            this.menuButton.setBackground(buttonColor);
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

    public void setIsGrid(Boolean isGrid) {
        this.isGrid = isGrid;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int
            width = getSize().width,
            height = getSize().height;

        if(!isExtended()) {
            g2.setColor(null);
            g2.fillRoundRect(margin, margin, width - margin * 2, height - margin * 2, radius, radius);
        } else {
            g2.setColor(getBackground());
            g2.fillRoundRect(margin, margin, width - margin * 2, height - margin * 2, radius, radius);
        }
    }

    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int
            width = getSize().width,
            height = getSize().height;

        if(isExtended()) {
            g2.setColor(borderColor);
            g2.drawRoundRect(margin, margin, width - 1 - margin * 2, height - 1 - margin * 2, radius, radius);
        }
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

        public HamburgerMenu build() {
            return new HamburgerMenu(this);
        }
    }
}
