package imagesimilarity;

public class NormalSimilarity {
	
	public static double getScore(ProcessedImage img1, ProcessedImage img2) {
		double score = 0;
		int[][] hist1 = img1.getColorHist();
		int[][] hist2 = img2.getColorHist();
		
		for(int i = 0; i < 3; i++)
			for(int j = 0; j < 256; j++)
			{
				double diff = Math.abs(hist1[i][j]-hist2[i][j]);
				double max = Math.max(hist1[i][j], hist2[i][j]);
				score += hist1[i][j] * (1 - (diff / max));
			}
		
		return score;
	}
}