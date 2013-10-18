package imagesimilarity;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class SobelFilter {
	private static final SobelFilter instance = new SobelFilter();
	
	// 3x3 Sobel filter that find horizontal edges
	private static int[][] filterX = {
		{1, 2, 1},
		{0, 0, 0},
		{-1, -2, -1}
	};	
	
	// 3x3 Sobel filter that find vertical edges
	private static int[][] filterY = {
		{-1, 0, 1},
		{-2, 0, 2},
		{-1, 0, 1}
	};
	
	private SobelFilter() {};
	
	public static SobelFilter getInstance() {
		return instance;
	}
	
	// Execute Sobel filter on the image passed in as parameter.
	// The original is preserved as the filter is applied to its clone.
	public static BufferedImage apply(BufferedImage image) {
		int width = image.getWidth();
		int height = image.getHeight();
		int imageType = image.getType();
		BufferedImage result = new BufferedImage(width, height, imageType); 
		
		// Copy image to result, prevent overwriting the original image.
		clone(image, result);
		result = apply(filterX, result);
		result = apply(filterX, result);
		
		
		
		
		
		return null;
	}
	
	private static void clone(BufferedImage orig, BufferedImage clone) {
		Graphics g = clone.createGraphics();
		g.drawImage(orig, 	// source image
					0,		// x-coord to start drawing
					0,		// y-coord to start drawing
					null);  // object to be notified of progress
		g.dispose();
	}
	
	private static BufferedImage apply(int[][] filter, BufferedImage image) {
		for (int i = 0; i < image.getWidth(); i++) {
			for (int j = 0; j < image.getHeight(); j++) {
				// For each pixel in the image, we apply the filter
				for (int ii = 0; ii < filter.length; ii++) {
					for (int jj = 0; jj < filter[0].length; jj++) {
						int sum = 0;
						
					}
				}
			}
		}
		
		
		
		return null;
	}
	
}
