package lucene;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;


public class Searcher {
	private static final Searcher instance = new Searcher();
	// options for the searcher
	private static IndexSearcher indexSearcher;
	private static String type = Constants.SEARCH_TYPE_NORMAL;
	private static String similarity = Constants.SIMILARITY_COSINE;
	private static int pseudoRF = 0;

	// Singleton pattern
	private Searcher() { 
		try {
			File indexDirectory = new File(Constants.DIR_PATH_INDEX);
			Directory directory = FSDirectory.open(indexDirectory);
			IndexReader indexReader = IndexReader.open(directory);
			indexSearcher = new IndexSearcher(indexReader);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Searcher getInstance() {
		return instance;
	}

	public static void setType(String type) {
		Searcher.type = type;
	}

	public static void setSimilarity(String similarity) {
		Searcher.similarity = similarity;
	}

	public static void setPseudoRF(int pseudoRF) {
		Searcher.pseudoRF = pseudoRF;
	}

	public static String[] search(String queryString) {
		Query query = parse(queryString);
		ArrayList<String> resultList = new ArrayList<String>();

		try {
			TopDocs topDocs = indexSearcher.search(query, 50);
			ScoreDoc[] scoreDocs = topDocs.scoreDocs;
			for (ScoreDoc scoreDoc : scoreDocs) {
				int docId = scoreDoc.doc;
				Document document = indexSearcher.doc(docId);
				String result = 
						String.format("<html>[%s] %s <font color=blue>%.3f</font></html>",
								document.get("id"),
								document.get("title"),
								(double) scoreDoc.score);
				resultList.add(result);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		
        String[] results = new String[resultList.size()];
		return resultList.toArray(results);
	}

	private static Query parse(String queryString) {
		QueryParser queryParser = new QueryParser(
				Constants.VERSION,
				"content",
				new StandardAnalyzer(Constants.VERSION));

		Query query = null;
		try {
			query = queryParser.parse(queryString);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return query;
	}



}
