package monster;

import javax.swing.*;

import player.Player;

import java.awt.Color;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ui.Cell;
import ui.GameManager;
import ui.ObjectOverlay;

public class MonsterSpawner {
    private Cell[][] grid;
    private Random random = new Random();
    private Player player;
    private ArrayList<Monster> monsters = new ArrayList<>();
    private ArrayList<FighterMonster> fighterMonsters = new ArrayList<>();
    private ArrayList<Cell> objectList;
    private String archer = "src/assets/rokue-like assets/archer.png";
    private String fighter = "src/assets/rokue-like assets/fighter.png";
    private String wizard = "src/assets/rokue-like assets/wizard.png";
    private Timer spawnTimer;
    private Timer actionTimer;
    private boolean isPaused = false;
    private GameManager gameManager;
    private FighterMonster fighterMonster;
    ObjectOverlay objectOverlay= new ObjectOverlay();
    
    public ArrayList<Monster> getMonsters() {
		return monsters;
	}

	//private Timer actionTimer;

    public MonsterSpawner( Player player, ArrayList<Cell> objectList, GameManager gameManager) {
        this.grid = gameManager.getGridLabels();
        this.player = player;
        this.objectList=  objectList;
        this.gameManager = gameManager;
    }
   
    
    public void startSpawning() {
        if (spawnTimer == null) {
            spawnTimer = new Timer(8000, e -> {
                try {
                    spawnMonster();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
            gameManager.addTimer(spawnTimer);
            System.out.println("8 seconds passed");
        }
        spawnTimer.start();

        if (actionTimer == null) {
            actionTimer = new Timer(1000, e -> {
                // Remove inactive monsters
                monsters.removeIf(monster -> monster instanceof WizardMonster && !((WizardMonster) monster).isActive());

                // Execute `act` for all active monsters
                for (Monster monster : monsters) {
                    if (monster instanceof WizardMonster wizardMonster) {
                        // Ensure wizard behavior triggers once per zone
                        wizardMonster.act(player);
                    } else {
                        monster.act(player); // Other monsters act normally
                    }
                }
            });
            gameManager.addTimer(actionTimer);
        }
        actionTimer.start();
    }
    
    public void stopSpawning() {
        if (spawnTimer != null) {
            spawnTimer.stop();
            System.out.println("Monster spawning stopped.");
        }
        if (actionTimer != null) {
            actionTimer.stop();
        }
        monsters.clear();
    }
    
    public void pauseSpawning() {
        if (!isPaused) {
            if (spawnTimer != null) spawnTimer.stop();
            if (actionTimer != null) actionTimer.stop();
            isPaused = true;
        }
    }

    public void resumeSpawning() {
        if (isPaused) {
            if (spawnTimer != null) spawnTimer.start();
            if (actionTimer != null) actionTimer.start();
            isPaused = false;
        }
    }

    private void spawnMonster() throws Exception {
    	

    	//int cellSize = 50; 
        int row, col;

        // Find a random empty cell
        row = random.nextInt(grid.length-1);
        col = random.nextInt(grid[0].length-1);
       
        boolean checkEmpty= grid[row][col].getIsEmpty();
          // playerin hareketiyle ayni zamanda gelebilirler.w
       
        while(checkEmpty != true) {
        	 
        	row = random.nextInt(grid.length-1);
            col = random.nextInt(grid[0].length-1);
            checkEmpty= grid[row][col].getIsEmpty();
           
           
        }
        
        Monster monster;
        int type = 2;//random.nextInt(3);
        
        Cell groundLabel = grid[row][col];

        switch (2) {
            case 0 -> {
                monster = new ArcherMonster(row, col, gameManager);
                objectOverlay.overlayLabel(groundLabel, new ImageIcon(archer), 32);
                
               // monsterLabel.setForeground(Color.red);
            }
            case 1 -> {
                monster = new FighterMonster(row, col,  gameManager);
               // System.out.println("fighter in  monster spawner");
                fighterMonster= (FighterMonster) monster;
                fighterMonsters.add(fighterMonster);
                System.out.println("fighter in  monster spawner:"+fighterMonster);
                objectOverlay.overlayLabel(groundLabel, new ImageIcon(fighter), 32);
                //monsterLabel.setForeground(Color.orange);
            }
            case 2 -> {
                monster = new WizardMonster(row, col, objectList, gameManager);
                objectOverlay.overlayLabel(groundLabel, new ImageIcon(wizard), 32);
            }
            default -> throw new IllegalStateException("Unexpected monster type: " + type);
        }

      
        
        monsters.add(monster);
       
        
    }

	public ArrayList<FighterMonster> getFighterMonsters() {
		return fighterMonsters;
	}

	

   
}
