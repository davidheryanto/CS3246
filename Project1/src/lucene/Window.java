package lucene;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.Enumeration;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
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
	// Singleton pattern
	private static final Window instance = new Window();

	private Window() { }

	public static Window getInstance() {
		return instance;
	}
	





	

	private static JFrame frmLuceneSearchEngine;
	private static JTextField textField;
	private static JScrollPane scrollPane;
	private static JList list;
	private static JButton btnNewButton_1;
	private static JButton btnNewButton_2;
	private static JComboBox comboBox;
	private static JComboBox comboBox_1;
	private static JLabel lblNewLabel;
	private static JLabel lblNewLabel_1;
	private static JComboBox comboBox_2;

	
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
		setUIFont(new javax.swing.plaf.FontUIResource("Segoe UI",Font.PLAIN,16));

		
		frmLuceneSearchEngine = new JFrame();
		frmLuceneSearchEngine.setTitle("Lucene Search Engine");
		frmLuceneSearchEngine.getContentPane().setFont(new Font("Segoe UI Light", Font.PLAIN, 14));
		frmLuceneSearchEngine.setSize(800, 500);
		frmLuceneSearchEngine.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		
		JPanel filePanel = new JPanel();
		frmLuceneSearchEngine.getContentPane().add(filePanel, BorderLayout.SOUTH);
		GridBagLayout gbl_filePanel = new GridBagLayout();
		gbl_filePanel.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_filePanel.rowHeights = new int[] {30};
		gbl_filePanel.columnWeights = new double[]{1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		gbl_filePanel.rowWeights = new double[]{0.0};
		filePanel.setLayout(gbl_filePanel);
		
		btnNewButton_1 = new JButton("Import Query");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		GridBagConstraints gbc_btnNewButton_1 = new GridBagConstraints();
		gbc_btnNewButton_1.insets = new Insets(5, 5, 5, 5);
		gbc_btnNewButton_1.gridwidth = 6;
		gbc_btnNewButton_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnNewButton_1.gridx = 0;
		gbc_btnNewButton_1.gridy = 0;
		filePanel.add(btnNewButton_1, gbc_btnNewButton_1);
		
		btnNewButton_2 = new JButton("Export Result");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		GridBagConstraints gbc_btnNewButton_2 = new GridBagConstraints();
		gbc_btnNewButton_2.gridwidth = 6;
		gbc_btnNewButton_2.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnNewButton_2.insets = new Insets(5, 5, 5, 5);
		gbc_btnNewButton_2.gridx = 6;
		gbc_btnNewButton_2.gridy = 0;
		filePanel.add(btnNewButton_2, gbc_btnNewButton_2);
		
		
		JPanel searchPanel = new JPanel();
		frmLuceneSearchEngine.getContentPane().add(searchPanel, BorderLayout.NORTH);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{127, 70, 50, 60, 100, 50, 0};
		gbl_panel.rowHeights = new int[] {30};
		gbl_panel.columnWeights = new double[]{1.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0};
		searchPanel.setLayout(gbl_panel);

		textField = new JTextField("Type query");
		

		textField.addActionListener(Controller.getInstance());
		textField.setColumns(10);
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.insets = new Insets(5, 5, 5, 5);
		gbc_textField.ipadx = 10;
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 0;
		gbc_textField.gridy = 0;
		searchPanel.add(textField, gbc_textField);
		
		lblNewLabel = new JLabel("Pseudo RF");
		lblNewLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.insets = new Insets(5, 5, 5, 5);
		gbc_lblNewLabel.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblNewLabel.gridx = 1;
		gbc_lblNewLabel.gridy = 0;
		searchPanel.add(lblNewLabel, gbc_lblNewLabel);
		
		comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"0", "1", "2", "3", "4", "5"}));
		GridBagConstraints gbc_comboBox = new GridBagConstraints();
		gbc_comboBox.insets = new Insets(5, 5, 5, 5);
		gbc_comboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox.gridx = 2;
		gbc_comboBox.gridy = 0;
		searchPanel.add(comboBox, gbc_comboBox);
		
		lblNewLabel_1 = new JLabel("Similarity");
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.insets = new Insets(5, 5, 5, 5);
		gbc_lblNewLabel_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblNewLabel_1.gridx = 3;
		gbc_lblNewLabel_1.gridy = 0;
		searchPanel.add(lblNewLabel_1, gbc_lblNewLabel_1);
		
		comboBox_1 = new JComboBox();
		comboBox_1.setModel(new DefaultComboBoxModel(new String[] {"Cosine", "Jaccard", "Term Correlation"}));
		GridBagConstraints gbc_comboBox_1 = new GridBagConstraints();
		gbc_comboBox_1.insets = new Insets(5, 5, 5, 5);
		gbc_comboBox_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox_1.gridx = 4;
		gbc_comboBox_1.gridy = 0;
		searchPanel.add(comboBox_1, gbc_comboBox_1);
		
		comboBox_2 = new JComboBox();
		comboBox_2.setModel(new DefaultComboBoxModel(new String[] {"Search", "Refine Query"}));
		GridBagConstraints gbc_comboBox_2 = new GridBagConstraints();
		gbc_comboBox_2.insets = new Insets(5, 5, 5, 5);
		gbc_comboBox_2.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox_2.gridx = 5;
		gbc_comboBox_2.gridy = 0;
		searchPanel.add(comboBox_2, gbc_comboBox_2);

		list = new JList<String>();
		scrollPane = new JScrollPane(list);

		frmLuceneSearchEngine.getContentPane().add(scrollPane, BorderLayout.CENTER);

		frmLuceneSearchEngine.setVisible(true);
	}

}
