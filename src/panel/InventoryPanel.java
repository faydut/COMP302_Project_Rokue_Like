package panel;


import javax.swing.*;
import java.awt.*;
import ui.GameManager;

public class InventoryPanel extends JPanel {
	private static final String inventory = "src/assets/rokue-like assets/Inventory.png";
	private JLabel timeLabel; // Label for displaying the remaining time
    private JPanel pauseScreen;
    private boolean isPaused = false;
    private JFrame parentFrame; // Reference to the main game frame
    private GameManager gameManager; // Reference to the GameManager
   
    public InventoryPanel(JFrame parentFrame, GameManager gameManager) {
        this.parentFrame = parentFrame;
        this.gameManager = gameManager; // Store the reference
        setLayout(null); // Use absolute layout
        setBackground(new Color(60, 40, 50)); // Set the dark background color
        setBounds(800, 10, 200, 550); // Position on the right side

        // Add Inventory UI Components
        addControlButtons();
        addTitle();
        addHearts();
        addInventoryItems();
    }
    
    private void addControlButtons() {
        // Add Exit button
        JButton exitButton = new JButton();
        exitButton.setIcon(new ImageIcon("src/assets/items/exit16.png")); // Exit button image path
        exitButton.setBounds(20, 10, 32, 32);
        exitButton.setContentAreaFilled(false);
        exitButton.setBorderPainted(false);
        exitButton.setFocusPainted(false);
        exitButton.addActionListener(e -> System.exit(0));
        add(exitButton);

        // Add Pause button
        JButton pauseButton = new JButton();
        pauseButton.setIcon(new ImageIcon("src/assets/items/pause16.png")); // Pause button image path
        pauseButton.setBounds(60, 10, 32, 32);
        pauseButton.setContentAreaFilled(false);
        pauseButton.setBorderPainted(false);
        pauseButton.setFocusPainted(false);
        pauseButton.addActionListener(e -> showPauseScreen());
        add(pauseButton);
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
            ImageIcon heartIcon = new ImageIcon("src/assets/rokue-like assets/heart.png");
            Image scaledHeart = heartIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
            ImageIcon scaledHeartIcon = new ImageIcon(scaledHeart);

            JLabel heartLabel = new JLabel(scaledHeartIcon);
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
    
    private void showPauseScreen() {
        if (isPaused) return;

        isPaused = true;
        gameManager.pauseGame();

        JDialog pauseDialog = new JDialog(parentFrame, "Paused", true);
        pauseDialog.setSize(400, 400);
        pauseDialog.setLayout(new BorderLayout());
        pauseDialog.setLocationRelativeTo(parentFrame);

        JPanel panel = new JPanel();
        panel.setBackground(new Color(0, 0, 0, 200));
        panel.setLayout(new BorderLayout());

        JButton resumeButton = new JButton("Resume");
        resumeButton.setFont(new Font("Arial", Font.BOLD, 20));
        resumeButton.setFocusPainted(false);
        resumeButton.addActionListener(e -> {
            pauseDialog.dispose();
            isPaused = false;
            gameManager.resumeGame();
        });

        panel.add(resumeButton, BorderLayout.CENTER);
        pauseDialog.add(panel, BorderLayout.CENTER);

        pauseDialog.setVisible(true);
    }

    
    private void removePauseScreen() {
        remove(pauseScreen);
        revalidate();
        repaint();
        isPaused = false;
    }
}
