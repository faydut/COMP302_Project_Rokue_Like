package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class BuildMode {
    private JFrame frame;
    private JPanel hallPanel, objectPanel, bottomPanel;
    private JButton nextButton, playButton;
    private String groundPath= "src/assets/items/floor_mud_e.png";
    private String wallPath1 = "src/assets/items/wall_gargoyle_red_1.png";
    private String wallPath2 = "src/assets/items/wall_goo.png";
    private String wallPath3 = "src/assets/items/wall_gratings.png";
    
    private CreateImageIcon iconCreator = new CreateImageIcon();
    
    private final int GRID_ROWS = 12;
    private final int GRID_COLS = 12;
    private  final String closeDoor = "src/assets/items/door_closed.png";

    private String[] hallNames = {"Hall Of Earth", "Hall Of Air", "Hall Of Water", "Hall of Fire"};
    private int[] minRequirements = {2, 2, 2, 2};
    private int currentHallIndex = 0;

    private int currentObjectCount = 0; // Counter for objects in the hall
    private JLabel hallTitleLabel;
    private ArrayList<ImageIcon[][]> completedHalls = new ArrayList<>(); // List to store all completed halls


    private Icon[][] currentHallGrid; // 2D array to store the current hall grid state
   

    public ArrayList<ImageIcon[][]> getCompletedHalls() { // getter for completed halls arraylist
		return completedHalls;
	}

	public BuildMode() {
        frame = new JFrame("Build Mode");
        frame.setSize(1200, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        
        frame.getContentPane().setBackground(new Color(50, 34, 40));

        initializeObjectPanel();
        initializeHallPanel();
        initializeBottomPanel();

        try {
			loadCurrentHall();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        frame.setVisible(true);
    }

    private void initializeHallPanel() {     // it will initialize hall panel
        hallPanel = new JPanel(new GridLayout(GRID_ROWS, GRID_COLS));
        hallPanel.setBounds(50, 50, 800, 600);
        hallPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        hallPanel.setBackground(new Color(60, 40, 50));
        frame.add(hallPanel);

        hallTitleLabel = new JLabel();
        hallTitleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        hallTitleLabel.setForeground(Color.white);
        hallTitleLabel.setBounds(50, 10, 400, 30);
        frame.add(hallTitleLabel);
    }

    private void initializeObjectPanel() {
        // Custom JPanel with a background image that scales while maintaining aspect ratio
           objectPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Draw the background image with scaling while maintaining aspect ratio
                try {
                    ImageIcon backgroundIcon = new ImageIcon("src/assets/items/build_mode.png");
                    Image backgroundImage = backgroundIcon.getImage();

                    // Get original image dimensions
                    int imageWidth = backgroundImage.getWidth(this);
                    int imageHeight = backgroundImage.getHeight(this);

                    // Calculate the scaling factor to fit the panel dimensions while preserving aspect ratio
                    double scale = Math.min((double) getWidth() / imageWidth, (double) getHeight() / imageHeight);

                    // Calculate new dimensions
                    int scaledWidth = (int) (imageWidth * scale);
                    int scaledHeight = (int) (imageHeight * scale);

                    // Center the image within the panel
                    int x = (getWidth() - scaledWidth) / 2;
                    int y = (getHeight() - scaledHeight) / 2;

                    // Draw the scaled image
                    g.drawImage(backgroundImage, x, y, scaledWidth, scaledHeight, this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        objectPanel.setBackground(new Color(0, 0, 0, 0));
        objectPanel.setBounds(800, 30, 250, 600); // Panel bounds
        objectPanel.setLayout(null); // Absolute positioning for precise alignment

        // Add the Exit button at the top center
        JButton exitButton = new JButton();
        exitButton.setIcon(new ImageIcon("src/assets/items/exit16.png")); // Exit button image path
        exitButton.setBounds((250 - 32) / 2, 10, 32, 32); // Centered horizontally in a 250px wide panel
        exitButton.setContentAreaFilled(false); // Remove background
        exitButton.setOpaque(false);           // Ensure transparency
        exitButton.setBorderPainted(false);    // Remove border
        exitButton.setFocusPainted(false);     // Disable focus outline
        exitButton.setBackground(new Color(0, 0, 0, 0)); // Fully transparent background
        exitButton.addActionListener(e -> System.exit(0)); // Exit on click
        objectPanel.add(exitButton);

        // Add draggable objects
        addDraggableObject("src/assets/items/box.png", 110, 130);
        addDraggableObject("src/assets/items/boxes_stacked.png", 110, 170);
        addDraggableObject("src/assets/items/chest_closed.png", 110, 210);
        addDraggableObject("src/assets/items/column_wall.png", 110, 250);
        addDraggableObject("src/assets/items/floor_ladder.png", 110, 290);
        addDraggableObject("src/assets/items/skull.png", 110, 330);
        addDraggableObject("src/assets/items/torch_no_flame.png", 110, 370);
        addDraggableObject("src/assets/items/flask_big_blue.png", 110, 410);
        addDraggableObject("src/assets/items/flask_big_green.png", 110, 450);

        frame.add(objectPanel);
    }

    private void initializeBottomPanel() {// for next and save button in bottom of screen
        bottomPanel = new JPanel();
        bottomPanel.setBounds(830, 650, 150, 30);
      //  bottomPanel.setBackground(new Color(30, 20, 40));
        bottomPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(new Color(50, 34, 40));
        nextButton = new JButton("Next Hall");
        nextButton.setFont(new Font("Arial", Font.BOLD, 16));
        nextButton.setBackground(new Color(50, 30, 60));
        nextButton.setForeground(new Color(50, 30, 60));
        nextButton.addActionListener(e -> {
			try {
				checkHallCompletion();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
        bottomPanel.add(nextButton);
     // Initialize the Play button (initially hidden)
        playButton = new JButton("Play");
        playButton.setFont(new Font("Arial", Font.BOLD, 16));
        playButton.setVisible(false); // Initially hidden
        playButton.addActionListener(e -> goToPlayMode());
        bottomPanel.add(playButton);

        frame.add(bottomPanel);
    }
    private void goToPlayMode() {
    	 SwingUtilities.invokeLater(() -> {
    		 frame.dispose();
             GameManager game = new GameManager(getCompletedHalls()); // Transition to GameManager
             try {
				game.startGame();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
         });
    }

    private void loadCurrentHall() throws Exception {
        int cellSize = 32;
        hallPanel.removeAll();
        currentObjectCount = 0;

        // Initialize the hall grid storage
        currentHallGrid = new Icon[GRID_ROWS][GRID_COLS];
        hallPanel.setLayout(null); // Use null layout for precise positioning
        hallPanel.setPreferredSize(new Dimension(GRID_COLS * cellSize, GRID_ROWS * cellSize));
        hallPanel.setBounds(50, 50, GRID_COLS * cellSize, GRID_ROWS * cellSize);

        updateHallTitle();
        ImageIcon floorIcon= iconCreator.getImageIcon(groundPath);
        floorIcon.setDescription("floor");
        
        ImageIcon wallIcon1= iconCreator.getImageIcon(wallPath1);
        wallIcon1.setDescription("wall");
        
        
       

        for (int row = 0; row < GRID_ROWS; row++) {
            for (int col = 0; col < GRID_COLS; col++) {
                final int currentRow = row; // Create a final copy of the row
                final int currentCol = col; // Create a final copy of the col

                JLabel cell = new JLabel();
                cell.setOpaque(true);
                cell.setBackground(new Color(80, 60, 70));
                //cell.setBorder(BorderFactory.createLineBorder(Color.black));
                cell.setHorizontalAlignment(SwingConstants.CENTER);
                cell.setVerticalAlignment(SwingConstants.CENTER);
                cell.setBounds(col * cellSize, row * cellSize, cellSize, cellSize);

                
                cell.setIcon(floorIcon);
                cell.setName("empty");
                if(row ==0 || col==0|| row == GRID_ROWS-1 || col== GRID_COLS-1) {
                	
                	    cell.setIcon(null);
                		cell.setIcon(wallIcon1);
                		cell.setName("wall");
                	
                	
                }
                // Custom cell logic (e.g., close door)
                if (row == GRID_ROWS - 1 && col == 3) {
                    ImageIcon icon = new ImageIcon(closeDoor);
                    icon.setDescription("door");
                    cell.setIcon(icon);
                }

                // Enable drag-and-drop functionality
                cell.setTransferHandler(new TransferHandler("icon") {
                    @Override
                    public boolean canImport(TransferSupport support) {
                        return support.isDataFlavorSupported(TransferableIcon.ICON_FLAVOR) &&
                               "empty".equals(cell.getName());
                    }

                    @Override
                    public boolean importData(TransferSupport support) {
                        if (canImport(support)) {
                            try {
                                ImageIcon newIcon = (ImageIcon) support.getTransferable().getTransferData(TransferableIcon.ICON_FLAVOR);

                                // Place the new object in the cell
                                addObjectOverlay(cell, newIcon);
                                currentHallGrid[currentRow][currentCol] = newIcon;
                                currentObjectCount++;
                                updateHallTitle();

                                return true;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        return false;
                    }                });

                hallPanel.add(cell);
            }
            hallPanel.revalidate();
            hallPanel.repaint();
        }
        }

    

    private void updateHallTitle() {
        String title = hallNames[currentHallIndex] + " " + currentObjectCount + "/" + minRequirements[currentHallIndex];
        hallTitleLabel.setText(title);
    }

    private void checkHallCompletion() throws Exception {
    	
        if (currentObjectCount >= minRequirements[currentHallIndex]) {
            //JOptionPane.showMessageDialog(frame, hallNames[currentHallIndex] + " is completed!", "Success", JOptionPane.INFORMATION_MESSAGE);
            
            // Save the completed hall grid
            saveCurrentHall();

            currentHallIndex++;
            if (currentHallIndex < hallNames.length) {
                loadCurrentHall();
               // JOptionPane.showMessageDialog(frame, bottomPanel);
            } else {
            	JOptionPane.showMessageDialog(frame, "All halls are completed! Click Play to start!", "Congratulations", JOptionPane.INFORMATION_MESSAGE);
                nextButton.setVisible(false); // Hide "Next" button
                playButton.setVisible(true);  // Show "Play" button
            }
        } else {
        	
            JOptionPane.showMessageDialog(frame, "You need at least " + minRequirements[currentHallIndex] + " objects to complete " + hallNames[currentHallIndex] + ".", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveCurrentHall() {
        ImageIcon[][] savedHall = new ImageIcon[GRID_ROWS][GRID_COLS];

        // Iterate through the hallPanel to get all JLabels
        for (Component comp : hallPanel.getComponents()) {
        	
            if (comp instanceof JLabel) {
                JLabel cell = (JLabel) comp;
                int row = cell.getY() / 32; // Calculate row based on position
                int col = cell.getX() / 32; // Calculate col based on position

                ImageIcon overlayIcon = getOverlayIcon(cell);
               
                // Save the overlay icon in the hall array if present
                if (overlayIcon != null) {
                	
                	overlayIcon.setDescription("object");
                    savedHall[row][col] = overlayIcon;
                    
                    //savedHall[row][col].set
                }
                else {
                	savedHall[row][col] = (ImageIcon) cell.getIcon();    
                }
                
                        }
        }

        // Add the saved hall icons to the list of completed halls
        completedHalls.add(savedHall);
         

        System.out.println("Hall saved with overlay icons successfully.");
    }

    private ImageIcon getOverlayIcon(JLabel cell) {
        for (Component child : cell.getComponents()) {
            if (child instanceof JLabel) {
                JLabel overlayLabel = (JLabel) child;
                ImageIcon overlayIcon = (ImageIcon) overlayLabel.getIcon();
                if (overlayIcon != null) {
                	
                	return overlayIcon;
                }
                }
        }
        return null; // No overlay icon present
    }


    private void addDraggableObject(String imagePath, int x, int y) {
        try {
            ImageIcon objectIcon = new ImageIcon(imagePath);

            JLabel objectLabel = new JLabel();
            objectLabel.setIcon(objectIcon);
            objectLabel.setBounds(x, y, 32, 32); // Position objects with custom coordinates
            objectLabel.setTransferHandler(new TransferHandler("icon") {
                @Override
                protected Transferable createTransferable(JComponent c) {
                    return new TransferableIcon((ImageIcon) ((JLabel) c).getIcon());
                }

                @Override
                public int getSourceActions(JComponent c) {
                    return COPY;
                }
            });

            objectLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    JComponent comp = (JComponent) e.getSource();
                    TransferHandler handler = comp.getTransferHandler();
                    if (handler != null) {
                        handler.exportAsDrag(comp, e, TransferHandler.COPY);
                    }
                }
            });

            objectPanel.add(objectLabel);
            objectPanel.revalidate();
            objectPanel.repaint();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    private void addObjectOverlay(JLabel groundLabel, ImageIcon objectIcon) {
        // Resize the object icon if needed
        //objectIcon = new ImageIcon(objectIcon.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH)); // Resize to 16x16

        // Overlay the object icon on the ground label
        groundLabel.setLayout(null); // Allow precise positioning of components

        JLabel objectOverlay = new JLabel();
        objectOverlay.setIcon(objectIcon);
        objectOverlay.setBounds((groundLabel.getWidth() - 16) / 2, (groundLabel.getHeight() - 16) / 2, 16, 16); // Center the object

        groundLabel.add(objectOverlay); // Add object overlay
        // Update the label's state to indicate it's occupied
        objectIcon.setDescription("object");
        groundLabel.revalidate();
        groundLabel.repaint();
    }

}