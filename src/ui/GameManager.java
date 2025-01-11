
package ui;

import javax.swing.*;

import enchantment.EnchantmentManager;
import frames.GameFrame;
import monster.MonsterSpawner;
import panel.GameOverPanel;
import panel.HallPanel;
import panel.InventoryPanel;
import panel.WinPanel;
import player.Player;
import player.PlayerManager;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;




public class GameManager  {
    private  final int gridSize = 12;
    private  final String rune = "src/assets/items/icons8-key-16.png";
    private  final String openDoor = "src/assets/items/door_open.png";
    private String wallPath= "src/assets/items/wall_gargoyle_red_1.png";
    private String groundPath= "src/assets/items/floor_mud_e.png";
    private  String closeDoorPath = "src/assets/items/door_closed.png";
    private GameOverPanel gameOverPanel = new GameOverPanel(); // Add this as a class field // importla birlikte bu

    public Timer hallTimer; // Timer for countdown
    public int timeLeft;  
	private int currentHallIndex = 0; // Tracks the current hall
	public  Player player;
	private Random random = new Random();
    public  Cell[][] gridLabels = new Cell[gridSize][gridSize];   
    private InventoryPanel inventoryPanel;
	private  HallPanel  hallPanel;
    private MonsterSpawner monsterSpawner;
    private boolean isPaused = false; // Variable to track the game's pause state
   
    private  GameFrame gameFrame;
    public  ArrayList<Cell >objectList= new ArrayList();
    private ArrayList<Cell> doorList= new ArrayList();
    public  ArrayList<Cell[][]> completedHalls;
    private String[] hallNames = {"Earth Hall", "Air Hall", "Water Hall", "Fire Hall"};
    private CreateImageIcon iconCreator = new CreateImageIcon(); 
    private ObjectOverlay objectOverlay = new ObjectOverlay();
    private WinPanel winPanel= new WinPanel();
    private EnchantmentManager enchantmentManager ;
    
    private MouseListener objectMouseListener;
    
    private PlayerManager playerManager = new PlayerManager(this,gridLabels);
    
   
    
    public void startGame() throws Exception {
    	
    	gameFrame = new GameFrame("Play Mode", 1200, 800, new Color(50, 34, 40));
    	gameFrame.requestFocusInWindow();
        
        System.out.println("Completed halls: " + completedHalls.get(currentHallIndex));
        initializeHallPanel();
        
        initializeUI();
        addRuneRandomly();
        initializeInventoryPanel();
        player= playerManager.placePlayerRandomly();
        playerManager.addGameKeyListener(gameFrame);
       
        monsterSpawner = new MonsterSpawner( getPlayer(), getObjectList(), this);
        monsterSpawner.startSpawning();  
       
        startTimerForCurrentHall();
        enchantmentManager = new EnchantmentManager(this, monsterSpawner.getFighterMonsters());
        
        enchantmentManager.startEnchantmentSpawning();
        enchantmentManager.addKeyListener(gameFrame);
      
        
       

        // Add all other components to the GameFrame
        gameFrame.addComponent(hallPanel.getHallTitleLabel());
        gameFrame.addComponent(hallPanel.getHallTitleLabel());
        gameFrame.addComponent(inventoryPanel);
       
        gameFrame.setVisible(true);
        gameFrame.makeVisible();


    }
    
    private void initializeHallPanel() {
        hallPanel = new HallPanel(gridSize); // Create the HallPanel instance
        gameFrame.add(hallPanel); // Add the HallPanel directly to the frame
    }
    
    public GameFrame getGameFrame() {
		return gameFrame;
	}



	public GameManager(ArrayList<Cell[][]> completedHalls ) {
    	this.completedHalls= completedHalls;
    }
    
    public PlayerManager getPlayerManager() {
        return playerManager;
    }
    
    

    public  Player getPlayer() {
		return player;
	}



	public  Cell[][] getGridLabels() {
		return gridLabels;
	}



	public  ArrayList<Cell> getObjectList() {
		return objectList;
	}

	//PAUSE RESUME LOGIC

	public void pauseGame() {
	    if (hallTimer != null) {
	        hallTimer.stop();
	    }
	    if (monsterSpawner != null) {
	    	monsterSpawner.pauseSpawning();
	    }
	    if (enchantmentManager != null) {
	    	enchantmentManager.pauseSpawning();
	    }
	    System.out.println("Game paused.");
	}

	public void resumeGame() {
	    if (hallTimer != null) {
	        hallTimer.start(); // Restart the hall timer
	    }
	    if (monsterSpawner != null) {
	    	monsterSpawner.resumeSpawning(); // Resume monster spawning
	    }
	    if (enchantmentManager != null) {
	    	enchantmentManager.resumeSpawning(); // Resume monster spawning
	    }

	    gameFrame.requestFocusInWindow(); // Regain focus
	    System.out.println("Game resumed.");
	}
	
	public boolean isPaused() {
        return isPaused;
    }


	public void gameOver(String message) {
	    if (hallTimer != null) {
	        hallTimer.stop(); // Stop the hall timer
	    }
	    if (monsterSpawner != null) {
	    	monsterSpawner.stopSpawning(); // Stop monster spawning
	    }
	    if (enchantmentManager != null) {
	    	enchantmentManager.stopSpawning(); // Stop monster spawning
	    }
	    gameOverPanel.displayGameOverPanel(gameFrame, this, message);

	}

	


	private void initializeInventoryPanel() {
		System.out.println("Initializing Inventory Panel");
	    inventoryPanel = new InventoryPanel(gameFrame, this); // Pass the GameManager instance
	    System.out.println("inventory panel in initializeInventoryPanel: "+inventoryPanel);
	    gameFrame.getContentPane().add(inventoryPanel);
	}
	public InventoryPanel getInventoryPanel() {
    	System.out.println("panel in gamemanager:"+inventoryPanel);
		return inventoryPanel;
	}
    

	
	
	private void updateHallTitle() {
	    String title = hallNames[currentHallIndex];
	    hallPanel.updateTitle(title);
	}
	
	private int countObjectsInCurrentHall() {
		Cell[][] selectedHall = completedHalls.get(currentHallIndex);
	    int count = 0;

	    for (int i = 0; i < gridSize; i++) {
	        for (int j = 0; j < gridSize; j++) {
	            if (selectedHall[i][j].getName()=="object"){
	                count++;
	            }
	        }
	    }
	    return count;
	}

	
	private void initializeUI() throws Exception {
	    int cellSize = 48;

	    // Ensure there are completed halls available
	    if (completedHalls == null || completedHalls.isEmpty()) {
	        return;
	    }

	   
	    Cell[][] selectedHall = completedHalls.get(currentHallIndex);
	    
	    hallPanel.setLayout(new GridLayout(gridSize, gridSize)); // Use a grid layout for the hallPanel
	    hallPanel.setPreferredSize(new Dimension(gridSize * cellSize, gridSize * cellSize)); // Set the preferred size
	    hallPanel.setBounds(70, 50, gridSize * cellSize, gridSize * cellSize); // Set precise bounds

	    hallPanel.removeAll();
	    ImageIcon floorIcon = iconCreator.getImageIcon(groundPath,cellSize,cellSize);
 	    ImageIcon wallIcon = iconCreator.getImageIcon(wallPath,cellSize,cellSize);
 	    ImageIcon doorIcon = iconCreator.getImageIcon(closeDoorPath,cellSize,cellSize);


	    for (int i = 0; i < gridSize; i++) {
	        for (int j = 0; j < gridSize; j++) {
	            Cell cell = selectedHall[i][j];
	            
	            gridLabels[i][j] = cell;
	            gridLabels[i][j].setBounds(j * cellSize, i * cellSize, cellSize, cellSize);
	            gridLabels[i][j].setOpaque(true);
	            gridLabels[i][j].setHorizontalAlignment(SwingConstants.CENTER);
	            gridLabels[i][j].setVerticalAlignment(SwingConstants.CENTER);
	            
	            
	            if("wall".equals(gridLabels[i][j].getName())){
	            	gridLabels[i][j].setIcon(wallIcon);
	            	
	            }
	            if("floor".equals(gridLabels[i][j].getName())){
	            	gridLabels[i][j].setIcon(floorIcon);
	            	
	            }
               
	            if ("object".equals( gridLabels[i][j].getName())) {
	            	System.out.println("yes this is object");
	            	
	            	ImageIcon objectIcon= (ImageIcon) cell.getIcon();
	            	
	            	objectOverlay.revertToGround(gridLabels[i][j]);
	            	ImageIcon floorIconn = iconCreator.getImageIcon(groundPath, 48,48);  	
	            	gridLabels[i][j].setIcon(floorIconn);
	                objectOverlay.addObjectOverlay(gridLabels[i][j], objectIcon);
	                
	                objectList.add(gridLabels[i][j]);
	                System.out.println("in initialize");
	                checkMouseListeners(gridLabels[i][j]);
	                removeAllMouseListeners(gridLabels[i][j]);
	                addMouseListener(i, j);
	            }
	            if("door".equals(gridLabels[i][j].getName())) {
	            	gridLabels[i][j].setIcon(doorIcon);
	            	
	            	doorList.add(gridLabels[i][j]);
	            }
	            
	           
	            hallPanel.add(gridLabels[i][j]);
	        }
	    }

	    updateHallTitle();

	    // Refresh the hallPanel
	    hallPanel.revalidate();
	    hallPanel.repaint();
	}

	
	public void addExtraTime(int seconds) {
		 timeLeft += seconds; // Add the extra time
	        inventoryPanel.updateTimeLabel(timeLeft); // Update the UI to reflect the new time
	        System.out.println("Added " + seconds + " seconds. New time left: " + timeLeft);
	   
	       
	    
	}
	private void startTimerForCurrentHall() {
	    // Calculate time based on the number of objects in the hall
	    int objectCount = countObjectsInCurrentHall();
	    timeLeft = 100;//objectCount * 5; // 5 seconds per object
	    
	    
	    inventoryPanel.updateTimeLabel(timeLeft);
	    hallTimer = new Timer(1000, e -> {
	        if (timeLeft > 0) {	
	            timeLeft--;
	            inventoryPanel.updateTimeLabel(timeLeft);
	            
	        } else {
	            ((Timer) e.getSource()).stop();
	            
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
		System.out.println("before add rune:"+objectList.get(num).getCellRune());
		System.out.println("empty before add rune:"+objectList.get(num).getIsEmpty());
		objectList.get(num).setCellRune("rune");
		
		System.out.println("after add rune:"+objectList.get(num).getCellRune());
		System.out.println("name of rune object:"+objectList.get(num).getIcon());
		
		
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

        
        objectList.clear();
        doorList.clear();

        
        hallPanel.revalidate();
        hallPanel.repaint();
    
    }
    
    public void loadNextHall() throws Exception {
    	
        
        if (currentHallIndex < completedHalls.size()-1) {
        	if (monsterSpawner != null) {
        		monsterSpawner.stopSpawning();
            }
        	  
        	  currentHallIndex++;
        	 
              updateHallTitle();
        	  initializeUI();
        	  initializeInventoryPanel();
        	  inventoryPanel.clearInventoryLabel();
        	  addRuneRandomly(); 
              player=  playerManager.placePlayerRandomly();   
              System.out.println("second get player:"+getPlayer());
              monsterSpawner.getFighterMonsters().clear();
              monsterSpawner = new MonsterSpawner(getPlayer(), getObjectList(), this);
              System.out.println("size:"+getObjectList().size());
              monsterSpawner.startSpawning();  
              startTimerForCurrentHall();
              System.out.println("fighter in game manager:"+monsterSpawner.getFighterMonsters());
              
              enchantmentManager.stopSpawning();
              enchantmentManager.clearEnchantmentsMap();
              enchantmentManager = new EnchantmentManager(this, monsterSpawner.getFighterMonsters());
             
              enchantmentManager.startEnchantmentSpawning();
              //enchantmentManager.resetSpawning();
              enchantmentManager.addKeyListener(gameFrame);
            
           
            
            
        } else {
        	if (monsterSpawner != null) {
        		monsterSpawner.stopSpawning();
            }
            // Display the win panel
        	winPanel.displayWinPanel(gameFrame, this);
        }
    }
    private  Cell findDoorLabel() {
    	return doorList.get(0);
    }
    
    private void checkMouseListeners(JComponent component) {
        MouseListener[] listeners = component.getMouseListeners();

        if (listeners.length > 0) {
            System.out.println("MouseListeners found on: " + component.getName());
            for (MouseListener listener : listeners) {
                System.out.println("Listener: " + listener);
            }
        } else {
            System.out.println("No MouseListeners found on: " + component.getName());
        }
    }
private void addMouseListener(int row, int col) {
	  
	    objectMouseListener =  new MouseAdapter() {
	        @Override
	        public void mousePressed(MouseEvent e) {
	        	System.out.println("in mouse clicked");
	        	if (SwingUtilities.isLeftMouseButton(e)) {// BUTTON1 for left click
	        		 
	        	 
	                System.out.println("Cell clicked: (" + row + ", " + col + ")");
	                try {
	                    handleCellClick(row, col);
	                   // gridLabels[row][col].revalidate(); // Force UI update
	                  //  gridLabels[row][col].repaint();
	                } catch (Exception ex) {
	                    ex.printStackTrace();
	                }
	        
	        }
	        }
	    
	    };
	   
	    gridLabels[row][col].setVisible(true);
	    gridLabels[row][col].setEnabled(true);
	    gridLabels[row][col].addMouseListener(objectMouseListener);

	    
	}


    private void handleCellClick(int row, int col) throws Exception {
    	int distance = Math.abs(player.getRow() - row) + Math.abs(player.getCol() - col);
        String RuneName =  gridLabels[row][col].getCellRune();
        System.out.println("distance:"+distance);
        System.out.println("runeName:"+RuneName);
       
        // Check if the clicked cell hides the rune
        if(distance ==1) {
 
        	if (RuneName.equals("rune")) { // this is check for rune.
        		
                System.out.println("You found the hidden rune!");
                SoundPlayer.playSound("src/assets/sounds/door-opening.wav");
                Cell runeLabel = gridLabels[row][col];
                objectOverlay.revertToGround(runeLabel);
               
              
                runeLabel.setEmpty(false);
                ImageIcon runeIcon= new ImageIcon(rune);
                objectOverlay.addObjectOverlay(runeLabel,runeIcon);
                
                 
                 ImageIcon icon = new ImageIcon(openDoor);
                 Cell door= findDoorLabel();
                 ImageIcon floorIcon= iconCreator.getImageIcon(groundPath,48,48);
                
                 door.setIcon(floorIcon);
                 door.setName("door");
                 
                 
                 objectOverlay.overlayLabel(door, icon, 48);
                 door.setEmpty(true);
              
                
                         
  } 
        	
        	
        	
        	
        }
        else {
            System.out.println("This object is empty.");
        } 
        

    
}
    
    private void removeAllMouseListeners(JComponent component) {
        MouseListener[] listenersBefore = component.getMouseListeners();
      
        for (MouseListener listener : listenersBefore) {
            component.removeMouseListener(listener);
        }

        MouseListener[] listenersAfter = component.getMouseListeners();
       

        if (listenersAfter.length > 0) {
           
        } else {
          
        }
    }

	


    
    
    

   


} 

    

    
