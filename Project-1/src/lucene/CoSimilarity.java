package lucene;

import org.apache.lucene.search.DefaultSimilarity;

public class CoSimilarity extends DefaultSimilarity {
	
	private static final long serialVersionUID = 1861894260983098746L;

	public float idf(int docFreq, int numDocs) {
		return 1;
	}
}