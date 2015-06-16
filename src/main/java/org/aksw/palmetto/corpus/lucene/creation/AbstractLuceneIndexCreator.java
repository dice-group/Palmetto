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
