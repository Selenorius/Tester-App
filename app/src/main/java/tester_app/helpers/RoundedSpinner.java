package tester_app.helpers;

import static tester_app.helpers.Constants.examAddBorderColor;
import static tester_app.helpers.Constants.fieldColor;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JSpinner;
import javax.swing.SpinnerModel;

public class RoundedSpinner extends JSpinner {
    private boolean borderPaint;
    private Color borderColor;
    private int radius;
    private RoundedSpinner.DefaultEditor editor;

    public RoundedSpinner(SpinnerModel model) {
        super(model);

        editor = (JSpinner.DefaultEditor) this.getEditor();
        borderPaint = true;
        borderColor = examAddBorderColor;
        setBackground(fieldColor);
        setForeground(Color.WHITE);
        editor.getTextField().setText("Goal");
        editor.getTextField().setBackground(fieldColor);
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
        editor = (JSpinner.DefaultEditor) this.getEditor();
        borderPaint = true;
        borderColor = examAddBorderColor;
        setBackground(fieldColor);
        setForeground(Color.WHITE);
        editor.getTextField().setText("Goal");
        editor.getTextField().setBackground(fieldColor);
        editor.getTextField().setForeground(Color.WHITE);
        editor.getTextField().setCaretColor(Color.WHITE);
        radius = 10;
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
        
        if(borderPaint) {
            g2.setColor(borderColor);
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
        }
    }  
}
