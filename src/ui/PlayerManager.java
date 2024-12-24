package ui;

import javax.swing.*;

import frames.GameFrame;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

public class PlayerManager {
    private  Player player;
    private final GameFrame gameFrame;
    private final Random random = new Random();
    private final int gridSize= 12;
    private final String empty= "empty";
    private ObjectOverlay objectOverlay= new ObjectOverlay();
    private final String playerPath= "src/assets/rokue-like assets/player.png";
    public  JLabel[][] gridLabels ;
    private final GameManager gameManager;
    public PlayerManager(GameManager gameManager, JLabel[][] gridLabels) {
    	this.gridLabels= gridLabels;
        this.gameManager = gameManager;
        
        this.gameFrame= gameManager.getGameFrame();
    }

    public Player getPlayer() {
        return player;
    }

    public Player placePlayerRandomly() {
        int playerRow; // Player's starting position
        int playerCol;

        // Generate initial random position
        playerRow = random.nextInt(gridSize - 2) + 1; 
        playerCol = random.nextInt(gridSize - 2) + 1; 
        System.out.println("playerRow randomly: " + playerRow);
        System.out.println("playerCol randomly: " + playerCol);

        // Initialize player with the initial random position
        player = new Player(playerRow, playerCol, gameFrame);
        player.setCol(playerCol);
        player.setRow(playerRow);

        JLabel playerLabel = gridLabels[playerRow][playerCol]; // Get the label for the initial position
        System.out.println("playerLabel.getName: " + playerLabel.getName());

        // Check if the label is not empty and update the position if needed
        while (!playerLabel.getName().equals(empty)) { 
            playerRow = random.nextInt(gridSize - 2) + 1; 
            playerCol = random.nextInt(gridSize - 2) + 1; 
            System.out.println("loop");

            // Update player position
            player.setCol(playerCol);
            player.setRow(playerRow);

            // Update playerLabel to the new random position
            playerLabel = gridLabels[playerRow][playerCol];
        }

        // Add the player overlay to the final valid position
        objectOverlay.addPlayerOverlay(playerLabel, playerPath);
        System.out.println("is here");
        return player;
    }

    public void addGameKeyListener(JFrame frame) {
        KeyListener gameKeyListener = new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_W -> movePlayer(-1, 0); // Move up
                    case KeyEvent.VK_A -> movePlayer(0, -1); // Move left
                    case KeyEvent.VK_S -> movePlayer(1, 0); // Move down
                    case KeyEvent.VK_D -> movePlayer(0, 1); // Move right
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {}

            @Override
            public void keyTyped(KeyEvent e) {}
        };

        frame.addKeyListener(gameKeyListener);
    }

 private void movePlayer(int rowChange, int colChange) {
    	
    	int playerRow=  player.getRow();
        int playerCol= player.getCol();
      
        int newRow = playerRow + rowChange;
        int newCol = playerCol + colChange;
        
       
        if(isEmpty(newRow,newCol)==true ||gridLabels[newRow][newCol].getName().equals("openDoor")  ) {
        	

              JLabel currentLabel = gridLabels[playerRow][playerCol];
              objectOverlay. revertToGround(currentLabel);
      
              JLabel newLabel = gridLabels[newRow][newCol];
              objectOverlay. addPlayerOverlay(newLabel,playerPath);
            
            player.setCol(newCol);
            player.setRow(newRow);
            currentLabel.setName("empty");
            System.out.println("newRow :"+newRow);
            System.out.println("newCol:"+newCol);
            System.out.println("get name :"+gridLabels[newRow][newCol].getName());
            
            CheckPlayerInDoor(newRow, newCol);   
            
           
        }
        
    }
 public void CheckPlayerInDoor(int newRow, int newCol) {
	 if ("openDoor".equals(gridLabels[newRow][newCol].getName())) {
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
 	JLabel cell= gridLabels[row][col];
 	
 	if(cell.getName()==	empty) {
 		
 		return true;
 	}
 	return false;
 	
 }
 
 
    
    
    
    

   
}
