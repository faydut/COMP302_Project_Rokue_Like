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
        // Spawn monsters every 8 seconds
        Timer spawnTimer = new Timer(8000, e -> spawnMonster());
       

        // Make all monsters act every second
       
        spawnTimer.start();
        Timer actionTimer = new Timer(1000, e -> {
            for (Monster monster : monsters) {
                monster.act(player); // Each monster acts periodically
            }
        });
        actionTimer.start();
       
    }

    private void spawnMonster() {
    	

    	int cellSize = 60; 
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
                
               // monsterLabel.setForeground(Color.red);
            }
            case 1 -> {
                monster = new FighterMonster(row, col, grid);
                ImageIcon image= new ImageIcon(fighter);
                monsterLabel.setIcon(image);// Fighter symbol
                //monsterLabel.setForeground(Color.orange);
            }
            case 2 -> {
                monster = new WizardMonster(row, col, grid,objectList);
                ImageIcon image= new ImageIcon(wizard);
                monsterLabel.setIcon(image);
            }
            default -> throw new IllegalStateException("Unexpected monster type: " + type);
        }

        
       // monsterLabel.setOpaque(true);
        //monsterLabel.setBackground(Color.BLACK);
        // Add the monster to the list of active monsters
        
        monsters.add(monster);
       
        
    }

   
}
