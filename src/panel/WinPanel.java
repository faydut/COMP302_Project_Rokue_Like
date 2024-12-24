package panel;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import ui.GameManager;

public class WinPanel {
	
	public  void displayWinPanel(JFrame frame, GameManager manager) {
        // Create a modal dialog
        JDialog winDialog = new JDialog(frame, "Congratulations!", true);
        winDialog.setSize(300, 200);
        winDialog.setLayout(null);
        winDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        winDialog.setLocationRelativeTo(frame); // Center on the main frame

        // Add a label for the win message
        JLabel winMessage = new JLabel("You win the game!", SwingConstants.CENTER);
        winMessage.setFont(new Font("Arial", Font.BOLD, 18));
        winMessage.setBounds(50, 30, 200, 30);
        winDialog.add(winMessage);

        // Add a "Start New Game" button
        JButton startNewGameButton = new JButton("Start New Game");
        startNewGameButton.setBounds(30, 100, 120, 30);
        startNewGameButton.setBackground(Color.red);
        startNewGameButton.addActionListener(e -> {
            winDialog.dispose(); // Close the dialog
            try {
                 manager.startGame(); // Restart the game
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        winDialog.add(startNewGameButton);

        // Add an "Exit" button
        JButton exitButton = new JButton("Exit");
        exitButton.setBounds(160, 100, 90, 30);
        exitButton.setBackground(Color.red);
        exitButton.addActionListener(e -> {
            System.exit(0); // Exit the application
        });
        winDialog.add(exitButton);

        // Show the dialog
        winDialog.setVisible(true);
    }


}
