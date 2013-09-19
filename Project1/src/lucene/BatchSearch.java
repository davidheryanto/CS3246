package lucene;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class BatchSearch {
	public static void runBatchQuery(String inputPath) throws IOException {
		ArrayList<String[]> docID = new ArrayList<String[]>();

		QueryList[] queries = QueryReader.readQuery(inputPath);
		for(QueryList list : queries ) {
			String[] results = Searcher.search(list.getQuery());
			docID.add( getIDs(results) );
		}

		String outputPath = (new File(inputPath).getParentFile()).getAbsolutePath() + "\\results.txt";
		ResultWriter.writeResults(queries, docID, outputPath);
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