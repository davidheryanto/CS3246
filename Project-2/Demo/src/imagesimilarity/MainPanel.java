package imagesimilarity;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class MainPanel extends JPanel {
	private static final long serialVersionUID = -7633345270628142437L;
	private Image img;
	private BufferedImage imgBuf;
	private int width, height;
    private Histogram histogram;

    MainPanel(Histogram hist) {
        setTransferHandler(new ImageTransferHandler(this));
        this.histogram = hist;
    }
    
    public BufferedImage getBufImg() {
    	return imgBuf;
    }
    
    void addFiles(File[] files) {
    	String imagePath1 = files[0].getAbsolutePath();
    	
    	
//		Image img1 = null;
//		try {
//			img1 = ImageIO.read(new File(imagePath1));
//			buffered1 = ImageIO.read(new File(imagePath1));
//			hist1.load(imagePath1); // paint histogram
//			hist1.repaint();
//			img1 = img1.getScaledInstance(width, -1, img1.SCALE_DEFAULT);
//		} catch (IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		imageLabel1.setIcon(new ImageIcon(img1));
    	
    	try {
    		img = ImageIO.read(files[0]);
    		imgBuf = ImageIO.read(files[0]);
    		
    		double scale = imgBuf.getHeight() / 150.0;
    		height = 150;
    		width = (int) (imgBuf.getWidth() / scale);
    		
    		histogram.load(imagePath1); // paint histogram
			histogram.repaint();
			
			// TODO update this
			// test sobel filter
			// img = SobelFilter.apply(imgBuf);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	
        if (img != null) {
            repaint();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLUE);
        if (img != null) {
        	drawImage((Graphics2D) g, width, height);
        }
    }

    private void drawImage(Graphics2D g2, int width, int height) {
        if (img != null) {
            g2.drawImage(img, 0, 0, width, height, null);
        } else {
            g2.drawRect(10, 10, width - 20, height - 20);
        }
    }
}