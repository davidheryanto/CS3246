package imagesimilarity;

import imagesimilarity.ColorCoherence.Result;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import javax.imageio.ImageIO;

public class Indexer {
	// Images to be indexed is located in Project-2/Demo/data/dataset"
	private static String DATA_PATH = "data/dataset";
	private static String INDEX_PATH = "index";
	private static String INDEX_COLOR_PATH = "index/color.txt";
	private static String INDEX_EDGE_PATH = "index/edge.txt";
	private static String INDEX_CCV_PATH = "index/ccv.txt";
	
	private static int SIZE = 256;
	// Red, Green, Blue   
	private static int NUMBER_OF_COLOURS = 3;

	private static int RED = 0;
	private static int GREEN = 1;
	private static int BLUE = 2;
	
	public static void index() throws IOException {
		// clear index folder
		File indexFolder = new File(INDEX_PATH);
		for (File f : indexFolder.listFiles()) {
			f.delete();
		}
		
		// Create index for color histogram
		File folder = new File(DATA_PATH);
		for (File file : folder.listFiles()) {
			if (!isImage(file)) {
				continue;
			}
			BufferedImage img = ImageIO.read(file);
			img = ImageHelper.resize(img, 200);
			
			indexCCV(img, file.getAbsolutePath());
			indexColor(img, file.getAbsolutePath());
			indexEdge(img, file.getAbsolutePath());
			
			
			System.out.println(file.getName() + " indexed");
		}
		
		System.out.println("-------------------------------");
		System.out.println("Finish indexing");
		
		read();
	}
	
	public static void indexEdge(BufferedImage image, String filePath) {
		BufferedImage filteredImage = FilterSobel.apply(image);
		
		int width = filteredImage.getWidth();
		int height = filteredImage.getHeight();
		
		int[][] hist = new int[NUMBER_OF_COLOURS][8]; // 8 directions
		
		for (int i = 0; i < NUMBER_OF_COLOURS; i++) {
			for (int j = 0; j < 8; j++) {
				hist[i][j] = 0;
			}
		}

		for(int i = 0; i < width; i++) {
			for(int j = 0; j < height; j++) 
			{
				Color c = new Color(filteredImage.getRGB(i, j));
				hist[RED][c.getRed()]++;
				hist[GREEN][c.getGreen()]++;
				hist[BLUE][c.getBlue()]++;
			}
		}
		

		try {
			boolean fileExists = new File(INDEX_EDGE_PATH).exists();
			FileWriter fw = fileExists 
					? new FileWriter(INDEX_EDGE_PATH, true)
					: new FileWriter(INDEX_EDGE_PATH, false);
			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter writer = new PrintWriter(bw);
			
			writer.println(filePath);	//print image number
			
			for(int i = 0; i < NUMBER_OF_COLOURS; i++)
			{
				for(int j = 0; j < 8; j++)
				{
					writer.print(hist[i][j]);
					writer.print(" ");
				}
				writer.println();
			}
			
			writer.println("****************");
			writer.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void indexColor(BufferedImage image, String filePath) {
		int width = image.getWidth();
		int height = image.getHeight();
		
		int [][] hist = new int[NUMBER_OF_COLOURS][SIZE];
				
		for (int i = 0; i < NUMBER_OF_COLOURS; i++) {
			for (int j = 0; j < SIZE; j++) {
				hist[i][j] = 0;
			}
		}

		for(int i = 0; i < width; i++) {
			for(int j = 0; j < height; j++) 
			{
				Color c = new Color(image.getRGB(i, j));
				hist[RED][c.getRed()]++;
				hist[GREEN][c.getGreen()]++;
				hist[BLUE][c.getBlue()]++;
			}
		}
		
		
		try {
			boolean fileExists = new File(INDEX_COLOR_PATH).exists();
			FileWriter fw = fileExists 
					? new FileWriter(INDEX_COLOR_PATH, true)
					: new FileWriter(INDEX_COLOR_PATH, false);
			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter writer = new PrintWriter(bw);
			
			writer.println(filePath);	//print image number
			
			for(int i = 0; i < NUMBER_OF_COLOURS; i++)
			{
				for(int j = 0; j < SIZE; j++)
				{
					writer.print(hist[i][j]);
					writer.print(" ");
				}
				writer.println();
			}
			
			writer.println("****************");
			writer.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void indexCCV(BufferedImage img, String filePath) {
		ColorCoherence.extract(img);
		Result[] CCVarray = ColorCoherence.getResults();
		
		try {
			boolean fileExists = new File(INDEX_CCV_PATH).exists();
			FileWriter fw = fileExists 
					? new FileWriter(INDEX_CCV_PATH, true)
					: new FileWriter(INDEX_CCV_PATH, false);
			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter writer = new PrintWriter(bw);
			
			writer.println(filePath);	//print image number
			
			for(int i = 0; i < CCVarray.length; i++)
			{
				writer.println(CCVarray[i].coherent.length);
				for(int j = 0; j < CCVarray[i].coherent.length; j++)
				{
					writer.print(CCVarray[i].coherent[j]);
					writer.print(" ");
				}
				writer.println();
				writer.println(CCVarray[i].incoherent.length);
				for(int j = 0; j < CCVarray[i].incoherent.length; j++)
				{
					writer.print(CCVarray[i].incoherent[j]);
					writer.print(" ");
				}
				writer.println();
			}
			
			writer.println("****************");
			writer.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public static boolean isImage(File file) {
		String fileName = file.getName();
		int index = fileName.indexOf(".");
		String extension = fileName.substring(index + 1);
		
		return extension.equals("jpg") || extension.equals("jpeg");
	}

	public static ArrayList<ProcessedImage> read() throws IOException {
		ArrayList<ProcessedImage> processedImages =  new ArrayList<ProcessedImage>();
		readIndexCCV(processedImages);
		readIndexColor(processedImages);
		readIndexEdge(processedImages);
		
		System.out.println("-----------------------");
		System.out.println("Finish reading index");
		
		
		return processedImages;
	}
	
	public static void readIndexCCV(
			ArrayList<ProcessedImage> imgList) throws IOException {
		
		String[] coherent = new String[3];
		String[] coherentSize = new String[3];
		String[] incoherent = new String[3];
		String[] incoherentSize = new String[3];
		
		if (!new File(INDEX_CCV_PATH).exists()) {
			return; 
		}
		
		BufferedReader br = new BufferedReader(new FileReader(INDEX_CCV_PATH));
		
		String filePath = br.readLine();
		while (filePath != null) {
			Result[] results = new Result[3];
			
			// System.out.println("Reading ccv index for " + fileName);
			
			for(int i = 0; i < 3; i++)
			{
				coherentSize[i] = br.readLine();
				coherent[i] = br.readLine();
				incoherentSize[i] = br.readLine();
				incoherent[i] = br.readLine();
				
				results[i] = new Result();
				results[i].coherent = scan(coherent[i], coherentSize[i]);
				results[i].incoherent = scan(incoherent[i], incoherentSize[i]);
			}
			
			ProcessedImage pi = new ProcessedImage();
			pi.setCCV(results);
			pi.setFilePath(filePath);
			pi.setFileName(getFileName(filePath));
			imgList.add(pi);
			
			br.readLine(); // Line containing "***********************"
			filePath = br.readLine();
		}
		br.close();
	}

	public static void readIndexColor(
			ArrayList<ProcessedImage> imgList) throws IOException {
		if (!new File(INDEX_COLOR_PATH).exists()) {
			return; 
		}
		
		String redLine = null;
		String greenLine = null;
		String blueLine = null;

		BufferedReader br = new BufferedReader(
				new FileReader(INDEX_COLOR_PATH));
		
		String filePath = br.readLine();
		int index = 0;
		while (filePath != null) {
			ProcessedImage pi = imgList.get(index);
			
			redLine = br.readLine();
			greenLine = br.readLine();
			blueLine = br.readLine();
			
			int[][] hist = new int[NUMBER_OF_COLOURS][SIZE];
			hist[0] = scan(redLine);
			hist[1] = scan(greenLine);
			hist[2] = scan(blueLine);
			
			pi.setColorHist(hist);
			
			br.readLine();
			filePath = br.readLine();
			index++;
		}
		br.close();
	}
	
	public static void readIndexEdge(
			ArrayList<ProcessedImage> imgList) throws IOException {
		if (!new File(INDEX_EDGE_PATH).exists()) {
			return; 
		}
		
		
		String redLine = null;
		String greenLine = null;
		String blueLine = null;

		BufferedReader br = new BufferedReader(
				new FileReader(INDEX_EDGE_PATH));
		
		String filePath = br.readLine();
		int index = 0;
		while (filePath != null) {
			ProcessedImage pi = imgList.get(index);
			
			redLine = br.readLine();
			greenLine = br.readLine();
			blueLine = br.readLine();
			
			int[][] hist = new int[NUMBER_OF_COLOURS][8];
			
			
			
			hist[0] = scan(redLine, "8");
			hist[1] = scan(greenLine, "8");
			hist[2] = scan(blueLine, "8");
			
			pi.setEdgeHist(hist);
			
			br.readLine();
			filePath = br.readLine();
			index++;
		}
		br.close();
	}

	public static String getFileName(String filePath) {
		int index = filePath.lastIndexOf('\\');
		return filePath.substring(index + 1);
	}
	
	private static int[] scan(String line) {
		int[] array = new int[SIZE];
		
		Scanner sc = new Scanner(line);
		for(int i = 0; i < SIZE; i++)
			array[i] = sc.nextInt();
		sc.close();
		
		return array;
	}
	
	private static int[] scan(String line, String size) {
		int size1 = Integer.parseInt(size);
		int[] array = new int[size1];
		
		Scanner sc = new Scanner(line);
		for(int i = 0; i < size1; i++)
			array[i] = sc.nextInt();
		sc.close();
		
		return array;
	}
}