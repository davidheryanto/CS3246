package imagesimilarity;

import java.awt.Color;
import java.awt.image.BufferedImage;

// Class containing helper methods to modify image pixels
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
	// and quantization is linear i.e. each level is mapped from same # of distinct orig color 
	//
	// The returned image will have max level of n in each rgb channel
	// e.g. for red = [0...n)
	public static BufferedImage quantizeColor(BufferedImage img, int n) {
		int width = img.getWidth();
		int height = img.getHeight();
		int imgType = img.getType();
		BufferedImage result = new BufferedImage(width, height, imgType);

		// Assume orig color has 256 levels
		int originalLevelCount = 256;
		int rangePerLevel = originalLevelCount / n;
		
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				Color c = new Color(img.getRGB(i, j));
				int red = c.getRed();
				int green = c.getGreen();
				int blue = c.getBlue();

				// quantize each color
				result.setRGB(i, j, new Color(
						red / rangePerLevel,
						green / rangePerLevel,
						blue / rangePerLevel
						).getRGB());
			}
		}
		
		return result;
	}
}
