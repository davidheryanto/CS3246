package lucene;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.NumericField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class Indexer {
	private static final Indexer instance = new Indexer();

	// Singleton pattern
	private Indexer() { }
	
	public static Indexer getInstance() {
		return instance;
	}
	
	private static IndexWriter getDefaultIndexWriter() {
		Directory dir = getDirectory(Constants.DIR_PATH_INDEX);
		IndexWriterConfig config = new IndexWriterConfig(
				Constants.VERSION, new MyAnalyzer(Constants.VERSION));
		try {
			return new IndexWriter(dir, config);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static int index(Hashtable<Integer, Paper> paperTable) {
		// Ensure that directory is writable
		IndexWriter indexWriter = getDefaultIndexWriter();
		
		int indexCount = 0;
		try {
			// loop over the Hashtable
			Enumeration<Integer> fileNumberEnum = paperTable.keys();
			while( fileNumberEnum.hasMoreElements() ) {
				Integer fileNumber = fileNumberEnum.nextElement();
				Paper paper = paperTable.get(fileNumber);
				
				// index the fields of Paper
				Document document = new Document();
				
				Field fileNumField = new Field("fileNumber", fileNumber.toString(), Field.Store.YES, Field.Index.NO);
				document.add(fileNumField);
				
				Field titleField = new Field("title", paper.getTitle(), Field.Store.YES, Field.Index.ANALYZED, Field.TermVector.YES);
				titleField.setBoost(Constants.BOOST_TITLE);
				document.add(titleField);
				
				Field summaryField = new Field("summary", paper.getSummary(), Field.Store.NO, Field.Index.ANALYZED, Field.TermVector.YES);
				summaryField.setBoost(Constants.BOOST_SUMMARY);
				document.add(new Field("summary", paper.getSummary(), Field.Store.NO, Field.Index.ANALYZED, Field.TermVector.YES));
				
				NumericField yearField = new NumericField("year").setIntValue( paper.getYear() );
				document.add(yearField);
				
				if (paper.getAuthors() != null) {
					for ( String author : paper.getAuthors() ) {
						Field authorField = new Field("author", author.trim(), Field.Store.YES, Field.Index.ANALYZED , Field.TermVector.YES);
						authorField.setBoost(Constants.BOOST_AUTHOR);
						document.add(authorField);
					}
				}
				
				if (paper.getKeywords() != null) {
					for ( String keyword : paper.getKeywords() ) {
						Field keywordField = new Field("keyword", keyword, Field.Store.NO, Field.Index.ANALYZED, Field.TermVector.YES);
						keywordField.setBoost(Constants.BOOST_KEYWORD);
						document.add(keywordField);
					}
				}
				
				String fullSearchableText = 
							paper.getTitle() + " " + 
							paper.getSummary() + " " + 
							paper.getKeywordString() + " " +
							paper.getAuthorString() + " " ;
				
				document.add( new Field("content", fullSearchableText, Field.Store.NO, Field.Index.ANALYZED) );
				indexWriter.addDocument(document);
				indexCount++;
			}
			
			indexWriter.close();
			indexWriter = null;
			
		} catch (IOException e) {
			e.printStackTrace();
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
