package enchantment;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import monster.ArcherMonster;
import monster.FighterMonster;
import monster.Monster;
import monster.WizardMonster;
import panel.InventoryPanel;
import player.Player;
import ui.Cell;
import ui.GameManager;
import ui.ObjectOverlay;
import ui.SoundPlayer;

import java.util.ArrayList;

public class EnchantmentManager {
	
	private Timer spawnTimer,actionTimer, removalTimer;
   	private Cell enchantmentCell;
	private boolean isPaused = false;
	private Random random = new Random();
	private Cell[][] grid;
	private int enchantmentRow, enchantmentCol;
	private GameManager gameManager;
	private ObjectOverlay objectOverlay= new ObjectOverlay();
	private String timePath = "src/assets/items/monster_goblin.png";
    private String livePath = "src/assets/items/monster_elemental_gold_short.png";
    private String luringGemPath = "src/assets/items/monster_necromancer.png";
    private String cloakOfProtectionPath = "src/assets/items/monster_elemental_air.png";
    private String revealPath = "src/assets/items/monster_elemental_plant.png";
    private  Enchantment enchantment;
    private String direction="";
    private boolean isLureModeActive = false; 
    private MouseListener enchantmentMouseListener;
    private  ArrayList<Cell >objectList;
    private  Player player;
    private  LinkedHashMap<String, List<Enchantment>> enchantmentsMap ;
    private  ArrayList<FighterMonster> fighterMonsters;
    private InventoryPanel inventoryPanel;
    private String time="time";
    private String live="live";
    private String luringGem= "luring gem";
    private String cloakOfProtection="cloak of protection";
    private String reveal= "reveal";
    
    
	
	public EnchantmentManager(GameManager gameManager, ArrayList<FighterMonster> fighterMonsters) {
		this.gameManager= gameManager;
		grid= gameManager.getGridLabels();
		this.fighterMonsters= fighterMonsters;
		player= gameManager.getPlayer();
		inventoryPanel= gameManager.getInventoryPanel();
		
		objectList= gameManager.getObjectList();
		enchantmentsMap= new LinkedHashMap<>();
	}
    
    public void startEnchantmentSpawning() {
    	System.out.println("system.out.println:"+spawnTimer);
    	if(spawnTimer!=null) {
    		spawnTimer.stop();
    		spawnTimer=null;
    	}
        if (spawnTimer == null) {
            spawnTimer = new Timer(12000, e -> {
				try {
					spawnEnchantment();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			});
            gameManager.addTimer(spawnTimer); // Register the spawn timer with GameManager
            System.out.println("12 seconds passed");
        }
        spawnTimer.start();
        


    }
    public void clearEnchantmentsMap() {
    	enchantmentsMap.clear();
    }
    public void resetSpawning() {
        if (spawnTimer != null) {
            spawnTimer.stop(); // Stop the existing timer
            System.out.println("Spawning timer reset.");
        }
        spawnTimer = new Timer(12000, e -> {
            try {
                spawnEnchantment();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        spawnTimer.start(); // Start a fresh timer
    }

    
   

	public void stopSpawning() {
        if (spawnTimer != null) {
            spawnTimer.stop();
            System.out.println("Monster spawning stopped.");
            gameManager.removeTimer(spawnTimer); // Remove the spawn timer from GameManager
        }
        if (actionTimer != null) {
            actionTimer.stop();
            gameManager.removeTimer(actionTimer); // Remove the action timer from GameManager
        }
        if (removalTimer != null) {
        	removalTimer.stop();
        	gameManager.removeTimer(removalTimer); // Remove the removal timer from GameManager
        }
       
    }
    
    public void pauseSpawning() {
        if (!isPaused) {
            if (spawnTimer != null) spawnTimer.stop();
            if (actionTimer != null) actionTimer.stop();
            if (removalTimer != null) removalTimer.stop();
            isPaused = true;
        }
    }

    public void resumeSpawning() {
        if (isPaused) {
            if (spawnTimer != null) spawnTimer.start();
            if (actionTimer != null) actionTimer.start();
            if (removalTimer != null) removalTimer.start();
            isPaused = false;
        }
    }
    public Cell findRuneCell() {
    	
		for(Cell cell: objectList) {
			if(cell.getCellRune().equals("rune")) {
				return cell;
			}
			
			
				
			
		}
		System.out.println("there is no rune in object list");
		return null;
	}

    
    
    public  void spawnEnchantment() throws Exception {

    	
      

        // Find a random empty cell
    	enchantmentRow = random.nextInt(grid.length-1);
    	enchantmentCol = random.nextInt(grid[0].length-1);
       
        boolean checkEmpty= grid[enchantmentRow][enchantmentCol].getIsEmpty();
        System.out.println("enchantment cell empty or not:"+checkEmpty);
       
        while(checkEmpty != true) {
        	 
        	enchantmentRow = random.nextInt(grid.length-1);
        	enchantmentCol = random.nextInt(grid[0].length-1);
            checkEmpty= grid[enchantmentRow][enchantmentCol].getIsEmpty();
           
           
        }
        
        
        
        int type = random.nextInt(5);
        System.out.println("enchantment row:"+enchantmentRow);
        System.out.println("enchantment col:"+enchantmentCol);
        enchantmentCell = grid[enchantmentRow][enchantmentCol];
        
        System.out.println("type:"+type);
        switch (type) {
            case 0 -> {
               enchantment = new ExtraTimeEnchantment(time, gameManager);
               objectOverlay.overlayLabel(enchantmentCell, new ImageIcon(timePath),32);
               enchantmentCell.setEmpty(false);
             //  enchantmentCell.setText("time");
               System.out.println("is in time");
               // monsterLabel.setForeground(Color.red);
            }
            case 1 -> {
            	 enchantment= new ExtraLiveEnchantment(live, gameManager.getPlayer());
            	 objectOverlay.overlayLabel(enchantmentCell, new ImageIcon(livePath),32);
            	 enchantmentCell.setEmpty(false);
            	// enchantmentCell.setText("live");
            	 System.out.println("is in live");
            }
            case 2 -> {
            	 enchantment= new RevealEnchantment(reveal, gameManager, findRuneCell().getCellXPosition(), findRuneCell().getCellYPosition());
            	 objectOverlay.overlayLabel(enchantmentCell, new ImageIcon(revealPath),32);
            	 enchantmentCell.setEmpty(false);
            	 //enchantmentCell.setText("reveal");
            	 System.out.println("is in reveal");
            	 
            }
            case 3 -> {
            	 enchantment= new CloakOfProtectionEnchantment(cloakOfProtection, gameManager.getPlayer());
            	 objectOverlay.overlayLabel(enchantmentCell, new ImageIcon(cloakOfProtectionPath),32);
            	 enchantmentCell.setEmpty(false);
            	// enchantmentCell.setText("cloak");
            	 System.out.println("is in cloak");
             }
            case 4 -> {
            	 enchantment= new LuringGemEnchantment(luringGem, fighterMonsters,player );
            	 objectOverlay.overlayLabel(enchantmentCell, new ImageIcon(luringGemPath),32);
            	 enchantmentCell.setEmpty(false);
            	// enchantmentCell.setText("luring");
            	 System.out.println("is in luring");
             }
            default -> throw new IllegalStateException("Unexpected monster type: " + type);
        }
        
        addEnchantmentMouseListener(enchantmentCell, enchantment );

        // Start the removal timer
        if (removalTimer != null) {
            removalTimer.stop(); // Stop any existing removal timer
        }
        removalTimer = new Timer(6000, e -> removeEnchantmentfromHall(enchantmentCell));
        System.out.println("6 seconds passed");
        removalTimer.setRepeats(false); // Ensure the timer only runs once
        gameManager.addTimer(removalTimer); // Register the removal timer with GameManager
        removalTimer.start();

        
        
        // Schedule removal after 6 seconds
        
      
		
		
	}
    
    private void removeEnchantment(String type) throws Exception {
        if (checkEnchantmentInMap(type)) {
            List<Enchantment> enchantmentList = enchantmentsMap.get(type);
            enchantmentList.remove(0); // Remove the first enchantment of the specified type
            inventoryPanel.addInventoryItems(enchantmentsMap);
            
            if (enchantmentList.isEmpty()) {
                enchantmentsMap.remove(type);
                inventoryPanel.addInventoryItems(enchantmentsMap);
            }
            System.out.println("Removed one " + type + " enchantment. Updated map: " + enchantmentsMap);
        } else {
            System.out.println("No enchantments of type: " + type + " to remove.");
        }
    }
    private boolean checkEnchantmentInMap(String enchantName) {
    	System.out.println("hashmap in check:"+enchantmentsMap);
    	System.out.println("type in check:"+enchantName);    
    	if(enchantmentsMap.containsKey(enchantName)) {
    		return true;
    	}
    	return false;
    }
    
    private Enchantment getEnchantmentFromMap(String type) {
        if (checkEnchantmentInMap(type)) {
            return enchantmentsMap.get(type).get(0); // Return the first enchantment of the specified type
        }
        System.out.println("No enchantments of type: " + type + " found.");
        return null;
    }
    
    	
    
    
    

    private void addEnchantment(Cell cell, Enchantment enchantment) throws Exception {
        System.out.println("Adding enchantment: " + enchantment.getType());
        
        enchantmentsMap.computeIfAbsent(enchantment.getType(), k -> new ArrayList<>()).add(enchantment);
        System.out.println("map in add enchantment:"+enchantmentsMap);
        
        inventoryPanel.addInventoryItems(enchantmentsMap);

        System.out.println("Updated enchantments map: " + enchantmentsMap);
        removeEnchantmentfromHall(cell);
    }
    
    
    private void addEnchantmentMouseListener(Cell cell, Enchantment type) {
        // Add a MouseListener to detect clicks on the enchantment cell
    	enchantmentMouseListener = new MouseAdapter() {
        
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    System.out.println("Enchantment clicked: " + type);
                    System.out.println("name of enchantment is:"+type.getType());
                     
                    
                    
              	  if (type instanceof ExtraTimeEnchantment) {
              		  System.out.println("yes this is time enchantment");
                        ((ExtraTimeEnchantment) type).useEnchantment(); // am not sure. look at here:
                        removeEnchantmentfromHall(cell);
                    }
              	  else if (type instanceof ExtraLiveEnchantment) {
              		System.out.println("yes this is live enchantment");
                     ((ExtraLiveEnchantment) type).useEnchantment(); // am not sure. look at here:
                       removeEnchantmentfromHall(cell);
                 }
              	  else {
              		  System.out.println("ench not time or live");
              		  try {
						addEnchantment(cell, type);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
              		  
              	  }
              	
                }
            }
            
        };
        cell.addMouseListener(enchantmentMouseListener);
        
        
    }

    private void handleDirectionalKey(KeyEvent e) throws Exception {
    	System.out.println("Lure mode activated. Press A, D, W, or S to throw the lure.");
        switch (e.getKeyCode()) {
            case KeyEvent.VK_A:
            	System.out.println("A");
                direction = "A"; // Left
                break;
            case KeyEvent.VK_D:
            	System.out.println("d");
                direction = "D"; // Right
                break;
            case KeyEvent.VK_W:
            	System.out.println("w");
                direction = "W"; // Up
                break;
            case KeyEvent.VK_S:
            	System.out.println("s");
                direction = "S"; // Down
                break;
            default:
                System.out.println("Invalid key pressed in lure mode. Press A, D, W, or S.");
                return;
        }

        if (!direction.isEmpty()) {
            System.out.println("Lure thrown in direction: " + direction);
            SoundPlayer.playSound("src/assets/sounds/luringGemSound.wav");
            Enchantment enchantment = getEnchantmentFromMap(luringGem);
            if (enchantment instanceof LuringGemEnchantment) {
            	((LuringGemEnchantment) enchantment).setLuringActiveOfFighter(true);
                ((LuringGemEnchantment) enchantment).useEnchantment(direction); // Use the enchantment
               
            }
            removeEnchantment(luringGem); // Remove the used enchantment from the bag
            
            isLureModeActive = false; // Exit lure mode
            direction = ""; // reset direction;
        }
    }

public void addKeyListener(JFrame frame) {
    	
        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
            	System.out.println("before key pressed hash map:"+enchantmentsMap);
                if (e.getKeyCode() == KeyEvent.VK_R) {
                    System.out.println("R key pressed. Highlighting the area...");
                        if(checkEnchantmentInMap(reveal)) {
                        	System.out.println("yes in may bag");
                        	getEnchantmentFromMap(reveal).useEnchantment(); 
                            try {
								removeEnchantment(reveal);
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
                        }
//                        else {
//                        	
//                        	String  errorMessage= "Reveal enchantment is not in your bag";
//                        	System.out.println(errorMessage);
//                        	JOptionPane.showMessageDialog(frame, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
//                        }
                    }
                   
                
                if (e.getKeyCode() == KeyEvent.VK_P) {
                    System.out.println("P key pressed. protect the player...");
                   if(checkEnchantmentInMap(cloakOfProtection)) {
                	   getEnchantmentFromMap(cloakOfProtection).useEnchantment();
                       try {
						removeEnchantment(cloakOfProtection);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
                    	
                    }
//                    else {
//                    	String  errorMessage=  "Cloak of protection is not in your bag";
//                    	System.out.println(errorMessage);
//                    	JOptionPane.showMessageDialog(frame, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
//                    }

                }

                if (e.getKeyCode() == KeyEvent.VK_B) {
                    System.out.println("B key pressed. Entering lure mode...");
                    System.out.println("enc map: in press:"+enchantmentsMap);
                    if (checkEnchantmentInMap(luringGem)) {
                        isLureModeActive = true; // Activate lure mode
                        
                        System.out.println("Lure mode activated. Press A, D, W, or S to throw the lure.");
                    } 
//                    else {
//                        String errorMessage = "Luring gem is not in your bag";
//                        System.out.println(errorMessage);
//                        JOptionPane.showMessageDialog(frame, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
//                    }
                }
                if (isLureModeActive) {
                	try {
						handleDirectionalKey(e);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} // Handle directional keys while in lure mode
                }

                
                
                }
        });
    }

    
    public void removeEnchantmentfromHall(Cell cell) {
    	if (enchantmentMouseListener != null) {
            cell.removeMouseListener(enchantmentMouseListener);
            enchantmentMouseListener = null; // Clean up the reference
        }
    	objectOverlay.revertToGround(cell);
    	
    	
    }

}
