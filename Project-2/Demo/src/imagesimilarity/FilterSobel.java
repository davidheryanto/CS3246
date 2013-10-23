package imagesimilarity;

import java.awt.Color;
import java.awt.image.BufferedImage;

// Contains definition for Sobel filter
public class FilterSobel {
	private static int normalizeFactor = 4;
	
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
	
	public static int[][] getFilterX() {
		return filterX;
	}

	public static int[][] getFilterY() {
		return filterY;
	}

	public static int getNormalizeFactor() {
		return normalizeFactor;
	}
	
	public static BufferedImage apply(BufferedImage img) {
		int width = img.getWidth();
		int height = img.getHeight();
		int imgType = img.getType();
		int filterRadius = filterX.length / 2;

		BufferedImage result = new BufferedImage(width, height, imgType);

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				int redX = 0;
				int greenX = 0;
				int blueX = 0;
				
				int redY = 0;
				int greenY = 0;
				int blueY = 0;

				// For each pixel in the img, we apply the filter.
				for (int ii = i - filterRadius, fi = 0; 
						ii <= i + filterRadius; 
						ii++, fi++) {
					for (int jj = j - filterRadius, fj = 0;
							jj <= j + filterRadius;
							jj++, fj++) {
						// Check if index is out of bounds.
						if (!(ii >= 0 && ii < width 
						   && jj >= 0 && jj < height)) {
							continue;
						}
						// Get color of the pixel of source img
						Color c = new Color(img.getRGB(ii, jj));

						// Apply the filter to all 3 colors
						redX += c.getRed() * filterX[fi][fj]; 
						greenX += c.getGreen() * filterX[fi][fj];
						blueX += c.getBlue() * filterX[fi][fj];
						
						redY += c.getRed() * filterY[fi][fj]; 
						greenY += c.getGreen() * filterY[fi][fj];
						blueY += c.getBlue() * filterY[fi][fj];
					}
				}
				
				double red = Math.toDegrees(Math.atan2(redY, redX));
				double green = Math.toDegrees(Math.atan2(greenY, greenX));
				double blue = Math.toDegrees(Math.atan2(blueY, blueX));
				
				int redDir = convertToDir(red);
				int greenDir = convertToDir(green);
				int blueDir = convertToDir(blue);
				

				// Replace the pixel rgb value with the new one
				result.setRGB(i, j, new Color(redDir, greenDir, blueDir).getRGB());
			}
		}

		return result;
	}

	// Convert angle to 8 distinct directions eg east, northeast, north, etc...
	private static int convertToDir(double angle) {
		if (angle < 0) {
			angle += 360;
		}

		if (angle >= 337.5 || angle < 22.5) {
			return 0;
		} else if (angle < 67.5) {
			return 1;
		} else if (angle < 112.5) {
			return 2;
		} else if (angle < 157.5) {
			return 3;
		} else if (angle < 202.5) {
			return 4;
		} else if (angle < 247.5) {
			return 5;
		} else if (angle < 292.5) {
			return 6;
		} else {
			return 7;
		}
	}
}
