package peopleanalytics.logic;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

import peopleanalytics.ui.Window;

public class Listener implements ActionListener {
	private static Listener instance = new Listener();
	private static File fileRead;

	private Listener() {

	}

	public static Listener getInstance() {
		return instance;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {

		case "Open Image" :
			getAndShowImage();
			break;

		case "Detect Face" :
			FaceDetection faceDetection = new FaceDetection();
			try {
				BufferedImage img = faceDetection.detect(fileRead);
				showImage(img);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			break;
		}

	}



	private void getAndShowImage() {
		FileNameExtensionFilter imgFilter = 
				new FileNameExtensionFilter("Image Files", "jpg", "jpeg", "bmp", "gif", "png");
		fileRead = getFile(imgFilter);

		if (fileRead != null) {
			Window.setDetectFaceMenuEnabled(true);
			showImage(fileRead);
		}
	}

	private File getFile(FileNameExtensionFilter imgFilter) {
		File defaultPath = new File("data");
		JFileChooser fc = new JFileChooser(defaultPath);
		fc.setFileFilter(imgFilter);

		if (fc.showOpenDialog(Window.getFrame()) == JFileChooser.APPROVE_OPTION) {
			fileRead = fc.getSelectedFile();
		}

		return fileRead;
	}

	private void showImage(BufferedImage img) {
		JFrame frame = Window.getFrame();
		JPanel panel = Window.getPanel();

		Image resizedImg = getResizedImage(img, 400);
		JLabel picLabel = new JLabel(new ImageIcon(resizedImg));

		panel.removeAll();
		panel.add(picLabel);
		panel.revalidate(); //  Force JPanel to repaint
		frame.pack();  //  Resize JFrame to fit content
	}

	private void showImage(File file) {
		try {
			BufferedImage img = ImageIO.read(file);
			showImage(img);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Image getResizedImage(BufferedImage img, int newWidth) {
		double scale = (double) newWidth / img.getWidth();
		int newHeight = (int) (img.getHeight() * scale);

		return img.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
	}
}
