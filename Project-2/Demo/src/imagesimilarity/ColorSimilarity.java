package imagesimilarity;

public class ColorSimilarity {
	
	public static double[][] computeColorSimilarity() {
		double Tcolor = 0.1;
		int size = 256;
		double[][] colorSim = new double[size*size*size][size*size*size];
		double[][][] y = new double[size][size][size],
				cb = new double[size][size][size],
				cr = new double[size][size][size];
		int n1=0, n2=0;
		
		for(int i = 0; i < size; i++)
			for(int j = 0; j < size; j++)
				for(int k = 0; k < size; k++)
				{
					int r = i;
	            	int g = j;
	            	int b = k;
	            	y[i][j][k]  = (int)( 0.299   * r + 0.587   * g + 0.114   * b);
	        		cb[i][j][k] = (int)(-0.16874 * r - 0.33126 * g + 0.50000 * b);
	        		cr[i][j][k] = (int)( 0.50000 * r - 0.41869 * g - 0.08131 * b);
				}
		
		for(int i = 0; i < size; i++)
			for(int j = 0; j < size; j++)
				for(int k = 0; k < size; k++)
				{
					for(int ii = 0; ii < size; ii++)
						for(int jj = 0; jj < size; jj++)
							for(int kk = 0; kk < size; kk++)
						{
							double dist = Math.sqrt( Math.pow(y[i][j][k]-y[ii][jj][kk], 2) + 
										Math.pow(cb[i][j][k]-cb[ii][jj][kk], 2) + 
										Math.pow(cr[i][j][k]-cr[ii][jj][kk], 2) );
							if(dist > Tcolor)
								colorSim[n1][n2] = 0;
							else
								colorSim[n1][n2] = 1 - (dist/Tcolor);
							n2++;
						}
					n1++;
				}
		
		return colorSim;
	}
}