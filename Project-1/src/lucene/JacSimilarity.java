package lucene;

import org.apache.lucene.search.DefaultSimilarity;

public class JacSimilarity extends DefaultSimilarity {
	
	private static final long serialVersionUID = -6875796663642320140L;

	@Override
	// Jaccard coefficient is only concerned about matching terms, not freq of match
	public float tf(float freq) {
		return freq >= 1.0f ? 1.0f : 0f;
	}
	
	@Override
	// Jaccard coefficient is not affected by idf
	public float idf(int docFreq, int numDocs) {
		return 1.0f;
	}
}