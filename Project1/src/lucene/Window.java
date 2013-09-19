package lucene;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.FontUIResource;

public class Window {
	private static final Window instance = new Window();

	private static JFrame frame;
	private static JTextField textField;
	private static JScrollPane scrollPane;
	private static JList<String> list;

	private static JButton importButton;
	private static JComboBox<String> similarityComboBox;
	private static JComboBox<String> searchTypeComboBox;
	private static JCheckBox reIndexCheckBox;
	private static JCheckBox pseudoCheckBox;

	// Singleton pattern
	private Window() { }

	public static Window getInstance() {
		return instance;
	}

	/**
	 * @wbp.parser.entryPoint
	 */
	public static void initialize(ListModel<String> model) {
		setUI();
		initFrame();
		initSearchPanel();
		initScrollPane(model);
		initFilePanel();
		initKeys();
		
		frame.setVisible(true);
	}

	private static void initKeys() {
		KeyboardFocusManager.getCurrentKeyboardFocusManager()
		  .addKeyEventDispatcher(Controller.getInstance() );
	}

	private static void initFrame() {
		frame = new JFrame();
		frame.setIconImage(new ImageIcon("lucene.png").getImage());
		frame.setTitle("Lucene Search Engine");
		frame.setSize(1080, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// get screen properties
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) ((dim.getWidth() - frame.getWidth()) / 2);
		int y = (int) ((dim.getHeight() - frame.getHeight()) / 2);
		frame.setLocation(x, y);
	}

	private static void initSearchPanel() {
		JPanel searchPanel = new JPanel();
		frame.getContentPane().add(searchPanel, BorderLayout.NORTH);

		// Setup GridBag Layout
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{127, 0, 70, 60, 100, 50, 0};
		gbl_panel.rowHeights = new int[] {30};
		gbl_panel.columnWeights = new double[]{1.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0};
		searchPanel.setLayout(gbl_panel);

		// Add input field
		textField = new JTextField("Type query");
		textField.addFocusListener(Controller.getInstance());

		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.insets = new Insets(5, 5, 5, 5);
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 0;
		gbc_textField.gridy = 0;
		searchPanel.add(textField, gbc_textField);

		// Add check box for re-indexing option
		reIndexCheckBox = new JCheckBox("Re-index", true);
		GridBagConstraints gbc_reindex = new GridBagConstraints();
		gbc_reindex.insets = new Insets(5, 5, 5, 5);
		gbc_reindex.gridx = 1;
		gbc_reindex.gridy = 0;
		searchPanel.add(reIndexCheckBox, gbc_reindex);
		
		// Add check box for Pseudo RF
		pseudoCheckBox = new JCheckBox("Pseudo RF");
		GridBagConstraints gbc_pseudo = new GridBagConstraints();
		gbc_pseudo.insets = new Insets(5, 5, 5, 5);
		gbc_pseudo.gridx = 2;
		gbc_pseudo.gridy = 0;
		searchPanel.add(pseudoCheckBox, gbc_pseudo);

		// Add similarity options
		JLabel similarityLabel = new JLabel("Similarity");
		similarityLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints gbcSimilarityLabel = new GridBagConstraints();
		gbcSimilarityLabel.insets = new Insets(5, 5, 5, 5);
		gbcSimilarityLabel.fill = GridBagConstraints.HORIZONTAL;
		gbcSimilarityLabel.gridx = 3;
		gbcSimilarityLabel.gridy = 0;
		searchPanel.add(similarityLabel, gbcSimilarityLabel);

		similarityComboBox = new JComboBox<String>();
		similarityComboBox.setModel(
				new DefaultComboBoxModel<String>(
						new String[] {
								Constants.SIMILARITY_DEFAULT,
								Constants.SIMILARITY_COSINE, 
								Constants.SIMILARITY_JACCARD, 
								Constants.SIMILARITY_TERM_CORRELATION}));
		GridBagConstraints gbcSimilarityComboBox = new GridBagConstraints();
		gbcSimilarityComboBox.insets = new Insets(5, 5, 5, 5);
		gbcSimilarityComboBox.fill = GridBagConstraints.HORIZONTAL;
		gbcSimilarityComboBox.gridx = 4;
		gbcSimilarityComboBox.gridy = 0;
		searchPanel.add(similarityComboBox, gbcSimilarityComboBox);

		// Add options for search type
		searchTypeComboBox = new JComboBox<String>();
		searchTypeComboBox.setModel(
				new DefaultComboBoxModel<String>(
						new String[] {
								Constants.SEARCH_TYPE_NORMAL, 
								Constants.SEARCH_TYPE_REFINE}));
		GridBagConstraints gbcSearchTypeComboBox = new GridBagConstraints();
		gbcSearchTypeComboBox.insets = new Insets(5, 5, 5, 0);
		gbcSearchTypeComboBox.fill = GridBagConstraints.HORIZONTAL;
		gbcSearchTypeComboBox.gridx = 5;
		gbcSearchTypeComboBox.gridy = 0;
		searchPanel.add(searchTypeComboBox, gbcSearchTypeComboBox);
	}

	private static void initScrollPane(ListModel<String> model) {
		list = new JList<String>(model);
		scrollPane = new JScrollPane(list);

		frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
	}

	private static void initFilePanel() {
		JPanel filePanel = new JPanel();
		frame.getContentPane().add(filePanel, BorderLayout.SOUTH);

		// Add button to import file
		GridBagLayout gbl_filePanel = new GridBagLayout();
		gbl_filePanel.columnWidths = new int[]{250, 0};
		gbl_filePanel.rowHeights = new int[] {30};
		gbl_filePanel.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_filePanel.rowWeights = new double[]{0.0};
		filePanel.setLayout(gbl_filePanel);

		importButton = new JButton("Import Query");
		importButton.addActionListener(Controller.getInstance() );
		GridBagConstraints gbc_btnNewButton_1 = new GridBagConstraints();
		gbc_btnNewButton_1.insets = new Insets(5, 5, 5, 5);
		gbc_btnNewButton_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnNewButton_1.gridx = 0;
		gbc_btnNewButton_1.gridy = 0;
		filePanel.add(importButton, gbc_btnNewButton_1);
	}

	private static void setUIFont(FontUIResource font)
	{
		Enumeration<Object> keys = UIManager.getDefaults().keys();
		while (keys.hasMoreElements())
		{
			Object key = keys.nextElement();
			Object value = UIManager.get(key);
			if (value instanceof javax.swing.plaf.FontUIResource)
			{
				UIManager.put(key, font);
			}
		}
	}


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

		setUIFont(new FontUIResource("Segoe UI",Font.PLAIN,16));
	}

	public static String getQueryString() {
		return textField.getText().trim();
	}

	public static String getSearchType() {
		return searchTypeComboBox.getSelectedItem().toString();
	}
	
	public static String getSimilarity() {
		return similarityComboBox.getSelectedItem().toString();
	}
	
	public static String[] getSelectedDocumentFileNames() {
		List<String> selectedDocumentList = list.getSelectedValuesList();
		List<String> selectedDocumentFileNamesList = new ArrayList<String>();

		for (int i = 0; i < selectedDocumentList.size(); i++) {
			String line = selectedDocumentList.get(i);
			int startIndex = line.indexOf('[');
			int endIndex = line.indexOf(']');

			if (startIndex > 0 && endIndex > 0) {
				selectedDocumentFileNamesList.add(line.substring(startIndex + 1, endIndex));
			}
		}

		String[] selecteDocumentFileNames = new String[selectedDocumentFileNamesList.size()];
		return selectedDocumentFileNamesList.toArray(selecteDocumentFileNames);
	}

	public static boolean isPseudoChecked() {
		return pseudoCheckBox.isSelected();
	}

	public static boolean isReIndexChecked() {
		return reIndexCheckBox.isSelected();
	}
	
	public static void uncheckReIndex() {
		reIndexCheckBox.setSelected(false);
	}
	
	public static void setTextField(String queryString) {
		textField.setText(queryString);
	}
	
	public static void selectList(int[] indices) {
		list.clearSelection();
		list.setSelectedIndices(indices);
	}
	
	public static Component getFrame() {
		return frame;
	}
}
