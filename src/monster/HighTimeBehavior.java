package monster;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.TimerTask;
import player.Player;
import ui.Cell;

public class HighTimeBehavior implements WizardBehavior {
    private Timer behaviorTimer; // Timer for moving the rune

    @Override
    public void executeBehavior(WizardMonster monster, Player player, int remainingTime) {
        if (!monster.isActive()) return;

        ArrayList<Cell> objectList = monster.getObjectList();
        behaviorTimer = new Timer(3000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!monster.isActive()) {
                    behaviorTimer.stop();
                    monster.gameManager.removeTimer(behaviorTimer); // Remove the timer from GameManager
                    return;
                }

                // Clear the old rune location
                for (Cell obj : objectList) {
                    if ("rune".equals(obj.getName())) {
                        obj.setName("nonempty");
                    }
                }

                // Move the rune to a new random location
                int randomIndex = new Random().nextInt(objectList.size());
                objectList.get(randomIndex).setName("rune");
                System.out.println("Wizard moved the rune to a new location!");
            }
        });

        behaviorTimer.setRepeats(true);
        behaviorTimer.start();
        monster.gameManager.addTimer(behaviorTimer); // Register the timer with GameManager
    }
}