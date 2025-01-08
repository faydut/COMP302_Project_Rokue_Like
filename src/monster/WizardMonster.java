package monster;

import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.Timer;
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
    	if (!isActive) return; // Prevent execution if the wizard is inactive
    	
        // This method will be called every second
        int totalTime = 100; // Assuming total time is 100 for percentage calculation
        int remainingTime = gameManager.timeLeft;
        int percentage = (remainingTime * 100) / totalTime;

        if (percentage < 30) {
            behavior = new LowTimeBehavior();
        } else if (percentage > 70) {
            behavior = new HighTimeBehavior();
        } else {
            behavior = new MidTimeBehavior();
        }

        behavior.executeBehavior(this, player, remainingTime);
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
    
    public void disappear() {
    	isActive = false; // Mark the wizard as inactive
        objectOverlay.revertToGround(grid[row][col]);
        System.out.println("Wizard disappeared.");
    }
}
