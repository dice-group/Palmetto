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

import com.carrotsearch.hppc.IntOpenHashSet;
import com.carrotsearch.hppc.ObjectObjectOpenHashMap;

public class LuceneCorpusAdapter implements BooleanDocumentSupportingAdapter {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(LuceneCorpusAdapter.class);

    private String fieldName;
    private DirectoryReader dirReader;
    private AtomicReader reader[];

    public static LuceneCorpusAdapter create(String indexPath, String fieldName)
            throws CorruptIndexException, IOException {
        DirectoryReader dirReader = DirectoryReader.open(new SimpleFSDirectory(
                new File(indexPath)));
        List<AtomicReaderContext> leaves = dirReader.leaves();
        AtomicReader reader[] = new AtomicReader[leaves.size()];
        for (int i = 0; i < reader.length; i++) {
            reader[i] = leaves.get(i).reader();
        }
        return new LuceneCorpusAdapter(dirReader, reader, fieldName);
    }

    protected LuceneCorpusAdapter(DirectoryReader dirReader, AtomicReader reader[], String fieldName) {
        this.dirReader = dirReader;
        this.reader = reader;
        this.fieldName = fieldName;
    }

    private void requestDocumentsWithWord(String word, IntOpenHashSet documents) {
        DocsEnum docs = null;
        Term term = new Term(fieldName, word);
        try {
            for (int i = 0; i < reader.length; i++) {
                docs = reader[i].termDocsEnum(term);
                if (docs != null) {
                    while (docs.nextDoc() != DocsEnum.NO_MORE_DOCS) {
                        documents.add(docs.docID());
                    }
                }
            }
        } catch (IOException e) {
            LOGGER.error("Error while requesting documents for word \"" + word
                    + "\".", e);
        }
    }

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

    public void getDocumentsWithWords(
            ObjectObjectOpenHashMap<String, IntOpenHashSet> wordDocMapping) {
        Object keys[] = (Object[]) wordDocMapping.keys;
        Object values[] = (Object[]) wordDocMapping.values;
        for (int i = 0; i < wordDocMapping.allocated.length; ++i) {
            if (wordDocMapping.allocated[i]) {
                requestDocumentsWithWord((String) keys[i],
                        (IntOpenHashSet) values[i]);
            }
        }
    }
}
