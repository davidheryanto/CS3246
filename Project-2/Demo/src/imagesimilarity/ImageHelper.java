package imagesimilarity;

import java.awt.Color;
import java.awt.image.BufferedImage;

// Class containing helper methods to modify images
public class ImageHelper {
	
	// Get the average of the two images
	public static BufferedImage add(BufferedImage img1, BufferedImage img2) {
		int width = img1.getWidth();
		int height = img1.getHeight();
		int imgType = img1.getType();
		
		BufferedImage result = new BufferedImage(width, height, imgType);
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				Color c1 = new Color(img1.getRGB(i, j));
				Color c2 = new Color(img2.getRGB(i, j));
				
				int red = (c1.getRed() + c2.getRed()) / 2;
				int green = (c1.getGreen() + c2.getGreen()) / 2;
				int blue = (c1.getBlue() + c2.getBlue()) / 2;
				
				result.setRGB(i, j, new Color(red, green, blue).getRGB());
			}
		}
		return result;
	}
	
	// Quantize the color of an image into n distinct level for each rgb channel
	// Assume that original image contains 256 levels of rgb channel
	public static BufferedImage quantizeColor(BufferedImage img, int n) {
		
		
		
		
		
	}
}
