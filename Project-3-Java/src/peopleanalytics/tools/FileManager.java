package peopleanalytics.tools;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import au.com.bytecode.opencsv.CSVWriter;

public class FileManager {
	public static void main(String[] args) throws IOException {
		writeCSV("C:\\Users\\David\\Desktop\\female.csv");
	}
	
	
	public static void writeCSV(String CSVFilePath) throws IOException {
		CSVWriter writer = new CSVWriter(new FileWriter(CSVFilePath), ',');
		File folder = new File("C:\\Users\\David\\Git\\School\\CS3246\\Project-3\\PeopleAnalytics\\Data\\asian-female");
		List<String[]> entryList = new ArrayList<>();
		
		for (File f : folder.listFiles()) {
			System.out.println(f.getAbsolutePath());
			
			String[] entry = {f.getAbsolutePath(), "1"};
			entryList.add(entry);
		}
		
		writer.writeAll(entryList);
		writer.close();
		
		
		
		
//		
//		
//		
//		
//		
//		try {
//			CSVWriter writer = new CSVWriter(new FileWriter(CSVFilePath));
//			String[] entries =  "first#second#third".split("#");
//			writer.writeNext(entries);
//			writer.close();
//			
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		
	}
	
	
}
