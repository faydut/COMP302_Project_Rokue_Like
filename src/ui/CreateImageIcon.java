package ui;

import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;

public class CreateImageIcon {
	private ImageResizer resizer = new ImageResizer();
	
	
	
	public ImageIcon getImageIcon (String path, int targetWidth,int targetHeight) throws Exception {
		
		BufferedImage image = resizer.convertImage(path,targetWidth, targetHeight);
        ImageIcon icon = new ImageIcon(image);
        return icon;
		
		
	}
	
	

}
