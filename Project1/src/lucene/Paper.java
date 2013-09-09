package lucene;

public class Paper {
	private String id;
	private String title;
	private String summary;
	private String content;
	
	public Paper(String id, String title, String summary, String content) {
		this.id = id;
		this.title = title;
		this.summary = summary;
		this.content = content;
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

	public String getContent() {
		return content;
	}
	
	
}
