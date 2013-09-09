package lucene;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

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
		String[] fileNames = folder.list();
		
		ArrayList<Paper> papers = new ArrayList<Paper>();
		try {
			for (String fileName : fileNames) {
				String filePath = path + "/" + fileName;
				byte[] byteRead = Files.readAllBytes(Paths.get(filePath));
				String stringRead = encoding.decode(ByteBuffer.wrap(byteRead)).toString();
				
				// TODO: exclude the wrapper tags
				// TODO: create proper Paper objects
				
				System.out.println(stringRead);
				
				Paper paper = new Paper(null, null, null, stringRead);
				papers.add(paper);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return papers.toArray(new Paper[papers.size()]);
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
