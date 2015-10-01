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
package org.aksw.palmetto.corpus.lucene.creation;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import org.aksw.palmetto.corpus.lucene.LuceneCorpusAdapter;
import org.aksw.palmetto.corpus.lucene.SimpleAnalyzer;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.FieldInfo.IndexOptions;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.FSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class creates the Lucene indexes which are used to access the reference corpus during the coherence calculation
 * using a {@link LuceneCorpusAdapter}.
 * 
 * @author Michael R&ouml;der (roeder@informatik.uni-leipzig.de)
 * 
 */
public class SimpleLuceneIndexCreator extends AbstractLuceneIndexCreator {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(SimpleLuceneIndexCreator.class);

    /**
     * Constructor.
     * 
     * @param textFieldName
     *            The name of the field in which the document texts are stored.
     */
    public SimpleLuceneIndexCreator(String fieldName) {
        super(fieldName);
    }

    /**
     * Constructor.
     * 
     * @param textFieldName
     *            The name of the field in which the document texts are stored.
     * @param commitInterval
     *            The interval in which changes are committed to the index.
     */
    public SimpleLuceneIndexCreator(String textFieldName, int commitInterval) {
        super(textFieldName, commitInterval);
    }

    /**
     * Creates the index.
     * 
     * @param indexPath
     *            The path to the director in which the Lucene index will be created
     * @param docIterator
     *            Iterator that iterates over the document texts.
     * @return true if the creation was successful, else false.
     */
    public boolean createIndex(File indexPath, Iterator<String> docIterator) {
        LOGGER.info("Starting index creation...");
        IndexWriter writer = null;
        indexPath.mkdirs();
        Analyzer analyzer = new SimpleAnalyzer(true);
        try {
            IndexWriterConfig config = new IndexWriterConfig(version, analyzer);
            config.setOpenMode(OpenMode.CREATE);

            FieldType fieldType = new FieldType(TextField.TYPE_NOT_STORED);
            fieldType.setIndexed(true);
            fieldType.setIndexOptions(IndexOptions.DOCS_AND_FREQS);
            fieldType.freeze();

            writer = new IndexWriter(FSDirectory.open(indexPath), config);
            String text;
            int count = 0;
            while (docIterator.hasNext()) {
                text = docIterator.next();
                if (text.length() > 0) {
                    writer.addDocument(toLuceneDocument(analyzer, text, fieldType));
                    ++count;
                    if (count >= commitInterval) {
                        writer.commit();
                        System.gc();
                        count = 0;
                    }
                } else {
                    LOGGER.warn("Got a document without content.");
                }
            }
            LOGGER.info("Finished index creation.");
        } catch (IOException e) {
            LOGGER.error("Error while creating Index. Aborting.", e);
            return false;
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (Exception e) {
                }
            }
        }
        return true;
    }
}
