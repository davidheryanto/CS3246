package lucene;

import java.io.Reader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.PorterStemFilter;
import org.apache.lucene.analysis.StopAnalyzer;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.util.Version;

public class MyAnalyzer extends Analyzer {
	private Version version;
	
	public MyAnalyzer(Version version) {
		this.version = version;
	}
	
	@Override
	public TokenStream tokenStream(String fieldName, Reader reader) {
        TokenStream ts = new StandardTokenizer(version, reader);
        ts = new StopFilter(version, ts, StopAnalyzer.ENGLISH_STOP_WORDS_SET);
        ts = new LowerCaseFilter(version, ts);
        ts = new StandardFilter(version, ts);
        ts = new PorterStemFilter(ts);
        
        return ts;
	}
}
