package imagesimilarity;

import imagesimilarity.ColorCoherence.Result;

public class ProcessedImage {
	private String filePath;
	private Result[] CCV;
	
	public Result[] getCCV() {
		return CCV;
	}
	public void setCCV(Result[] cCV) {
		CCV = cCV;
	}
}
