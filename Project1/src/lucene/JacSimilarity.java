package lucene;

import org.apache.lucene.search.DefaultSimilarity;

public class JacSimilarity extends DefaultSimilarity {
	
	public float tf(float freq) {
		return 1;
	}
}