package lucene;

import java.awt.AWTEvent;
import java.awt.KeyEventDispatcher;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JTextField;

public class Controller implements FocusListener, KeyEventDispatcher, ActionListener {

	private static final Controller instance = new Controller();
	private static Hashtable<Integer, Paper> paperTable;
	private static DefaultListModel<String> model = new DefaultListModel<>();

	// Singleton pattern
	private Controller() { }

	public static Controller getInstance() {
		return instance;
	}
	
	public static Hashtable<Integer, Paper> getPaperTable() {
		if (paperTable == null) {
			paperTable = DocumentParser.parseDocument(Constants.DIR_PATH_DATA);
		}
		
		return paperTable;
	}
	
	// The starting point for the program
	public static void main(String[] args) {
		Window.initialize(model);
	}

	private void search(String queryString) {
		if (Window.isReIndexChecked()) {
			paperTable = Controller.getPaperTable();
			Indexer.index(paperTable);
		}
		
		String[] results = Searcher.search(queryString);

		// updateModel(results);
	}

	private void updateModel(String[] results) {
		if (!model.isEmpty()) {
			model.clear();
		}

		if (results == null) {
			return;
		}
		
		for (String result : results) {
			model.addElement(result);
		}
	}
	
	private static void printTabDelimited(Hashtable<Integer, Paper> paperTable) {
		Enumeration<Integer> fileNumberEnum = paperTable.keys();
		
		while( fileNumberEnum.hasMoreElements() ) {
			Integer fileNumber = fileNumberEnum.nextElement();
			Paper paper = paperTable.get(fileNumber);
			
			StringBuilder sb = new StringBuilder();
			String authors = "";
			
			if (paper.getAuthors() != null) {
				for (String a : paper.getAuthors()) {
					sb.append(", " + a);
				}
				authors = sb.toString();
				authors = authors.substring(authors.indexOf(",") + 2);
			}
			
			sb = new StringBuilder();
			String keywords = "";
			if (paper.getKeywords() != null) {
				for (String k : paper.getKeywords()) {
					sb.append(", " + k);
				}
				keywords = sb.toString();
				keywords = keywords.substring(keywords.indexOf(",") + 2);
			}
			
			System.out.printf("%s\t%s\t%s\t%d\t%s\t%s%n", 
					fileNumber.toString(),
					paper.getTitle(),
					paper.getSummary(),
					paper.getYear(),
					authors,
					keywords);
		}
		
	}

	/********************* Action, Focus and Event Listeners *************************/


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
	public boolean dispatchKeyEvent(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			if (e.getID() == KeyEvent.KEY_RELEASED) {
				search(Window.getQueryString());
				Window.uncheckReIndex();
				return true;
			}
		}
		
		
		return false;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		if (obj instanceof JButton) {
			JFileChooser fc = new JFileChooser(System.getProperty("user.home") + "/Desktop");
			int returnVal = fc.showOpenDialog( Window.getFrame() );
		    if(returnVal == JFileChooser.APPROVE_OPTION) {
		       System.out.println("You chose to open this file: " +
		            fc.getSelectedFile().getAbsolutePath());
		    }
		}
		
	}


}
