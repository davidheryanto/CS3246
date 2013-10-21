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
    	
    	try {
    		img = ImageIO.read(files[0]);
    		imgBuf = ImageIO.read(files[0]);
    		
    		double scale = imgBuf.getHeight() / 150.0;
    		height = 150;
    		width = (int) (imgBuf.getWidth() / scale);
    		
    		histogram.load(imagePath1); // paint histogram
			histogram.repaint();
			
			// TODO update this
			// Test Sobel Filter
			// -----------------------
			// img = SobelFilter.apply(imgBuf);
			BufferedImage temp1;
			BufferedImage temp2;
			
			Filter filter = new Filter();
			
			// Find horizontal edges
			filter.setFilter(FilterSobel.getFilterX());
			temp1 = filter.apply(imgBuf, FilterSobel.getNormalizeFactor());
			
			// Find vertical edges
			filter.setFilter(FilterSobel.getFilterY());
			temp2 = filter.apply(imgBuf, FilterSobel.getNormalizeFactor());
			
			// Compute the average of the 2 images
			img = ImageHelper.add(temp1, temp2);
			
			// Test Blurring
			// -------------------------------
			FilterBlur.setRadius(7);
			filter.setFilter(FilterBlur.getFilter());
			img = filter.apply(imgBuf, FilterBlur.getNormalizeFactor());
			
			// Test Quantization
			// -------------------------------
			// reduce no of colors intensity level from 256 -> quantizationLevel
			int quantizationLevel = 12;	
			img = ImageHelper.quantizeColor(imgBuf, quantizationLevel);
			
			// For debugging
			// Print image pixel value into console
			ImageHelper.print((BufferedImage) img);
			
			
			
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