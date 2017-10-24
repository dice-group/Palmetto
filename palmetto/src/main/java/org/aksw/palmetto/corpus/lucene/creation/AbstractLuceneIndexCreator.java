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
