package lucene;

import org.apache.lucene.util.Version;

public class Constants {
	public static final Version VERSION = Version.LUCENE_36;
	public static final String DIR_PATH_DATA = "./data";
	public static final String DIR_PATH_INDEX = "./index";
	
	public static final String LABEL_FRAME = "Lucene Search Engine";
	public static final String LABEL_TEXTFIELD = "Type Query";
	public static final String LABEL_BUTTON_SEARCH = "Search";
	public static final String LABEL_BUTTON_SEARCH_REFINE = "Refine Query";
	public static final String LABEL_BUTTON_QUERY_IMPORT = "Import Query";
	public static final String LABEL_BUTTON_QUERY_EXPORT = "Export Query";
	
	
	public static final String SEARCH_TYPE_NORMAL = "Search";
	public static final String SEARCH_TYPE_REFINE = "Refine Query";
	
	
	public static final String SIMILARITY_DEFAULT = "Default";
	public static final String SIMILARITY_COSINE = "Cosine";
	public static final String SIMILARITY_JACCARD = "Jaccard";
	public static final String SIMILARITY_TERM_CORRELATION = "Term Correlation";
	
	public static final int QUERY_EXPANSION_N_TERMS = 5;
	
	public static final float BOOST_TITLE = 1.5F;
	public static final float BOOST_SUMMARY = 0.8F;
	public static final float BOOST_KEYWORD = 1.2F;
	public static final float BOOST_AUTHOR = 2.0F;
	
}
