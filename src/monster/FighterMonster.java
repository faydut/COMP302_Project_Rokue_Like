package monster;

import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import ui.Player;

class FighterMonster extends Monster {
	 private  JLabel[] []grid;
	 private String fighter = "src/assets/rokue-like assets/fighter.png";
   public FighterMonster(int row, int col, JLabel[] []grid) {
       super(row, col);
       this.grid= grid;
   }

   @Override
   public void act(Player player) {
   	//System.out.println("it is in fighterdww");
       int distance = Math.abs(player.getRow() - row) + Math.abs(player.getCol() - col);
       if (distance == 1) {
           player.loseLife();
       } 
          fighterMovement();
       
           
       
   }
   
   private void fighterMovement() {
	   int[][] possibleMoves = {
               {0, 1},  // Move right
               {1, 0},  // Move down
               {-1, 0}, // Move up
               {0, -1}, // Move left
               {0, 0}   // Stay in place
           };

           // Select a random move
           int[] selectedMove = possibleMoves[random.nextInt(possibleMoves.length)];
           int newRow = row + selectedMove[0];
           int newCol = col + selectedMove[1];
       if (grid[newRow][newCol].getName() == "empty") {
    	   //
    	   JLabel currentLabel = grid[row][col];
           revertToGround(currentLabel); // Revert the current label to the ground icon

           // Handle the new cell
           JLabel newLabel = grid[newRow][newCol];
           addFighterOverlay(newLabel); // 
           
           row = newRow;
           col = newCol;
       }
	   
   }
   
   
   private void addFighterOverlay(JLabel groundLabel) {
	    // Create a fighter icon
	    ImageIcon fighterIcon = new ImageIcon(fighter);
	    fighterIcon = new ImageIcon(fighterIcon.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH)); // Resize to 16x16

	    // Set up overlay
	    groundLabel.setLayout(null); // Allow precise positioning
	    JLabel fighterOverlay = new JLabel();
	    fighterOverlay.setIcon(fighterIcon);
	    fighterOverlay.setBounds((groundLabel.getWidth() - 16) / 2, (groundLabel.getHeight() - 16) / 2, 16, 16); // Center fighter

	    // Add overlay
	    groundLabel.add(fighterOverlay);
	    groundLabel.setName("nonempty"); // Mark the label as occupied
	    groundLabel.revalidate();
	    groundLabel.repaint();
	}
   private void revertToGround(JLabel label) {
	    if (label.getName().equals("nonempty")) {
	        // Clear any overlays (e.g., the fighter icon)
	        label.removeAll();

	        // Revalidate and repaint the label to ensure changes are applied
	        label.revalidate();
	        label.repaint();

	        // Reset the label's state to "empty"
	        label.setName("empty");
	    }
	}


}
