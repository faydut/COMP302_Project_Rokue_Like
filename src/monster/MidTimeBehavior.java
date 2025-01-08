package monster;

import player.Player;
import ui.ObjectOverlay;

import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MidTimeBehavior implements WizardBehavior {
    private final ObjectOverlay objectOverlay = new ObjectOverlay();

    @Override
    public void executeBehavior(WizardMonster monster, Player player, int remainingTime) {
        if (!monster.isActive()) return; // Prevent execution if the wizard is inactive

        System.out.println("Wizard is indecisive. It will disappear in 2 seconds.");
        Timer timer = new Timer(2000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                monster.disappear();
            }
        });
        timer.setRepeats(false);
        timer.start();
    }
}
