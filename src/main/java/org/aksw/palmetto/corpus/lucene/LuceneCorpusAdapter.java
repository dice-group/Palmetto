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
package org.aksw.palmetto.corpus.lucene;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.aksw.palmetto.corpus.BooleanDocumentSupportingAdapter;
import org.apache.lucene.index.AtomicReader;
import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.DocsEnum;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.SimpleFSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.carrotsearch.hppc.IntArrayList;
import com.carrotsearch.hppc.IntOpenHashSet;
import com.carrotsearch.hppc.ObjectObjectOpenHashMap;

/**
 * This class can make usage of a given Lucene index as corpus.
 * 
 * @author m.roeder
 * 
 */
public class LuceneCorpusAdapter implements BooleanDocumentSupportingAdapter {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(LuceneCorpusAdapter.class);

    protected String fieldName;
    protected DirectoryReader dirReader;
    protected AtomicReader reader[];
    protected AtomicReaderContext contexts[];

    /**
     * Creates a corpus adapter which uses the Lucene index with the given path and searches on the field with the given
     * field name.
     * 
     * @param indexPath
     * @param fieldName
     * @return
     * @throws CorruptIndexException
     * @throws IOException
     */
    public static LuceneCorpusAdapter create(String indexPath, String fieldName)
            throws CorruptIndexException, IOException {
        DirectoryReader dirReader = DirectoryReader.open(new SimpleFSDirectory(
                new File(indexPath)));
        List<AtomicReaderContext> leaves = dirReader.leaves();
        AtomicReader reader[] = new AtomicReader[leaves.size()];
        AtomicReaderContext contexts[] = new AtomicReaderContext[leaves.size()];
        for (int i = 0; i < reader.length; i++) {
            contexts[i] = leaves.get(i);
            reader[i] = contexts[i].reader();
        }
        return new LuceneCorpusAdapter(dirReader, reader, contexts, fieldName);
    }

    protected LuceneCorpusAdapter(DirectoryReader dirReader, AtomicReader reader[], AtomicReaderContext contexts[],
            String fieldName) {
        this.dirReader = dirReader;
        this.reader = reader;
        this.contexts = contexts;
        this.fieldName = fieldName;
    }

    private void requestDocumentsWithWordAsSet(String word, IntOpenHashSet documents) {
        DocsEnum docs = null;
        Term term = new Term(fieldName, word);
        try {
            int baseDocId;
            for (int i = 0; i < reader.length; i++) {
                docs = reader[i].termDocsEnum(term);
                baseDocId = contexts[i].docBase;
                if (docs != null) {
                    while (docs.nextDoc() != DocsEnum.NO_MORE_DOCS) {
                        documents.add(baseDocId + docs.docID());
                    }
                }
            }
        } catch (IOException e) {
            LOGGER.error("Error while requesting documents for word \"" + word
                    + "\".", e);
        }
    }

    /**
     * Closes the Lucene index.
     */
    public void close() {
        for (int i = 0; i < reader.length; i++) {
            try {
                reader[i].close();
            } catch (IOException e) {
                // nothing to do
            }
        }
    }

    public int getNumberOfDocuments() {
        try {
            int numDocs = dirReader.getDocCount(fieldName);
            if (numDocs < 0) {
                LOGGER.warn("Coudln't get the number of documents with the indexed field \"" + fieldName
                        + "\". Using the number of all documents in the index instead.");
            }
        } catch (IOException e) {
            LOGGER.warn("Coudln't get the number of documents with the indexed field \"" + fieldName
                    + "\". Using the number of all documents in the index instead.", e);
        }
        return dirReader.numDocs();
    }

    public void getDocumentsWithWordsAsSet(
            ObjectObjectOpenHashMap<String, IntOpenHashSet> wordDocMapping) {
        Object keys[] = (Object[]) wordDocMapping.keys;
        Object values[] = (Object[]) wordDocMapping.values;
        for (int i = 0; i < wordDocMapping.allocated.length; ++i) {
            if (wordDocMapping.allocated[i]) {
                requestDocumentsWithWordAsSet((String) keys[i],
                        (IntOpenHashSet) values[i]);
            }
        }
    }

    @Override
    public void getDocumentsWithWords(ObjectObjectOpenHashMap<String, IntArrayList> wordDocMapping) {
        Object keys[] = (Object[]) wordDocMapping.keys;
        Object values[] = (Object[]) wordDocMapping.values;
        for (int i = 0; i < wordDocMapping.allocated.length; ++i) {
            if (wordDocMapping.allocated[i]) {
                requestDocumentsWithWord((String) keys[i],
                        (IntArrayList) values[i]);
            }
        }
    }

    private void requestDocumentsWithWord(String word, IntArrayList documents) {
        DocsEnum docs = null;
        Term term = new Term(fieldName, word);
        try {
            int baseDocId;
            for (int i = 0; i < reader.length; i++) {
                docs = reader[i].termDocsEnum(term);
                baseDocId = contexts[i].docBase;
                if (docs != null) {
                    while (docs.nextDoc() != DocsEnum.NO_MORE_DOCS) {
                        documents.add(docs.docID() + baseDocId);
                    }
                }
            }
        } catch (IOException e) {
            LOGGER.error("Error while requesting documents for word \"" + word
                    + "\".", e);
        }
    }
}
