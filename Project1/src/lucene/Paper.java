package lucene;

public class Paper {
	private String id;
	private String title;
	private String summary;
	private int year;
	private String[] authors;
	private String[] keywords;
	
	public Paper(String id, String title, String summary,
			int year, String[] authors, String[] keywords) {
		this.id = id;
		this.title = title;
		this.summary = summary;
		this.year = year;
		this.authors = authors;
		this.keywords = keywords;
	}

	public String getId() {
		return id;
	}
	
	public String getTitle() {
		return title;
	}

	public String getSummary() {
		return summary;
	}

	public int getYear() {
		return year;
	}

	public String[] getAuthors() {
		return authors;
	}

	public String[] getKeywords() {
		return keywords;
	}
	
	
}
