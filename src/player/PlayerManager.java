package player;

import javax.swing.*;

import frames.GameFrame;
import ui.Cell;
import ui.GameManager;
import ui.ObjectOverlay;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

public class PlayerManager {
    private  Player player;
    private final GameFrame gameFrame;
    private final Random random = new Random();
    private final int gridSize= 12;
   
    private ObjectOverlay objectOverlay= new ObjectOverlay();
    private final String playerPath= "src/assets/rokue-like assets/player.png";
    private ImageIcon playerIcon= new ImageIcon(playerPath);
    public  Cell[][] gridLabels ;
    private final GameManager gameManager;
    
    public PlayerManager(GameManager gameManager, Cell[][] gridLabels) {
    	this.gridLabels= gridLabels;
        this.gameManager = gameManager;
        this.gameFrame= gameManager.getGameFrame();
    }

    public Player getPlayer() {
        return player;
    }

    public Player placePlayerRandomly() throws Exception {
    	
        int playerRow; // Player's starting position
        int playerCol;

        // Generate initial random position
        playerRow = random.nextInt(gridSize - 2) + 1; 
        playerCol = random.nextInt(gridSize - 2) + 1; 
        System.out.println("playerRow randomly: " + playerRow);
        System.out.println("playerCol randomly: " + playerCol);

        // Initialize player with the initial random positio
       
        player = new Player(playerRow, playerCol, gameFrame, gameManager);
        player.setCol(playerCol);
        player.setRow(playerRow);

        Cell playerLabel = gridLabels[playerRow][playerCol]; // Get the label for the initial position
        System.out.println("playerLabel.isEmpty: " + playerLabel.getIsEmpty());
        
        // Check if the label is not empty and update the position if needed
        while (!playerLabel.getIsEmpty()) { 
            playerRow = random.nextInt(gridSize - 2) + 1; 
            playerCol = random.nextInt(gridSize - 2) + 1; 
            System.out.println("loop");

            // Update player position
            player.setCol(playerCol);
            player.setRow(playerRow);
           // playerLabel.setEmpty(false);
            // Update playerLabel to the new random position
            playerLabel = gridLabels[playerRow][playerCol];
        }

        // Add the player overlay to the final valid position
        
        objectOverlay.overlayLabel(playerLabel, playerIcon,32);
        System.out.println("is here");
        return player;
    }

    public void addGameKeyListener(JFrame frame) {
        KeyListener gameKeyListener = new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                try {
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_UP -> {
                            try {
                                movePlayer(-1, 0); // Move up
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                System.out.println("Error while moving player up: " + ex.getMessage());
                            }
                        }
                        case KeyEvent.VK_LEFT -> {
                            try {
                                movePlayer(0, -1); // Move left
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                System.out.println("Error while moving player left: " + ex.getMessage());
                            }
                        }
                        case KeyEvent.VK_DOWN -> {
                            try {
                                movePlayer(1, 0); // Move down
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                System.out.println("Error while moving player down: " + ex.getMessage());
                            }
                        }
                        case KeyEvent.VK_RIGHT -> {
                            try {
                                movePlayer(0, 1); // Move right
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                System.out.println("Error while moving player right: " + ex.getMessage());
                            }
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    System.out.println("Unexpected error in key handling: " + ex.getMessage());
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {}

            @Override
            public void keyTyped(KeyEvent e) {}
        };

        frame.addKeyListener(gameKeyListener);
    }

 private void movePlayer(int rowChange, int colChange) throws Exception {
	 if (gameManager.isPaused()) return;
    	
    	int playerRow=  player.getRow();
        int playerCol= player.getCol();
      
        int newRow = playerRow + rowChange;
        int newCol = playerCol + colChange;
        
       
        if(isEmpty(newRow,newCol)==true   ) {
        	

              Cell currentLabel = gridLabels[playerRow][playerCol];
              objectOverlay. revertToGround(currentLabel);
      
              Cell newLabel = gridLabels[newRow][newCol];
              newLabel.setEmpty(false);
              objectOverlay. overlayLabel(newLabel,playerIcon,32);
            
            player.setCol(newCol);
            player.setRow(newRow);
            currentLabel.setEmpty(true);
            
            
            CheckPlayerInDoor(newRow, newCol);   
            
           
        }
        
    }
 public void CheckPlayerInDoor(int newRow, int newCol) {
	 if ("door".equals(gridLabels[newRow][newCol].getName())) {
         System.out.println("Player reached the open door. Loading next hall...");
         gameManager.hallTimer.stop();
         gameManager. cleanHall();
    	    gridLabels[player.getRow()][player.getCol()].setIcon(null); 
    	    
         try {
        	 gameManager.loadNextHall();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} // Transition to the next hall
     }
	 

	 
 }
 private boolean isEmpty(int row, int col) {
 	//boolean empty= false;
 	Cell cell= gridLabels[row][col];
 	
 	if(cell.getIsEmpty()==true) {
 		
 		return true;
 	}
 	return false;
 	
 }
 
 
    
    
    
    

   
}
