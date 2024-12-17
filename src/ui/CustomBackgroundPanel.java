package ui;

import javax.swing.*;

import java.awt.*;

public class CustomBackgroundPanel extends JPanel {
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Set the background color to purple
        g.setColor(new Color(128, 0, 128));
        g.fillRect(0, 0, getWidth(), getHeight()); // Fill the panel with purple
    }
}
