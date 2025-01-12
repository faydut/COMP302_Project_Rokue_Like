package ui;

import javax.swing.Timer;
import java.io.*;
import java.util.List;
import javax.swing.*;
import enchantment.EnchantmentManager;
import frames.GameFrame;
import monster.MonsterSpawner;
import panel.HallPanel;
import panel.InventoryPanel;
import panel.WinPanel;
import player.Player;
import player.PlayerManager;
import save.GameState;

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

    private List<Timer> timers = new ArrayList<>();
    public Timer hallTimer; // Timer for countdown
    public int timeLeft;
    public int currentHallIndex = 0; // Tracks the current hall
    public  Player player;
    private Random random = new Random();
    public  Cell[][] gridLabels = new Cell[gridSize][gridSize];
    private Cell[][] grid;
    private InventoryPanel inventoryPanel;
    private  HallPanel  hallPanel;
    public MonsterSpawner monsterSpawner;
    private boolean isPaused = false; // Variable to track the game's pause state

    public  GameFrame gameFrame;
    public  ArrayList<Cell >objectList= new ArrayList();
    private ArrayList<Cell> doorList= new ArrayList();
    public  ArrayList<Cell[][]> completedHalls;
    private String[] hallNames = {"Earth Hall", "Air Hall", "Water Hall", "Fire Hall"};
    private CreateImageIcon iconCreator = new CreateImageIcon();
    private ObjectOverlay objectOverlay = new ObjectOverlay();
    private WinPanel winPanel= new WinPanel();
    public EnchantmentManager enchantmentManager ;

    private MouseListener objectMouseListener;

    private PlayerManager playerManager = new PlayerManager(this,gridLabels);


    public void saveGame(String filePath) {
        GameState gameState = new GameState(completedHalls, currentHallIndex, player.getRow(), player.getCol(), timeLeft, player.getLives());
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(gameState);
            System.out.println("Game saved successfully!");
        } catch (IOException e) {
            System.err.println("Error saving game: " + e.getMessage());
        }
    }

    public void loadGame(String filePath) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            GameState gameState = (GameState) ois.readObject();

            // Validate the game state
            if (gameState.getHallStates() == null || gameState.getHallStates().isEmpty()) {
                throw new Exception("Saved halls are missing or corrupted.");
            }
            this.completedHalls = gameState.getHallStates();
            this.currentHallIndex = gameState.getCurrentHallIndex();
            this.timeLeft = gameState.getTimeLeft();

            if (gameFrame == null) {
                gameFrame = new GameFrame("Play Mode", 1200, 800, new Color(50, 34, 40));
            }

            if (player == null) {
                player = new Player(gameState.getPlayerRow(), gameState.getPlayerCol(), gameState.getPlayerLives());
            } else {
                player.setRow(gameState.getPlayerRow());
                player.setCol(gameState.getPlayerCol());
            }
            player.setLives(gameState.getPlayerLives());

            initializeGameFromState();
            System.out.println("Game loaded successfully!");
        } catch (Exception e) {
            throw new RuntimeException("Failed to load game: " + e.getMessage(), e);
        }
    }



    private void refreshComponentsAfterLoad() {
        initializeMissingComponents(); // Ensure all required components exist
        restoreInventoryScreen();      // Restore inventory panel
        gameFrame.revalidate();        // Refresh the game frame
        gameFrame.repaint();           // Repaint the game frame
    }





    private void loadHallState() throws Exception {
        if (completedHalls == null || completedHalls.isEmpty()) {
            throw new Exception("No hall states available to load.");
        }

        hallPanel.removeAll(); // Clear the panel to avoid duplicates
        Cell[][] currentHall = completedHalls.get(currentHallIndex);

        int cellSize = 48;
        ImageIcon floorIcon = iconCreator.getImageIcon(groundPath, cellSize, cellSize);
        ImageIcon wallIcon = iconCreator.getImageIcon(wallPath, cellSize, cellSize);
        ImageIcon doorIcon = iconCreator.getImageIcon(closeDoorPath, cellSize, cellSize);

        grid = new Cell[gridSize][gridSize]; // Initialize the grid

        for (int row = 0; row < gridSize; row++) {
            for (int col = 0; col < gridSize; col++) {
                Cell cell = currentHall[row][col];
                if (cell == null) {
                    cell = new Cell(); // Create a new Cell if missing
                    cell.setName("floor");
                    cell.setEmpty(true);
                }

                // Populate both grid and gridLabels
                grid[row][col] = cell;
                gridLabels[row][col] = cell;

                cell.setBounds(col * cellSize, row * cellSize, cellSize, cellSize);
                cell.setOpaque(true);
                cell.setHorizontalAlignment(SwingConstants.CENTER);
                cell.setVerticalAlignment(SwingConstants.CENTER);

                switch (cell.getName()) {
                    case "wall":
                        cell.setIcon(wallIcon);
                        break;
                    case "floor":
                        cell.setIcon(floorIcon);
                        break;
                    case "door":
                        cell.setIcon(doorIcon);
                        break;
                    default:
                        break;
                }
                hallPanel.add(cell);
            }
        }

        hallPanel.revalidate();
        hallPanel.repaint();
    }





    private void loadPlayerState() {
        if (player != null && gridLabels != null) {
            int playerRow = player.getRow();
            int playerCol = player.getCol();

            // Ensure the player is placed on the grid
            if (gridLabels[playerRow][playerCol] != null) {
                gridLabels[playerRow][playerCol].setIcon(new ImageIcon("src/assets/player_sprite.png")); // Path to player sprite
            } else {
                System.err.println("Error: Player's position is invalid.");
            }
        }
    }



    private void initializeMissingComponents() {
        if (gameFrame == null) {
            gameFrame = new GameFrame("Play Mode", 1200, 800, new Color(50, 34, 40));
        }

        if (hallPanel == null) {
            initializeHallPanel();
        }

        if (inventoryPanel == null) {
            initializeInventoryPanel();
        }
    }


    void initializeGameFromState() {
        try {
            initializeMissingComponents(); // Ensure all components are initialized

            if (completedHalls == null || completedHalls.isEmpty()) {
                throw new Exception("No completed halls available in the save state.");
            }

            loadHallState();       // Load hall tiles and objects
            loadPlayerState();     // Place player sprite
            restoreInventoryScreen(); // Restore inventory UI

            // Add KeyListener to the gameFrame for player movement
            if (gameFrame.getKeyListeners().length == 0) {
                gameFrame.addKeyListener(new KeyListener() {
                    @Override
                    public void keyTyped(KeyEvent e) {
                        // Not needed for movement
                    }

                    @Override
                    public void keyPressed(KeyEvent e) {
                        handlePlayerMovement(e.getKeyCode());
                    }

                    @Override
                    public void keyReleased(KeyEvent e) {
                        // Not needed for movement
                    }
                });
            }

            if (hallTimer != null) {
                hallTimer.stop();
            }
            startTimerForCurrentHall();

            if (monsterSpawner != null) {
                monsterSpawner.stopSpawning();
            }
            monsterSpawner = new MonsterSpawner(player, getObjectList(), this);
            monsterSpawner.startSpawning();

            if (enchantmentManager != null) {
                enchantmentManager.stopSpawning();
                enchantmentManager.clearEnchantmentsMap();
            }
            enchantmentManager = new EnchantmentManager(this, monsterSpawner.getFighterMonsters());
            enchantmentManager.startEnchantmentSpawning();
            enchantmentManager.addKeyListener(gameFrame);

            gameFrame.setVisible(true);
            gameFrame.requestFocusInWindow();

            System.out.println("Game state initialized successfully from saved data.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(gameFrame, "Failed to initialize from saved state: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void handlePlayerMovement(int keyCode) {
        int currentRow = player.getRow();
        int currentCol = player.getCol();

        int newRow = currentRow, newCol = currentCol;

        switch (keyCode) {
            case KeyEvent.VK_UP -> newRow--;
            case KeyEvent.VK_DOWN -> newRow++;
            case KeyEvent.VK_LEFT -> newCol--;
            case KeyEvent.VK_RIGHT -> newCol++;
            default -> {
                return; // Ignore other keys
            }
        }

        // Validate and update player's position
        if (newRow >= 0 && newRow < grid.length && newCol >= 0 && newCol < grid[0].length) {
            if (grid[newRow][newCol].getIsEmpty()) {
                // Update grid state
                grid[currentRow][currentCol].setEmpty(true);
                grid[newRow][newCol].setEmpty(false);

                // Move player
                player.setRow(newRow);
                player.setCol(newCol);

                // Update player sprite on the grid
                gridLabels[currentRow][currentCol].setIcon(null);
                gridLabels[newRow][newCol].setIcon(new ImageIcon("src/assets/player_sprite.png"));
            }
        }
    }



    private void restoreInventoryScreen() {
        if (inventoryPanel == null) {
            initializeInventoryPanel(); // Initialize if not already set up
        }

        // Update inventory items and UI
        try {
            inventoryPanel.addItemtoMap(); // Ensure items are loaded
        } catch (Exception e) {
            System.err.println("Error restoring inventory items: " + e.getMessage());
        }

        inventoryPanel.updateLives(player.getLives());
        inventoryPanel.updateTimeLabel(timeLeft);

        // Add inventory panel to the game frame if not already present
        if (gameFrame.getContentPane().getComponentCount() == 0) {
            gameFrame.add(inventoryPanel);
        }
    }



    public void startGame() throws Exception {
        gameFrame = new GameFrame("Play Mode", 1200, 800, new Color(50, 34, 40));
        gameFrame.requestFocusInWindow();

        System.out.println("Completed halls: " + completedHalls.get(currentHallIndex));
        initializeHallPanel();

        initializeUI();
        addRuneRandomly();
        initializeInventoryPanel();
        player = playerManager.placePlayerRandomly();
        playerManager.addGameKeyListener(gameFrame);

        monsterSpawner = new MonsterSpawner(getPlayer(), getObjectList(), this);
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

    public void initializeHallPanel() {
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

    public void addTimer(Timer timer) {
        timers.add(timer);
    }

    // Pause all timers
    public void pauseAllTimers() {
        for (Timer timer : timers) {
            if (timer.isRunning()) {
                timer.stop();
            }
        }
        if (hallTimer != null) hallTimer.stop();
        if (monsterSpawner != null) monsterSpawner.pauseSpawning();
        if (enchantmentManager != null) enchantmentManager.pauseSpawning();
    }


    // Resume all timers
    public void resumeAllTimers() {
        for (Timer timer : timers) {
            timer.start();
        }
        if (hallTimer != null) hallTimer.start();
        if (monsterSpawner != null) monsterSpawner.resumeSpawning();
        if (enchantmentManager != null) enchantmentManager.resumeSpawning();
    }

    // Remove a timer from the central list
    public void removeTimer(Timer timer) {
        timers.remove(timer);
    }



    // Pause the game
    public void pauseGame() {
        isPaused = true;
        pauseAllTimers(); // Pause all registered timers and components
        System.out.println("Game paused.");
    }

    public void resumeGame() {
        isPaused = false;
        resumeAllTimers(); // Resume all registered timers
        gameFrame.requestFocusInWindow(); // Regain focus
        System.out.println("Game resumed.");
    }

    public boolean isPaused() {
        return isPaused;
    }


    // Handle game over logic
    public void gameOver(String message) {
        isPaused = true; // Pause the game
        pauseAllTimers(); // Pause all registered timers and components
        JOptionPane.showMessageDialog(gameFrame, message, "Game Over", JOptionPane.INFORMATION_MESSAGE);
        new MainMenu(); // Navigate to the main menu
        gameFrame.dispose(); // Close the game frame
        System.out.println("Game over: " + message);
    }



    public void initializeInventoryPanel() {
        System.out.println("Initializing Inventory Panel");

        if (inventoryPanel == null) {
            inventoryPanel = new InventoryPanel(gameFrame, this); // Pass the GameManager instance
        }

        // Remove and re-add to ensure proper layout
        gameFrame.getContentPane().remove(inventoryPanel);
        gameFrame.getContentPane().add(inventoryPanel);

        inventoryPanel.setBounds(800, 50, 200, 570); // Ensure proper positioning
        inventoryPanel.setVisible(true);

        gameFrame.revalidate();
        gameFrame.repaint();

        System.out.println("Inventory Panel Initialized: " + inventoryPanel);
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
    public void startTimerForCurrentHall() {
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

        int length = objectList.size();
        if (length == 0) {
            System.out.println("No objects available to add a rune.");
            return; // Skip if there are no objects
        }

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
        if (completedHalls == null || completedHalls.isEmpty()) {
            throw new Exception("No halls available"); // Throw an exception if no halls are available
        }

        if (currentHallIndex < completedHalls.size() - 1) {
            if (monsterSpawner != null) {
                monsterSpawner.stopSpawning();
            }

            currentHallIndex++;

            updateHallTitle();
            initializeUI();
            initializeInventoryPanel();
            inventoryPanel.clearInventoryLabel();
            addRuneRandomly();
            player = playerManager.placePlayerRandomly();
            System.out.println("second get player:" + getPlayer());

            monsterSpawner.getFighterMonsters().clear();
            monsterSpawner = new MonsterSpawner(getPlayer(), getObjectList(), this);
            monsterSpawner.startSpawning();
            startTimerForCurrentHall();

            enchantmentManager.stopSpawning();
            enchantmentManager.clearEnchantmentsMap();
            enchantmentManager = new EnchantmentManager(this, monsterSpawner.getFighterMonsters());
            enchantmentManager.startEnchantmentSpawning();
            enchantmentManager.addKeyListener(gameFrame);
        } else {
            if (monsterSpawner != null) {
                monsterSpawner.stopSpawning();
            }
            winPanel.displayWinPanel(gameFrame, this);
        }
    }

    private Cell findDoorLabel() {
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
        objectMouseListener = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                System.out.println("in mouse clicked");
                if (SwingUtilities.isLeftMouseButton(e)) { // BUTTON1 for left click
                    System.out.println("Cell clicked: (" + row + ", " + col + ")");
                    try {
                        handleCellClick(row, col);
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

    public void handleCellClick(int row, int col) throws Exception {
        int distance = Math.abs(player.getRow() - row) + Math.abs(player.getCol() - col);
        String RuneName = gridLabels[row][col].getCellRune();
        System.out.println("distance:" + distance);
        System.out.println("runeName:" + RuneName);

        if (distance == 1) {
            if ("rune".equals(RuneName)) {
                System.out.println("You found the hidden rune!");
                SoundPlayer.playSound("src/assets/sounds/door-opening.wav");
                Cell runeLabel = gridLabels[row][col];
                objectOverlay.revertToGround(runeLabel);

                runeLabel.setEmpty(false);
                ImageIcon runeIcon = new ImageIcon(rune);
                objectOverlay.addObjectOverlay(runeLabel, runeIcon);

                ImageIcon icon = new ImageIcon(openDoor);
                Cell door = findDoorLabel();
                ImageIcon floorIcon = iconCreator.getImageIcon(groundPath, 48, 48);

                door.setIcon(floorIcon);
                door.setName("door");

                objectOverlay.overlayLabel(door, icon, 48);
                door.setEmpty(true);
            }
        } else {
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
            System.out.println("Failed to remove all mouse listeners.");
        } else {
            System.out.println("All mouse listeners removed.");
        }
    }
}
