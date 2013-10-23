package imagesimilarity;

import imagesimilarity.ColorCoherence.Result;

public class ProcessedImage {
	private String filePath;
	private Result[] CCV;
	private int[][] edgeHist;
	
	public Result[] getCCV() {
		return CCV;
	}
	public void setCCV(Result[] cCV) {
		CCV = cCV;
	}
	public int[][] getEdgeHist() {
		return edgeHist;
	}
	public void setEdgeHist(int[][] hist) {
		edgeHist = hist;
	}
}
