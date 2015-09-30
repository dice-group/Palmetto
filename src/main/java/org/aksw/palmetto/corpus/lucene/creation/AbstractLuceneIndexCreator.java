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

import java.io.IOException;
import java.io.StringReader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.IntField;
import org.apache.lucene.util.Version;

/**
 * This is an abstract class with general index creation functionality.
 * 
 * @author Michael R&ouml;der (roeder@informatik.uni-leipzig.de)
 * 
 */
public abstract class AbstractLuceneIndexCreator {

    protected static final Version version = Version.LUCENE_44;
    protected static final int DEFAULT_COMMIT_INTERVAL = 1000;

    /**
     * The name of the field in which the document texts are stored.
     */
    protected String textFieldName;

    /**
     * The interval in which changes are committed to the index.
     */
    protected int commitInterval;

    /**
     * Constructor.
     * 
     * @param textFieldName
     *            The name of the field in which the document texts are stored.
     */
    public AbstractLuceneIndexCreator(String textFieldName) {
        this(textFieldName, DEFAULT_COMMIT_INTERVAL);
    }

    /**
     * Constructor.
     * 
     * @param textFieldName
     *            The name of the field in which the document texts are stored.
     * @param commitInterval
     *            The interval in which changes are committed to the index.
     */
    public AbstractLuceneIndexCreator(String textFieldName, int commitInterval) {
        this.textFieldName = textFieldName;
        this.commitInterval = commitInterval;
    }

    protected Document toLuceneDocument(Analyzer analyzer, String text, FieldType fieldType)
            throws IOException {
        Document document = new Document();
        document.add(new Field(textFieldName, analyzer.tokenStream(textFieldName,
                new StringReader(text)), fieldType));
        return document;
    }

    protected void addDocumentLength(Document document, String docLengthFieldName, FieldType docLengthFieldType,
            int documentLength) {
        document.add(new IntField(docLengthFieldName, documentLength, docLengthFieldType));
    }

    public int getCommitInterval() {
        return commitInterval;
    }

    public void setCommitInterval(int commitInterval) {
        this.commitInterval = commitInterval;
    }

    public String getTextFieldName() {
        return textFieldName;
    }
}
