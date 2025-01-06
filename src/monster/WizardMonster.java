package monster;

import java.util.ArrayList;


import javax.swing.JLabel;
import javax.swing.Timer;

import player.Player;
import ui.Cell;
import ui.GameManager;

public class WizardMonster extends Monster {
    private Cell[][] grid;
    private ArrayList<Cell> objectList;

    public WizardMonster(int row, int col, ArrayList<Cell> objectList, GameManager gameManager) {
        super(row, col, gameManager);
        this.grid = gameManager.getGridLabels();
        this.objectList = objectList;
    }

    @Override
    protected void performAction(Player player) {
        if (!gameManager.isPaused()) {
            teleportRune();
        }
    }

    private void teleportRune() {
    	for(Cell cell: objectList) {
    		if(cell.getCellRune().equals("rune")) {
    			cell.setCellRune("notRune");
    		}
    	}
        int num = new java.util.Random().nextInt(objectList.size());
        objectList.get(num).setCellRune("rune");
    }
}
