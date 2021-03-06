package lucene;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.TermFreqVector;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.DefaultSimilarity;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.SearcherFactory;
import org.apache.lucene.search.SearcherManager;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;


public class Searcher {
	private static final Searcher instance = new Searcher();

	// Singleton pattern
	private Searcher() { }

	public static Searcher getInstance() {
		return instance;
	}
	
	public static String[] search(String queryString) {
		IndexSearcher indexSearcher = getIndexSearcher();
		Query query = parse(queryString);
		
		
		//options for search type, pseudo rf or similarity
		if (Window.getSearchType() == Constants.SEARCH_TYPE_REFINE) {
			query = expandQuery(query);
		}
		
		if (Window.isPseudoChecked()) {
			// Select top 5 results
			Window.selectList(new int[]{0, 1, 2, 3, 4});
			query = expandQuery(query);
		}
		
		// We need to update the docNumber in paperTable
		Hashtable<Integer, Paper> paperTable = Controller.getPaperTable();
		ArrayList<String> resultList = new ArrayList<String>();
		
		try {
			switch( Window.getSimilarity() ) {
			case Constants.SIMILARITY_DEFAULT :
				indexSearcher.setSimilarity(new DefaultSimilarity());
				break;
			case Constants.SIMILARITY_COSINE :
				indexSearcher.setSimilarity(new CosineSimilarity());
				break;
			case Constants.SIMILARITY_TERM_CORRELATION :
				indexSearcher.setSimilarity(new CoSimilarity());
				break;
			case Constants.SIMILARITY_JACCARD :
				indexSearcher.setSimilarity(new JacSimilarity());
				break;
			}
			
			System.out.println("Searching...");
			long start = System.currentTimeMillis();
			
			TopDocs topDocs = indexSearcher.search(query, 50);
			
			long end = System.currentTimeMillis();
			System.out.printf("%d results in %.3f ms%n", topDocs.totalHits, (double) (end-start) );
			
			
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
								highlight(document.get("title"), query),
								(double) scoreDoc.score);
				
				resultList.add(result);
			}
			indexSearcher.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			System.err.println("fileNumber field in index is not a number");
		}
		
		
        String[] results = new String[resultList.size()];
		return resultList.toArray(results);
	}

	private static String highlight(String title, Query query) {
		String delimiters = "title:|summary:|author:|keyword:|content:";
		String[] queries = query.toString().replaceAll("[()]", "").split(delimiters);
		String[] fragments = title.split(" ");
		
//		for (String s : fragments) {
//			System.out.println(s);
//		}
//		
//		for (String s : queries) {
//			System.out.println(s);
//		}
		
		for (int i = 0; i < fragments.length; i++) {
			for ( int j = 0; j < queries.length; j++) {
				if (queries[j].trim().length() < 3) {
					continue;
				}
				if (fragments[i].trim().toLowerCase().indexOf( queries[j].trim().toLowerCase() ) >= 0 ) {
					fragments[i] = "<span style='background-color: #FFFF00'>" + fragments[i] + "</span>";
					break;
				}
			}
		}
		
		StringBuilder sb = new StringBuilder("");
		for (String s : fragments) {
			sb.append(s + " ");
		}
		
		return sb.toString().trim();
	}

	private static Query expandQuery(Query originalQuery) {
		// delimiters will contain all the fields that are indexed
		String delimiters = "title:|summary:|author:|keyword:|content:";
		
		String[] originalTerms = originalQuery.toString().replaceAll("[()]", "").split(delimiters);
		ArrayList<String> originalList = getList(originalTerms);
		
		// paperTable contains a mapping from fileName to paper so we can get its DocNumber
		// with DocNumber we can get termFreqVector from indexReader
		Hashtable<Integer, Paper> paperTable = Controller.getPaperTable();
		
		String[] selectedDocumentFileNames = Window.getSelectedDocumentFileNames();
		ArrayList<Integer> selectedDocNumberList = new ArrayList<Integer>();
		
		for (String fileName : selectedDocumentFileNames) {
			try {
				int fileNumber = Integer.parseInt(fileName);
				int docNumber = paperTable.get(fileNumber).getDocNumber();
				selectedDocNumberList.add(docNumber);
			} catch (NumberFormatException e) {
				System.err.println("FileName is not a number");
			}
		}

		ArrayList<TermFreq> tfList = new ArrayList<TermFreq>();
		
		for (Integer docNumber : selectedDocNumberList) {
			try {
				IndexReader indexReader = getIndexReader();
				// get the term freq vector for fields: title, author, content, keyword, summary
				TermFreqVector[] tfvs = indexReader.getTermFreqVectors(docNumber);
				
				// for every field in tfvs
				for (TermFreqVector tfv : tfvs) {
					
					String[] terms = tfv.getTerms();
					
					// for every term we add its freq it to tfList
					for (int i = 0; i < terms.length; i++) {
						String term = terms[i].trim();
						
						// ignore short terms with character length less than 3
						if (term.length() < 3) {
							continue;
						}
						
						int freq = tfv.getTermFrequencies()[i];
						TermFreq tf = new TermFreq(term, freq);
						
						// Is tf already in the list?
						int index = tfList.indexOf(tf);
						if (index > 0) {
							tfList.get(index).addFreq(freq);
						} else {
							tfList.add(tf);
						}
					}
				}
				indexReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		// order tf descending by freq
		Collections.sort(tfList);
		int newTermCount = 0;
		
		// expand the query by adding N terms in the selected list with the highest freq
		for (TermFreq tf : tfList) {
			String newTerm = tf.getTerm();
			// is the new term not in original queryString
			if (!originalList.contains(newTerm) ){
				originalList.add(newTerm);
				newTermCount++;
			}
			if (newTermCount >= Constants.QUERY_EXPANSION_N_TERMS) {
				break;
			}
		}

		String newQueryString = getQueryString(originalList);
		Window.setTextField(newQueryString);
		
		return parse(newQueryString);
	}
	

	private static String getQueryString(ArrayList<String> list) {
		StringBuilder sb = new StringBuilder("");
		for (String s : list) {
			sb.append(s + " ");
		}
		return sb.toString().trim();
	}

	private static ArrayList<String> getList(String[] terms) {
		ArrayList<String> list = new ArrayList<String>();
		for (String s : terms) {
			s = s.toLowerCase().trim();
			if ( ! list.contains(s) ) {
				list.add(s);
			}
		}
		
		return list;
	}

	private static IndexSearcher getIndexSearcher() {
		try {
			Directory dir = FSDirectory.open(new File(Constants.DIR_PATH_INDEX) );
			SearcherManager mgr = new SearcherManager(dir, new SearcherFactory());
			return mgr.acquire();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	private static IndexReader getIndexReader() {
		try {
			Directory dir = FSDirectory.open(new File(Constants.DIR_PATH_INDEX) );
			return IndexReader.open(dir);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	private static Query parse(String queryString) {
		MultiFieldQueryParser queryParser = new MultiFieldQueryParser(
				Constants.VERSION,
                new String[] {"title", "summary", "keyword", "author"},
                new MyAnalyzer(Constants.VERSION));
		
//		QueryParser queryParser = new QueryParser(
//				Constants.VERSION, 
//				"content", 
//				new MyAnalyzer(Constants.VERSION) );
		
		Query query = null;
		try {
			query = queryParser.parse(queryString);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return query;
	}



}
