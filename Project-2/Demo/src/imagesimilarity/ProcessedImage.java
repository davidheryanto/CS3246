package imagesimilarity;

import imagesimilarity.ColorCoherence.Result;

public class ProcessedImage {
	private String filePath;
	private Result[] CCV;
	private int[][] edgeHist;
	private int[][] colorHist;
	
	public ProcessedImage() {
		
	}
	
	public String getFilePath() {
		return filePath;
	}
	
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
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

	public int[][] getColorHist() {
		return colorHist;
	}
	
	public void setColorHist(int[][] hist) {
		colorHist = hist;
	}
	// add other data eg histogram etc

}
