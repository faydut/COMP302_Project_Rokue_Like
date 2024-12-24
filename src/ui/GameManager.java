package ui;

import javax.swing.*;

import frames.GameFrame;
import monster.MonsterSpawner;
import panel.HallPanel;
import panel.InventoryPanel;
import panel.WinPanel;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

public class GameManager  {
    private  final int gridSize = 12;
    private  final String rune = "src/assets/items/icons8-key-16.png";
    private  final String openDoor = "src/assets/items/door_open.png";
    private  final String empty = "empty";
    private String groundPath= "src/assets/items/floor_mud_e.png";
    //private  final String playerPath = "src/assets/rokue-like assets/player.png";
    Timer hallTimer; // Timer for countdown
    private int timeLeft;    // Remaining time for the current hall
    private int currentHallIndex = 0; // Tracks the current hall
	public  Player player;
	Random random = new Random();
    public  JLabel[][] gridLabels = new JLabel[gridSize][gridSize];   
    private InventoryPanel inventoryPanel;
    private  HallPanel  hallPanel;
    private MonsterSpawner spawner;
    private boolean isPaused = false; // Variable to track the game's pause state
   
    private  GameFrame gameFrame;
    public  ArrayList<JLabel >objectList= new ArrayList();
    private ArrayList<JLabel> doorList= new ArrayList();
    public  ArrayList<ImageIcon[][]> completedHalls;
    private String[] hallNames = {"Earth Hall", "Air Hall", "Water Hall", "Fire Hall"};
    private CreateImageIcon iconCreator = new CreateImageIcon(); 
    private ObjectOverlay objectOverlay = new ObjectOverlay();
    private WinPanel winPanel= new WinPanel();
    
    private PlayerManager playerManager = new PlayerManager(this,gridLabels);
    
    public GameFrame getGameFrame() {
		return gameFrame;
	}



	public GameManager(ArrayList<ImageIcon[][]> completedHalls ) {
    	this.completedHalls= completedHalls;
    }
    
    
    
    public void startGame() throws Exception {
    	
    	gameFrame = new GameFrame("Play Mode", 1200, 800, new Color(50, 34, 40));
        
        System.out.println("Completed halls: " + completedHalls.get(currentHallIndex));
        initializeHallPanel();
        initializeInventoryPanel();
        initializeUI();
        addRuneRandomly();
        player= playerManager.placePlayerRandomly();
        playerManager.addGameKeyListener(gameFrame);
        System.out.println("player is null or not:"+ player);
        System.out.println("player getPlayer:"+ getPlayer());
        spawner = new MonsterSpawner(getGridLabels(), getPlayer(), getObjectList(), this);
        spawner.startSpawning();  
        startTimerForCurrentHall();
      
        
       

        // Add all other components to the GameFrame
        gameFrame.addComponent(hallPanel.getHallTitleLabel());
        gameFrame.addComponent(hallPanel.getHallTitleLabel());
        gameFrame.addComponent(inventoryPanel);

        // Make the frame visible
        gameFrame.makeVisible();


    }
    
    private void initializeHallPanel() {
        hallPanel = new HallPanel(gridSize); // Create the HallPanel instance
        gameFrame.add(hallPanel); // Add the HallPanel directly to the frame
    }
    
    

    public  Player getPlayer() {
		return player;
	}



	public  JLabel[][] getGridLabels() {
		return gridLabels;
	}



	public  ArrayList<JLabel> getObjectList() {
		return objectList;
	}

	//PAUSE RESUME LOGIC

	public void pauseGame() {
	    if (hallTimer != null) {
	        hallTimer.stop();
	    }
	    if (spawner != null) {
	        spawner.pauseSpawning();
	    }
	    System.out.println("Game paused.");
	}

	public void resumeGame() {
	    if (hallTimer != null) {
	        hallTimer.start(); // Restart the hall timer
	    }
	    if (spawner != null) {
	        spawner.resumeSpawning(); // Resume monster spawning
	    }
//	    reinitializeKeyListener(); // Ensure the KeyListener is active
	    gameFrame.requestFocusInWindow(); // Regain focus
	    System.out.println("Game resumed.");
	}
	
	public boolean isPaused() {
        return isPaused;
    }

//komik
//	public void reinitializeKeyListener() {
//	    playerManager.addGameKeyListener(gameFrame); // Re-add the KeyListener to the frame
//	    System.out.println("KeyListener reinitialized.");
//	}

	//PAUSE RESUME LOGIC
	
	//lose life time logic
	public void gameOver(String message) {
	    if (hallTimer != null) {
	        hallTimer.stop(); // Stop the hall timer
	    }
	    if (spawner != null) {
	        spawner.stopSpawning(); // Stop monster spawning
	    }

	    JOptionPane.showMessageDialog(gameFrame, message, "Game Over", JOptionPane.INFORMATION_MESSAGE);
	    new MainMenu(); // Return to main menu
	    gameFrame.dispose(); // Close the game frame
	}

	//lose life time logic


	private void initializeInventoryPanel() {
	    inventoryPanel = new InventoryPanel(gameFrame, this); // Pass the GameManager instance
	    gameFrame.getContentPane().add(inventoryPanel);
	}

	
	private void updateHallTitle() {
	    String title = hallNames[currentHallIndex];
	    hallPanel.updateTitle(title);
	}
	
	private int countObjectsInCurrentHall() {
		ImageIcon[][] selectedHall = completedHalls.get(currentHallIndex);
	    int count = 0;

	    for (int i = 0; i < gridSize; i++) {
	        for (int j = 0; j < gridSize; j++) {
	            if (selectedHall[i][j].getDescription()=="object"){
	                count++;
	            }
	        }
	    }
	    return count;
	}

	
	private void initializeUI() throws Exception {
	    int cellSize = 32;

	    // Ensure there are completed halls available
	    if (completedHalls == null || completedHalls.isEmpty()) {
	        
	        return;
	    }

	    // Retrieve the current hall's icons
	    ImageIcon[][] selectedHall = completedHalls.get(currentHallIndex);

	    hallPanel.setLayout(new GridLayout(gridSize, gridSize)); // Use a grid layout for the hallPanel
	    hallPanel.setPreferredSize(new Dimension(gridSize * cellSize, gridSize * cellSize)); // Set the preferred size
	    hallPanel.setBounds(50, 50, gridSize * cellSize, gridSize * cellSize); // Set precise bounds

	    
	    
	    hallPanel.removeAll();

	    for (int i = 0; i < gridSize; i++) {
	        for (int j = 0; j < gridSize; j++) {
	        	
	        	
	            if (gridLabels[i][j] == null) {
	                gridLabels[i][j] = new JLabel();
	            }

	            JLabel cellLabel = gridLabels[i][j];
	            cellLabel.setOpaque(true);
	            cellLabel.setHorizontalAlignment(SwingConstants.CENTER);
	            cellLabel.setVerticalAlignment(SwingConstants.CENTER);
	            cellLabel.setPreferredSize(new Dimension(cellSize, cellSize)); // Set preferred size
	            cellLabel.setBounds(j * cellSize, i * cellSize, cellSize, cellSize);
	            //cellLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                
	            ImageIcon cellIcon = selectedHall[i][j];
	            
	            if(cellIcon.getDescription().equals("object")) {
	            	ImageIcon floorIcon= iconCreator.getImageIcon(groundPath);
	            	cellLabel.setIcon(floorIcon);
	            	objectOverlay.addObjectOverlay(cellLabel,cellIcon);
	            	
	            	cellLabel.setName("nonempty");
	            	objectList.add(cellLabel); 
	            	addMouseListener(i,j);
	            	
	            }
	            else if(cellIcon.getDescription().equals("floor")) {
	            	cellLabel.setIcon(cellIcon); // Set the icon for this cell
	            	cellLabel.setName(empty);

	            }
	            else if (cellIcon.getDescription().equals("wall")) {
	            	 cellLabel.setIcon(cellIcon);
	            	 cellLabel.setName("wall"); // 
	            }else if (cellIcon.getDescription().equals("door")) {
	            	cellLabel.setIcon(cellIcon);
                    cellLabel.setName("closeDoor"); 
                    doorList.add(cellLabel);
	            }
	            else {
	            	System.out.println("garip");
	            }
	            
	            
	            

	            // Add cell to the hall panel
	            hallPanel.add(cellLabel);
	        }
	    }


	    updateHallTitle();

	    // Refresh the hallPanel
	    hallPanel.revalidate();
	    hallPanel.repaint();

	    
	}

	private void startTimerForCurrentHall() {
	    // Calculate time based on the number of objects in the hall
	    int objectCount = countObjectsInCurrentHall();
	    timeLeft = objectCount * 5; // 5 seconds per object
	    
	    // Create a Swing Timer to update the countdown every second
	    inventoryPanel.updateTimeLabel(timeLeft);
	    hallTimer = new Timer(1000, e -> {
	        if (timeLeft > 0) {	
	            timeLeft--;
	            inventoryPanel.updateTimeLabel(timeLeft);
	            //System.out.println("Time Left: " + timeLeft + " seconds");
	        } else {
	            ((Timer) e.getSource()).stop();
	            JOptionPane.showMessageDialog(gameFrame, "Time's up! Game Over.", "Game Over", JOptionPane.ERROR_MESSAGE);
	            gameOver("Time's up! Game Over."); //new addition
	            new MainMenu();
	            gameFrame.dispose(); // End the game
	        }
	    });
	    hallTimer.start();
	}
	
	private void addRuneRandomly() {
		int length= objectList.size();
		System.out.println("length of object list:"+length);
		
		int num= random.nextInt(length);
		objectList.get(num).setName("rune");
		//System.out.println("rune in which object:"+objectList.get(num).getName());
		
	}

	
    private void addMouseListener(int row, int col) {
    	gridLabels[row][col].addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
            	if (e.getButton() == MouseEvent.BUTTON1) { // Check for right-click  button 1 for left click and button3 for right click
            		System.out.println("it is clicked");
            		try {
						handleCellClick(row, col);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
                   
                    gridLabels[row][col].removeMouseListener(this);
                }
            }
        });
    	
    }
    public void cleanHall() {
    	 // Remove all components from the hall panel
        hallPanel.removeAll();

        // Clear gridLabels and reset each JLabel
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                if (gridLabels[i][j] != null) {
                    gridLabels[i][j].setIcon(null); // Remove any icons
                    gridLabels[i][j].setName(null); // Reset the name
                    gridLabels[i][j].removeAll();  // Remove any overlays
                }
            }
        }

        // Clear the objectList and doorList
        objectList.clear();
        doorList.clear();

        // Revalidate and repaint the hallPanel to reflect changes
        hallPanel.revalidate();
        hallPanel.repaint();
    
    }
    
    public void loadNextHall() throws Exception {
    	
        
        if (currentHallIndex < completedHalls.size()-1) {
        	if (spawner != null) {
                spawner.stopSpawning();
            }
        	  
        	  currentHallIndex++;
              updateHallTitle();
        	  initializeUI();
        	  addRuneRandomly(); 
              player=  playerManager.placePlayerRandomly();    // Place the player randoml
              System.out.println("second get player:"+getPlayer());
              spawner = new MonsterSpawner(getGridLabels(), getPlayer(), getObjectList(), this);
              spawner.startSpawning();  
              startTimerForCurrentHall();
           
            
            
        } else {
        	if (spawner != null) {
                spawner.stopSpawning();
            }
            // Display the win panel
        	winPanel.displayWinPanel(gameFrame, this);
        }
    }
    private  JLabel findDoorLabel() {
    	return doorList.get(0);
    }
    
   
    

    private void handleCellClick(int row, int col) throws Exception {
    	int distance = Math.abs(player.getRow() - row) + Math.abs(player.getCol() - col);
        String RuneName =  gridLabels[row][col].getName();
        System.out.println("runeName:"+RuneName);
       
        // Check if the clicked cell hides the rune
        if(distance ==1) {
 
        	if (RuneName.equals("rune")) { // this is check for rune.
        		
                System.out.println("You found the hidden rune!");
                SoundPlayer.playSound("src/assets/sounds/door-opening.wav");
                JLabel runeLabel = gridLabels[row][col];
                objectOverlay.revertToGround(runeLabel);
               
                runeLabel.setName("nonempty");
                ImageIcon runeIcon= new ImageIcon(rune);
                objectOverlay.addObjectOverlay(runeLabel,runeIcon);
                
                 
                 ImageIcon icon = new ImageIcon(openDoor);
                 JLabel door= findDoorLabel();
                 ImageIcon floorIcon= iconCreator.getImageIcon(groundPath);
                 //door.setIcon(null);
                 door.setIcon(floorIcon);
                 door.setName("openDoor");
                 
                objectOverlay.addObjectOverlayForDoor(door, icon);
              
                 System.out.println("waitt");
                
             
              
                
                	 
                 
                 
                         
  } 
        	
        	
        	
        	
        }
        else {
            System.out.println("This object is empty.");
        } 
        

    
}
   

    
    
    

   


} 

    

    