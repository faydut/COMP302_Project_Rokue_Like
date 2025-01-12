// GameState.java
package save;

import ui.Cell;
import java.io.Serializable;
import java.util.ArrayList;

public class GameState implements Serializable {
    private static final long serialVersionUID = 1L;

    private ArrayList<Cell[][]> hallStates;
    private int currentHallIndex;
    private int playerRow;
    private int playerCol;
    private int timeLeft;
    private int playerLives;

    public GameState(ArrayList<Cell[][]> hallStates, int currentHallIndex, int playerRow, int playerCol, int timeLeft, int playerLives) {
        this.hallStates = hallStates;
        this.currentHallIndex = currentHallIndex;
        this.playerRow = playerRow;
        this.playerCol = playerCol;
        this.timeLeft = timeLeft;
        this.playerLives = playerLives;
    }

    public ArrayList<Cell[][]> getHallStates() {
        return hallStates;
    }

    public int getCurrentHallIndex() {
        return currentHallIndex;
    }

    public int getPlayerRow() {
        return playerRow;
    }

    public int getPlayerCol() {
        return playerCol;
    }

    public int getTimeLeft() {
        return timeLeft;
    }

    public int getPlayerLives() {
        return playerLives;
    }
}