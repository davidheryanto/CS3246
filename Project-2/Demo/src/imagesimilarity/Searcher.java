package imagesimilarity;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class Searcher {
	

	private static final String EdgeIndex = "EdgeIndex.txt";
	private boolean isCheckedNormalHistogram = true; // default;
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
	
	public String[] search(BufferedImage inputImage) throws IOException {
		String[] rankedResults;// = new String[20];
		double[] scores;
		ProcessedImage img;
		
		scores = computeSimilarity(img);	
		
		rankedResults = sortScores(scores);
		
		return rankedResults;
	}
	
	private double[] computeSimilarity(ProcessedImage img1) throws IOException {
		double[] scores = new double[400];
		
		ProcessedImage img2;
		
		for(int i = 0; i < 400; i++)
		{
			if(isCheckedCCV & !isCheckedEdge)
				scores[i] = CCVSimilarity.getScore(img1, img2);
			if(!isCheckedCCV & isCheckedEdge)
			{
				int[][] hist;
				hist = Indexer.readIndex(Integer.toString(i),EdgeIndex);
				img2.setEdgeHist(hist);
				scores[i] = EdgeSimilarity.getScore(img1, img2);
			}
	}
	
		return scores;
	}
	
	private String[] sortScores(double[] scores) {
		String[] results = new String[20];
		
		return results;
	}
	
}