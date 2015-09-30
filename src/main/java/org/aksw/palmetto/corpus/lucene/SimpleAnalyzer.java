/**
 * This file is part of Palmetto.
 *
 * Palmetto is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Palmetto is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Palmetto.  If not, see <http://www.gnu.org/licenses/>.
 */
							(roeder@informatik.uni-leipzig.de)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
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
