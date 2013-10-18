package imagesimilarity;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;

public class ColorSimilarity {
	
	private static final ColorSimilarity instance = new ColorSimilarity();
	
	private ColorSimilarity() {};
	
	public static ColorSimilarity getInstance() {
		return instance;
	}
	
	public static double[][] computeColorSimilarity(BufferedImage image) {
		int width = image.getWidth();
		int height = image.getHeight();
		double[][] colorSim = new double[width*height][width*height];
		Raster raster = image.getRaster();
		double[][] y = new double[width][height],
				cb = new double[width][height],
				cr = new double[width][height];
		int n1=0, n2=0;
		
		for(int i = 0; i < width; i++)
			for(int j = 0; j < height; j++)
			{
				int r = raster.getSample(i,j,0);
            	int g = raster.getSample(i,j,1);
            	int b = raster.getSample(i,j,2);
            	y[i][j]  = (int)( 0.299   * r + 0.587   * g + 0.114   * b);
        		cb[i][j] = (int)(-0.16874 * r - 0.33126 * g + 0.50000 * b);
        		cr[i][j] = (int)( 0.50000 * r - 0.41869 * g - 0.08131 * b);
			}
		
		for(int i = 0; i < width; i++)
			for(int j = 0; j < height; j++)
			{
				for(int ii = 0; ii < width; ii++)
					for(int jj = 0; jj < height; jj++)
					{
						double dist = Math.sqrt( Math.pow(y[i][j]-y[ii][jj], 2) + Math.pow(cb[i][j]-cb[ii][jj], 2) + Math.pow(cr[i][j]-cr[ii][jj], 2) );
						colorSim[n1][n2] = dist;
						n2++;
					}
				n1++;
			}
		
		return colorSim;
	}
}