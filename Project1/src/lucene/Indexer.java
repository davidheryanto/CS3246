package lucene;

import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.NumericField;
import org.apache.lucene.index.IndexWriter;

// TODO: Need one more field for content (contains title, author keyword etc) OR use MultiField QueryParser
// TODO: Boost certain field

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
				document.add(new Field("summary", paper.getSummary(), Field.Store.NO, Field.Index.ANALYZED));
				document.add(new NumericField("year").setIntValue(paper.getYear()) );
				if (paper.getAuthors() != null) {
					for (String author : paper.getAuthors()) {
						document.add(new Field("author", author.trim(), Field.Store.YES, Field.Index.ANALYZED));
					}
				}
				if (paper.getKeywords() != null) {
					for (String keyword : paper.getKeywords()) {
						document.add(new Field("keyword", keyword, Field.Store.YES, Field.Index.ANALYZED));
					}
				}
				String fullSearchableText = paper.getTitle() + " " + paper.getSummary();
				if (paper.getAuthors() != null)
					fullSearchableText += " " + paper.getAuthors();
				if (paper.getKeywords() != null)
					fullSearchableText += " " + paper.getKeywords();
		        document.add(new Field("content", fullSearchableText, Field.Store.NO, Field.Index.ANALYZED));
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
