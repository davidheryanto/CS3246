package imagesimilarity;

public class EdgeSimilarity {
	
	public static double getScore(ProcessedImage img1, ProcessedImage img2) {
		double score = 0;
		int[][] hist1 = img1.getEdgeHist();
		int[][] hist2 = img2.getEdgeHist();
		
		for(int i = 0; i < 3; i++)
			for(int j = 0; j < 8; j++)
			{
				double diff = Math.abs(hist1[i][j]-hist2[i][j]);
				double max = Math.max(hist1[i][j], hist2[i][j]);
				
				if (max <= 0) {
					// both the sizes are 0, ignore this
					continue; 
				}
				
				score += hist1[i][j] * (1 - (diff / max));
			}
		
		return score;
	}
	
}