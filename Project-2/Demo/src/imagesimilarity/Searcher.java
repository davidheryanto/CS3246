package imagesimilarity;

import imagesimilarity.ColorCoherence.Result;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Searcher {
	
	private static final String EdgeIndex = "EdgeIndex.txt"; //temp
	private static final String NormalIndex = "Index.txt"; //temp
	private static final String CCVIndex = "CCVIndex.txt"; //temp
	private static boolean isCheckedNormalHistogram = true; // default;
	private static boolean isCheckedCCV;
	private static boolean isCheckedEdge;
	
	private static ArrayList<ProcessedImage> processedImages;
	
	public Searcher() {
		try {
			processedImages = Indexer.read();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
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
		int resultCount = 30;
		
		String[] rankedResults = new String[resultCount];
		double[] scores;
		
		ArrayList<ScoreImage> scoreImages = new ArrayList<ScoreImage>();
		
		ProcessedImage input = processImage(inputImage);
		
		for (ProcessedImage index : processedImages) {
			double score = CCVSimilarity.getScore(input, index);
			String filePath = index.getFilePath();
			scoreImages.add(new ScoreImage(score, filePath));
		}
		
		Collections.sort(scoreImages);
		int count = 0;
		for (ScoreImage si : scoreImages) {
			// limit top 30
			if (count >= resultCount) {
				break;
			}
			rankedResults[count] = si.getFilePath();
			count += 1;
		}
		
		return rankedResults;
	}
	
	private ProcessedImage processImage(BufferedImage image) {
		ProcessedImage img = new ProcessedImage();
		
		if(isCheckedNormalHistogram)
		{
			int[][] hist = getHistogram(image);
			img.setColorHist(hist);
		}
		
		if(isCheckedEdge)
		{
			BufferedImage result = FilterSobel.apply(image);
			int[][] hist = getHistogram(result);
			img.setEdgeHist(hist);
		}
		
		if(isCheckedCCV)
		{
			// Will this destroy the current image??
			image = ImageHelper.resize(image, 100);
			ColorCoherence.extract(image);
			Result[] results = ColorCoherence.getResults();
			img.setCCV(results);
		}
		
		return img;
	}
	
	private int[][] getHistogram(BufferedImage image) {
		int width = image.getWidth();
		int height = image.getHeight();
		int[][] hist = new int[3][256];
				
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 256; j++) {
				hist[i][j] = 0;
			}
		}
		
		for(int i = 0; i < width; i++)
			for(int j = 0; j < height; j++) 
			{
				Color c = new Color(image.getRGB(i, j));
				hist[0][c.getRed()]++;
				hist[1][c.getGreen()]++;
				hist[2][c.getBlue()]++;
			}
		
		return hist;
	}
	
	private double[] computeSimilarity(ProcessedImage img1) throws IOException {
		double[] scores = new double[400];
		
		ProcessedImage img2 = new ProcessedImage();
		
		for(int i = 0; i < 400; i++)
		{
			if(isCheckedNormalHistogram & !isCheckedCCV & !isCheckedEdge)
			{
				int[][] hist;
				hist = Indexer.readIndex(Integer.toString(i+1), NormalIndex);
				img2.setColorHist(hist);
				scores[i] = NormalSimilarity.getScore(img1, img2);
			}
			if(isCheckedCCV & !isCheckedEdge)
			{
//				Result[] results = Indexer.readIndexCCV(Integer.toString(i+1), CCVIndex);
//				img2.setCCV(results);
//				scores[i] = CCVSimilarity.getScore(img1, img2);
			}
			if(!isCheckedCCV & isCheckedEdge)
			{
				int[][] hist;
				hist = Indexer.readIndex(Integer.toString(i+1),EdgeIndex);
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