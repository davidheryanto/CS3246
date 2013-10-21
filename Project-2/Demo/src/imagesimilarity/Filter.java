package imagesimilarity;

import java.awt.Color;
import java.awt.image.BufferedImage;

// Filter applies given filter to an image
public class Filter {
	private int[][] filter;
	private int defaultNormalizeFactor = 9;
	
	public Filter() {
	
	}

	// Set the filter to the given filter
	public void setFilter(int[][] filter) {
		this.filter = filter;
	}
	
	public BufferedImage apply(BufferedImage img) {
		return apply(img, defaultNormalizeFactor);
	}
	
	public BufferedImage apply(BufferedImage img, int normalizeFactor) {
		int width = img.getWidth();
		int height = img.getHeight();
		int imgType = img.getType();
		int filterRadius = filter.length / 2;

		BufferedImage result = new BufferedImage(width, height, imgType);

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				int red = 0;
				int green = 0;
				int blue = 0;

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
						red += c.getRed() * filter[fi][fj]; 
						green += c.getGreen() * filter[fi][fj];
						blue += c.getBlue() * filter[fi][fj];
					}
				}
				
				// normalize the rgb value so it cannot exceed 255 and must be positive
				red = red > 0 ? red / normalizeFactor : 0;
				green = green > 0 ? green / normalizeFactor : 0;
				blue = blue > 0 ? blue / normalizeFactor : 0;

				// Replace the pixel rgb value with the new one
				result.setRGB(i, j, new Color(red, green, blue).getRGB());
			}
		}

		return result;
	}
	
	
}
