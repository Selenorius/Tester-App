package tester_app.helpers;

import static tester_app.helpers.Constants.addMargin;
import static tester_app.helpers.Constants.examAddBorderColor;
import static tester_app.helpers.Constants.fieldColor;
import static tester_app.helpers.Constants.margin;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;

public class RoundedSpinner extends JSpinner {
    private Color borderColor;
    private int radius;
    private RoundedSpinner.DefaultEditor editor;
    private GridBagLayout layout;
    private GridBagConstraints constraints;
    private JLabel label;

    public RoundedSpinner(SpinnerModel model, String text) {
        super(model);

        layout = new GridBagLayout();
        constraints = new GridBagConstraints();

        borderColor = examAddBorderColor;
        setBackground(fieldColor);
        this.setDoubleBuffered(true);
        addMargin(this, margin);

        label = new JLabel(text);
        label.setForeground(Color.WHITE);
        
        editor = (RoundedSpinner.DefaultEditor) this.getEditor();
        editor.setBackground(fieldColor);
        editor.setLayout(layout);

        constraints.fill =  GridBagConstraints.BOTH;
        constraints.weightx = 0.5;
        constraints.weighty = 0.5;
        constraints.insets  = new Insets(0, margin * 2, 0, margin * 2);

        editor.add(label, constraints);
        editor.getTextField().setBackground(fieldColor);
        editor.getTextField().setFocusable(false);
        editor.getTextField().setForeground(Color.WHITE.darker());
        editor.getTextField().setCaretColor(Color.WHITE);
        editor.getTextField().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                editor.getTextField().setFocusable(false);
                editor.getTextField().setForeground(Color.WHITE.darker());
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                editor.getTextField().setFocusable(true);
                editor.getTextField().setForeground(Color.WHITE);
            }
        });
        radius = 10;
    }
    public RoundedSpinner(SpinnerModel model) {
        super(model);

        layout = new GridBagLayout();
        constraints = new GridBagConstraints();

        borderColor = examAddBorderColor;
        setBackground(fieldColor);
        this.setDoubleBuffered(true);
        addMargin(this, margin);

        label = new JLabel();
        label.setForeground(Color.WHITE);
        
        editor = (RoundedSpinner.DefaultEditor) this.getEditor();
        editor.setBackground(fieldColor);
        editor.setLayout(layout);

        constraints.fill =  GridBagConstraints.BOTH;
        constraints.weightx = 0.5;
        constraints.weighty = 0.5;
        constraints.insets  = new Insets(0, margin * 2, 0, margin * 2);

        editor.getTextField().setBackground(fieldColor);
        editor.getTextField().setFocusable(false);
        editor.getTextField().setForeground(Color.WHITE.darker());
        editor.getTextField().setCaretColor(Color.WHITE);
        editor.getTextField().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                editor.getTextField().setFocusable(false);
                editor.getTextField().setForeground(Color.WHITE.darker());
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                editor.getTextField().setFocusable(true);
                editor.getTextField().setForeground(Color.WHITE);
            }
        });
        radius = 10;
    }
    public RoundedSpinner() {
        super();

        layout = new GridBagLayout();
        constraints = new GridBagConstraints();

        borderColor = examAddBorderColor;
        setBackground(fieldColor);
        this.setDoubleBuffered(true);
        addMargin(this, margin);

        label = new JLabel();
        label.setForeground(Color.WHITE);
        
        editor = (RoundedSpinner.DefaultEditor) this.getEditor();
        editor.setBackground(fieldColor);
        editor.setLayout(layout);

        constraints.fill =  GridBagConstraints.BOTH;
        constraints.weightx = 0.5;
        constraints.weighty = 0.5;
        constraints.insets  = new Insets(0, margin * 2, 0, margin * 2);

        editor.getTextField().setBackground(fieldColor);
        editor.getTextField().setFocusable(false);
        editor.getTextField().setForeground(Color.WHITE.darker());
        editor.getTextField().setCaretColor(Color.WHITE);
        editor.getTextField().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                editor.getTextField().setFocusable(false);
                editor.getTextField().setForeground(Color.WHITE.darker());
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                editor.getTextField().setFocusable(true);
                editor.getTextField().setForeground(Color.WHITE);
            }
        });
        radius = 10;
    }

    public void setText(String text) {
        label.setText(text);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
    }

    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        g2.setColor(borderColor);
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
    }  
}
