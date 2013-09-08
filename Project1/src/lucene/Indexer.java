package lucene;

import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;

public class Indexer {
	private static IndexWriter indexWriter;
	
	public Indexer(IndexWriter indexWriter) {
		Indexer.indexWriter = indexWriter;
	}
	
	public void index(Paper[] papers) {
		try {
			for (Paper paper : papers) {
				Document document = new Document();
				document.add(new Field("id", paper.getId(), Field.Store.YES, Field.Index.NO));
				document.add(new Field("title", paper.getTitle(), Field.Store.YES, Field.Index.ANALYZED));
				// TODO: Add the rest of fields
				
				indexWriter.addDocument(document);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
