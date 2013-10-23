package imagesimilarity;

// Contains definition for Sobel filter
public class FilterSobelOld {
	private static int normalizeFactor = 4;
	
	// 3x3 Sobel filter that find horizontal edges
	private static int[][] filterX = {
		{1, 2, 1},
		{0, 0, 0},
		{-1, -2, -1}
	};	
	
	// 3x3 Sobel filter that find vertical edges
	private static int[][] filterY = {
		{-1, 0, 1},
		{-2, 0, 2},
		{-1, 0, 1}
	};
	
	public static int[][] getFilterX() {
		return filterX;
	}

	public static int[][] getFilterY() {
		return filterY;
	}

	public static int getNormalizeFactor() {
		return normalizeFactor;
	}
}
