package ui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class ImageResizer {

    public BufferedImage convertImage(String path) throws Exception {
        try {
           

            // Check if the file exists
            File imageFile = new File(path);
            if (!imageFile.exists()) {
                throw new IllegalArgumentException("File does not exist at path: " + path);
            }

            // Read the input image
            BufferedImage originalImage = ImageIO.read(imageFile);
            if (originalImage == null) {
                throw new IllegalArgumentException("Failed to read the image. The file may not be a valid image: " + path);
            }

           

            // Determine the scaling factor to fit within 32x32
            int originalWidth = originalImage.getWidth();
            int originalHeight = originalImage.getHeight();
            int targetSize = 32;

            double scaleFactor = Math.min((double) targetSize / originalWidth, (double) targetSize / originalHeight);

            // Calculate the new dimensions
            int newWidth = (int) (originalWidth * scaleFactor);
            int newHeight = (int) (originalHeight * scaleFactor);

            // Resize the image to the new dimensions
            BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = resizedImage.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2d.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
            g2d.dispose();

            // Create a new 32x32 image and center the resized image within it
            BufferedImage finalImage = new BufferedImage(targetSize, targetSize, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2dFinal = finalImage.createGraphics();

            // Fill the background with transparent or any desired color
            g2dFinal.setColor(new Color(0, 0, 0, 0)); // Transparent background
            g2dFinal.fillRect(0, 0, targetSize, targetSize);

            // Center the resized image in the 32x32 canvas
            int xOffset = (targetSize - newWidth) / 2;
            int yOffset = (targetSize - newHeight) / 2;
            g2dFinal.drawImage(resizedImage, xOffset, yOffset, null);
            g2dFinal.dispose();

            
            return finalImage;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
