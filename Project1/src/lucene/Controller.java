package lucene;

import java.awt.AWTEvent;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;

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
	public static void index() {
		// TODO: Check if directory exists

		// Set up
		Directory directory = getDirectory(Main.DIR_PATH_INDEX);
		Analyzer analyzer = new StandardAnalyzer(Main.VERSION);
		IndexWriterConfig config = new IndexWriterConfig(Main.VERSION, analyzer);
		IndexWriter indexWriter = getIndexWriter(directory, config);
		Indexer.setIndexWriter(indexWriter);
		// Parse HTML files
		Paper[] papers = DocumentParser.parseDocument(Main.DIR_PATH_DATA);

		//		For debugging:
		//		printTabDelimited(papers); // for checking

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

	@Override
	public void eventDispatched(AWTEvent event) {
		System.out.printf("Event: %s%n%n", event.toString());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();


		if (obj instanceof JButton) {
			String buttonLabel = ((JButton) obj).getText();
			switch(buttonLabel) {
			case Window.LABEL_BUTTON_SEARCH :
				String queryString = Window.getQueryString();

				System.out.printf("------------------------%nSEARCHING...%n");
				SearchEngine instance = new SearchEngine();

				ScoreDoc[] hits = instance.performSearch(queryString, 10);

				// remove all strings in model
				Main.model.clear();

				System.out.println("Results found: " + hits.length);
				for (int i = 0; i < hits.length; i++) {
					try {
					
					ScoreDoc hit = hits[i];
					// Document doc = hit.doc();

					Query query = new QueryParser(Main.VERSION, "title",
							new StandardAnalyzer(Main.VERSION))
					.parse(queryString);

					Explanation explanation = instance.searcher.explain(query, hit.doc);

					Document doc = instance.searcher.doc(hits[i].doc); // This

					String resultString = 	doc.get("id") + "|" + 
							doc.get("title") + "|" + 
							doc.get("author") + " (" + 
							hit.score + ")";

					Main.model.addElement(resultString);

					System.out.println(resultString);
					System.out.println(explanation.toString());

					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
				System.out.println("performSearch done");



				TopDocs topDocs = search();
				print(topDocs); 


				SearchEngine instance1 = new SearchEngine();
				ScoreDoc[] hits1 = instance1.performSearch(queryString, 10);




				System.out.printf("Search Query: %s%n", queryString);
			}
		};
	}




	//		btnNewButton.addActionListener(new ActionListener() {
	//			public void actionPerformed(ActionEvent arg0) {
	//				
	//				
	//				
	//
	//				String queryString = textField.getText().trim();
	//				System.out.printf("------------------------%nSEARCHING...%n");
	//				SearchEngine instance = new SearchEngine();
	//
	//
	//				ScoreDoc[] hits = instance.performSearch(queryString, 10);
	//
	//				// remove all strings in model
	//				model.clear();
	//
	//				System.out.println("Results found: " + hits.length);
	//				for (int i = 0; i < hits.length; i++) {
	//					ScoreDoc hit = hits[i];
	//					// Document doc = hit.doc();
	//
	//					Query query = new QueryParser(VERSION, "title",
	//							new StandardAnalyzer(VERSION))
	//					.parse(queryString);
	//
	//					Explanation explanation = instance.searcher.explain(query, hit.doc);
	//
	//					Document doc = instance.searcher.doc(hits[i].doc); // This
	//
	//					String resultString = 	doc.get("id") + "|" + 
	//							doc.get("title") + "|" + 
	//							doc.get("author") + " (" + 
	//							hit.score + ")";
	//
	//					model.addElement(resultString);
	//
	//					System.out.println(resultString);
	//					System.out.println(explanation.toString());
	//
	//				}
	//				System.out.println("performSearch done");
	//
	//
	//
	//				TopDocs topDocs = search();
	//				print(topDocs); 
	//
	//
	//
	//
	//
	//				String queryString = textField.getText().trim();
	//
	//				SearchEngine instance = new SearchEngine();
	//				ScoreDoc[] hits = instance.performSearch(queryString, 10);
	//
	//
	//
	//
	//				System.out.printf("Search Query: %s%n", queryString);
	//			}
	//		});


}
