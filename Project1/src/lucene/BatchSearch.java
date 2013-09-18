package lucene;

import java.io.IOException;
import java.util.ArrayList;

public class BatchSearch {
	public static void runBatchQuery(String filePath) throws IOException {
		ArrayList<String[]> docID = new ArrayList<String[]>();
		
		QueryList[] queries = QueryReader.readQuery(filePath);
		for(QueryList list : queries ) {
			String[] results = Searcher.search(list.getQuery()); //Use Searcher.search(QueryString)
			docID.add( getIDs(results) );
		}
		
		int qNo = 0;
		for (String[] sArr : docID) {
			qNo++;
			for (String s : sArr) {
				System.out.println(qNo + "\t" + s);
			}
	}
	
		//ResultWriter.writeResults(queries, docID);
	}
		
	// Get the document filename from the formatted lines in the list
	private static String[] getIDs(String[] results) {
		String[] docIDs = new String[results.length];

		for (int i = 0; i < results.length; i++) {
			String s = results[i];
			int startIdx = s.indexOf('[') + 1;
			int endIdx = s.indexOf(']');
			String docID = s.substring(startIdx, endIdx);
		
			docIDs[i] = docID;
		}

		return docIDs;
		}
}