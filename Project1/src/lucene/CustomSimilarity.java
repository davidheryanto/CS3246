package lucene;

import org.apache.lucene.search.DefaultSimilarity;

public class CustomSimilarity extends DefaultSimilarity {
	
	public float idf(int docFreq, int numDocs) {
		return 1;
	}
}