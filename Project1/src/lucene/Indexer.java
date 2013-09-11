package lucene;

import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;

public class Indexer {
	private static IndexWriter indexWriter;
	
	public static void setIndexWriter(IndexWriter indexWriter) {
		Indexer.indexWriter = indexWriter;
	}
	
	public static int index(Paper[] papers) {
		int indexCount = 0;
		try {
			for (Paper paper : papers) {
				Document document = new Document();
				document.add(new Field("id", paper.getId(), Field.Store.YES, Field.Index.NO));
				document.add(new Field("title", paper.getTitle(), Field.Store.YES, Field.Index.ANALYZED));
				// TODO: Add the rest of fields
				document.add(new Field("summary", paper.getSummary(), Field.Store.NO, Field.Index.ANALYZED));
//				document.add(new Field("date", paper.getDate(), Field.Store.NO, Field.Index.NO));
//				document.add(new Field("author", paper.getAuthor(), Field.Store.YES, Field.Index.NOT_ANALYZED));
//				document.add(new Field("keyword", paper.getKeyword(), Field.Store.NO, Field.Index.ANALYZED));
				
				indexWriter.addDocument(document);
				indexCount++;
			}
			indexWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return indexCount;
	}
}
