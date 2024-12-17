package ui;


import javax.swing.*;
import java.awt.*;

public class InventoryPanel extends JPanel {
	private static final String inventory = "src/assets/rokue-like assets/Inventory.png";
	private JLabel timeLabel; // Label for displaying the remaining time
   
    public InventoryPanel() {
        setLayout(null); // Use absolute layout
        setBackground(new Color(60, 40, 50)); // Set the dark background color
        setBounds(800, 10, 200, 550); // Position on the right side

        // Add Inventory UI Components
        addTitle();
        addHearts();
        addInventoryItems();
    }

    private void addTitle() {
        timeLabel = new JLabel("Left Time: ");
        timeLabel.setForeground(Color.WHITE);
        timeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        timeLabel.setBounds(20, 50, 200, 30);
        add(timeLabel);
//        leftTimeLabel = new JLabel("Left Time:");
//        leftTimeLabel.setForeground(Color.WHITE);
//        leftTimeLabel.setFont(new Font("Arial", Font.BOLD, 16));
//        leftTimeLabel.setBounds(20, 20, 200, 30);
//        add(leftTimeLabel);
    }

    private void addHearts() {
        // Add 3 heart icons
        for (int i = 0; i < 3; i++) {
            JLabel heartLabel = new JLabel(new ImageIcon("src/assets/heart.png"));
            heartLabel.setBounds(20 + (i * 40), 90, 30, 30);
            add(heartLabel);
        }
    }

    private void addInventoryItems() {
    	ImageIcon image = new ImageIcon(inventory);
    	Image scaledImage = image.getImage().getScaledInstance(150, 300, Image.SCALE_SMOOTH);

        // Create a new ImageIcon for the resized image
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        JLabel inventoryLabel = new JLabel(scaledIcon);
        inventoryLabel.setForeground(Color.WHITE);
        inventoryLabel.setFont(new Font("Arial", Font.BOLD, 20));
        inventoryLabel.setBounds(-100, 120, 400, 400 );
        add(inventoryLabel);

        // Add inventory item slots
        int x = 20, y = 200, size = 50;
        for (int i = 0; i < 3; i++) {
            JLabel slot = new JLabel(new ImageIcon("src/assets/item" + (i + 1) + ".png"));
            slot.setBounds(x + (i * (size + 10)), y, size, size);
            add(slot);
        }
    }
    // Method to update the time label
    public void updateTimeLabel(int timeLeft) {
        timeLabel.setText("Time Left: " + timeLeft + " seconds");
    }
}
