package lucene;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.util.Version;

public class Main {
	private static final Version VERSION = Version.LUCENE_36;
	private static final String DIR_PATH_DATA = "./data";
	private static final String DIR_PATH_INDEX = "./index";
	
	public static void main(String[] args) {
		// index();
		
		
		
		try {
		
		System.out.printf("------------------------%nSEARCHING...%n");
		SearchEngine instance = new SearchEngine();
		ScoreDoc[] hits = instance.performSearch(args[0], 10);

		System.out.println("Results found: " + hits.length);
		for (int i = 0; i < hits.length; i++) {
			ScoreDoc hit = hits[i];
			// Document doc = hit.doc();
			Document doc = instance.searcher.doc(hits[i].doc); // This
																// retrieves
																// the

			System.out.println(doc.get("title") + " " + doc.get("author")
					+ " (" + hit.score + ")");

		}
		System.out.println("performSearch done");
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		
		
		
		
		
		
		TopDocs topDocs = search();
		print(topDocs); 
	}
	
	
	public static void index() {
		// TODO: Check if directory exists
		
		// Set up
		Directory directory = getDirectory(DIR_PATH_INDEX);
		Analyzer analyzer = new StandardAnalyzer(VERSION);
		IndexWriterConfig config = new IndexWriterConfig(VERSION, analyzer);
		IndexWriter indexWriter = getIndexWriter(directory, config);
		Indexer.setIndexWriter(indexWriter);
		// Parse HTML files
		Paper[] papers = DocumentParser.parseDocument(DIR_PATH_DATA);
		
		// For debugging:
		// printTabDelimited(papers); // for checking
		
		long startTime = System.currentTimeMillis();
		System.out.printf("Indexing documents...%n");
		int indexCount = Indexer.index(papers);
		long endTime = System.currentTimeMillis();
		System.out.printf("%d items indexed in %f ms.%n", indexCount, (double) (endTime - startTime));
	}

	
	public static TopDocs search() {
		return null;
	}
	
	public static void print(TopDocs topDocs) {
		
	}


	// Helper class
	public static Directory getDirectory(String path) {
		File file = new File(path);
		Directory directory = null;
		try {
			directory = FSDirectory.open(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return directory;
	}
	
	public static IndexWriter getIndexWriter(Directory directory,
			IndexWriterConfig config) {
		IndexWriter indexWriter = null;
		
		try {
			indexWriter = new IndexWriter(directory, config);
		} catch (CorruptIndexException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LockObtainFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return indexWriter;
	}
	
	private static void printTabDelimited(Paper[] papers) {
		for (Paper p : papers) {
			StringBuilder sb = new StringBuilder();
			String authors = "";
			if (p.getAuthors() != null) {
				for (String a : p.getAuthors()) {
					sb.append(", " + a);
				}
				authors = sb.toString();
				authors = authors.substring(authors.indexOf(",") + 2);
			}
			
			sb = new StringBuilder();
			String keywords = "";
			if (p.getKeywords() != null) {
				for (String k : p.getKeywords()) {
					sb.append(", " + k);
				}
				keywords = sb.toString();
				keywords = keywords.substring(keywords.indexOf(",") + 2);
			}
			
			
			
			System.out.printf("%s\t%s\t%s\t%d\t%s\t%s%n", 
					p.getId(),
					p.getTitle(),
					p.getSummary(),
					p.getYear(),
					authors,
					keywords);
		}
	}
}
