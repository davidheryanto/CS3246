package lucene;

public class QueryList {
	private String qNum;
	private String query;
	
	public QueryList(String qNum, String query) {
		this.qNum = qNum;
		this.query = query;
	}
	
	public String getQNum() {
		return qNum;
	}
	
	public String getQuery() {
		return query;
	}
}