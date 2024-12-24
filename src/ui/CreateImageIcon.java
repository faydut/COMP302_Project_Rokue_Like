package ui;

import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;

public class CreateImageIcon {
	private ImageResizer resizer = new ImageResizer();
	
	
	
	public ImageIcon getImageIcon (String path) throws Exception {
		
		BufferedImage image = resizer.convertImage(path);
        ImageIcon icon = new ImageIcon(image);
        return icon;
		
		
	}
	
	

}
