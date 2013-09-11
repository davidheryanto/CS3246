package lucene;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.util.Version;

public class Main {
	private static final Version VERSION = Version.LUCENE_36;
	
	private static final String DIR_PATH_DATA = "./data";
	private static final String DIR_PATH_INDEX = "./index";
	
	
	public static void main(String[] args) {
		int indexCount = index();
		TopDocs topDocs = search();
		print(topDocs);
	}
	
	
	public static int index() {
		Directory directory = getDirectory(DIR_PATH_INDEX);
		Analyzer analyzer = new StandardAnalyzer(VERSION);
		IndexWriterConfig config = new IndexWriterConfig(VERSION, analyzer);
		IndexWriter indexWriter = getIndexWriter(directory, config);
		Indexer.setIndexWriter(indexWriter);
		
		Paper[] papers = getPaper(DIR_PATH_DATA, Charset.defaultCharset());
		return Indexer.index(papers);
	}

	public static TopDocs search() {
		return null;
	}
	
	public static void print(TopDocs topDocs) {
		
	}


	// Helper class
	public static Paper[] getPaper(String path, Charset encoding) {
		File folder = new File(path);
		File[] files = folder.listFiles();

		ArrayList<Paper> papers = new ArrayList<Paper>();
		for (File file : files) {
			Paper paper = getPaper(file);
			
//			System.out.println(file.getName());
			

			// TODO: exclude the wrapper tags

			// TODO: create proper Paper objects


			//				Paper paper = new Paper(null, null, null, stringRead);
			//				papers.add(paper);
		}
		
		return papers.toArray(new Paper[papers.size()]);
	}
	
	public static Paper getPaper(File file) {
		ArrayList<String> textBlockList = getTextBlocks(file);
		
		
		
		textBlockList = clean(textBlockList);
		
//		for(String s : textBlockList) System.out.println(s);
		
		// The extraction must be done in order for proper extraction
		// i.e. title -> summary -> year -> ...
		String id = getId(file);
		String title =  extractTitle(textBlockList);
		String summary = extractSummary(textBlockList);
		int year = extractYear(textBlockList);
		String[] authors = extractAuthors(textBlockList);
		String[] keywords = extractKeyword(textBlockList);
		
		return null;
	}

	// Remove HTML tags and blocks that are numbers only
	private static ArrayList<String> clean(ArrayList<String> textBlockList) {
		ArrayList<String> cleanList = new ArrayList<String>();
		for (int i = 0; i < textBlockList.size(); i++) {
			String textBlock = textBlockList.get(i);
			if (textBlock.matches("(.*</?html>.*)|([0-9\\s.]*)") ) {
				continue;
			}
			cleanList.add(textBlock);
		}
		return cleanList;
	}

	// Remove title field (index 0) from the list.
	private static String extractTitle(ArrayList<String> textBlockList) {
		return textBlockList.remove(0);
	}


	private static String extractSummary(ArrayList<String> textBlockList) {
		String textBlock = textBlockList.get(0);
		int spaceIndex = textBlock.indexOf(" ");
		
		SimpleDateFormat sdf = new SimpleDateFormat("MMM,yyyy");
		try {
			sdf.parse(textBlock.substring(spaceIndex + 1));
		} catch (ParseException e) {
			// textBlock is a summary because it is not a date
			return textBlockList.remove(0);
		}
		// textBlock is date, no summary for this file
		return "";
	}


	private static int extractYear(ArrayList<String> textBlockList) {
		String textBlock = textBlockList.remove(0);
		String yearString = textBlock.substring(textBlock.length() - 4);
		
		return Integer.parseInt(yearString);
	}


	private static String[] extractAuthors(ArrayList<String> textBlockList) {
		String textBlock = textBlockList.remove(0);
		String[] authors = textBlock.split(",( .\\.)* ?");
		
		return authors;
	}


	private static String[] extractKeyword(ArrayList<String> textBlockList) {
		if (keywordExists(textBlockList)) {
			String textBlock = textBlockList.remove(0);
			String[] keywords = textBlock.split(", *");

			for (String s : keywords) System.out.println(s);
			return keywords;

		}
		
		return null;
	}
	
	public static boolean keywordExists(ArrayList<String> textBlockList) {
		if (textBlockList.size() < 1) {
			return false;
		}
		
		String textBlock = textBlockList.get(0).trim();
		String lastTwoChars = textBlock.substring(textBlock.length() - 2);
		if (lastTwoChars.equalsIgnoreCase("am") || lastTwoChars.equalsIgnoreCase("pm")) {
			return false;
		}
		
		return true;
	}

	public static String getId(File file) {
		String fileName = file.getName();
		return fileName.substring(fileName.indexOf(".html"));
	}
	
	public static ArrayList<String> getTextBlocks(File file) {
		ArrayList<String> textBlockList = new ArrayList<String>();
		try {
			FileReader fileReader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			
			StringBuilder stringBuilder = new StringBuilder();
			String line = bufferedReader.readLine();
			while (line != null) {
				if (line.length() < 1) {
					// Current line is an empty string
					String textBlock = stringBuilder.toString().trim();
					if (textBlock.length() > 0) {
						textBlockList.add(textBlock);
					}
					// reset stringBuilder
					stringBuilder = new StringBuilder();
					line = bufferedReader.readLine();
					continue;
				}
				
				stringBuilder.append(" " + line);
				line = bufferedReader.readLine();
			}
			
			String textBlock = stringBuilder.toString().trim();
			if (textBlock.length() > 0) {
				textBlockList.add(textBlock);
			}
			
			bufferedReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return textBlockList;
	}
	
	public static Directory getDirectory(String path) {
		File file = new File(path);
		Directory directory = null;
		try {
			directory = FSDirectory.open(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return directory;
	}
	
	public static IndexWriter getIndexWriter(Directory directory,
			IndexWriterConfig config) {
		IndexWriter indexWriter = null;
		
		try {
			indexWriter = new IndexWriter(directory, config);
		} catch (CorruptIndexException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LockObtainFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return indexWriter;
	}
}
