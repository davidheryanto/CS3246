package lucene;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class ResultWriter {
	
	public static void writeResults(QueryList[] queries, ArrayList<String[]> docID) throws FileNotFoundException {
		
		PrintWriter writer = new PrintWriter(Constants.FILENAME_RESULTS);
		
		String[] id;
		
		for(int i = 0; i < docID.size(); i++) {
			id = docID.get(i);
			for(int j=0; j < id.length; j++) {
				writer.println(queries[i].getQNum() + " " + id[0]);
			}
		}
		
		writer.close();
		
	}
}