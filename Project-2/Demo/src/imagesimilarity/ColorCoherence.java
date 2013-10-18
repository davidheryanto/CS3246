package imagesimilarity;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class ColorCoherence {
	
	private static final ColorCoherence instance = new ColorCoherence();
	
	private ColorCoherence() {};
	
	public static ColorCoherence getInstance() {
		return instance;
	}
	
	public static int[] getCCV(BufferedImage image) {
		
		int[] CCV;
		BufferedImage bi = blur(image);
		BufferedImage discreteColors = discretizeColors(bi);
		//TODO: compute connected components
		//TODO: compute coherent color histogram
		
		return CCV;
	}
	
	
	private static BufferedImage blur(BufferedImage image) {
		int width = image.getWidth();
		int height = image.getHeight();
		int imageType = image.getType();
		BufferedImage result = new BufferedImage(width, height, imageType);
		
		for(int i = 0; i < width; i++)
			for(int j = 0; j < height; j++)
			{
				int n = 0;
				int red = 0;
				int green = 0;
				int blue = 0;
				for(int ii = i-1; ii < i + 1; ii++)
					for(int jj = j - 1; jj < j + 1; jj++)
					{
						if(!(ii >= 0 && ii < width &&
							jj >= 0 && jj < height))
							continue;
						Color c = new Color(image.getRGB(ii, jj));
						red += c.getRed();
						green += c.getGreen();
						blue += c.getBlue();
						n++;
					}
				
				red = red > 0 ? red / n : 0;
				green = green > 0 ? green / n : 0;
				blue = blue > 0 ? blue / n : 0;
				
				result.setRGB(i, j, new Color(red, green, blue).getRGB());
			}
		
		return result;
	}
	
	private static BufferedImage discretizeColors(BufferedImage image) {
		int width = image.getWidth();
		int height = image.getHeight();
		int imageType = image.getType();
		BufferedImage result = new BufferedImage(width, height, imageType);
		
		for(int i = 0; i < width; i++)
			for(int j = 0; j < height; j++)
			{
				int red = 0;
				int green = 0;
				int blue = 0;
				Color c = new Color(image.getRGB(i, j));
				//TODO: reduce the number of colors to n colors
			}
		
		return result;
	}
}