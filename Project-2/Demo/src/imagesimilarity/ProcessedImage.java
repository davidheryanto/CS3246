package imagesimilarity;

import imagesimilarity.ColorCoherence.Result;

public class ProcessedImage {
	private String filePath;
	private Result[] CCV;
	
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
	
	// add other data eg histogram etc
}
