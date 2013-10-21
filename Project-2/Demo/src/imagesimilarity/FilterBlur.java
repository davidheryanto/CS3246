package imagesimilarity;

// Contains definition for blur filter
public class FilterBlur {
	
	private static int normalizeFactor;
	private static int[][] filter;
	private static int radius = 3;

	public static int[][] getFilter() {
		// Set filter with dimension radius x radius, all filled with 1
		filter = new int[radius][radius];
		for (int i = 0; i < filter.length; i++) {
			for (int j = 0; j < filter[0].length; j++) {
				filter[i][j] = 1;
			}
		}
		
		// Set the normalize factor to radius^2
		FilterBlur.normalizeFactor = radius * radius;
		
		return filter;
	}

	public static void setRadius(int radius) {
		FilterBlur.radius = radius;
	}
	
	public static int getNormalizeFactor() {
		return normalizeFactor;
	}
	
}