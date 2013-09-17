package lucene;


public class Searcher {
	private static final Searcher instance = new Searcher();
	private static String type;
	private static String similarity;
	private int pseudoRF;

	// Singleton pattern
	private Searcher() { }

	public static Searcher getInstance() {
		return instance;
	}
	
	public static void setType(String type) {
		Searcher.type = type;
	}

	public static void setSimilarity(String similarity) {
		Searcher.similarity = similarity;
	}

	public void setPseudoRF(int pseudoRF) {
		this.pseudoRF = pseudoRF;
	}

	



}
