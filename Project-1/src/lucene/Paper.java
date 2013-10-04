package lucene;


public class Paper {
	private int docNumber;
	private String title;
	private String summary;
	private int year;
	private String[] authors;
	private String[] keywords;

	public Paper(String title, String summary,
			int year, String[] authors, String[] keywords) {
		this.docNumber = -1;
		this.title = title;
		this.summary = summary;
		this.year = year;
		this.authors = authors;
		this.keywords = keywords;
	}

	public int getDocNumber() {
		return this.docNumber;
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

	// return enumerated keywords as a long string e.g. keyword1 keyword2 keyword3 ...
	public String getKeywordString() {
		StringBuilder stringBuilder = new StringBuilder("");
		if (keywords != null) {
			for (String keyword : keywords) {
				stringBuilder.append(keyword + " ");
			}
		}

		return stringBuilder.toString().trim();
	}

	// return enumerated authors as a long string e.g. author1 author2 author3 ...
	public String getAuthorString() {
		StringBuilder stringBuilder = new StringBuilder("");
		if (authors != null) {
			for (String author : authors) {
				stringBuilder.append(author + " ");
			}
		}

		return stringBuilder.toString().trim();
	}

	public void setDocNumber(int docNumber) {
		this.docNumber = docNumber;
	}
}
