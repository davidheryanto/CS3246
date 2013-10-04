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
import java.util.List;
import java.util.Scanner;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class Main {
	private static Version VERSION = Version.LUCENE_36;

	public static void main(String[] args) throws Exception {
		index();
		
		Scanner sc = new Scanner(System.in);
		while(true) {
			System.out.println("-------");
			System.out.println("QUERY: ");
			String query = sc.nextLine();
			if (query.equals("")) {
				break;
			}
			
			if (query.indexOf("r:") == 0) {
				// do relevance feedback
				String[] fbs = query.split(" ");
				
				// search with rel fb
			}
			
			search(query);
		}
		sc.close();
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
	
	public static void search(String query) throws Exception {
		Analyzer analyzer = new StandardAnalyzer(VERSION);
		QueryParser parser = new QueryParser(VERSION, "content", analyzer);
		
		String cwd = System.getProperty("user.dir"); // C:\Users\David\Git\CS3246\Project-1
		Directory dir = FSDirectory.open(new File(cwd + "/index"));
		IndexReader reader = IndexReader.open(dir);
		IndexSearcher searcher = new IndexSearcher(reader);
		TopDocs topDocs = searcher.search(parser.parse(query), 15);
		ScoreDoc[] scoreDocs = topDocs.scoreDocs;
		
		List<String> resultList = new ArrayList<String>();
		for (ScoreDoc sd : scoreDocs) {
			Document doc = searcher.doc(sd.doc);
			String result = doc.get("id") + "\t" + String.valueOf( sd.score );
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
}
