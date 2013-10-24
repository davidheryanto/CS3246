package imagesimilarity;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.SystemColor;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JSeparator;

public class Main {

	private JFrame frame;
	private DefaultListModel<String> modelString;
	private DefaultListModel<Image> model;
	private Searcher searcher;
	
	public DefaultListModel<Image> getModel() {
		return this.model;
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main window = new Main();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Main() throws IOException {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() throws IOException {
		setSystemLookAndFeel();
		frame = new JFrame();
		frame.setBounds(500, 20, 550, 700);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel imagePanel = new JPanel();
		imagePanel.setBackground(Color.WHITE);
		frame.getContentPane().add(imagePanel, BorderLayout.NORTH);
		imagePanel.setLayout(new BorderLayout(0, 0));

		model = new DefaultListModel<>();
		modelString = new DefaultListModel<>();
		
		JList<String> resultJList = new JList<>(modelString);
		resultJList.setBorder(null);
		resultJList.setBackground(SystemColor.inactiveCaptionBorder);
		resultJList.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		JScrollPane leftScrollPane = new JScrollPane(resultJList);
		leftScrollPane.setPreferredSize(new Dimension(150, 130));
		leftScrollPane.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		frame.getContentPane().add(leftScrollPane, BorderLayout.WEST);
		
		
		JPanel dropArea = new ImagePanel(model, modelString);
		dropArea.setBackground(Color.WHITE);
		imagePanel.add(dropArea, BorderLayout.SOUTH);
		dropArea.setPreferredSize(new Dimension(10, 100));
		dropArea.setBorder(null);
		dropArea.setToolTipText("");
		
		JLabel lblNewLabel = new JLabel("Drop an image below to search");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		imagePanel.add(lblNewLabel, BorderLayout.NORTH);
		lblNewLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));

		// Search results
		// ---------------------------------
		// Test manual input
		Image img = ImageIO.read(new File("0.jpg"));
		img = img.getScaledInstance(-1, 100, Image.SCALE_FAST);
		model.addElement(img);
		
		img = ImageIO.read(new File("16.jpg"));
		img = img.getScaledInstance(-1, 100, Image.SCALE_FAST);
		model.addElement(img);
		
		img = ImageIO.read(new File("18.jpg"));
		img = img.getScaledInstance(-1, 100, Image.SCALE_FAST);
		model.addElement(img);
		
		JList<Image> imageList = new JList<>(model);
		imageList.setBackground(SystemColor.control);
		imageList.setCellRenderer(new IconListRenderer());
		imageList.setBorder(new EmptyBorder(0, 0, 0, 0));
		
		JScrollPane scrollPane = new JScrollPane(imageList);
		scrollPane.setBorder(null);
		frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
		
		JSeparator separator = new JSeparator();
		scrollPane.setColumnHeaderView(separator);
		// End search results
		// ---------------------------------

		JPanel optionPanel = new JPanel();
		frame.getContentPane().add(optionPanel, BorderLayout.SOUTH);
		optionPanel.setLayout(new GridLayout(1, 0, 0, 0));

		final JCheckBox chckbxNormalHistogram = new JCheckBox("Color Histogram");
		chckbxNormalHistogram.setSelected(true);
		chckbxNormalHistogram.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				Searcher.setCheckedNormalHistogram(chckbxNormalHistogram.isSelected());
			}
		});
		chckbxNormalHistogram.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		optionPanel.add(chckbxNormalHistogram);

		final JCheckBox chckbxCcv = new JCheckBox("Color Coherence");
		chckbxCcv.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				Searcher.setCheckedCCV(chckbxCcv.isSelected());
			}
		});
		chckbxCcv.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		optionPanel.add(chckbxCcv);

		final JCheckBox chckbxEdgeDetection = new JCheckBox("Edge Detection");
		chckbxEdgeDetection.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				Searcher.setCheckedEdge(chckbxEdgeDetection.isSelected());
			}
		});
		chckbxEdgeDetection.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		optionPanel.add(chckbxEdgeDetection);
		
		JButton btnIndexImages = new JButton("Index Images");
		btnIndexImages.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					Indexer.index();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		optionPanel.add(btnIndexImages);
		
		
	}

	private void setSystemLookAndFeel() {
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

}
