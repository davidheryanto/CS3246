package lucene;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Window {
	// Singleton pattern
	private static final Window instance = new Window();

	private Window() { }

	public static Window getInstance() {
		return instance;
	}
	





	public static final String LABEL_BUTTON_SEARCH = "Search";

	private static JFrame frame;
	private static JTextField textField;
	private static JScrollPane scrollPane;
	private static JList list;

	public static void setUI() {
		try {
			// Set System L&F
			UIManager.setLookAndFeel(
					UIManager.getSystemLookAndFeelClassName());
		} 
		catch (UnsupportedLookAndFeelException e) {
			// handle exception
		}
		catch (ClassNotFoundException e) {
			// handle exception
		}
		catch (InstantiationException e) {
			// handle exception
		}
		catch (IllegalAccessException e) {
			// handle exception
		}
	}

	public static void setModel(ListModel<String> model) {
		list.setModel(model);
	}

	public static String getQueryString() {
		return textField.getText().trim();
	}

	/**
	 * Initialize the contents of the frame.
	 * @wbp.parser.entryPoint
	 */
	public static void initialize() {
		setUI();


		frame = new JFrame();
		frame.getContentPane().setFont(new Font("Segoe UI Light", Font.PLAIN, 14));
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.NORTH);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{127, 86, 89, 0, 0, 0, 0, 0, 0, 0};
		gbl_panel.rowHeights = new int[]{23, 0};
		gbl_panel.columnWeights = new double[]{1.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);

		textField = new JTextField("Type query");
		textField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent arg0) {
				textField.selectAll();
			}
		});

		textField.addActionListener(Controller.getInstance());
		textField.setColumns(10);
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.gridwidth = 3;
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.insets = new Insets(0, 0, 0, 5);
		gbc_textField.gridx = 0;
		gbc_textField.gridy = 0;
		panel.add(textField, gbc_textField);

		JButton btnNewButton = new JButton(LABEL_BUTTON_SEARCH);


		btnNewButton.addActionListener( Controller.getInstance() );

		//		
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
		//		

		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.gridwidth = 6;
		gbc_btnNewButton.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnNewButton.gridx = 3;
		gbc_btnNewButton.gridy = 0;
		panel.add(btnNewButton, gbc_btnNewButton);

		list = new JList<String>();
		scrollPane = new JScrollPane(list);
		frame.getContentPane().add(scrollPane, BorderLayout.CENTER);





		frame.setVisible(true);
	}

}
