package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class BuildMode {
    private JFrame frame;
    private JPanel hallPanel, objectPanel, bottomPanel;
    private JButton nextButton, playButton;

    private final int GRID_ROWS = 12;
    private final int GRID_COLS = 12;
    private  final String closeDoor = "src/assets/items/door_closed.png";

    private String[] hallNames = {"Earth Hall", "Air Hall", "Water Hall", "Fire Hall"};
    private int[] minRequirements = {6, 9, 13, 17};
    private int currentHallIndex = 0;

    private int currentObjectCount = 0; // Counter for objects in the hall
    private JLabel hallTitleLabel;

    private Icon[][] currentHallGrid; // 2D array to store the current hall grid state
    private ArrayList<Icon[][]> completedHalls = new ArrayList<>(); // List to store all completed halls

    public ArrayList<Icon[][]> getCompletedHalls() { // getter for completed halls arraylist
		return completedHalls;
	}

	public BuildMode() {
        frame = new JFrame("Build Mode");
        frame.setSize(1200, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        initializeObjectPanel();
        initializeHallPanel();
        initializeBottomPanel();

        loadCurrentHall();

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
        hallTitleLabel.setForeground(Color.BLACK);
        hallTitleLabel.setBounds(50, 10, 400, 30);
        frame.add(hallTitleLabel);
    }

    private void initializeObjectPanel() {
        objectPanel = new JPanel();
        objectPanel.setBounds(900, 50, 250, 600);
        objectPanel.setLayout(new BoxLayout(objectPanel, BoxLayout.Y_AXIS));
        objectPanel.setBackground(new Color(50, 30, 60));

        JLabel title = new JLabel("Build Mode");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        objectPanel.add(title);

        addDraggableObject("src/assets/rokue-like assets/wizard.png");
        addDraggableObject("src/assets/rokue-like assets/fighter.png");
        addDraggableObject("src/assets/rokue-like assets/archer.png");

        frame.add(objectPanel);
    }

    private void initializeBottomPanel() {// for next and save button in bottom of screen
        bottomPanel = new JPanel();
        bottomPanel.setBounds(1000, 650, 150, 30);
      //  bottomPanel.setBackground(new Color(30, 20, 40));
        bottomPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

        nextButton = new JButton("Next Hall");
        nextButton.setFont(new Font("Arial", Font.BOLD, 16));
        nextButton.setBackground(new Color(30, 20, 40));
        nextButton.setForeground(new Color(30, 20, 40));
        nextButton.addActionListener(e -> checkHallCompletion());
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
             game.startGame();
            
         });
    }

    private void loadCurrentHall() {
        hallPanel.removeAll();
        currentObjectCount = 0;

        // Initialize the hall grid storage
        currentHallGrid = new Icon[GRID_ROWS][GRID_COLS];

        updateHallTitle();

        for (int row = 0; row < GRID_ROWS; row++) {
            for (int col = 0; col < GRID_COLS; col++) {
            	 
            		 JLabel cell = new JLabel();
                     cell.setOpaque(true);
                     cell.setBackground(new Color(80, 60, 70));
                     cell.setBorder(BorderFactory.createLineBorder(Color.black));
                     cell.setHorizontalAlignment(SwingConstants.CENTER);
     	             cell.setVerticalAlignment(SwingConstants.CENTER);
     	             if(row==GRID_ROWS-1 && col ==3 ) {
     	            	 ImageIcon icon= new ImageIcon(closeDoor);
     	            	 cell.setIcon(icon);
     	             }

                     // Set TransferHandler to accept icons
                     cell.setTransferHandler(new TransferHandler("icon") {
                         @Override
                         public boolean canImport(TransferHandler.TransferSupport support) {
                             // Allow drop only if the cell is empty
                             return cell.getIcon() == null;
                         }

                         @Override
                         public boolean importData(TransferHandler.TransferSupport support) {
                             if (cell.getIcon() == null) {
                                 return super.importData(support);
                             }
                             return false; // Disallow if already occupied
                         }
                     });
                     

                     final int r = row, c = col;
                     cell.addPropertyChangeListener("icon", evt -> {
                         // Triggered when an icon is successfully dropped
                         if (cell.getIcon() != null && currentHallGrid[r][c] == null) {
                             currentHallGrid[r][c] = cell.getIcon(); // Update grid state
                             currentObjectCount++;
                             updateHallTitle();
                         }
                     });

                     hallPanel.add(cell);
            		
            	}
            	
               
            
        }

        hallPanel.revalidate();
        hallPanel.repaint();
    }
    private boolean isCellEmpty(int row, int col) {
        return currentHallGrid[row][col] == null; // Return true if the grid cell is empty
    }

    private void updateHallTitle() {
        String title = hallNames[currentHallIndex] + " " + currentObjectCount + "/" + minRequirements[currentHallIndex];
        hallTitleLabel.setText(title);
    }

    private void checkHallCompletion() {
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
        Icon[][] savedHall = new Icon[GRID_ROWS][GRID_COLS];
        for (int i = 0; i < GRID_ROWS; i++) {
            System.arraycopy(currentHallGrid[i], 0, savedHall[i], 0, GRID_COLS);
        }
        completedHalls.add(savedHall);
    }

    private void addDraggableObject(String imagePath) {
        ImageIcon icon = new ImageIcon(imagePath);
        Image scaledImage = icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        JLabel objectLabel = new JLabel(scaledIcon);
        
        objectLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        objectLabel.setTransferHandler(new TransferHandler("icon"));

        objectLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
            	
                JComponent comp = (JComponent) e.getSource();
                TransferHandler handler = comp.getTransferHandler();
                handler.exportAsDrag(comp, e, TransferHandler.COPY);
            }
            @Override
            public void mouseReleased(MouseEvent e) {
            	
                currentObjectCount++; // Increment when the mouse is released
                updateHallTitle();
            }
        });

        objectPanel.add(objectLabel);
        objectPanel.add(Box.createVerticalStrut(10));
    }

    
}
