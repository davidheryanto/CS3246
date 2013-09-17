package lucene;

// TEMP SEARCH ENGINE
// TODO: Modify "title" to "content"
import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class SearchEngine {
	public IndexSearcher searcher = null;
	private boolean VERBOSE = true;

	/** Creates a new instance of SearchEngine */

	public SearchEngine()  {
		FSDirectory indexDir = null;
		try {
			indexDir = FSDirectory.open(new File("index"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			searcher = new IndexSearcher(indexDir);
		} catch (CorruptIndexException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public ScoreDoc[] performSearch(String queryString, int noOfTopDocs) {

		Query query = null;
		try {
			query = new QueryParser(Version.LUCENE_CURRENT, "content",
					new StandardAnalyzer(Version.LUCENE_CURRENT))
					.parse(queryString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		long startTime = System.currentTimeMillis();
		TopDocs topDocs = null;
		try {
			topDocs = searcher.search(query, 60);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long endTime = System.currentTimeMillis();
		
//		System.out.println(topDocs);
		ScoreDoc[] scoreDocs = topDocs.scoreDocs;
		System.out.printf("******************%nQuery >> %s%n******************%n", query);
		if (VERBOSE)
			System.out.printf("Total hits in topDocs: %d (in %f ms)%n", 
					topDocs.totalHits, 
					(double) (endTime - startTime));
		for (int i = 0; i < scoreDocs.length; i++) {
			try {
				Document doc = searcher.doc(scoreDocs[i].doc);
			} catch (CorruptIndexException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} // This retrieves the
		}

		return scoreDocs;
	} // end of query(...)

}
