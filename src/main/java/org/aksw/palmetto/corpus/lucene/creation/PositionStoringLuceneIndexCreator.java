/**
 * The MIT License
 * Copyright (c) 2014 Agile Knowledge Engineering and Semantic Web (AKSW)
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

import org.aksw.palmetto.corpus.lucene.SimpleAnalyzer;
import org.aksw.palmetto.corpus.lucene.WindowSupportingLuceneCorpusAdapter;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.IntField;
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
 * using a {@link WindowSupportingLuceneCorpusAdapter}.
 * 
 * @author Michael R&ouml;der (roeder@informatik.uni-leipzig.de)
 * 
 */
public class PositionStoringLuceneIndexCreator extends AbstractLuceneIndexCreator {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(PositionStoringLuceneIndexCreator.class);

    protected String docLengthFieldName;

    public PositionStoringLuceneIndexCreator(String textFieldName, String docLengthFieldName) {
        super(textFieldName);
        this.docLengthFieldName = docLengthFieldName;
    }

    public PositionStoringLuceneIndexCreator(String textFieldName, String docLengthFieldName, int commitInterval) {
        super(textFieldName, commitInterval);
        this.docLengthFieldName = docLengthFieldName;
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
    public boolean createIndex(File indexPath, Iterator<IndexableDocument> docIterator) {
        LOGGER.info("Starting index creation...");
        IndexWriter writer = null;
        indexPath.mkdirs();
        Analyzer analyzer = new SimpleAnalyzer(true);
        try {
            IndexWriterConfig config = new IndexWriterConfig(version, analyzer);
            config.setOpenMode(OpenMode.CREATE);

            FieldType fieldType = new FieldType(TextField.TYPE_NOT_STORED);
            fieldType.setIndexed(true);
            fieldType.setStoreTermVectors(true);
            fieldType.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS);
            fieldType.freeze();

            FieldType docLengthFieldType = new FieldType(IntField.TYPE_STORED);
            docLengthFieldType.setIndexed(false);
            docLengthFieldType.freeze();

            writer = new IndexWriter(FSDirectory.open(indexPath), config);
            int count = 0;
            Document indexDocument;
            IndexableDocument currentDocument;
            while (docIterator.hasNext()) {
                currentDocument = docIterator.next();
                if (currentDocument.getText().length() > 0) {
                    indexDocument = toLuceneDocument(analyzer, currentDocument.getText(), fieldType);
                    addDocumentLength(indexDocument, docLengthFieldName, docLengthFieldType,
                            currentDocument.getNumberOfTokens());
                    writer.addDocument(indexDocument);
                    ++count;
                    if (count >= commitInterval) {
                        writer.commit();
                        System.gc();
                        count = 0;
                    }
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
