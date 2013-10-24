package imagesimilarity;

import java.util.ArrayList;
import java.util.Collections;

public class Test implements Comparable<Test> {
	
	
	public static void main(String[] args) {
		
		ArrayList<Test> list = new ArrayList<Test>();
		
		list.add(new Test(56, "test_1"));
		list.add(new Test(91, "test_2"));
		list.add(new Test(6, "test_3"));
		list.add(new Test(100, "test_4"));
		list.add(new Test(90, "test_5"));
		list.add(new Test(21, "test_6"));
		list.add(new Test(12, "test_7"));
		
		System.out.println("Before sorting:");
		for (Test t : list) {
			System.out.print(t + "\t");
		}
		System.out.println();
		
		Collections.sort(list);
		System.out.println("\nAfter sorting:");
		for (Test t : list) {
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
	
	Test(int score, String filename) {
		this.setScore(score);
		this.setFileName(filename);
	}
	@Override
	public int compareTo(Test that) {
		return this.getScore() - that.getScore();
	}
	
	@Override
	public String toString() {
		return String.format("%s [%d]", this.fileName, this.score);
	}
}
