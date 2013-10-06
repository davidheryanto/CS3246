package rf;

public class TermVector implements Comparable<TermVector> {
	// No of documents
	private int N = 900;
	
	private String term;
	private double weight;
	private int docFreq;
	private int termFreq;

	public String getTerm() {
		return term;
	}
	public void setTerm(String term) {
		this.term = term;
	}
	
	public double getWeight() {
		return weight;
	}
	
	private void setWeight(int termFreq, int docFreq) {
		if (termFreq <= 0 || docFreq <= 0) {
			this.weight = 0;
		} else {
			this.weight = termFreq * Math.log1p(N / docFreq);
		}
	}
	
	public int getDocFreq() {
		return docFreq;
	}
	
	private void setDocFreq(int docFreq) {
		this.docFreq = docFreq;
	}
	
	public int getTermFreq() {
		return termFreq;
	}
	
	public void setTermFreq(int termFreq) {
		this.termFreq = termFreq;
		this.setWeight(this.termFreq, this.docFreq);
	}
	
	public TermVector(String term, int termFreq, int docFreq) {
		this.setTerm(term);
		this.setTermFreq(termFreq);
		this.setDocFreq(docFreq);
		this.setWeight(termFreq, docFreq);
	}

	@Override
	public int compareTo(TermVector that) {
		return (int) Math.round( that.getWeight() - this.getWeight() );
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof TermVector) {
			TermVector that = (TermVector) obj;
			return this.getTerm().equals( that.getTerm() );
		}
		
		return false;
	}
}
