package imagesimilarity;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

// Class containing helper methods to modify image pixels
public class ImageHelper {
	
	// Resize image proportionally to targetWidth px
	public static BufferedImage resize(BufferedImage img, int targetWidth) {
		int width = img.getWidth();
		int height = img.getHeight();
		double ratio = (double) targetWidth / width;
		int targetHeight = (int) (ratio * height);
		
		BufferedImage result = new BufferedImage(targetWidth, targetHeight, img.getType());
		Graphics g = result.createGraphics();
		g.drawImage(img, 0, 0, targetWidth, targetHeight, null);
		g.dispose();
	
		return result;
	}
	
	// Get the gradient direction (after applying sobel filter)
	// imgH and imgV refers to image after applying horizontal edge and vertical edge filter resp.
	public static BufferedImage getGradient(BufferedImage imgH, BufferedImage imgV) {
		int width = imgH.getWidth();
		int height = imgH.getHeight();
		int imgType = imgH.getType();
		BufferedImage result = new BufferedImage(width, height, imgType);

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				Color cH = new Color(imgH.getRGB(i, j));
				Color cV = new Color(imgV.getRGB(i, j));

				double red = Math.toDegrees(Math.atan2(-cV.getRed(), cH.getRed()));
				double green = Math.toDegrees(Math.atan2(cV.getGreen(), cH.getGreen()));
				double blue = Math.toDegrees(Math.atan2(cV.getBlue(), cH.getBlue()));
				
//				System.out.printf("(%.1f, %.1f, %.1f\t", red, green, blue);
				
				
				int redDir = convertToDir(red);
				int greenDir = convertToDir(green);
				int blueDir = convertToDir(blue);
				
				result.setRGB(i, j, new Color(redDir, greenDir, blueDir).getRGB());
			}
		}
		return result;
	}
	
	// Conver angle to 8 distinct directions eg east, northeast, north, etc...
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
	
	
	
	// print pixel value of the images into console
	public static void print(BufferedImage img) {
		int width = img.getWidth();
		int height = img.getHeight();
		
		int colCount = 10;
		int rowCount = 10;
		
		int incrementX = width / colCount;
		int incrementY = height / rowCount;
		
		for (int i = 0; i < width; i += incrementX) {
			for (int j = 0; j < height; j += incrementY) {
				Color c = new Color(img.getRGB(i, j));
				int red = c.getRed();
				int green = c.getGreen();
				int blue = c.getBlue();

				System.out.printf("(%d, %d, %d)\t", red, green, blue);
			}
			System.out.println();
		}
	}
}
