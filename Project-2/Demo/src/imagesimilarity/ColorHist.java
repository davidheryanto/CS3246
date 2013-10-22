package imagesimilarity;

// Don't know why is this import here?
// import imagesearch.ImageSearchQbe;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.LineBorder;

public class ColorHist extends JFrame {

	public static void main(String[] args) {
		ColorHist example = new ColorHist();
	}


	private MainPanel dropArea1;
	private MainPanel dropArea2;
	
	private JLabel imageLabel1;
	private JLabel imageLabel2;
	private String imagePath1;
	private String imagePath2;
	private Image img1;
	private Image img2;
	private JLabel distLabel;
	
	private JPanel contentPane;
	private JPanel imagePanel;
	private JPanel histPanel;
	
	private JButton computeSimilarityButton;
	private String basePath;
	private int width = 300; // the size for each image result
	private int height = 300;
	
	private int dim = 64;
	
	private BufferedImage buffered1;
	private BufferedImage buffered2;
	private Histogram hist1;
	private Histogram hist2;
	
	public ColorHist() {
		basePath = "E:\\workspace\\ImageSearchFramework\\"; // change it when necessary
		
		imageLabel1 = new JLabel();
		imageLabel2 = new JLabel();
		
		computeSimilarityButton = new JButton("Compute similarity");
		distLabel = new JLabel("", JLabel.CENTER);
		distLabel.setFont(new Font("Serif", Font.BOLD, 20));
		
		imagePanel = new JPanel();

		hist1 = new Histogram();
		FlowLayout flowLayout = (FlowLayout) hist1.getLayout();
		flowLayout.setVgap(0);
		flowLayout.setHgap(0);
		hist2 = new Histogram();
		contentPane = (JPanel)this.getContentPane();
		
		setSize(1200,600);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		init();
	}
	
	public void init() {
		try {
			UIManager.setLookAndFeel(
			        UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e2) {
			e2.printStackTrace();
		} catch (InstantiationException e2) {
			e2.printStackTrace();
		} catch (IllegalAccessException e2) {
			e2.printStackTrace();
		} catch (UnsupportedLookAndFeelException e2) {
			e2.printStackTrace();
		}
		
		GridLayout gridLayout = new GridLayout();  // grid layout 1 * 2
		gridLayout.setColumns(2); 
		gridLayout.setRows(4); 
		contentPane.setLayout(gridLayout);
		
		dropArea1 = new MainPanel(hist1);
		dropArea1.setBorder(new LineBorder(Color.GRAY, 1));
		dropArea1.setBackground(Color.BLACK);
		
		dropArea2 = new MainPanel(hist2);
		dropArea2.setBorder(new LineBorder(Color.GRAY, 1));
		dropArea2.setBackground(Color.BLACK);
		
		contentPane.add(dropArea1);
		contentPane.add(dropArea2);
		
		contentPane.add(imageLabel1);
		contentPane.add(imageLabel2);
		contentPane.add(hist1);
		contentPane.add(hist2);
		contentPane.add(computeSimilarityButton);
		contentPane.add(distLabel);
		contentPane.setVisible(true);
		setVisible(true);
		repaint();
		
		
		computeSimilarityButton.addActionListener(new ActionListener() {
			// Replace with own code
			
			public void actionPerformed(ActionEvent e) {
				double sim = computeSimilarity();
				distLabel.setText("Similarity is " + Double.toString(sim));
			}
		});
		
		repaint();
		
	}
	
	// TODO: Modify Similarity
	public double computeSimilarity() {
		
		double[] hist1 = getHist(dropArea1.getBufImg());
		double[] hist2 = getHist(dropArea2.getBufImg());
		
		double distance = calculateDistance(hist1, hist2);
		return 1-distance;
	}
	
	public double[] getHist(BufferedImage image) {
		int imHeight = image.getHeight();
        int imWidth = image.getWidth();
        double[] bins = new double[dim*dim*dim];
        int step = 256 / dim;
        Raster raster = image.getRaster();
        for(int i = 0; i < imWidth; i++)
        {
            for(int j = 0; j < imHeight; j++)
            {
            	// rgb->ycrcb
            	int r = raster.getSample(i,j,0);
            	int g = raster.getSample(i,j,1);
            	int b = raster.getSample(i,j,2);
            	int y  = (int)( 0.299   * r + 0.587   * g + 0.114   * b);
        		int cb = (int)(-0.16874 * r - 0.33126 * g + 0.50000 * b);
        		int cr = (int)( 0.50000 * r - 0.41869 * g - 0.08131 * b);
        		
        		int ybin = y / step;
        		int cbbin = cb / step;
        		int crbin = cr / step;

                bins[ ybin*dim*dim+cbbin*dim+crbin*dim ] ++;

            }
        }
        
        //normalize
        for(int i = 0; i < 3*dim; i++) {
        	bins[i] = bins[i]/(imHeight*imWidth);
        }
        
        return bins;
	}
	
	public double calculateDistance(double[] array1, double[] array2)
    {
		// Euclidean distance
        /*double Sum = 0.0;
        for(int i = 0; i < array1.length; i++) {
           Sum = Sum + Math.pow((array1[i]-array2[i]),2.0);
        }
        return Math.sqrt(Sum);
        */
        
        // Bhattacharyya distance
		double h1 = 0.0;
		double h2 = 0.0;
		int N = array1.length;
        for(int i = 0; i < N; i++) {
        	h1 = h1 + array1[i];
        	h2 = h2 + array2[i];
        }

        double Sum = 0.0;
        for(int i = 0; i < N; i++) {
           Sum = Sum + Math.sqrt(array1[i]*array2[i]);
        }
        double dist = Math.sqrt( 1 - Sum / Math.sqrt(h1*h2));
        return dist;
    }
}
