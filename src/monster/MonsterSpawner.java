package monster;

import javax.swing.*;

import ui.Player;

import java.awt.Color;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MonsterSpawner {
    private JLabel[][] grid;
    private Random random = new Random();
    private Player player;
    private ArrayList<Monster> monsters = new ArrayList<>();
    private ArrayList<JLabel> objectList;
    private String archer = "src/assets/rokue-like assets/archer.png";
    private String fighter = "src/assets/rokue-like assets/fighter.png";
    private String wizard = "src/assets/rokue-like assets/wizard.png";
    private Timer spawnTimer;
    
    public ArrayList<Monster> getMonsters() {
		return monsters;
	}

	//private Timer actionTimer;

    public MonsterSpawner(JLabel[][] grid, Player player, ArrayList<JLabel> objectList) {
        this.grid = grid;
        this.player = player;
        this.objectList=  objectList;
    }
   

    public void startSpawning() {
    	monsters.clear();

    	 if (spawnTimer == null) {
    	        spawnTimer = new Timer(8000, e -> {
    	            System.out.println("8 seconds passed");
    	            spawnMonster();
    	        });
    	    }
    	    spawnTimer.start();
       

        // Make all monsters act every second
       
        
        
        Timer actionTimer = new Timer(1000, e -> {
        	System.out.println("monster length:"+monsters.size());
            for (Monster monster : monsters) {
                monster.act(player); // Each monster acts periodically
            }
        });
        actionTimer.start();
       
    }
    public void stopSpawning() {
        if (spawnTimer != null) {
            spawnTimer.stop(); // Stop the spawn timer
            System.out.println("Monster spawning stopped.");
        }
        monsters.clear(); // Clear the list of monsters
    }

    private void spawnMonster() {
    	

    	//int cellSize = 50; 
        int row, col;

        // Find a random empty cell
        row = random.nextInt(grid.length-1);
        col = random.nextInt(grid[0].length-1);
       
        String celltype= grid[row][col].getName();
          // playerin hareketiyle ayni zamanda gelebilirler.w
       
        while(celltype != "empty") {
        	 
        	row = random.nextInt(grid.length-1);
            col = random.nextInt(grid[0].length-1);
            celltype= grid[row][col].getName();
           
           
        }
        
        Monster monster;
        int type = random.nextInt(3);
        
        JLabel groundLabel = grid[row][col];

        switch (type) {
            case 0 -> {
                monster = new ArcherMonster(row, col);
                overlayMonster(groundLabel, new ImageIcon(archer), "nonempty");
                
               // monsterLabel.setForeground(Color.red);
            }
            case 1 -> {
                monster = new FighterMonster(row, col, grid);
                overlayMonster(groundLabel, new ImageIcon(fighter), "nonempty");
                //monsterLabel.setForeground(Color.orange);
            }
            case 2 -> {
                monster = new WizardMonster(row, col, grid,objectList);
                overlayMonster(groundLabel, new ImageIcon(wizard), "nonempty");
            }
            default -> throw new IllegalStateException("Unexpected monster type: " + type);
        }

      
        
        monsters.add(monster);
       
        
    }
    private void overlayMonster(JLabel groundLabel, ImageIcon monsterIcon, String monsterType) {
        // Create a new JLabel for the monster
        JLabel monsterLabel = new JLabel();
        monsterIcon = new ImageIcon(monsterIcon.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH)); // Resize monster icon
        monsterLabel.setIcon(monsterIcon);

        // Set bounds to center the monster on the ground label
        monsterLabel.setBounds((groundLabel.getWidth() - 16) / 2, (groundLabel.getHeight() - 16) / 2, 16, 16);

        // Add the monster label to the ground label
        groundLabel.setLayout(null); // Allow precise positioning
        groundLabel.add(monsterLabel); // Overlay the monster
        groundLabel.setName(monsterType); // Update the label's state to indicate it's occupied by a monster
        groundLabel.revalidate();
        groundLabel.repaint();
    }


   
}
