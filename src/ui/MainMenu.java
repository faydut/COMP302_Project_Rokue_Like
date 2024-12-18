package ui;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenu {

    private JFrame frame;
    private String rokuePath= "src/assets/rokue-like assets/Rokue-like logo 4.png";
    		

    public MainMenu() {
        // Initialize the main frame
        frame = new JFrame("Main Menu");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 800);
        frame.setLayout(new BorderLayout());
        
        //frame.getContentPane().setBackground(); // Dark background color
        
        
        JPanel backgroundPanel = new JPanel();
        backgroundPanel.setBackground(new Color(50, 30, 60)); // Purple color
        backgroundPanel.setLayout(new BorderLayout()); // For arranging components
        frame.add(backgroundPanel);
        
        ImageIcon image = new ImageIcon(rokuePath);
        JLabel imageLabel = new JLabel(image);
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        backgroundPanel.add(imageLabel, BorderLayout.CENTER);
               
        
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(50, 30, 60)); // Match the purple background
        titlePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        
        JLabel titleLabel = new JLabel("Welcome to the Game");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);   
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        //titleLabel.setBounds(400, 50, 400, 40);
        titlePanel.add(titleLabel);
        backgroundPanel.add(titlePanel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10)); // Buttons side by side
        buttonPanel.setOpaque(false);
        
        // Add "Start a New Game" button
        JButton startButton = new JButton("Start a New Game");
       // startButton.setBounds(200, 600, 200, 50);
        buttonPanel.add(startButton);

        // Add "Help" button
        JButton helpButton = new JButton("Help");
       // helpButton.setBounds(500, 600, 200, 50);
        buttonPanel.add(helpButton);

        // Add "Exit" button
        JButton exitButton = new JButton("Exit");
       // exitButton.setBounds(800, 600, 200, 50);
        buttonPanel.add(exitButton);
        
        backgroundPanel.add(buttonPanel, BorderLayout.SOUTH);
        frame.setVisible(true);

        // Add action listeners to buttons
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose(); // Close the main menu
                launchGame();    // Start the game
            }
        });

        helpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showHelpDialog();
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0); // Exit the application
            }
        });

        // Display the frame
        frame.setLocationRelativeTo(null); // Center the frame
        frame.setVisible(true);
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
                "Welcome to the Game!\n\n- Start a New Game: Launches the game with empty halls.\n" +
                        "- Help: Shows this information.\n" +
                        "- Exit: Quits the game.",
                "Help",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}
