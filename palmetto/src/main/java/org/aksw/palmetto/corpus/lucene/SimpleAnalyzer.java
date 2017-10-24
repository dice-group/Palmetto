/**
 * Palmetto - Palmetto is a quality measure tool for topics.
 * Copyright © 2014 Data Science Group (DICE) (michael.roeder@uni-paderborn.de)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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

/**
 * A simple Lucene Analyzer used for the index creation.
 * 
 * @author m.roeder
 * 
 */
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
