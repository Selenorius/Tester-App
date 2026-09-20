package tester_app.helpers;

import static tester_app.helpers.Constants.addMargin;
import static tester_app.helpers.Constants.backgroundColor;
import static tester_app.helpers.Constants.buttonFont;
import static tester_app.helpers.Constants.margin;
import static tester_app.helpers.Constants.selectionColor;
import static tester_app.helpers.Constants.textFont;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SwingConstants;

public class RoundedSpinner extends JSpinner {
    private RoundedSpinner.DefaultEditor editor;
    private GridBagLayout layout;
    private GridBagConstraints constraints;
    private RoundedLabel label;

    private Color borderColor;
    private int radius;
    private Boolean borderPainted;

    public RoundedSpinner(SpinnerModel model, String text) {
        super(model);

        this.borderPainted = false;

        style(text);
    }
    public RoundedSpinner(SpinnerModel model) {
        super(model);

        this.borderPainted = false;

        style();
    }
    public RoundedSpinner() {
        super();

        this.borderPainted = false;

        style();
    }

    public void style(String... text) {
        layout = new GridBagLayout();
        constraints = new GridBagConstraints();

        borderColor = backgroundColor.brighter();
        setBackground(backgroundColor);
        this.setDoubleBuffered(true);
        this.setOpaque(false);
        addMargin(this, margin);

        label = new RoundedLabel("", SwingConstants.LEFT);
        for(String s : text) {
            label.setText(s);
        }
        label.setForeground(Color.WHITE);
        label.setFont(buttonFont);
        
        editor = (RoundedSpinner.DefaultEditor) this.getEditor();
        editor.setBackground(backgroundColor);
        editor.setLayout(layout);

        constraints.fill =  GridBagConstraints.BOTH;
        constraints.weightx = 0.5;
        constraints.weighty = 0.5;
        constraints.insets  = new Insets(0, margin * 2, 0, margin * 2);

        editor.add(label, constraints);
        editor.getTextField().setBackground(backgroundColor);
        editor.getTextField().setFocusable(false);
        editor.getTextField().setForeground(Color.WHITE);
        editor.getTextField().setCaretColor(Color.WHITE);
        editor.getTextField().setFont(textFont);
        editor.getTextField().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                editor.getTextField().setFocusable(false);
                editor.getTextField().setForeground(Color.WHITE);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                editor.getTextField().setFocusable(true);
                editor.getTextField().setForeground(selectionColor);
            }
        });
        radius = 10;
    }

    // GETTERS
    public String getText() {
        return label.getText();
    }

    // SETTERS
    public void setText(String text) {
        label.setText(text);
    }

    public void setBackground(Color color) {
        super.setBackground(color);

        editor = (RoundedSpinner.DefaultEditor) this.getEditor();
        editor.setBackground(color);
        editor.getTextField().setBackground(color);
    }

    public void setBorderColor(Color color) {
        borderColor = color;
    }

    public void setBorderPainted(Boolean borderPainted) {
        this.borderPainted = borderPainted;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);

        g2.dispose();
    }

    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

        if(borderPainted) {
            g2.setColor(borderColor);
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
        }

        g2.dispose();
    }  
}
