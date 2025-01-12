package monster;

import player.Player;
import ui.GameManager;
import ui.ObjectOverlay;

import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MidTimeBehavior implements WizardBehavior {
    private final ObjectOverlay objectOverlay = new ObjectOverlay();
    private Timer behaviorTimer; // Declare the timer as a class-level field

    @Override
    public void executeBehavior(WizardMonster monster, Player player, int remainingTime) {
        if (!monster.isActive()) return; // Prevent execution if the wizard is inactive

        System.out.println("Wizard is indecisive. It will disappear in 2 seconds.");
        behaviorTimer = new Timer(2000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                monster.disappear();
                monster.gameManager.removeTimer(behaviorTimer); // Remove the timer from GameManager
            }
        });

        behaviorTimer.setRepeats(false);
        behaviorTimer.start();

        // Register the timer with GameManager for pause/resume support
        monster.gameManager.addTimer(behaviorTimer);
    }
}

