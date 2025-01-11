package test;
import org.junit.Before;
import org.junit.Test;

import monster.FighterMonster;
import player.Player;
import ui.GameManager;

import static org.junit.Assert.*;

public class FighterMonsterTest {

    private FighterMonster monster;
    private GameManager gameManager;
    private Player player;

    @Before
    public void setUp() {
        gameManager = new GameManager(null);  
        player = new Player(5, 5, null, gameManager);  
        monster = new FighterMonster(0, 0, gameManager);  
    }

    @Test
    public void testMoveRandomly() throws Exception {
        int initialRow = monster.getNewRow();
        int initialCol = monster.getNewCol();
        monster.moveRandomly();
        assertNotEquals("Monster should move to a new position", initialRow, monster.getNewRow());
        assertNotEquals("Monster should move to a new position", initialCol, monster.getNewCol());
        assertTrue("New row should be within bounds", monster.getNewRow() >= 0 && monster.getNewRow() < gameManager.getGridLabels().length);
        assertTrue("New column should be within bounds", monster.getNewCol() >= 0 && monster.getNewCol() < gameManager.getGridLabels()[0].length);
    }

    @Test
    public void testAttackPlayer() {
        monster.setNewRow(4);
        monster.setNewCol(5);
        int initialLives = player.getLives();
        monster.performAction(player);

        assertEquals("Player's life should be reduced by 1", initialLives - 1, player.getLives());
    }

    @Test
    public void testMoveToOccupiedCell() throws Exception {
        gameManager.getGridLabels()[1][0].isEmpty = false;  

        monster.setNewRow(0);
        monster.setNewCol(0);

        monster.moveRandomly();

        assertNotEquals("Monster should not move to an occupied cell", 1, monster.getNewRow());
        assertNotEquals("Monster should not move to an occupied cell", 0, monster.getNewCol());
    }
}
