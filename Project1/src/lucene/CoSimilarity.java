package lucene;

import org.apache.lucene.search.DefaultSimilarity;

public class CoSimilarity extends DefaultSimilarity {
	
	public float idf(int docFreq, int numDocs) {
		return 1;
	}
}