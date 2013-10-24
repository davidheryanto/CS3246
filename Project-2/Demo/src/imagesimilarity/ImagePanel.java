package imagesimilarity;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class ImagePanel extends JPanel {
	private Image image;
	private int width;
	private int height;
	private double scale;
	
	private DefaultListModel<Image> model;
	private Searcher searcher;

	ImagePanel(DefaultListModel<Image> model) {
		setTransferHandler(new ImageTransferHandler(this));
		this.model = model;
		this.searcher = new Searcher();
	}

	void addFiles(File[] files) {
		for (File file : files) {
			try {
				image = ImageIO.read(file);
				width = ((BufferedImage) image).getWidth();
				height = ((BufferedImage) image).getHeight();
				// Scale down height to 100
				scale = 100.0 / height;
				
				// TODO (David) modify this
				Image input = ImageIO.read(new File(file.getAbsolutePath()));
				// TOO do we need to resize it first?
				String[] results = searcher.search((BufferedImage) input);
				
				model.clear();
				for (String resultPath : results) {
					Image img = ImageIO.read(new File(resultPath));
					img = img.getScaledInstance(-1, 100, Image.SCALE_FAST);
					model.addElement(img);
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (image != null) {
			repaint();
		}
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.WHITE);
		width = (int) (scale * width);
		height = (int) (scale * height);
		drawImage((Graphics2D) g, width, height);
	}

	private void drawImage(Graphics2D g2, int width, int height) {
		if (image != null) {
			g2.drawImage(image, 150, 0, width, height, null);
		} else {
			g2.drawRect(10, 10, width - 20, height - 20);
		}
	}
}
