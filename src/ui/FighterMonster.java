package ui;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

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
           grid[row][col].setName("empty"); // Clear old position
           grid[row][col].setIcon(null);
           row = newRow;
           col = newCol;
           
           ImageIcon image= new ImageIcon(fighter);
           grid[row][col].setIcon(image);// Fighter symbol
           grid[row][col].setName("nonempty");
       }
	   
   }
}
