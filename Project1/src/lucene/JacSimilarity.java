package lucene;

import org.apache.lucene.search.DefaultSimilarity;

public class JacSimilarity extends DefaultSimilarity {
	
	private static final long serialVersionUID = -6875796663642320140L;

	public float tf(float freq) {
		return 1;
	}
}