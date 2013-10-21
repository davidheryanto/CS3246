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
		// BufferedImage bi = blur(image);
		// BufferedImage discreteColors = discretizeColors(bi);
		//TODO: compute connected components
		//TODO: compute coherent color histogram
		
		return null;
	}
}