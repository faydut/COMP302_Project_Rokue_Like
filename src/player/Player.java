package player;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import frames.GameFrame;
import panel.InventoryPanel;
import ui.GameManager;

public class Player {
	private GameFrame frame;
    private int row, col;
    private int lives = 3;//
    private boolean wearingCloak = false;
    private GameManager gameManager;
    private InventoryPanel panel;

    
    public Player(int row, int col, GameFrame frame, GameManager gameManager) {
    	this.frame = frame;
        this.row = row;
        this.col = col;
        this.gameManager = gameManager;
        
        panel= gameManager.getInventoryPanel();
        
    }

    public void addLive(int live) {
    	lives+=live;
    	panel.updateLives(lives);
    }

    public void loseLife() {
        lives--;
        panel.updateLives(lives);
        
        if (lives <= 0) {
            gameManager.gameOver("You Lose All Lives"); // Notify GameManager of game over
        }
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

    public boolean getWearingCloak() {
        return wearingCloak;
    }

    public void setWearCloak() {
        this.wearingCloak = true;
    }


}
