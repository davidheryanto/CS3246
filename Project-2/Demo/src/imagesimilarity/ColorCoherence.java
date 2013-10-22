package imagesimilarity;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

// Class to compute the color coherence vector of an image
public class ColorCoherence {
	private int[][] testArray = {
			{1,2,2,5,5,3,3,3},
			{2,1,1,4,4,5,3,2},
			{5,1,1,4,3,3,2,2},
			{2,2,2,5,2,3,2,2},
			{2,2,2,5,3,1,1,3},
			{5,5,5,5,3,4,1,1},
			{3,3,4,4,4,5,5,1},
			{3,3,4,4,5,5,5,5}
	};
	
	// TODO do blurring
	
	private int quantizationLevel;
	// The threshold determines whether a pixel block is coherent
	private int threshold;
//	private int[] color;
//	private int[] coherent;
//	private int[] incoherent;
	
	// For i-th region,
	// we have red[i] of size = redSize[i] ... for other colors
	private List<Integer> redList = new ArrayList<Integer>();
	private List<Integer> redSizeList = new ArrayList<Integer>();
	
	private List<Integer> greenList = new ArrayList<Integer>();
	private List<Integer> greenSizeList = new ArrayList<Integer>();
	
	private List<Integer> blueList = new ArrayList<Integer>();
	private List<Integer> blueSizeList = new ArrayList<Integer>();
	
	
	public ColorCoherence(int quantizationLevel, int threshold) {
		this.quantizationLevel = quantizationLevel;
//		color = new int[quantizationLevel];
//		coherent = new int[quantizationLevel];
//		incoherent = new int[quantizationLevel];
		
		this.threshold = threshold;
	}
	
//	public int[] getColor() {
//		return color;
//	}
//
//	public int[] getCoherent() {
//		return coherent;
//	}
//
//	public int[] getIncoherent() {
//		return incoherent;
//	}

	// Extract the CCV for the given image
	// Fill up the results on color, coherent and incoherent array
	// The index these arrays correspond to each other e.g.
	// color[i] has coherent[i] size of coherent pixels
	//          and incoherent[i] size of incoherent pixels
	public void extract(BufferedImage img) {
		int width = img.getWidth();
		int height = img.getHeight();
		
		int[][] Red = new int[width][height];
		int[][] Green = new int[width][height];
		int[][] Blue = new int[width][height];
		// Set the rgb values of the images on the 3 arrays
		setMatrix(img, Red, Green, Blue);
		
		// TODO Testing
		int[][] L = new int[testArray.length][testArray[0].length];
		print(testArray);
		
		int size = label(testArray, L, redList, redSizeList);
		while (size > 0) {
			size = label(testArray, L, redList, redSizeList);
		}
		
		System.out.println("--------------------------------------");
		print(redList);
		print(redSizeList);
		
		// End testing
		// ------------------
		
		// Create and label different regions
		
	}
	
	// Call the recusive function
	// and return the size of the region.
	// If no more unlabelled pixel, return -1.
	public int label(int[][] A, int[][] L, List<Integer> Channel, List<Integer> Size) {
		// Find an unlabelled pixel
		for (int i = 0; i < A.length; i++) {
			for (int j = 0; j < A[0].length; j++) {
				// Check if (i,j) is not labelled
				if (L[i][j] < 1) {
					int size = label(A, L, i, j, 1);
					
					Channel.add(A[i][j]);
					Size.add(size);
					
					return size;
				}
			}
		}
		// No more unlabelled pixel
		return -1;
	}
	
	// Searching start from (x,y).
	// Image is represented by array A.
	// L[i][j] indicates whether pixel (i,j) is already labelled:
	// value of 1 means labelled, 0 otherwise.
	// Finally, we return the size of this region.
	public int label(int[][] A, int[][] L, int x, int y, int size) {
		L[x][y] = 1;
		// Check its neighbour, 8-connectivity
		for (int i = x - 1; i <= x + 1; i++) {
			for (int j = y - 1; j <= y + 1; j++) {
				// Check for out of bounds
				if (i < 0 || i >= A.length || 
						j < 0 || j >= A[0].length || 
						(i == x && j == y)) {
					continue;
				}
				
				// Check if color(i,j) = color(x,y) and (i,j) is unlabelled.
				// If yes we recurse from there.
//				System.out.printf("A[%d][%d] = %d%n", x, y, A[x][y]);
//				System.out.printf("A[%d][%d] = %d%n", i, j, A[i][j]);
				if (A[i][j] == A[x][y] && L[i][j] < 1) {
//					System.out.println("Find same");
					size = label(A, L, i, j, size + 1);
				}
//				System.out.println();
			}
		}
		return size;
	}
	
	
	
	public void setMatrix(BufferedImage img, int[][] Red, int[][] Green, int[][] Blue) {
		int width = img.getWidth();
		int height = img.getHeight();
		
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				Color c = new Color(img.getRGB(i, j));
				Red[i][j] = c.getRed();
				Green[i][j] = c.getGreen();
				Blue[i][j] = c.getBlue();
			}
		}
	}
	

	public static int[] getCCV(BufferedImage image) {
		
		int[] CCV;
		// BufferedImage bi = blur(image);
		// BufferedImage discreteColors = discretizeColors(bi);
		//TODO: compute connected components
		//TODO: compute coherent color histogram
		
		return null;
	}
	
	// Helper methods
	public static void print(int[][] A) {
		for (int i = 0; i < A.length; i++) {
			for (int j = 0; j < A[0].length; j++) {
				System.out.printf("%d\t", A[i][j]);
			}
			System.out.println();
		}
	}
	
	public static void print(List<Integer> list) {
		for (int i : list) {
			System.out.printf("%d\t", i);
		}
		System.out.println();
	}
}