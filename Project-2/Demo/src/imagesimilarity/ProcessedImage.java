package imagesimilarity;

import imagesimilarity.ColorCoherence.Result;

public class ProcessedImage {
	private String fileName;
	private String filePath;
	private Result[] CCV;
	private int[][] edgeHist;
	private int[][] colorHist;
	
	public ProcessedImage() {
		
	}
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
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
	
	@Override
	public boolean equals(Object o) {
	    return (o instanceof ProcessedImage && 
	    		this.getFileName() == ((ProcessedImage) o).getFileName());
	}

}
