package imagesimilarity;

import imagesimilarity.ColorCoherence.Result;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Indexer {
	
	private static int SIZE = 256;
	// Red, Green, Blue   
	private static int NUMBER_OF_COLOURS = 3;

	private static int RED = 0;
	private static int GREEN = 1;
	private static int BLUE = 2;
	
	public static void index(BufferedImage image, int fileNum, String outputFile) {
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
			PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(outputFile,true)));
			writer.println(fileNum);	//print image number
			
			for(int i = 0; i < NUMBER_OF_COLOURS; i++)
			{
				/*if(i==0)
					writer.print("RED ");
				if(i==1)
					writer.print("GREEN ");
				if(i==2)
					writer.print("BLUE ");*/
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
	
	public static void indexCCV(Result[] CCVarray, int fileNum, String outputFile) {
		
		try {
			PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(outputFile,true)));
			writer.println(fileNum);	//print image number
			
			for(int i = 0; i < CCVarray.length; i++)
			{
				/*if(i==0)
					writer.print("RED ");
				if(i==1)
					writer.print("GREEN ");
				if(i==2)
					writer.print("BLUE ");*/
				for(int j = 0; j < CCVarray[i].coherent.length; j++)
				{
					writer.print(CCVarray[i].coherent[j]);
					writer.print(" ");
				}
				writer.println();
				for(int j = 0; j < CCVarray[i].incoherent.length; j++)
				{
					writer.print(CCVarray[i].incoherent[j]);
					writer.print(" ");
				}
				writer.println();
				writer.println();
			}
			
			writer.println("****************");
			writer.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static int[][] readIndex(String fileNum, String filename) throws IOException {
		String redLine = null;
		String greenLine = null;
		String blueLine = null;
		int[][] hist = new int[NUMBER_OF_COLOURS][SIZE];
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));
			while(br.readLine()!=fileNum);
			redLine = br.readLine();
			greenLine = br.readLine();
			blueLine = br.readLine();
			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		hist[0] = scan(redLine);
		hist[1] = scan(greenLine);
		hist[2] = scan(blueLine);
		
		return hist;
	}
	
	private static int[] scan(String line) {
		int[] array = new int[SIZE];
		
		Scanner sc = new Scanner(line);
		for(int i = 0; i < SIZE; i++)
			array[i] = sc.nextInt();
		
		return array;
	}
}