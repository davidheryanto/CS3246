package lucene;

public class QueryList {
	private int qNum;
	private String query;
	
	public QueryList(int qNum, String query) {
		this.qNum = qNum;
		this.query = query;
	}
	
	public int getQNum() {
		return qNum;
	}
	
	public String getQuery() {
		return query;
	}
}