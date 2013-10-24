package imagesimilarity;

import java.util.ArrayList;
import java.util.Collections;

public class ScoreImage implements Comparable<ScoreImage> {
	
	
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
	
	
	
	
	
	
	
	
	
	
	
	
	
	private int score;
	private String fileName;
	
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	ScoreImage(int score, String filename) {
		this.setScore(score);
		this.setFileName(filename);
	}
	@Override
	public int compareTo(ScoreImage that) {
		return this.getScore() - that.getScore();
	}
	
	@Override
	public String toString() {
		return String.format("%s [%d]", this.fileName, this.score);
	}
}
