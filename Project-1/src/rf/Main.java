package rf;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;
import java.util.Scanner;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermFreqVector;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class Main {
	private static Version VERSION = Version.LUCENE_36;
	private static Hashtable<Integer, Integer> docIdNumMap;

	public static void main(String[] args) throws Exception {
		index();
		
		Scanner sc = new Scanner(System.in);
		String query = "";
		while(true) {
			System.out.println("-------");
			System.out.println("QUERY: ");
			String newQuery = sc.nextLine();
			if (newQuery.equals("")) {
				break;
			}

			// Can refactor to one overloaded search()
			if (newQuery.indexOf("r:") == 0) {
				// do relevance feedback
				String[] feedbacks = newQuery.split(" ");
				search(query, feedbacks);
			} else {
				search(newQuery);
			}
			query = newQuery;
		}
		sc.close();
	}
	
	public static void search(String query, String[] feedbacks) throws Exception {
		ArrayList<TermVector> sumRelDoc = new ArrayList<TermVector>();
		
		for (int i = 1; i < feedbacks.length; i++) {
			int docNum = docIdNumMap.get( Integer.parseInt( feedbacks[i] ) );
			
			String cwd = System.getProperty("user.dir");
			Directory dir = FSDirectory.open(new File(cwd + "/index"));
			IndexReader reader = IndexReader.open(dir);
			IndexSearcher searcher = new IndexSearcher(reader);
			
			TermFreqVector tfv = reader.getTermFreqVector(docNum, "content");
			
			Term[] terms = new Term[tfv.getTerms().length];
			for (int j = 0; j < tfv.getTerms().length; j++) {
				terms[j] = new Term("content", tfv.getTerms()[j]);
			}
			
			@SuppressWarnings("deprecation")
			int[] docFreqs = searcher.docFreqs(terms);
			
			for (int j = 0; j < tfv.getTerms().length; j++) {
				if ( tfv.getTerms()[j].length() <= 2 || isNumeric( tfv.getTerms()[j] ) ) {
					continue;
				}
				
				TermVector termVector = new TermVector( tfv.getTerms()[j], tfv.getTermFrequencies()[j], docFreqs[j] );
				int index = sumRelDoc.indexOf(termVector);
				if (index > 0) {
					int newFreq = sumRelDoc.get(index).getTermFreq() + termVector.getTermFreq();
					sumRelDoc.get(index).setTermFreq(newFreq);
				} else {
					// new term
					sumRelDoc.add(termVector);
				}
			}
			searcher.close();
		}
		
		System.out.println();
		Collections.sort(sumRelDoc);
		int col = 0, count = 0;
		for (TermVector termVector : sumRelDoc) {
			if (count >= 30) break;
			
			if (col == 0) System.out.println();
			System.out.printf( "(%s, %.3f)\t", termVector.getTerm(), termVector.getWeight() ); 
			col = (col + 1) % 3;
			count += 1;
		}
		System.out.println("\n---------------------------");
	}

	public static void index() throws IOException {
		String cwd = System.getProperty("user.dir"); // C:\Users\David\Git\CS3246\Project-1
		
		// delete folder
		File folder = new File(cwd + "/index");
		if ( folder.exists() ) {
			File[] files = folder.listFiles();
			for (File f : files) {
				f.delete();
			}
		}
		
		
		Directory dir = FSDirectory.open(new File(cwd + "/index"));
		Analyzer analyzer = new StandardAnalyzer(VERSION);
		IndexWriterConfig config = new IndexWriterConfig(VERSION, analyzer);

		IndexWriter writer = new IndexWriter(dir, config);
		String[] filePaths = getFilePaths(cwd);
		for (String filePath : filePaths) {
			String content = readFile(filePath);
			String id = filePath
							.substring(
									filePath.lastIndexOf('\\') + 1,
									filePath.lastIndexOf('.')
									);
			
			Document doc = new Document();
			doc.add(new Field(
						"content",
						content,
						Field.Store.NO,
						Field.Index.ANALYZED,
						Field.TermVector.YES
					));
			doc.add(new Field(
					"id",
					id,
					Field.Store.YES,
					Field.Index.NO,
					Field.TermVector.NO
				));
			writer.addDocument(doc);
		}
		writer.close();
	}

	public static String[] getFilePaths(String cwd) {
		File folder = new File(cwd + "/data");
		File[] files = folder.listFiles();
		
		String[] filePaths = new String[files.length];
		for (int i = 0; i < files.length; i++) {
			filePaths[i] = files[i].getAbsolutePath();
		}

		return filePaths;
	}

	public static String readFile(String path) throws IOException 
	{
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return Charset
				.defaultCharset()
				.decode(ByteBuffer.wrap(encoded)).toString();
	}
	
	public static void search(String queryString) throws Exception {
		Analyzer analyzer = new StandardAnalyzer(VERSION);
		QueryParser parser = new QueryParser(VERSION, "content", analyzer);
		
		String cwd = System.getProperty("user.dir"); // C:\Users\David\Git\CS3246\Project-1
		Directory dir = FSDirectory.open(new File(cwd + "/index"));
		IndexReader reader = IndexReader.open(dir);
		IndexSearcher searcher = new IndexSearcher(reader);
		Query query = parser.parse(queryString);
		TopDocs topDocs = searcher.search(query, 15);
		ScoreDoc[] scoreDocs = topDocs.scoreDocs;
		
		docIdNumMap = new Hashtable<>();
		List<String> resultList = new ArrayList<String>();
		for (ScoreDoc sd : scoreDocs) {
			Document doc = searcher.doc(sd.doc);
			String result = doc.get("id") + "\t" + String.valueOf( sd.score );
			
			docIdNumMap.put( Integer.parseInt(doc.get("id") ), sd.doc);
			// Add explanation to result
			// Explanation exp = searcher.explain(query, sd.doc);
			// result += "\n" + exp.toString();
			
			resultList.add(result);
		}
		
		Collections.sort(resultList, 
				new Comparator<String>() {
					public int compare(String a, String b) {
						int valA = Integer.parseInt( a.substring(0,4) );
						int valB = Integer.parseInt( b.substring(0,4) );
						return valA - valB;
					}
				}
		);

		for (int i = 0; i < resultList.size(); i++) {
			System.out.printf("%d\t%s%n", i + 1, resultList.get(i));
		}
		
		searcher.close();
	}
	
	public static boolean isNumeric(String str)  
	{  
	  try  
	  {  
	    Double.parseDouble(str);  
	  }  
	  catch(NumberFormatException nfe)  
	  {  
	    return false;  
	  }  
	  return true;  
	}
}
