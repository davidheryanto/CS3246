package lucene;

import java.awt.AWTEvent;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JTextField;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Explanation;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;

public class Controller implements AWTEventListener, ActionListener {
	// Singleton pattern
	private static final Controller instance = new Controller();
	
	private static DefaultListModel<String> model = new DefaultListModel<>();
	
	private Controller() { }
	
	public static Controller getInstance() {
		return instance;
	}
	
	// The starting point for the program
	public static void main(String[] args) {
		Window.initialize(model);
	}
	
	public static void index() {
		// TODO: Check if directory exists

		// Set up
		Directory directory = getDirectory(Constants.DIR_PATH_INDEX);
		Analyzer analyzer = new StandardAnalyzer(Constants.VERSION);
		IndexWriterConfig config = new IndexWriterConfig(Constants.VERSION, analyzer);
		IndexWriter indexWriter = getIndexWriter(directory, config);
		Indexer.setIndexWriter(indexWriter);
		// Parse HTML files
		Paper[] papers = DocumentParser.parseDocument(Constants.DIR_PATH_DATA);

		//		For debugging:
		//		printTabDelimited(papers); // for checking

		long startTime = System.currentTimeMillis();
		System.out.printf("Indexing documents...%n");
		int indexCount = Indexer.index(papers);
		long endTime = System.currentTimeMillis();
		System.out.printf("%d items indexed in %f ms.%n", indexCount, (double) (endTime - startTime));
	}


	

	public static void print(TopDocs topDocs) {

	}


	// Helper class
	public static Directory getDirectory(String path) {
		File indexFolder = new File(path);
		if (indexFolder.exists() && indexFolder.isDirectory()) {
			for (File file : indexFolder.listFiles()) {
				file.delete();
			}
		}

		Directory directory = null;
		try {
			directory = FSDirectory.open(indexFolder);
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
			e.printStackTrace();
		} catch (LockObtainFailedException e) {
			e.printStackTrace();
		} catch (IOException e) {
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

	private void search(String queryString) {
		System.out.printf("------------------------%nSEARCHING...%n");
		SearchEngine instance = new SearchEngine();

		ScoreDoc[] hits = instance.performSearch(queryString, 10);

		// remove all strings in model
		model.clear();

		System.out.println("Results found: " + hits.length);
		for (int i = 0; i < hits.length; i++) {
			try {
			
			ScoreDoc hit = hits[i];
			// Document doc = hit.doc();

			Query query = new QueryParser(Constants.VERSION, "title",
					new StandardAnalyzer(Constants.VERSION))
			.parse(queryString);

			Explanation explanation = instance.searcher.explain(query, hit.doc);

			Document doc = instance.searcher.doc(hits[i].doc); // This

			String resultString = 	doc.get("id") + "|" + 
					doc.get("title") + "|" + 
					doc.get("author") + " (" + 
					hit.score + ")";

			model.addElement(resultString);

			System.out.println(resultString);
			System.out.println(explanation.toString());

			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		System.out.println("performSearch done");

		System.out.printf("Search Query: %s%n", queryString);
	}
	
/********************* Action and Event Listeners *************************/
	
	@Override
	public void eventDispatched(AWTEvent event) {
		System.out.printf("Event: %s%n%n", event.toString());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();


		if (obj instanceof JButton) {
			
		};
		
		if (obj instanceof JTextField) {
			search(Window.getQueryString());
		}
	}

	
}
