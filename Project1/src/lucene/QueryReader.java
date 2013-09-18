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
		String qNum = null;
		String query = null;

		while((line = br.readLine()) != null) {
			if( line.isEmpty() )
				continue;
			if( line.equals("<DOC>"))
				continue;
			if(line.equals("</DOC>")) {
				queries.add(new QueryList(qNum,query));
				continue;
			}
			if(line.length() > 7) {
				if(line.substring(0, 7).equals("<DOCNO>")) {
					qNum = getQNum(line);
					continue;
				}
			}
			if(query == null)
				query = line;
			else
				query = query + " " + line;
		}
		
		br.close();

		return queries.toArray(new QueryList[queries.size()]);
	}

	private static String getQNum(String line) {
		String qNum;
		line = line.replaceAll("</?DOCNO>", "");
		qNum = line.trim();
		return qNum;
	}

}