package tester_app.helpers;

import static tester_app.helpers.Constants.addMargin;
import static tester_app.helpers.Constants.backgroundColor;
import static tester_app.helpers.Constants.borderColor;
import static tester_app.helpers.Constants.buttonBackgroundColor;
import static tester_app.helpers.Constants.buttonBorderColor;
import static tester_app.helpers.Constants.margin;
import static tester_app.helpers.Constants.selectionColor;
import static tester_app.helpers.Constants.styleButton;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class HamburgerMenu extends RoundedPanel {
    private RoundedButton menuButton;
    private RoundedPanel menu;
    private GridBagLayout layout;
    private GridBagConstraints constraints;
    private int radius;

    public HamburgerMenu(HamburgerMenuBuilder builder) {
        super();

        this.radius = 10;
        layout = new GridBagLayout();
        constraints = new GridBagConstraints();

        this.setBackground(backgroundColor);
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
        styleButton(menuButton, builder.text, builder.icon, JButton.RIGHT);

        menu = new RoundedPanel();
        menu.setBackground(null);
        menu.setVisible(false);
        menu.setLayout(layout);

        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridx = 1;
        constraints.weightx = 0.5;
        constraints.weighty = 0.5;

        this.add(menuButton, constraints);
        this.add(menu, constraints);

        this.setVisible(true);
    }

    public void toggle() {
        if(isToggled()) {
            addMargin(this, 0);

            menuButton.setBackground(buttonBackgroundColor);
            menuButton.setrButtonBorderColor(buttonBorderColor);

            menu.setVisible(false);
        } else {
            addMargin(this, margin * 2);

            menuButton.setBackground(selectionColor);
            menuButton.setrButtonBorderColor(selectionColor);

            menu.setVisible(true);
        }
    }

    public Boolean isToggled() {
        return menu.isVisible();
    }

    public void clearMenu() {
        menu.removeAll();
    }
    
    public Boolean isEmpty() {
        return menu.getComponents().length == 0;
    }

    public void addComponent(Component c, int anchor) {
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridx = 1;
        constraints.weightx = 0.5;
        constraints.weighty = 0.5;
        constraints.anchor = anchor;

        menu.add(c, constraints);
    }
    public void addComponent(Component c) {
        addComponent(c, GridBagConstraints.CENTER);
    }

    @Override
    protected void paintComponent(Graphics g) {
        int
            width = getSize().width,
            height = getSize().height;

        if(isToggled()) {
            g.setColor(selectionColor);
        } else {
            g.setColor(getBackground());
        }
        g.fillRoundRect(margin, margin, width - margin * 2, height - margin * 2, radius, radius);
    }

    @Override
    protected void paintBorder(Graphics g) {
        int
            width = getSize().width,
            height = getSize().height;

        if(isToggled()) {
            g.setColor(selectionColor);
        } else {
            g.setColor(borderColor);
        }

        g.drawRoundRect(margin, margin, width - 1 - margin * 2, height - 1 - margin * 2, radius, radius);
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
