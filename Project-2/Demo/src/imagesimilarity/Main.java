package imagesimilarity;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;

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
import java.awt.SystemColor;

public class Main {

	private JFrame frame;

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
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		setSystemLookAndFeel();
		frame = new JFrame();
		frame.setBounds(200, 200, 500, 700);
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

		JCheckBox chckbxNormalHistogram = new JCheckBox("Normal Histogram");
		chckbxNormalHistogram.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		optionPanel.add(chckbxNormalHistogram);

		JCheckBox chckbxCcv = new JCheckBox("Color Coherence");
		chckbxCcv.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		optionPanel.add(chckbxCcv);

		JCheckBox chckbxEdgeDetection = new JCheckBox("Edge Detection");
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
