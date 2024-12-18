package ui;

import javax.swing.*;

import java.awt.Color;
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
        
        JLabel monsterLabel = grid[row][col];

        switch (type) {
            case 0 -> {
                monster = new ArcherMonster(row, col);
                ImageIcon image= new ImageIcon(archer);
                monsterLabel.setIcon(image);
                monsterLabel.setName("nonempty");
                
               // monsterLabel.setForeground(Color.red);
            }
            case 1 -> {
                monster = new FighterMonster(row, col, grid);
                ImageIcon image= new ImageIcon(fighter);
                monsterLabel.setIcon(image);// Fighter symbol
                monsterLabel.setName("nonempty");
                //monsterLabel.setForeground(Color.orange);
            }
            case 2 -> {
                monster = new WizardMonster(row, col, grid,objectList);
                ImageIcon image= new ImageIcon(wizard);
                monsterLabel.setIcon(image);
                monsterLabel.setName("nonempty");
            }
            default -> throw new IllegalStateException("Unexpected monster type: " + type);
        }

        
       // monsterLabel.setOpaque(true);
        //monsterLabel.setBackground(Color.BLACK);
        // Add the monster to the list of active monsters
        
        monsters.add(monster);
       
        
    }

   
}
