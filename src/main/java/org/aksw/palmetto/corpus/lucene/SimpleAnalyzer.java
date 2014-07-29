/**
 * Copyright (C) 2014 Michael Röder (michael.roeder@unister.de)
 *
 * Licensed under the Creative Commons Attribution-NonCommercial 4.0
 * International Public License (the "License").
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://creativecommons.org/licenses/by-nc/4.0/legalcode
 *
 * Unless required by applicable law or agreed to in writing, a file
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
