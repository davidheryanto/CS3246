package lucene;

import org.apache.lucene.search.DefaultSimilarity;

public class CosineSimilarity extends DefaultSimilarity {
	private static final long serialVersionUID = -6875796663642320140L;

	@Override
	// Cosine similarity is not affected by coord() which is overlap/maxOVerlap
	public float coord(int overlap, int maxOverlap) {
		return 1.0f;
	}
}