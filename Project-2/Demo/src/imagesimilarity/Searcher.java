package imagesimilarity;

import imagesimilarity.ColorCoherence.Result;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;

public class Searcher {
	
	private static final String EdgeIndex = "EdgeIndex.txt"; //temp
	private static final String NormalIndex = "Index.txt"; //temp
	private static final String CCVIndex = "CCVIndex.txt"; //temp
	private static boolean isCheckedNormalHistogram = true; // default;
	private static boolean isCheckedCCV;
	private static boolean isCheckedEdge;
	
	public static void setCheckedNormalHistogram(boolean isCheckedNormalHistogram) {
		Searcher.isCheckedNormalHistogram = isCheckedNormalHistogram;
	}
	
	public static void setCheckedCCV(boolean isCheckedCCV) {
		Searcher.isCheckedCCV = isCheckedCCV;
	}
	
	public static void setCheckedEdge(boolean isCheckedEdge) {
		Searcher.isCheckedEdge = isCheckedEdge;
	}
	
	public String[] search(BufferedImage inputImage) throws IOException {
		String[] rankedResults;// = new String[20];
		double[] scores;
		ProcessedImage img = new ProcessedImage();
		
		//TODO: Process input image
		
//		scores = computeSimilarity(img);	
//		
//		rankedResults = sortScores(scores);
		
		// Testing, can remove this now
		// * the returned result shd be the ABSOLUTE filepath of the results images
		// below RELATIVE only for testing
		String[] test = {"t1.jpg", "16.jpg"};
		return test;
		
//		return rankedResults;
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
				Result[] results = Indexer.readIndexCCV(Integer.toString(i), CCVIndex);
				img2.setCCV(results);
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