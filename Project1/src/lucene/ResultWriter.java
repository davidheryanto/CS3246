package lucene;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class ResultWriter {
	
	public static void writeResults(QueryList[] queries, ArrayList<String[]> docID, String outputPath) 
			throws FileNotFoundException {
		
		PrintWriter writer = new PrintWriter(outputPath);
		String[] id;
		
		System.out.println("Saving results to " + outputPath);
		for(int i = 0; i < docID.size(); i++) {
			id = docID.get(i);
			for(int j=0; j < id.length; j++) {
				writer.println(queries[i].getQNum() + "\t" + id[j]);
			}
			writer.println("**************");
		}
		writer.close();
		
	}
}