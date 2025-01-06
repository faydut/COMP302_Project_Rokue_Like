package panel;


import javax.swing.*;

import enchantment.Enchantment;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import ui.GameManager;
import ui.ImageResizer;

public class InventoryPanel extends JPanel {
	private static final String inventory = "src/assets/rokue-like assets/Inventory.png";
	private JLabel timeLabel; // Label for displaying the remaining time
   // private JPanel pauseScreen;
    private boolean isPaused = false;
    private JFrame parentFrame; // Reference to the main game frame
    private GameManager gameManager; // Reference to the GameManager
    private JLabel heartLabel;
    private JLabel livesLabel;
    private ImageResizer imageResizer = new ImageResizer();
    private LinkedHashMap<String, ImageIcon> inventoryItems = new LinkedHashMap<>();
    private String luringGemPath = "src/assets/items/monster_necromancer.png";
    private String cloakOfProtectionPath = "src/assets/items/monster_elemental_air.png";
    private String revealPath = "src/assets/items/monster_elemental_plant.png";
   private  JLabel inventoryLabel ;
  // private JPanel slotPanel;
    //private String enchantmentName;
   
    public InventoryPanel(JFrame parentFrame, GameManager gameManager) {
        this.parentFrame = parentFrame;
        this.gameManager = gameManager; // Store the reference
        
        setLayout(null); // Use absolute layout
        setBackground(new Color(60, 40, 50)); // Set the dark background color
        setBounds(800, 50, 200, 570); // Position on the right side

        // Add Inventory UI Components
        addControlButtons();
        addTitle();
        initializeHearts();
        try {
        	addInventory();
        	addItemtoMap();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
        
    }

    

    private void initializeHearts() {
        // Create and scale the heart icon
        ImageIcon heartIcon = new ImageIcon("src/assets/rokue-like assets/heart.png");
        Image scaledHeart = heartIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        ImageIcon scaledHeartIcon = new ImageIcon(scaledHeart);

        // Create the heart label to display the heart icon
        heartLabel = new JLabel(scaledHeartIcon);
        heartLabel.setBounds(20, 90, 30, 30);
        add(heartLabel);

        // Create the lives label to display the number of lives
        livesLabel = new JLabel("x " + 3);
        livesLabel.setBounds(60, 90, 50, 30); // Adjust position next to the heart icon
        livesLabel.setFont(new Font("Arial", Font.BOLD, 20));
        livesLabel.setForeground(Color.WHITE);
        updateLives(3);
        
        
        
    }

    // Method to update the number of lives displayed
    public void updateLives(int live) {
    	
    	System.out.println("playerLives:"+live);
        livesLabel.setText("x " + live);

        add(livesLabel);
        
    }
    private void addItemtoMap() throws Exception {
    	BufferedImage resizedImage = imageResizer.convertImage(luringGemPath,24,24);
        ImageIcon luringIcon = new ImageIcon(resizedImage);
        inventoryItems.put("luring gem", luringIcon);
        
        
        BufferedImage resizedImage2 = imageResizer.convertImage(cloakOfProtectionPath,24,24);
        ImageIcon cloakIcon = new ImageIcon(resizedImage2);
        inventoryItems.put("cloak of protection", cloakIcon);
        
        BufferedImage resizedImage3 = imageResizer.convertImage(revealPath,24,24);
        ImageIcon revealIcon = new ImageIcon(resizedImage3);
        inventoryItems.put("reveal", revealIcon);
        
        
    	
    }
    public void addInventory() throws Exception {
    	BufferedImage resizedImage = imageResizer.convertImage(inventory,250,300);
        ImageIcon bufferedIcon = new ImageIcon(resizedImage);
        inventoryLabel = new JLabel(bufferedIcon);
        
        
        inventoryLabel.setForeground(Color.WHITE);
        inventoryLabel.setFont(new Font("Arial", Font.BOLD, 20));
        inventoryLabel.setBounds(-100, 120, 400, 400 );
        
        add(inventoryLabel);
    }

   
        
    
       
    public void addInventoryItems(LinkedHashMap<String, List<Enchantment>> enchantmentsMap) throws Exception {
    	clearInventoryLabel();

        int size = enchantmentsMap.size();
        JPanel slotPanel = new JPanel();
        slotPanel.setLayout(null);
        slotPanel.setOpaque(false);
        slotPanel.setBounds(-100, 120, 400, 400); // Adjust based on slots

        List<String> enchantmentNames = new ArrayList<>(enchantmentsMap.keySet());

        for (int i = 0; i < size; i++) {
            int count = enchantmentsMap.get(enchantmentNames.get(i)).size();
            JLabel slotLabel = new JLabel(inventoryItems.get(enchantmentNames.get(i)));
            int slotPanelWidth = 400; // Width of the slotPanel
            int slotPanelHeight = 400; // Height of the slotPanel
            int slotLabelWidth = 80; // Width of the slotLabel
            int slotLabelHeight = 80; // Height of the slotLabel

            // Calculate the centered position
            int x = ((slotPanelWidth - slotLabelWidth) / 2) + 50;
            int y = ((slotPanelHeight - slotLabelHeight) / 2) - 90;

            // Set the bounds for slotLabel
            slotLabel.setBounds(x + (i * 50), y, slotLabelWidth, slotLabelHeight);
            
            slotPanel.add(slotLabel);

            // Add count label if count > 1
            if (count > 1) {
                JLabel countLabel = new JLabel();
                countLabel.setText(null);
                countLabel.setText("x " + count);
                countLabel.setFont(new Font("Arial", Font.BOLD, 18));
                countLabel.setForeground(Color.WHITE); // White color for visibility
                countLabel.setBounds(235+(i*50), 50, 50, 20); // Position the count below the icon
                
                slotPanel.add(countLabel);
            }
        }

        // Add the slot panel to the inventory label
        inventoryLabel.add(slotPanel);
        inventoryLabel.revalidate();
        inventoryLabel.repaint();
    }
    public void clearInventoryLabel() {
        inventoryLabel.removeAll();  // Remove all components
        inventoryLabel.revalidate(); // Revalidate the container to reflect changes
        inventoryLabel.repaint();    // Repaint to update the UI
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

    
   
}
