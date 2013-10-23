package imagesimilarity;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.LineBorder;

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
		frame.setBounds(200, 200, 700, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(10, 40));
		panel.setBorder(new LineBorder(Color.LIGHT_GRAY));
		panel.setToolTipText("");
		frame.getContentPane().add(panel, BorderLayout.NORTH);
		
		JLabel lblNewLabel = new JLabel("Drop an image here to search");
		lblNewLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		panel.add(lblNewLabel);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBorder(null);
		frame.getContentPane().add(scrollPane, BorderLayout.CENTER);

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
