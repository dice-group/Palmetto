package org.aksw.palmetto.corpus.lucene.creation;

/**
 * A simple structure containing the text of a document and its number of tokens.
 * 
 * @author Michael R&ouml;der (roeder@informatik.uni-leipzig.de)
 * 
 */
public class IndexableDocument {

    private String text;
    private int numberOfTokens;

    public IndexableDocument(String text, int numberOfTokens) {
        this.text = text;
        this.numberOfTokens = numberOfTokens;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getNumberOfTokens() {
        return numberOfTokens;
    }

    public void setNumberOfTokens(int numberOfTokens) {
        this.numberOfTokens = numberOfTokens;
    }
}
