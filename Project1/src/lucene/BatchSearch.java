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
			docID.add(Searcher.search(list.getQuery()));
		}
		
		ResultWriter.writeResults(queries, docID);
	}
	
}