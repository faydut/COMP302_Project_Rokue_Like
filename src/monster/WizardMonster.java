package monster;

import java.util.ArrayList;


import javax.swing.JLabel;
import javax.swing.Timer;

import ui.GameManager;
import ui.Player;

public class WizardMonster extends Monster {
    private JLabel[][] grid;
    private ArrayList<JLabel> objectList;

    public WizardMonster(int row, int col, JLabel[][] grid, ArrayList<JLabel> objectList, GameManager gameManager) {
        super(row, col, gameManager);
        this.grid = grid;
        this.objectList = objectList;
    }

    @Override
    protected void performAction(Player player) {
        if (!gameManager.isPaused()) {
            teleportRune();
        }
    }

    private void teleportRune() {
        for (JLabel obj : objectList) {
            if (obj.getName().equals("rune")) {
                obj.setName("nonempty");
            }
        }
        int num = new java.util.Random().nextInt(objectList.size());
        objectList.get(num).setName("rune");
    }
}
