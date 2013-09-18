package lucene;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.NumericField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.AlreadyClosedException;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

// TODO: Need one more field for content (contains title, author keyword etc) OR use MultiField QueryParser
// TODO: Boost certain field

public class Indexer {
	private static final Indexer instance = new Indexer();
	private static IndexWriter indexWriter;

	// Singleton pattern
	private Indexer() { }
	
	public static Indexer getInstance() {
		return instance;
	}
	
	private static void setDefaultIndexWriter() {
		Directory directory = getDirectory(Constants.DIR_PATH_INDEX);
		IndexWriterConfig config = new IndexWriterConfig(
				Constants.VERSION, new StandardAnalyzer(Constants.VERSION));
		try {
			indexWriter = new IndexWriter(directory, config);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void setIndexWriter(IndexWriter indexWriter) {
		Indexer.indexWriter = indexWriter;
	}

	public static int index(Hashtable<Integer, Paper> paperTable) {
		// Ensure that directory is writable
		setDefaultIndexWriter();
		
		int indexCount = 0;
		try {
			// loop over the Hashtable
			Enumeration<Integer> fileNumberEnum = paperTable.keys();
			while( fileNumberEnum.hasMoreElements() ) {
				Integer fileNumber = fileNumberEnum.nextElement();
				Paper paper = paperTable.get(fileNumber);
				
				// index the fields of Paper
				Document document = new Document();
				document.add(new Field("fileNumber", fileNumber.toString(), Field.Store.YES, Field.Index.NO));
				document.add(new Field("title", paper.getTitle(), Field.Store.YES, Field.Index.ANALYZED, Field.TermVector.YES));
				document.add(new Field("summary", paper.getSummary(), Field.Store.NO, Field.Index.ANALYZED, Field.TermVector.YES));
				document.add(new NumericField("year").setIntValue(paper.getYear()) );
				if (paper.getAuthors() != null) {
					for (String author : paper.getAuthors()) {
						document.add(new Field("author", author.trim(), Field.Store.YES, Field.Index.ANALYZED, Field.TermVector.YES));
					}
				}
				if (paper.getKeywords() != null) {
					for (String keyword : paper.getKeywords()) {
						document.add(new Field("keyword", keyword, Field.Store.YES, Field.Index.ANALYZED, Field.TermVector.YES));
					}
				}
				String fullSearchableText = 
							paper.getTitle() + " " + 
							paper.getSummary() + " " + 
							paper.getKeywordString() + " " +
							paper.getAuthorString() + " " ;
				
				document.add(new Field("content", fullSearchableText, Field.Store.NO, Field.Index.ANALYZED, Field.TermVector.YES));
				indexWriter.addDocument(document);
				indexCount++;
			}
			
			indexWriter.close();
			
		} catch (IOException e) {
			e.printStackTrace();
			
		} catch (AlreadyClosedException e) {
			setDefaultIndexWriter();
			index(paperTable);
		}
		
		
		return indexCount;
	}
	
	public static Directory getDirectory(String path) {
		File indexFolder = new File(path);
		if (indexFolder.exists() && indexFolder.isDirectory()) {
			for (File file : indexFolder.listFiles()) {
				file.delete();
			}
		}

		Directory directory = null;
		try {
			directory = FSDirectory.open(indexFolder);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return directory;
	}
}
