package lucene;

import java.util.ArrayList;
import java.util.Collections;

public class TermFreq implements Comparable<TermFreq>{
	private String term;
	private int freq;

	public TermFreq(String term, int freq) {
		this.term = term;
		this.freq = freq;
	}

	public void addFreq(int n) {
		this.freq += n;
	}

	public String getTerm() {
		return this.term;
	}

	public int getFreq() {
		return this.freq;
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof TermFreq){
			return this.term.equals( ((TermFreq) (other)).term );
		}

		return false;
	}

	@Override
	public int compareTo(TermFreq other) {
		return other.freq - this.freq;
	}

	// for testing
	public static void main(String[] args) {
		ArrayList<TermFreq> tf = new ArrayList<TermFreq>();

		tf.add( new TermFreq("massa", 50) );
		tf.add( new TermFreq("massa2", 20) );
		tf.add( new TermFreq("massa3", 30) );
		tf.add( new TermFreq("massa4", 12) );
		tf.add( new TermFreq("massa5", 3) );
		tf.add( new TermFreq("massa6", 2) );
		tf.add( new TermFreq("massa7", 9) );

		Collections.sort(tf);

		for (TermFreq t : tf) {
			System.out.println(t.term + "\t" +  t.freq);
		}
		
		TermFreq tf1 = new TermFreq("mast", 56);
		TermFreq tf2 = new TermFreq("mast", 36);
		System.out.println(tf1.equals(tf2));
		
	}

}
