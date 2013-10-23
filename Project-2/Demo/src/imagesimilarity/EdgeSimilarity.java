package imagesimilarity;

public class EdgeSimilarity {
	
	public static double getScore(ProcessedImage img1, ProcessedImage img2) {
		//double score = 0.0;
		int[][] hist1 = img1.getEdgeHist();
		int[][] hist2 = img2.getEdgeHist();
		
		double distance = calculateDistance(hist1, hist2);
		
		return 1-distance;
	}
	
	private double calculateDistance(int[][] array1, int[][] array2) {
		
	}
}