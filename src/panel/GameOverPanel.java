package panel;

import java.awt.Color;
import java.awt.Font;
import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.BorderFactory;

import ui.BuildMode;
import ui.Cell;
import ui.GameManager;
import ui.MainMenu;
import java.util.ArrayList;

public class GameOverPanel {
    public void displayGameOverPanel(JFrame frame, GameManager manager, String message) {
        JDialog gameOverDialog = new JDialog(frame, "Game Over", true);
        gameOverDialog.setSize(600, 400);
        gameOverDialog.setLayout(null);
        gameOverDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        gameOverDialog.setLocationRelativeTo(frame);
        gameOverDialog.getContentPane().setBackground(new Color(40, 40, 40)); // Dark background

        JLabel gameOverTitle = new JLabel("GAME OVER", SwingConstants.CENTER);
        gameOverTitle.setFont(new Font("Arial", Font.BOLD, 48));
        gameOverTitle.setForeground(Color.RED);
        gameOverTitle.setBounds(100, 50, 400, 60);
        gameOverDialog.add(gameOverTitle);

        JLabel gameOverMessage = new JLabel(message, SwingConstants.CENTER);
        gameOverMessage.setFont(new Font("Arial", Font.BOLD, 24));
        gameOverMessage.setForeground(Color.WHITE);
        gameOverMessage.setBounds(50, 130, 500, 40);
        gameOverDialog.add(gameOverMessage);

        JButton retryButton = new JButton("RETRY");
        retryButton.setBounds(150, 220, 300, 50);
        retryButton.setFont(new Font("Arial", Font.BOLD, 24));
        retryButton.setBackground(new Color(220, 20, 20));
        retryButton.setForeground(Color.WHITE);
        retryButton.setFocusPainted(false);
        retryButton.setBorder(BorderFactory.createRaisedBevelBorder());
        retryButton.addActionListener(e -> {
            gameOverDialog.dispose();
            frame.dispose();
            new MainMenu(); // Return to main menu
        });
        gameOverDialog.add(retryButton);

        JButton exitButton = new JButton("EXIT");
        exitButton.setBounds(150, 290, 300, 50);
        exitButton.setFont(new Font("Arial", Font.BOLD, 24));
        exitButton.setBackground(new Color(40, 40, 40));
        exitButton.setForeground(Color.WHITE);
        exitButton.setFocusPainted(false);
        exitButton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        exitButton.addActionListener(e -> {
            System.exit(0); // Terminate the program
        });
        gameOverDialog.add(exitButton);

        retryButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                retryButton.setBackground(new Color(255, 30, 30));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                retryButton.setBackground(new Color(220, 20, 20));
            }
        });

        exitButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                exitButton.setBackground(new Color(60, 60, 60));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                exitButton.setBackground(new Color(40, 40, 40));
            }
        });

        gameOverDialog.setVisible(true);
    }
}
