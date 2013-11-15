package crowdanalytics.tools;

import java.io.FileWriter;
import java.io.IOException;

import au.com.bytecode.opencsv.CSVWriter;

public class FileManager {
	public static void main(String[] args) {
		writeCSV("belle.csv");
	}
	
	
	public static void writeCSV(String CSVFilePath) {
		try {
			CSVWriter writer = new CSVWriter(new FileWriter(CSVFilePath));
			String[] entries =  "first#second#third".split("#");
			writer.writeNext(entries);
			writer.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
}
