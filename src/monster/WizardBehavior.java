package monster;

import player.Player;

public interface WizardBehavior {
    void executeBehavior(WizardMonster monster, Player player, int remainingTime);
}
