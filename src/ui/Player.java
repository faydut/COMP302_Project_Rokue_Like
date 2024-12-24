package ui;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import frames.GameFrame;

public class Player {
	private GameFrame frame;
    private int row, col;
    private int lives = 3;
    private boolean wearingCloak = false;
    private GameManager gameManager;

    
    public Player(int row, int col, GameFrame frame, GameManager gameManager) {
    	this.frame = frame;
        this.row = row;
        this.col = col;
        this.gameManager = gameManager;
    }

    public int getRow() {
        return row;
    }

    public int getLives() {
		return lives;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public void setCol(int col) {
		this.col = col;
	}

	public int getCol() {
        return col;
    }

    public boolean isWearingCloak() {
        return wearingCloak;
    }

    public void wearCloak() {
        this.wearingCloak = true;
    }

    public void loseLife() {
        lives--;
        System.out.println("Life lost! Remaining lives: " + lives);
        if (lives <= 0) {
            gameManager.gameOver("You Lose All Lives"); // Notify GameManager of game over
        }
    }

}
