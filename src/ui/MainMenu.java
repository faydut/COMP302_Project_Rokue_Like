package ui;
import javax.swing.*;
import java.awt.*;



public class MainMenu {

    private JFrame frame;
    private String rokuePath= "src/assets/rokue-like assets/Rokue-like logo 4.png";
    		

    public MainMenu() {
        // Initialize the main frame
        frame = new JFrame("Main Menu");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());
        
        
     


        // Background panel
        JPanel backgroundPanel = new JPanel();
        backgroundPanel.setBackground(new Color(50, 34, 40)); // Purple color
        backgroundPanel.setLayout(new BoxLayout(backgroundPanel, BoxLayout.Y_AXIS));
        frame.add(backgroundPanel);

        
        
        // "Welcome to the Game" message
        JLabel welcomeLabel = new JLabel("Welcome to the Game");
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 36));
        backgroundPanel.add(Box.createVerticalStrut(20)); // Spacing
        backgroundPanel.add(welcomeLabel);



 
        // Logo panel
        ImageIcon image = new ImageIcon(new ImageIcon(rokuePath).getImage().getScaledInstance(250, 250, Image.SCALE_SMOOTH));
        JLabel imageLabel = new JLabel(image);
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        backgroundPanel.add(Box.createVerticalStrut(10)); // Spacing
        backgroundPanel.add(imageLabel);



        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS)); // Vertical layout
        buttonPanel.setOpaque(false);

        JButton startButton = createPlayfulButton("START NEW GAME");
        JButton helpButton = createPlayfulButton("HELP");
        JButton exitButton = createPlayfulButton("EXIT");

        buttonPanel.add(Box.createVerticalStrut(20)); // Spacing
        buttonPanel.add(startButton);
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(helpButton);
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(exitButton);

        backgroundPanel.add(Box.createVerticalStrut(20)); // Spacing
        backgroundPanel.add(buttonPanel);


        
        // Add action listeners to buttons
        startButton.addActionListener(e -> {
            frame.dispose(); // Close the main menu
            launchGame();    // Start the game
        });
        helpButton.addActionListener(e -> showHelpDialog());


        exitButton.addActionListener(e -> System.exit(0)); // Exit the application

        // Display the frame
        frame.setLocationRelativeTo(null); // Center the frame
        frame.setVisible(true);
    }
    
    

    
    

    

    
    
 // Create a playful styled button
    private JButton createPlayfulButton(String text) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setBackground(new Color(255, 182, 193)); // Light pink background
        button.setForeground(Color.DARK_GRAY);
        button.setFont(new Font("Comic Sans MS", Font.BOLD, 18));
        button.setPreferredSize(new Dimension(300, 40));
        button.setMaximumSize(new Dimension(300, 40)); // To match "ROKUE-LIKE" title width
        button.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);


        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(255, 105, 180)); // Hot pink on hover
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(255, 182, 193)); // Light pink when not hovered
            }
        });

        return button;
    }

		        

		    
    // Launch the game
    private void launchGame() {
        SwingUtilities.invokeLater(() -> {
            BuildMode buildMode= new BuildMode();
        });
    }

    // Show Help Dialog
    private void showHelpDialog() {
        JOptionPane.showMessageDialog(
                frame,
                "Welcome to the Game!\n\n" +
                        "- Start a New Game: Launches the game with empty halls where you can design and place objects.\n" +
                        "- How to Play:\n" +
                        "   1. Navigate through the dungeon halls (Earth, Air, Water, Fire) to find the hidden runes.\n" +
                        "   2. Use the arrow keys to move the hero:\n" +
                        "      - Arrow Up: Move up.\n" +
                        "      - Arrow Down: Move down.\n" +
                        "      - Arrow Left: Move left.\n" +
                        "      - Arrow Right: Move right.\n" +
                        "   3. Click on objects to search for runes.\n" +
                        "   4. Avoid monsters that try to stop you:\n" +
                        "      - Archer: Shoots arrows if you're too close.\n" +
                        "      - Fighter: Attacks with a dagger when adjacent.\n" +
                        "      - Wizard: Teleports the rune to confuse you.\n" +
                        "   5. Collect enchantments to gain advantages:\n" +
                        "      - Extra Time: Adds time to complete the hall.\n" +
                        "      - Extra Life: Grants an additional life.\n" +
                        "      - Cloak of Protection: Makes you invisible to archers.\n" +
                        "      - Luring Gem: Distracts fighters.\n" +
                        "      - Reveal: Hints at the rune's location.\n" +
                        "- Hall Design Requirements:\n" +
                        "   - Hall of Earth: At least 6 objects.\n" +
                        "   - Hall of Air: At least 9 objects.\n" +
                        "   - Hall of Water: At least 13 objects.\n" +
                        "   - Hall of Fire: At least 17 objects.\n" +
                        "- Controls:\n" +
                        "   - Movement: Arrow keys (Up, Down, Left, Right).\n" +
                        "   - Pause: Press 'Pause' to stop and resume the game as needed.\n" +
                        "- Exit: Quits the game and returns to the main menu.\n\n" +
                        "Enjoy your adventure and good luck!",
                "Help",
                JOptionPane.INFORMATION_MESSAGE
        );

}

}