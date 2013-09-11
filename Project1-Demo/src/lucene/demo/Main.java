/*
 * Main.java
 *
 * Created on 6 March 2006, 11:51
 *
 */

package lucene.demo;

import lucene.demo.search.*;

import org.apache.lucene.document.Document;
import org.apache.lucene.search.ScoreDoc;

public class Main {
	/** Creates a new instance of Main */
	public Main() {
	}

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String[] args) {
		String queryString = getQueryString(args);
		if (queryString == null) {
			System.err.println("No query string provided, program exiting...");
			System.err.println("Usage: Main <query string>");
			System.exit(0);
		}

		try {
			// build a Lucene index
			System.out.println("rebuildIndexes");
			Indexer indexer = new Indexer();
			indexer.rebuildIndexes();
			System.out.println("rebuildIndexes done");

			// perform search on "Dame museum"
			// and retrieve the result
			System.out.printf("------------------------%nSEARCHING...%n");
			SearchEngine instance = new SearchEngine();
			ScoreDoc[] hits = instance.performSearch(queryString, 10);

			System.out.println("Results found: " + hits.length);
			for (int i = 0; i < hits.length; i++) {
				ScoreDoc hit = hits[i];
				// Document doc = hit.doc();
				Document doc = instance.searcher.doc(hits[i].doc); // This
																	// retrieves
																	// the

				System.out.println(doc.get("name") + " " 
						+  doc.get("city") + "(" + hit.score + ")");

			}
			System.out.println("performSearch done");
		} catch (Exception e) {
            System.out.println("Exception caught.\n");
            System.out.println(e.toString());
        }
	}

	private static String getQueryString(String[] args) {
		if (args.length < 1) {
			return null;
		}
		
		StringBuilder stringBuilder = new StringBuilder();
		for (String s : args) {
			stringBuilder.append(" " + s);
		}
		String queryString = stringBuilder.toString().trim();
		return queryString;
	}

}
