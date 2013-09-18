package lucene;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.TermFreqVector;
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
	
	private static IndexSearcher indexSearcher;
	private static IndexReader indexReader;
	
	// options for the searcher
//	private static String type = Constants.SEARCH_TYPE_NORMAL;
//	private static String similarity = Constants.SIMILARITY_COSINE;
//	private static int pseudoRF = 0;

	// Singleton pattern
	private Searcher() { 
		try {
			File indexDirectory = new File(Constants.DIR_PATH_INDEX);
			Directory directory = FSDirectory.open(indexDirectory);
			indexReader = IndexReader.open(directory);
			indexSearcher = new IndexSearcher(indexReader);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Searcher getInstance() {
		return instance;
	}

	public static String[] search(String queryString) {
		Query query = parse(queryString);
		
		
		//options for search type, pseudo rf or similarity
		if (Window.getSearchType() == Constants.SEARCH_TYPE_REFINE) {
			query = expandQuery(query);
			
			// do something else?
		}
		
		
		
		// We need to update the docNumber in paperTable
		Hashtable<Integer, Paper> paperTable = Controller.getPaperTable();
		ArrayList<String> resultList = new ArrayList<String>();
		
		try {
			TopDocs topDocs = indexSearcher.search(query, 50);
			ScoreDoc[] scoreDocs = topDocs.scoreDocs;
			for (ScoreDoc scoreDoc : scoreDocs) {
				int docNumber = scoreDoc.doc;
				Document document = indexSearcher.doc(docNumber);
				
				// Update the docNumber in the paperTable
				Integer fileNumber = Integer.parseInt(document.get("fileNumber"));
				Paper paper = paperTable.get(fileNumber);
				paper.setDocNumber(docNumber);
				
				String result = 
						String.format("<html>[%s] %s <font color=blue>%.3f</font></html>",
								document.get("fileNumber"),
								document.get("title"),
								(double) scoreDoc.score);
				resultList.add(result);
				
				
				// test term frequency
//				System.out.println(docNumber);
//				TermFreqVector[] tfvs = indexReader.getTermFreqVectors(docNumber);
//				System.out.println(tfvs == null);
//				for (TermFreqVector tfv : tfvs) {
//					System.out.println("************************************");
//					System.out.println("Field : " + tfv.getField());
//					
//					System.out.println("Terms :");
//					for ( String s : tfv.getTerms() ) {
//						System.out.print(s + ", ");
//					}
//					
//					System.out.println("Term Freq :");
//					for ( int f : tfv.getTermFrequencies() ) {
//						System.out.print(f + ", ");
//					}
//				}
				
				
//				System.out.println(query.toString());
				
				
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			System.err.println("fileNumber field in index is not a number");
		}
		
        String[] results = new String[resultList.size()];
		return resultList.toArray(results);
	}

	private static Query expandQuery(Query originalQuery) {
		// delimiters will contain all the fields that are indexed
		String delimiters = "title:|summary:|author:|keyword:|content:";
		String[] originalTerms = originalQuery.toString().split(delimiters);

		// get selectedDocumentFilenames
		String[] selectedDocumentFileNames = Window.getSelectedDocumentFileNames();

		// get their DocId
		ArrayList<Integer> selectedDocNumberList = new ArrayList<Integer>();
		Hashtable<Integer, Paper> paperTable = Controller.getPaperTable();
		for (String fileName : selectedDocumentFileNames) {
			
			try {
				int fileNumber = Integer.parseInt(fileName);
				int docNumber = paperTable.get(fileNumber).getDocNumber();
				selectedDocNumberList.add(docNumber);
			} catch (NumberFormatException e) {
				System.err.println("FileName is not a number");
			}
		}

		// find their term freq cf with query term
		for (Integer docNumber : selectedDocNumberList) {
			try {
				TermFreqVector[] tfvs = indexReader.getTermFreqVectors(docNumber);
				
				
				
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}




		return null;
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
