package imagesimilarity;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.SystemColor;

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

public class Main {

	private JFrame frame;
	private Searcher searcher;

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
	public Main() {
		initialize();
		searcher = new Searcher();
		
		// 		--------------------Test-------------------------
		//		try {
		//			ColorCoherence.setQuantizationLevel(64);
		//			ColorCoherence.setThreshold(32); // assume image is 100x100
		//			
		//			ProcessedImage p1 = new ProcessedImage();
		//			BufferedImage img = ImageIO.read(new File("18.jpg"));
		//			img = ImageHelper.resize(img, 100);
		//			ColorCoherence.extract(img);
		//			Result[] results = ColorCoherence.getResults();
		//			p1.setCCV(results);
		//			
		//			ProcessedImage p2 = new ProcessedImage();
		//			img = ImageIO.read(new File("19.jpg"));
		//			img = ImageHelper.resize(img, 100);
		//			ColorCoherence.extract(img);
		//			results = ColorCoherence.getResults();
		//			p2.setCCV(results);
		//			
		//			ProcessedImage p3 = new ProcessedImage();
		//			img = ImageIO.read(new File("17.jpg"));
		//			img = ImageHelper.resize(img, 100);
		//			ColorCoherence.extract(img);
		//			results = ColorCoherence.getResults();
		//			p3.setCCV(results);
		//			
		//			ProcessedImage p4 = new ProcessedImage();
		//			img = ImageIO.read(new File("t3.jpg"));
		//			img = ImageHelper.resize(img, 100);
		//			ColorCoherence.extract(img);
		//			results = ColorCoherence.getResults();
		//			p4.setCCV(results);
		//			
		//			
		//			double score_1_2 = CCVSimilarity.getScore(p1, p2);
		//			double score_1_3 = CCVSimilarity.getScore(p1, p3);
		//			double score_1_4 = CCVSimilarity.getScore(p1, p4);
		//			
		//			System.out.printf("%.3f\t%.3f\t%.3f%n", 
		//					score_1_2, score_1_3, score_1_4);
		//			
		//			
		//		} catch (IOException e) {
		//			e.printStackTrace();
		//		}
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		setSystemLookAndFeel();
		frame = new JFrame();
		frame.setBounds(500, 50, 500, 700);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel imagePanel = new JPanel();
		imagePanel.setBackground(Color.WHITE);
		frame.getContentPane().add(imagePanel, BorderLayout.NORTH);
		imagePanel.setLayout(new BorderLayout(0, 0));

		JPanel dropArea = new ImagePanel();
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
		DefaultListModel<String> model = new DefaultListModel<>();
		model.addElement("0.jpg");
		model.addElement("16.jpg");
		model.addElement("20.jpg");
		
		JList<String> imageList = new JList<>(model);
		imageList.setBackground(SystemColor.control);
		imageList.setCellRenderer(new IconListRenderer());
		imageList.setBorder(new EmptyBorder(0, 150, 0, 0));
		
		JScrollPane scrollPane = new JScrollPane(imageList);
		scrollPane.setBorder(null);
		frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
		// End search results
		// ---------------------------------

		JPanel optionPanel = new JPanel();
		frame.getContentPane().add(optionPanel, BorderLayout.SOUTH);
		optionPanel.setLayout(new GridLayout(1, 0, 0, 0));

		final JCheckBox chckbxNormalHistogram = new JCheckBox("Normal Histogram");
		chckbxNormalHistogram.setSelected(true);
		chckbxNormalHistogram.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				searcher.setCheckedNormalHistogram(chckbxNormalHistogram.isSelected());
			}
		});
		chckbxNormalHistogram.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		optionPanel.add(chckbxNormalHistogram);

		final JCheckBox chckbxCcv = new JCheckBox("Color Coherence");
		chckbxCcv.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				searcher.setCheckedCCV(chckbxCcv.isSelected());
			}
		});
		chckbxCcv.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		optionPanel.add(chckbxCcv);

		final JCheckBox chckbxEdgeDetection = new JCheckBox("Edge Detection");
		chckbxEdgeDetection.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				searcher.setCheckedEdge(chckbxEdgeDetection.isSelected());
			}
		});
		chckbxEdgeDetection.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		optionPanel.add(chckbxEdgeDetection);
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
