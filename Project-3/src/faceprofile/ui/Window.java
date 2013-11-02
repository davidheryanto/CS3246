package faceprofile.ui;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Observable;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.filechooser.FileNameExtensionFilter;


public class Window extends Observable {

	private JFrame frame;
	private FileNameExtensionFilter imgFilter = new FileNameExtensionFilter("Image Files", "jpg", "jpeg", "bmp", "gif", "png");

	/**
	 * Create the application.
	 */
	public Window() {
		initialize();
		this.frame.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		JMenu mnNewMenu = new JMenu("File");
		menuBar.add(mnNewMenu);
		
		JMenuItem mntmNewMenuItem = new JMenuItem("Open Image");
		mntmNewMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				File defaultPath = new File("data");
				JFileChooser fc = new JFileChooser(defaultPath);
				fc.setFileFilter(imgFilter);
				
				if (fc.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
					File file = fc.getSelectedFile();
					
					// Notify the observers that image has been selected
					Window.this.setChanged();
					Window.this.notifyObservers(file);
				}
			}
		});
		mnNewMenu.add(mntmNewMenuItem);
	}

	
	
}
