package ui;

import javax.swing.*;
import java.awt.*;

public class HallPanel extends JPanel {
    private Image backgroundImage;

  

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }
}