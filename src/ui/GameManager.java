package ui;

import javax.swing.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Random;

public class GameManager  {
    private  final int gridSize = 12;
    private  final String rune = "src/assets/rokue-like assets/key3.png";
    private  final String openDoor = "src/assets/items/door_open.png";
    private  final String closeDoor = "src/assets/items/door_closed.png";
    private  final String wall = "#";
    private  final String empty = "empty";
    //private static final String nonEmpty = "nonempty";
    private  final String PLAYER = "src/assets/rokue-like assets/player.png";
    private Timer hallTimer; // Timer for countdown
    private int timeLeft;    // Remaining time for the current hall
    private int currentHallIndex = 0; // Tracks the current hall
	private  final int EXIT_ON_CLOSE = 0;
	public  Player player;
	Random random = new Random();
    public  JLabel[][] gridLabels = new JLabel[gridSize][gridSize];
    
    private InventoryPanel inventoryPanel;
    private      HallPanel  hallPanel;
    private MonsterSpawner spawner; 
    
    private  JFrame frame= new JFrame();
    public  ArrayList<JLabel >objectList= new ArrayList();
    private ArrayList<JLabel> doorList= new ArrayList();
    public  ArrayList<Icon[][]> completedHalls;
    
   
    public GameManager(ArrayList<Icon[][]> completedHalls ) {
    	this.completedHalls= completedHalls;
    }
    
    
    
    public void startGame() {
    	
    	CustomBackgroundPanel backgroundPanel = new CustomBackgroundPanel();
        backgroundPanel.setLayout(null); // Use null layout for manual positioning
        frame.setContentPane(backgroundPanel);
    	
        frame.setTitle("Grid Hall Game");
        frame.setSize(1200, 800);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setVisible(true);
       // frame.setLayout(new GridLayout(GRID_SIZE, GRID_SIZE));

       // initializeGrid();
        initializeHallPanel();
        initializeInventoryPanel();
        initializeUI();
        addRuneRandomly();
        placePlayerRandomly();
        spawner = new MonsterSpawner(getGridLabels(), getPlayer(), getObjectList());
        spawner.startSpawning();  
        startTimerForCurrentHall();
       frame.addKeyListener(new KeyListener() {
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
        });

        frame.setFocusable(true); // Ensure the JFrame listens for key events
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



	public void placePlayerRandomly() {
		    int playerRow; // Player's starting position
	        int playerCol;
    	       // int cellSize = 60;
                playerRow = random.nextInt(gridSize - 2) +1; // Avoid walls
                playerCol = random.nextInt(gridSize - 2) +1;
               
        player = new Player(playerRow, playerCol,frame);
       
        player.setCol(playerCol);
        player.setRow(playerRow);
        JLabel playerLabel= gridLabels[playerRow][playerCol];
        
        while(playerLabel.getName()!=empty) {
        	playerRow = random.nextInt(gridSize - 2) ; // Avoid walls
            playerCol = random.nextInt(gridSize - 2) ;
        	
            player.setCol(playerCol);
            player.setRow(playerRow);
        }
        ImageIcon image= new ImageIcon(PLAYER);
        playerLabel.setIcon(image);  // Place the player at the random position  // bunu degistirdim
       // playerLabel.setForeground(Color.WHITE);
        //playerLabel.setOpaque(true);
       // playerLabel.setBackground(Color.BLUE);
    }
	


	private void initializeInventoryPanel() {
		inventoryPanel = new InventoryPanel();
	    frame.getContentPane().add(inventoryPanel);

    }
	private void initializeHallPanel() {
        hallPanel = new HallPanel();
        hallPanel.setLayout(null); // Use null layout for manual positioning
        hallPanel.setBackground(Color.LIGHT_GRAY); // Example background for hall panel
        frame.add(hallPanel, BorderLayout.CENTER); // Add hallPanel to center of frame
        
    }
	
	private int countObjectsInCurrentHall() {
	    Icon[][] selectedHall = completedHalls.get(currentHallIndex);
	    int count = 0;

	    for (int i = 0; i < gridSize; i++) {
	        for (int j = 0; j < gridSize; j++) {
	            if (selectedHall[i][j] != null) {
	                count++;
	            }
	        }
	    }
	    return count;
	}

	
	private void initializeUI() {
	    int cellSize = 50;

	    // Check if completedHalls is not empty
	    if (completedHalls == null || completedHalls.isEmpty()) {
	        System.out.println("No completed halls available.");
	        return;
	    }

	    // Retrieve the first completed hall (you can choose another index if needed)
	    Icon[][] selectedHall = completedHalls.get(currentHallIndex);

	    for (int i = 0; i < gridSize; i++) {
	        for (int j = 0; j < gridSize; j++) {
	        	
	            gridLabels[i][j] = new JLabel();
	            gridLabels[i][j].setOpaque(false);
	            gridLabels[i][j].setHorizontalAlignment(SwingConstants.CENTER);
	            gridLabels[i][j].setVerticalAlignment(SwingConstants.CENTER);
	            gridLabels[i][j].setFont(new Font("Arial", Font.BOLD, 24));
	            gridLabels[i][j].setBounds(j * cellSize, i * cellSize, cellSize, cellSize);
	            gridLabels[i][j].setBorder(BorderFactory.createLineBorder(Color.black)); //
	            hallPanel.add(gridLabels[i][j]);

	            frame.getContentPane().add(gridLabels[i][j]);
                if(i==gridSize-1 && j==3 ) {                      // I put door
                	ImageIcon icon= new ImageIcon(closeDoor);
                	gridLabels[i][j].setIcon(icon);
                	gridLabels[i][j].setName("openDoor");
                	gridLabels[i][j].setHorizontalAlignment(SwingConstants.CENTER);
    	            gridLabels[i][j].setVerticalAlignment(SwingConstants.CENTER);
                	doorList.add(gridLabels[i][j]);
                }
	            if (i == 0 || i == gridSize - 1 || j == 0 || j == gridSize - 1) {
	                gridLabels[i][j].setText(wall); // Create walls at the edges
	            } else {
	                // Load the icons from the completed hall into the grid
	                Icon icon = selectedHall[i][j];
	               // System.out.println("icon:"+icon);
	                gridLabels[i][j].setIcon(icon);
	                addMouseListener(i, j);

	                if (icon != null) {
	                    gridLabels[i][j].setName("nonempty"); // Example name for non-empty cells
	                    objectList.add(gridLabels[i][j]);
	                   
	                    
	                    // buraya randomly rune yerlestirilecek.
	                } else {
	                	gridLabels[i][j].setName(empty);
	                    
	                }
	               // System.out.println("get text:: "+ gridLabels[i][j].getText());
	            }
	        }
	    }
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
	            System.out.println("Time Left: " + timeLeft + " seconds");
	        } else {
	            ((Timer) e.getSource()).stop();
	            JOptionPane.showMessageDialog(frame, "Time's up! Game Over.", "Game Over", JOptionPane.ERROR_MESSAGE);
	            frame.dispose(); // End the game
	        }
	    });
	    hallTimer.start();
	}
	
	private void addRuneRandomly() {
		int length= objectList.size();
		
		int num= random.nextInt(length);
		objectList.get(num).setName("rune");
		System.out.println("rune in which object:"+objectList.get(num).getName());
		
	}

	
    private void addMouseListener(int row, int col) {
    	gridLabels[row][col].addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
            	if (e.getButton() == MouseEvent.BUTTON1) { // Check for right-click  button 1 for left click and button3 for right click
            		System.out.println("it is clicked");
            		handleCellClick(row, col);
                   
                    gridLabels[row][col].removeMouseListener(this);
                }
            }
        });
    	
    }
    private void cleanHall() {
    	for( int i=0; i<gridSize-1; i++) {
    		for(int j=0; j<gridSize-1; j++) {
    			gridLabels[i] [j].setIcon(null);
    			
    		}
    	}
    	
    
    }
    private void loadNextHall() {
        currentHallIndex++;
        if (currentHallIndex < completedHalls.size()) {
        	if (spawner != null) {
                spawner.stopSpawning();
            }
        	  initializeUI();           // Load the next hall's UI
              placePlayerRandomly();    // Place the player randomly
              startTimerForCurrentHall(); // Restart the hall timer
              addRuneRandomly(); 
               spawner = new MonsterSpawner(getGridLabels(), getPlayer(), getObjectList());
              spawner.startSpawning();  
           
            
            
        } else {
            JOptionPane.showMessageDialog(frame, "Congratulations! You completed all halls!", "Game Completed", JOptionPane.INFORMATION_MESSAGE);
            spawner.stopSpawning();
            frame.dispose(); // End the game
        }
    }
    private  JLabel findDoorLabel() {
    	return doorList.get(0);
    }

    private void handleCellClick(int row, int col) {
    	int distance = Math.abs(player.getRow() - row) + Math.abs(player.getCol() - col);
        String RuneName =  gridLabels[row][col].getName();
        System.out.println("runeName:"+RuneName);
       
        // Check if the clicked cell hides the rune
        if(distance ==1) {
 
        	if (RuneName.equals("rune")) { // this is check for rune.
        		
                System.out.println("You found the hidden rune!");
                SoundPlayer.playSound("src/assets/sounds/door-opening.wav");
             
                ImageIcon originalIcon = new ImageIcon(rune);

                // Resize the image to fit the JLabel
                Image scaledImage = originalIcon.getImage().getScaledInstance(gridLabels[row][col].getWidth(), gridLabels[row][col].getHeight(), Image.SCALE_SMOOTH);
                ImageIcon scaledIcon = new ImageIcon(scaledImage);
               
                
                 gridLabels[row][col] .setIcon(scaledIcon);  // Reveal the rune
                // gridLabels[row][col].setBackground(new Color(128, 0, 128));
                // gridLabels[row][col].setOpaque(true);
                 hallTimer.stop(); // Stop the timer as the rune is found
                 ImageIcon icon = new ImageIcon(openDoor);
                 JLabel door= findDoorLabel();
                 door.setIcon(icon);
                 System.out.println("waitt");
                 
            	// loadNextHall(); // Load the next hall    
             
              
                
                	 
                 
                 
                         
  } 
        	
        	
        	
        	
        }
        else {
            System.out.println("This object is empty.");
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
    

    private void movePlayer(int rowChange, int colChange) {
    	int playerRow=  player.getRow();
        int playerCol= player.getCol();
        int newRow = playerRow + rowChange;
        int newCol = playerCol + colChange;
        
       
        if(isEmpty(newRow,newCol)==true ||gridLabels[newRow][newCol].getName().equals("openDoor")  ) {
        	gridLabels[playerRow][playerCol].setName(empty);	
        	gridLabels[playerRow][playerCol].setIcon(null);
        	
          	 playerRow = newRow;
             playerCol = newCol;
            
            
            ImageIcon image= new ImageIcon(PLAYER);
            gridLabels[playerRow][playerCol].setIcon(image);
            
            player.setCol(playerCol);
            player.setRow(playerRow);
            if ("openDoor".equals(gridLabels[newRow][newCol].getName())) {
                System.out.println("Player reached the open door. Loading next hall...");
                cleanHall();
           	    gridLabels[player.getRow()][player.getCol()].setIcon(null); 
           	    objectList.clear();
           	    doorList.clear();
                loadNextHall(); // Transition to the next hall
            }
           
            
           
        }
        
    }
} 
    

    