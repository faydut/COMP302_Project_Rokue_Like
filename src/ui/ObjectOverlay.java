package ui;

import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class ObjectOverlay {
	
	
	
	 public void addPlayerOverlay(JLabel groundLabel, String  playerPath) {
	        // Create a player icon
	        ImageIcon playerIcon = new ImageIcon(playerPath);
	        playerIcon = new ImageIcon(playerIcon.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH)); // Resize to 16x16
	          
	        // Overlay the player icon on the ground label
	        groundLabel.setLayout(null); // Allow precise positioning of components

	        JLabel playerOverlay = new JLabel();
	        playerOverlay.setIcon(playerIcon);
	        playerOverlay.setName("nonempty");
	        playerOverlay.setBounds((groundLabel.getWidth() - 16) / 2, (groundLabel.getHeight() - 16) / 2, 16, 16); // Center the player

	        groundLabel.add(playerOverlay); // Add player overlay
	        
	        groundLabel.revalidate();
	        groundLabel.repaint();
	    }
	
	
	 public void addObjectOverlay(JLabel groundLabel, ImageIcon objectIcon) {
	        // Resize the object icon if needed
	        //objectIcon = new ImageIcon(objectIcon.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH)); // Resize to 16x16

	        // Overlay the object icon on the ground label
	    	
	    			
	        groundLabel.setLayout(null); // Allow precise positioning of components

	        JLabel objectOverlay = new JLabel();
	        objectOverlay.setIcon(objectIcon);
	        objectOverlay.setBounds((groundLabel.getWidth() - 16) / 2, (groundLabel.getHeight() - 16) / 2, 16, 16); // Center the object
	        objectOverlay.setName("nonempty");
	        groundLabel.add(objectOverlay); // Add object overlay
	        // Update the label's state to indicate it's occupied

	        groundLabel.revalidate();
	        groundLabel.repaint();
	    }
	    public  void addObjectOverlayForDoor(JLabel groundLabel, ImageIcon objectIcon) {
	        // Resize the object icon if needed
	        //objectIcon = new ImageIcon(objectIcon.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH)); // Resize to 16x16

	        // Overlay the object icon on the ground label
	    	
	    			
	        groundLabel.setLayout(null); // Allow precise positioning of components

	        JLabel objectOverlay = new JLabel();
	        objectOverlay.setIcon(objectIcon);
	        int overlayWidth = 30;  // Width of the overlay
	        int overlayHeight = 30; // Height of the overlay
	        int x = 0;             // Align to the leftmost side
	        int y = (groundLabel.getHeight() - overlayHeight) / 2; // Center vertically

	        objectOverlay.setBounds(x,y ,overlayWidth, overlayHeight); // Center the object
	        objectOverlay.setName("nonempty");
	        groundLabel.add(objectOverlay); // Add object overlay
	        // Update the label's state to indicate it's occupied

	        groundLabel.revalidate();
	        groundLabel.repaint();
	    }
	    

	    public void revertToGround(JLabel label) {
	        ImageIcon groundIcon = (ImageIcon) label.getIcon(); // Get the current icon
	        if (groundIcon != null) {
	            label.setIcon(groundIcon); // Revert to ground icon
	            label.setName("empty"); // Mark as empty
	            label.removeAll(); // Remove any overlays
	            System.out.println("it is removes");
	            label.revalidate();
	            label.repaint();
	        }
	    }

}
