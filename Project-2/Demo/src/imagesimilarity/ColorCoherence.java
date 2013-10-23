package imagesimilarity;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

// TODO: set threshold as a % of image size

// Class to compute the color coherence vector of an image
// Usage:
// ColorCoherence.setQuantizationLevel(...);
// ColorCoherence.setThreshold(...);
// ColorCoherence.extract(BufferedImage ...);
// 
// ColorCoherence.getResults() returns Result[]
// *Result class defined at the bottom
// 
public class ColorCoherence {
	// Singleton pattern
	// -----------------
	private static final ColorCoherence instance = new ColorCoherence();

	private ColorCoherence() { }

	public static ColorCoherence getInstance() {
		return instance;
	}
	// End Singleton
	// -------------

	// Default values
	private static int quantizationLevel = 16;
	// The threshold determines whether a pixel block is coherent
	private static int threshold = 4;

	public static void setQuantizationLevel(int level) {
		ColorCoherence.quantizationLevel = level;
	}

	public static void setThreshold(int threshold) {
		ColorCoherence.threshold = threshold;
	}

	// For i-th region in Red channel,
	// we have level red[i] and size = redSize[i] ... for other colors
	private static List<Integer> redList = new ArrayList<Integer>();
	private static List<Integer> redSizeList = new ArrayList<Integer>();

	private static List<Integer> greenList = new ArrayList<Integer>();
	private static List<Integer> greenSizeList = new ArrayList<Integer>();

	private static List<Integer> blueList = new ArrayList<Integer>();
	private static List<Integer> blueSizeList = new ArrayList<Integer>();

	private static Result[] results;
	
	public static Result[] getResults() {
		return results;
	}

	// Extract the CCV for the given image
	// Fill up the results on color, coherent and incoherent array
	// The index these arrays correspond to each other e.g.
	// color[i] has coherent[i] size of coherent pixels
	//          and incoherent[i] size of incoherent pixels
	public static void extract(BufferedImage img) {
		int width = img.getWidth();
		int height = img.getHeight();

		int[][] Red = new int[width][height];
		int[][] Green = new int[width][height];
		int[][] Blue = new int[width][height];
		
		// Blur and quantize image
		Filter.setFilter(FilterBlur.getFilter());
		Filter.setNormalizeFactor(FilterBlur.getNormalizeFactor());
		img = Filter.apply(img);
		img = ImageHelper.quantizeColor(img, quantizationLevel);
		
		// Set the rgb values of the images on the 3 arrays above
		setMatrix(img, Red, Green, Blue);

		// Clear Lists
		redList.clear();
		redSizeList.clear();

		greenList.clear();
		greenSizeList.clear();

		blueList.clear();
		blueSizeList.clear();
		
		
		// Create and label different regions for all three channels
		// Label shows which pixel has been visited
		int[][] Label = new int[Red.length][Red[0].length];
		int size = label(Red, Label, redList, redSizeList);
		while (size > 0) {
			// Not all fields have been labelled
			size = label(Red, Label, redList, redSizeList);
		}
		
		// Do the same for green and blue channels
		// Reset Label
		Label = new int[Green.length][Green[0].length];
		size = label(Green, Label, greenList, greenSizeList);
		while (size > 0) {
			size = label(Green, Label, greenList, greenSizeList);
		}
		
		Label = new int[Blue.length][Blue[0].length];
		size = label(Blue, Label, blueList, blueSizeList);
		while (size > 0) {
			size = label(Blue, Label, blueList, blueSizeList);
		}
		
		setResults();
		
		
		// Test
		// ---------------
		//		int[][] test = {
		//				{2,1,2,2,1,1},
		//				{2,2,1,2,1,1},
		//				{2,1,3,2,1,1},
		//				{2,2,2,1,3,3},
		//				{2,2,1,1,3,3},
		//				{2,2,1,1,3,3}
		//		};
		//		Label = new int[test.length][test[0].length];
		//		print(test);
		//		int size = label(test, Label, redList, redSizeList);
		//		while (size > 0) {
		//			// Not all fields have been labelled
		//			size = label(test, Label, redList, redSizeList);
		//		}
		// End Test 
		// ----------------
	}
	
	private static void setResults() {
		// result 1, 2, 3 -> r, g, b 
		results = new Result[3];
		for (int i = 0; i < results.length; i++) {
			results[i] = new Result();
		}
		setResultsForColor(results[0], redList, redSizeList);
		setResultsForColor(results[1], greenList, greenSizeList);
		setResultsForColor(results[2], blueList, blueSizeList);
		
//		print(results[0].coherent);
//		print(results[0].incoherent);
//		
//		System.out.println();
//		
//		print(results[1].coherent);
//		print(results[1].incoherent);
//		
//		System.out.println();
//		
//		print(results[2].coherent);
//		print(results[2].incoherent);
	}

	private static void setResultsForColor(
			Result result, List<Integer> levelList, List<Integer> levelSizeList) {
		
		result.coherent = new int[quantizationLevel];
		result.incoherent = new int[quantizationLevel];
		
		for (int i = 0; i < levelList.size(); i++) {
			if (levelSizeList.get(i) >= threshold) {
				result.coherent[levelList.get(i)] += levelSizeList.get(i);
			} else {
				result.incoherent[levelList.get(i)] += levelSizeList.get(i);
			}
		}
	}

	// Call the recusive function
	// and return the size of the region.
	// If no more unlabelled pixel, return -1.
	private static int label(int[][] A, int[][] L, List<Integer> Channel, List<Integer> Size) {
		// Find an unlabelled pixel
		// int dotCount = 0;
		
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
//			System.out.print("\rLabelling");
//			for (int k = 0; k < dotCount; k++) {
//				System.out.print(".");
//			}
//			dotCount = (dotCount + 1) % 30;
		}
		// No more unlabelled pixel
		return -1;
	}

	// Searching start from (x,y).
	// Image is represented by array A.
	// L[i][j] indicates whether pixel (i,j) is already labelled:
	// value of 1 means labelled, 0 otherwise.
	// Finally, we return the size of this region.
	private static int label(int[][] A, int[][] L, int x, int y, int size) {
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
				if (A[i][j] == A[x][y] && L[i][j] < 1) {
					//	System.out.println("Find same");
					size = label(A, L, i, j, size + 1);
				}
			}
		}
		return size;
	}



	private static void setMatrix(BufferedImage img, int[][] Red, int[][] Green, int[][] Blue) {
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

	// Helper methods
	private static void print(int[] A) {
		for (int i = 0; i < A.length; i++) {
			System.out.print(A[i] + "\t");
		}
		System.out.println();
	}
	
	private static void print(int[][] A) {
		for (int i = 0; i < A.length; i++) {
			for (int j = 0; j < A[0].length; j++) {
				System.out.printf("%d\t", A[i][j]);
			}
			System.out.println();
		}
		System.out.println();
	}

	private static void print(List<Integer> list) {
		for (int i : list) {
			System.out.printf("%d\t", i);
		}
		System.out.println();
	}
	
	static class Result {
		int[] coherent;
		int[] incoherent;
		
		Result(){};
	}
}