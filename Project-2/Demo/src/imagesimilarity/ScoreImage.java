package imagesimilarity;

import java.util.ArrayList;
import java.util.Collections;

public class ScoreImage implements Comparable<ScoreImage> {
	
	ScoreImage(double score, String filePath) {
		this.setScore(score);
		this.setFilePath(filePath);
	}
	
	public static void main(String[] args) {
		
		ArrayList<ScoreImage> list = new ArrayList<ScoreImage>();
		
		list.add(new ScoreImage(56, "test_1"));
		list.add(new ScoreImage(91, "test_2"));
		list.add(new ScoreImage(6, "test_3"));
		list.add(new ScoreImage(100, "test_4"));
		list.add(new ScoreImage(90, "test_5"));
		list.add(new ScoreImage(21, "test_6"));
		list.add(new ScoreImage(12, "test_7"));
		
		System.out.println("Before sorting:");
		for (ScoreImage t : list) {
			System.out.print(t + "\t");
		}
		System.out.println();
		
		Collections.sort(list);
		System.out.println("\nAfter sorting:");
		for (ScoreImage t : list) {
			System.out.print(t + "\t");
		}
		System.out.println();
	}
	
	private double score;
	private String filePath;
	
	public double getScore() {
		return score;
	}
	public void setScore(double score) {
		this.score = score;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
	@Override
	public int compareTo(ScoreImage that) {
		return (int) (that.getScore() - this.getScore());
	}
	
	@Override
	public String toString() {
		return String.format("%s [%d]", this.filePath, this.score);
	}
}
