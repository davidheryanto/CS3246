package imagesimilarity;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Indexer {
	
	private static int SIZE = 256;
	// Red, Green, Blue   
	private static int NUMBER_OF_COLOURS = 3;

	private static int RED = 0;
	private static int GREEN = 1;
	private static int BLUE = 2;
	
	public static void index(BufferedImage image, int fileNum, String outputName) {
		int width = image.getWidth();
		int height = image.getHeight();
		int[][] hist = new int[NUMBER_OF_COLOURS][SIZE];
				
		for (int i = 0; i < NUMBER_OF_COLOURS; i++) {
			for (int j = 0; j < SIZE; j++) {
				hist[i][j] = 0;
			}
		}
		
		for(int i = 0; i < width; i++)
			for(int j = 0; j < height; j++) 
			{
				Color c = new Color(image.getRGB(i, j));
				hist[RED][c.getRed()]++;
				hist[GREEN][c.getGreen()]++;
				hist[BLUE][c.getBlue()]++;
			}
		
		try {
			PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(outputName,true)));
			writer.println(fileNum);	//print image number
			
			for(int i = 0; i < NUMBER_OF_COLOURS; i++)
			{
				if(i==0)
					writer.print("RED ");
				if(i==1)
					writer.print("GREEN ");
				if(i==2)
					writer.print("BLUE ");
				for(int j = 0; j < SIZE; j++)
				{
					writer.print(hist[i][j]);
					writer.print(" ");
				}
				writer.println();
			}
			
			writer.println("****************");
			writer.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}