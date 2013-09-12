package lucene;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
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

	public SearchEngine() throws IOException {
		FSDirectory indexDir = FSDirectory.open(new File("index"));
		searcher = new IndexSearcher(indexDir);

	}

	public ScoreDoc[] performSearch(String queryString, int noOfTopDocs)
			throws Exception {

		Query query = new QueryParser(Version.LUCENE_CURRENT, "title",
				new StandardAnalyzer(Version.LUCENE_CURRENT))
				.parse(queryString);

		long startTime = System.currentTimeMillis();
		TopDocs topDocs = searcher.search(query, 10, new Sort(new SortField("id", SortField.INT)));
		long endTime = System.currentTimeMillis();
		
//		System.out.println(topDocs);
		ScoreDoc[] scoreDocs = topDocs.scoreDocs;
		if (VERBOSE)
			System.out.printf("Total hits in topDocs: %d (in %f ms)%n", 
					topDocs.totalHits, 
					(double) (endTime - startTime));
		for (int i = 0; i < scoreDocs.length; i++) {
			Document doc = searcher.doc(scoreDocs[i].doc); // This retrieves the
		}

		return scoreDocs;
	} // end of query(...)

}
