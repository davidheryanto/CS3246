package imagesimilarity;

import java.awt.Color;
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
	// The original image is preserved
	public static BufferedImage apply(BufferedImage image) {
		
		BufferedImage resultX = apply(filterX, image);
		BufferedImage resultY = apply(filterY, image);
		BufferedImage result = add(resultX, resultY);
		
		return result;
	}
	
	private static BufferedImage apply(int[][] filter, BufferedImage image) {
		int width = image.getWidth();
		int height = image.getHeight();
		int imageType = image.getType();

		BufferedImage result = new BufferedImage(width, height, imageType);

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				int red = 0;
				int green = 0;
				int blue = 0;

				// For each pixel in the image, we apply the filter.
				for (int ii = i - 1, fi = 0; ii < i + 2 ; ii++, fi++) {
					for (int jj = j - 1, fj = 0; jj < j + 2; jj++, fj++) {
						// Check if index is out of bounds.
						if (!(ii >= 0 && ii < width 
						   && jj >= 0 && jj < height)) {
							continue;
						}
						Color c = new Color(image.getRGB(ii, jj));

						int t = c.getGreen();
						int tt = filter[fi][fj];
						
						red += c.getRed() * filter[fi][fj]; 
						green += c.getGreen() * filter[fi][fj];
						blue += c.getBlue() * filter[fi][fj];
					}
				}
				
				// normalize the rgb value so it cannot exceed 255 and must be positive
				red = red > 0 ? red / 4 : 0;
				green = green > 0 ? green / 4 : 0;
				blue = blue > 0 ? blue / 4 : 0;

				// Replace the pixel rgb value with the new one
				
				result.setRGB(i, j, new Color(red, green, blue).getRGB());
			}
		}

		return result;
	}

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
}
