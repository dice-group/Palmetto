package org.aksw.palmetto.corpus.lucene;

import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.LowerCaseFilterFactory;
import org.apache.lucene.analysis.pattern.PatternTokenizerFactory;
import org.apache.lucene.analysis.util.AbstractAnalysisFactory;
import org.apache.lucene.util.Version;

public class SimpleAnalyzer extends Analyzer {

    private static final Version version = Version.LUCENE_44;
    private static final String PATTERN = "([^\\p{Punct}\\p{Space}]+([\\p{Punct}][^\\p{Punct}\\p{Space}]+)*)";

    private PatternTokenizerFactory tokenizerFactory;
    private LowerCaseFilterFactory lowerCaseFilterFactory;

    public SimpleAnalyzer(boolean lowerCase) {
	Map<String, String> parameters = new HashMap<String, String>();
	parameters.put(PatternTokenizerFactory.PATTERN, PATTERN);
	parameters.put(PatternTokenizerFactory.GROUP, "0");
	parameters.put(AbstractAnalysisFactory.LUCENE_MATCH_VERSION_PARAM,
		version.name());
	tokenizerFactory = new PatternTokenizerFactory(parameters);
	if (lowerCase) {
	    parameters = new HashMap<String, String>();
	    parameters.put(AbstractAnalysisFactory.LUCENE_MATCH_VERSION_PARAM,
		    version.name());
	    lowerCaseFilterFactory = new LowerCaseFilterFactory(parameters);
	} else {
	    lowerCaseFilterFactory = null;
	}
    }

    @Override
    protected TokenStreamComponents createComponents(String fieldName,
	    Reader reader) {
	Tokenizer tokenizer = tokenizerFactory.create(reader);
	if (lowerCaseFilterFactory != null) {
	    return new TokenStreamComponents(tokenizer,
		    lowerCaseFilterFactory.create(tokenizer));
	} else {
	    return new TokenStreamComponents(tokenizer);
	}
    }

}
