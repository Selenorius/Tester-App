package tester_app.helpers;

import static tester_app.helpers.Constants.addMargin;
import static tester_app.helpers.Constants.fieldColor;
import static tester_app.helpers.Constants.margin;
import static tester_app.helpers.Constants.styleButton;

import java.awt.Color;
import java.awt.Component;
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

public class HamburgerMenu extends RoundedPanel {
    private RoundedButton
        menuButton,
        empty;
    private RoundedPanel menu;
    private GridBagLayout layout;
    private GridBagConstraints constraints;
    private int radius;
    private JLabel text = new JLabel("Nothing here...");
    private Color
        selectionColor,
        borderColor;

    public HamburgerMenu(HamburgerMenuBuilder builder) {
        super();

        this.radius = 10;
        this.selectionColor = Constants.selectionColor;
        this.borderColor = null;

        layout = new GridBagLayout();
        constraints = new GridBagConstraints();

        this.setBackground(null);
        this.setBorderPainted(false);
        this.setLayout(layout);
        this.setSize(this.getSize().width + margin, this.getSize().height + margin);
        addMargin(this, 0);

        menuButton = new RoundedButton();
        menuButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                toggle();
            }
        });
        styleButton(menuButton, builder.text, builder.icon);

        menu = new RoundedPanel();
        menu.setBackground(null);
        menu.setBorderPainted(false);
        menu.setVisible(false);
        menu.setLayout(layout);
        addMargin(menu, 0);

        empty = new RoundedButton();
        empty.setBackground(fieldColor);
        empty.setBorderColor(fieldColor);
        empty.setSelectionColor(fieldColor);
        empty.setLayout(layout);

        text.setForeground(Color.WHITE);
        empty.add(text);

        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridx = 2;
        constraints.weightx = 2;
        this.add(menu, constraints);
        addComponent(empty);

        constraints.gridx = 1;
        constraints.weightx = 0.5;
        constraints.weighty = 0.5;
        constraints.insets = new Insets(0, 0, 0, 0);
        this.add(menuButton, constraints);

        if(isEmpty()) {
            empty.setVisible(true);
        }

        this.setVisible(true);
    }

    public void toggle() {
        if(isExtended()) {
            addMargin(this, 0);

            menu.setVisible(false);
        } else {
            addMargin(this, margin * 2);

            if(isEmpty()) {
                empty.setVisible(true);
            } else {
                empty.setVisible(false);
            }

            menu.setVisible(true);
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

    public void addComponent(Component c, int anchor, int fill, double weighty) {
        constraints.fill = fill;
        constraints.gridx = 1;
        constraints.weightx = 0.5;
        constraints.weighty = weighty;
        constraints.anchor = anchor;
        if(c.getClass() == RoundedButton.class || c.getClass() == HamburgerMenu.class) {
            constraints.insets = new Insets(0, 0, 0, 0);
        } else {
            constraints.insets = new Insets(margin, margin, margin, margin);
        }

        menu.add(c, constraints);
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

    // SETTERS
    public void setSelectionColor(Color selectionColor) {
        this.menuButton.setSelectionColor(selectionColor);
        this.selectionColor = selectionColor;
    }
    
    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
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

    public static class HamburgerMenuBuilder {
        protected String text;
        protected Image icon;

        public HamburgerMenuBuilder text(String text) {
            this.text = text;
            return this;
        }

        public HamburgerMenuBuilder icon(Image icon) {
            this.icon = icon;
            return this;
        }

        public HamburgerMenu build() {
            return new HamburgerMenu(this);
        }
    }
}
