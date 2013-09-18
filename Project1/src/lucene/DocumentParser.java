package lucene;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Hashtable;

public class DocumentParser {
	// return a Hashtable array mapping of paper fileNumber -> paper object
	public static Hashtable<Integer, Paper> parseDocument(String path) {
		File folder = new File(path);
		File[] files = folder.listFiles();

		Hashtable<Integer, Paper> paperTable = new Hashtable<Integer, Paper>();
		for (File file : files) {
			int fileNumber = getFileNumber(file);
			Paper paper = getPaper(file);
			paperTable.put(fileNumber, paper);
		}
		
		return paperTable;
	}
	
	private static Paper getPaper(File file) {
		ArrayList<String> textBlockList = getTextBlocks(file);
		textBlockList = clean(textBlockList);
		
		// The extraction must be done in order for proper extraction
		// i.e. title -> summary -> year -> ...
		String title =  extractTitle(textBlockList);
		String summary = extractSummary(textBlockList);
		int year = extractYear(textBlockList);
		String[] authors = extractAuthors(textBlockList, file);
		String[] keywords = extractKeyword(textBlockList);
		
		return new Paper(title, summary, year, authors, keywords);
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
	
	private static int getFileNumber(File file) {
		String fileName = file.getName();
		String fileNumberString = fileName.substring(0, fileName.indexOf(".html"));
		Integer fileNumber = null;
		try {
			fileNumber = Integer.parseInt(fileNumberString);
		} catch (NumberFormatException e) {
			System.err.println("Parsing error: Data filename is not a number");
		}
		return fileNumber;
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


	private static String[] extractAuthors(ArrayList<String> textBlockList, File file) {
		
		if (authorExists(textBlockList, file)) {
			String textBlock = textBlockList.remove(0);
			String[] authors = textBlock.split(",( ?[A-z]+\\.)+ ?");

			return authors;
		}
		
		return null;
	}

	// Assume author name format: lastName, I. J. and so on
	// Therefore these may cause wrong parsing:
	// 1. authors whose initials are without '.'
	// 2. Chinese author such as Wu, Sheng-Chuan Abel
	// 3. authors with only one word such as Langdon 
	//    (because how to differentiate b/w author and keyword? 
	//     We use Initial I. to differentiate but the consequence is this error)
	private static boolean authorExists(ArrayList<String> textBlockList, File file) {
		if (textBlockList.size() < 1) {
			return false;
		}
		
		String textBlock = textBlockList.get(0).trim();
		if (textBlock.matches( "( ?[A-z'\\-\\s\\.]+,*( ?[A-z]+\\.)+)+" ) ) {
			return true;
		}
		
		return false;
	}

	private static String[] extractKeyword(ArrayList<String> textBlockList) {
		if (keywordExists(textBlockList)) {
			String textBlock = textBlockList.remove(0);
			String[] keywords = textBlock.split(", *");

			return keywords;

		}
		
		return null;
	}
	
	private static boolean keywordExists(ArrayList<String> textBlockList) {
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

	
	private static ArrayList<String> getTextBlocks(File file) {
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
	
	
}
