package lucene;

import java.io.IOException;
import java.util.ArrayList;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Explanation;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;

public class BatchSearch {
	public void runBatchQuery() throws IOException {
		
		ArrayList<String[]> docID = new ArrayList<String[]>();
		
		QueryList[] queries = QueryReader.readQuery(Constants.FILENAME_QUERY);
		
		for(QueryList list : queries ) {
			docID.add(Searcher.search(list.getQuery())); //Use Searcher.search(QueryString)
		}
		
		ResultWriter.writeResults(queries, docID);
	}
	
/*	private String[] search(String queryString) {
		

		String[] hits = Searcher.search(queryString);
		
		String[] resultString = new String[hits.length];

		for (int i = 0; i < hits.length; i++) {
			try {
			
			ScoreDoc hit = hits[i];
			// Document doc = hit.doc();

			Query query = new QueryParser(Constants.VERSION, "title",
					new StandardAnalyzer(Constants.VERSION))
			.parse(queryString);

			Explanation explanation = instance.searcher.explain(query, hit.doc);

			Document doc = instance.searcher.doc(hits[i].doc); // This

			resultString[i] = doc.get("id");

			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		return resultString;
	}
*/
	
}