package ui;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenu {

    private JFrame frame;

    public MainMenu() {
        // Initialize the main frame
        frame = new JFrame("Main Menu");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 800);
        frame.setLayout(null);
        frame.getContentPane().setBackground(new Color(50, 30, 60)); // Dark background color

        // Add title label
        JLabel titleLabel = new JLabel("Welcome to the Game");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setBounds(150, 50, 400, 40);
        frame.add(titleLabel);

        // Add "Start a New Game" button
        JButton startButton = new JButton("Start a New Game");
        startButton.setBounds(200, 120, 200, 50);
        frame.add(startButton);

        // Add "Help" button
        JButton helpButton = new JButton("Help");
        helpButton.setBounds(200, 190, 200, 50);
        frame.add(helpButton);

        // Add "Exit" button
        JButton exitButton = new JButton("Exit");
        exitButton.setBounds(200, 260, 200, 50);
        frame.add(exitButton);

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
