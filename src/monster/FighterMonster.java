package monster;

import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import ui.Player;
import ui.GameManager;

public class FighterMonster extends Monster {
    private JLabel[][] grid;
    private String fighterIconPath = "src/assets/rokue-like assets/fighter.png";

    public FighterMonster(int row, int col, JLabel[][] grid, GameManager gameManager) {
        super(row, col, gameManager);
        this.grid = grid;
    }

    @Override
    protected void performAction(Player player) {
        int distance = Math.abs(player.getRow() - row) + Math.abs(player.getCol() - col);
        if (distance == 1) {
            player.loseLife();
        }
        moveRandomly();
    }

    private void moveRandomly() {
        int[][] moves = {{0, 1}, {1, 0}, {-1, 0}, {0, -1}, {0, 0}}; // Possible moves
        int[] selectedMove = moves[new java.util.Random().nextInt(moves.length)];
        int newRow = row + selectedMove[0];
        int newCol = col + selectedMove[1];

        if (grid[newRow][newCol].getName().equals("empty")) {
            JLabel currentLabel = grid[row][col];
            revertToGround(currentLabel);

            JLabel newLabel = grid[newRow][newCol];
            addFighterOverlay(newLabel);

            row = newRow;
            col = newCol;
        }
    }

    private void addFighterOverlay(JLabel groundLabel) {
        ImageIcon fighterIcon = new ImageIcon(new ImageIcon(fighterIconPath)
            .getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH));
        JLabel fighterOverlay = new JLabel(fighterIcon);
        fighterOverlay.setBounds((groundLabel.getWidth() - 16) / 2, 
                                  (groundLabel.getHeight() - 16) / 2, 16, 16);
        groundLabel.setLayout(null);
        groundLabel.add(fighterOverlay);
        groundLabel.setName("nonempty");
        groundLabel.revalidate();
        groundLabel.repaint();
    }

    private void revertToGround(JLabel label) {
        if (label.getName().equals("nonempty")) {
            label.removeAll();
            label.revalidate();
            label.repaint();
            label.setName("empty");
        }
    }
}

