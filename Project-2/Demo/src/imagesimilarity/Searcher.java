package imagesimilarity;

public class Searcher {
	
	private boolean isCheckedNormalHistogram = true; // default
	private boolean isCheckedCCV;
	private boolean isCheckedEdge;
	
	public void setCheckedNormalHistogram(boolean isCheckedNormalHistogram) {
		this.isCheckedNormalHistogram = isCheckedNormalHistogram;
	}
	public void setCheckedCCV(boolean isCheckedCCV) {
		this.isCheckedCCV = isCheckedCCV;
	}
	public void setCheckedEdge(boolean isCheckedEdge) {
		this.isCheckedEdge = isCheckedEdge;
	}
	
	
}