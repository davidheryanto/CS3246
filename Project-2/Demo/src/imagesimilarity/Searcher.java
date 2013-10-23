package imagesimilarity;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

public class Searcher {
	
	private static final String EdgeIndex = "EdgeIndex.txt";
	private static final String NormalIndex = "Index.txt";
	private boolean isCheckedNormalHistogram;
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
		ProcessedImage img = new ProcessedImage();
		
		//TODO: Process input image
		
		scores = computeSimilarity(img);	
		
		rankedResults = sortScores(scores);
		
		return rankedResults;
	}
	
	private double[] computeSimilarity(ProcessedImage img1) throws IOException {
		double[] scores = new double[400];
		
		ProcessedImage img2 = new ProcessedImage();
		
		for(int i = 0; i < 400; i++)
		{
			if(isCheckedNormalHistogram & !isCheckedCCV & !isCheckedEdge)
			{
				int[][] hist;
				hist = Indexer.readIndex(Integer.toString(i), NormalIndex);
				img2.setColorHist(hist);
				scores[i] = NormalSimilarity.getScore(img1, img2);
			}
			if(isCheckedCCV & !isCheckedEdge)
			{
				
				scores[i] = CCVSimilarity.getScore(img1, img2);
			}
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
		double[] scoresToSort = scores;
		
		Arrays.sort(scoresToSort);
		for(int i = 0; i < scoresToSort.length/2; i++)
		{
			double temp = scoresToSort[i];
			scoresToSort[i] = scoresToSort[scoresToSort.length-(i+1)];
			scoresToSort[scoresToSort.length-(i+1)] = temp;
		}
		
		for(int i = 0; i < 20; i++)
			for(int j = 0; j < scores.length; j++)
			{
				if(scoresToSort[i] == scores[j])
				{
					results[i] = Integer.toString(j);
					break;
				}
			}
		
		return results;
	}
	
}