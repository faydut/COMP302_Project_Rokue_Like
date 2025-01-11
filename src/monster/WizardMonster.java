package monster;

import java.util.Timer;
import java.util.ArrayList;
import javax.swing.JLabel;
import player.Player;
import ui.Cell;
import ui.GameManager;
import ui.ObjectOverlay;

public class WizardMonster extends Monster {
    private Cell[][] grid;
    private ArrayList<Cell> objectList;
    private WizardBehavior behavior;
    private ObjectOverlay objectOverlay = new ObjectOverlay();
    public int row, col; // Ensure these fields are accessible
    private boolean isActive = true;
    private Timer behaviorTimer; // Reference to the timer used in behaviors
    private int lastBehaviorZone = -1; // Keeps track of the last executed behavior zone

    public WizardMonster(int row, int col, ArrayList<Cell> objectList, GameManager gameManager) {
        super(row, col, gameManager);
        this.grid = gameManager.getGridLabels();
        this.objectList = objectList;
        this.row = row;
        this.col = col;
        
    }
    
    public Cell[][] getGrid() {
        return grid; // Ensure `grid` is of type `Cell[][]`
    }

    public ArrayList<Cell> getObjectList() {
        return objectList;
    }

    @Override
    protected void performAction(Player player) {
        if (!gameManager.isPaused()) {
//            teleportRune();
        }
    }
    
    public boolean isActive() {
        return isActive;
    }

    
    public void act(Player player) {
        if (!isActive) return;

        int totalTime = 100; // Total time for the level (adjust as needed)
        int remainingTime = gameManager.timeLeft;
        int percentage = (remainingTime * 100) / totalTime;

        // Determine behavior zone: 0 (low), 1 (mid), 2 (high)
        int currentZone = (percentage < 30) ? 0 : (percentage > 70 ? 2 : 1);

        // If the zone has not changed, do nothing
        if (currentZone == lastBehaviorZone) return;

        // Execute behavior for the current zone
        lastBehaviorZone = currentZone;
        switch (currentZone) {
            case 0 -> new LowTimeBehavior().executeBehavior(this, player, remainingTime);
            case 1 -> new MidTimeBehavior().executeBehavior(this, player, remainingTime);
            case 2 -> new HighTimeBehavior().executeBehavior(this, player, remainingTime);
        }
    }

//    private void teleportRune() {
//    	for(Cell cell: objectList) {
//    		if(cell.getCellRune().equals("rune")) {
//    			cell.setCellRune("notRune");
//    		}
//    	}
//        int num = new java.util.Random().nextInt(objectList.size());
//        objectList.get(num).setCellRune("rune");
//    }
    
    public void setBehaviorTimer(Timer timer) {
        if (behaviorTimer != null) {
            behaviorTimer.cancel();
            behaviorTimer.purge();
        }
        this.behaviorTimer = timer;
    }
    
    public void disappear() {
        isActive = false; // Mark the wizard as inactive
        if (behaviorTimer != null) {
            behaviorTimer.cancel();
            behaviorTimer.purge();
        }
        objectOverlay.revertToGround(grid[row][col]);
        System.out.println("Wizard disappeared.");
    }
}
