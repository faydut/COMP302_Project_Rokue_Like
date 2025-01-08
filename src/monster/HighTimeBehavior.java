package monster;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import player.Player;
import ui.Cell;

public class HighTimeBehavior implements WizardBehavior {
    @Override
    public void executeBehavior(WizardMonster monster, Player player, int remainingTime) {
        ArrayList<Cell> objectList = monster.getObjectList();
        Timer timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                for (Cell obj : objectList) {
                    if ("rune".equals(obj.getName())) {
                        obj.setName("nonempty");
                    }
                }

                int randomIndex = new Random().nextInt(objectList.size());
                objectList.get(randomIndex).setName("rune");
                System.out.println("Wizard moved the rune to a new location!");
            }
        }, 0, 3000);
    }
}
