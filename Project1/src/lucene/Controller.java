package lucene;

import java.awt.AWTEvent;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JScrollPane;
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

public class Controller implements AWTEventListener, ActionListener, FocusListener, KeyListener {

	private static final Controller instance = new Controller();
	private static DefaultListModel<String> model = new DefaultListModel<>();

	// Singleton pattern
	private Controller() { }

	public static Controller getInstance() {
		return instance;
	}

	// The starting point for the program
	public static void main(String[] args) {
		Window.initialize(model);
	}

	

	private void search(String queryString) {
		if (Window.isReIndexChecked()) {
			Paper[] papers = DocumentParser.parseDocument(Constants.DIR_PATH_DATA);
			Indexer.index(papers);
		}
		
		String searchType = Window.getSearchType();
		String[] results = null;

		switch(searchType) {
		case Constants.SEARCH_TYPE_NORMAL:
			results = Searcher.search(queryString);
			break;
		}

		updateModel(model, results);
	}

	private void updateModel(DefaultListModel<String> model, String[] results) {
		if (!model.isEmpty()) {
			model.clear();
		}

		for (String result : results) {
			model.addElement(result);
		}
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

	/********************* Action, Focus and Event Listeners *************************/

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

	@Override
	public void focusGained(FocusEvent e) {
		Object obj = e.getSource();

		if (obj instanceof JTextField) {
			((JTextField) obj).selectAll();
		}

	}

	@Override
	public void focusLost(FocusEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(KeyEvent e) {
		Object obj = e.getSource();

		if (e.getKeyCode() == KeyEvent.VK_ENTER && obj instanceof JList<?>) {
			search(Window.getQueryString());
		}

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}


}
