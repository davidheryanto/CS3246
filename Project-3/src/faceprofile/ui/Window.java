package faceprofile.ui;
import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import faceprofile.logic.Listener;


public class Window {

	private static JFrame frame;
	private static JPanel panel;
	private static JMenuItem openImageMenu;
	private static JMenuItem detectFaceMenu;

	private Window() {
		
	}
	
	public static JFrame getFrame() {
		return frame;
	}

	public static JPanel getPanel() {
		return panel;
	}
	
	public static void setDetectFaceMenuEnabled(boolean state) {
		detectFaceMenu.setEnabled(state);
	}

	/**
	 * Initialize the contents of the frame.
	 * @wbp.parser.entryPoint JavaDoc
	 */
	public static void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);

		JMenu menu = new JMenu("File");
		menuBar.add(menu);

		openImageMenu = new JMenuItem("Open Image");
		openImageMenu.addActionListener(Listener.getInstance());
		menu.add(openImageMenu);
		
		detectFaceMenu = new JMenuItem("Detect Face");
		detectFaceMenu.setEnabled(false);
		detectFaceMenu.addActionListener(Listener.getInstance());
		menu.add(detectFaceMenu);

		panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		
		frame.setVisible(true);
	}

	

	
}
