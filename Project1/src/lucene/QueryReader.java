package lucene;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class QueryReader {
	public static QueryList[] readQuery(String path) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(path));
		ArrayList<QueryList> queries = new ArrayList<QueryList>();
		String line;
		int qNum = 0;
		String query = null;
		while((line = br.readLine()) != null) {
			if( line.isEmpty() )
				continue;
			if(line.equals("</DOC>")) {
				queries.add(new QueryList(qNum,query));
				continue;
			}
			if(line.substring(0, 6).equals("<DOCNO>")) {
				qNum = getQNum(line);
				continue;
			}
			if(query == null)
				query = line;
			else
				query = query + " " + line;
		}
		
		return queries.toArray(new QueryList[queries.size()]);
	}
	
	private static int getQNum(String line) {
		int qNum = 0;
		line = line.replaceAll("</?DOCNO>", "");
		qNum = Integer.parseInt(line.trim().substring(1));
		return qNum;
	}

}