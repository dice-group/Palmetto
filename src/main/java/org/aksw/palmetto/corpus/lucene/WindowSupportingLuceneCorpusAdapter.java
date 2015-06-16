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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;

import org.aksw.palmetto.corpus.WindowSupportingAdapter;
import org.apache.lucene.index.AtomicReader;
import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.DocsAndPositionsEnum;
import org.apache.lucene.index.DocsEnum;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.SimpleFSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.carrotsearch.hppc.IntArrayList;
import com.carrotsearch.hppc.IntIntOpenHashMap;
import com.carrotsearch.hppc.IntObjectOpenHashMap;

public class WindowSupportingLuceneCorpusAdapter extends LuceneCorpusAdapter implements WindowSupportingAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(WindowSupportingLuceneCorpusAdapter.class);

    public static final String HISTOGRAM_FILE_SUFFIX = ".histogram";

    public static WindowSupportingLuceneCorpusAdapter create(String indexPath, String textFieldName,
            String docLengthFieldName)
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
        int histogram[][] = null;
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            fis = new FileInputStream(indexPath + HISTOGRAM_FILE_SUFFIX);
            ois = new ObjectInputStream(fis);
            histogram = (int[][]) ois.readObject();
        } catch (Exception e) {
            LOGGER.error("Couldn't read histogram file. Returning null.", e);
        } finally {
            try {
                ois.close();
                fis.close();
            } catch (Exception e) {
                // nothing to do
            }
        }
        if (histogram == null) {
            return null;
        }

        return new WindowSupportingLuceneCorpusAdapter(dirReader, reader, contexts, textFieldName, docLengthFieldName,
                histogram);
    }

    protected int histogram[][];
    protected String docLengthFieldName;

    protected WindowSupportingLuceneCorpusAdapter(DirectoryReader dirReader, AtomicReader[] reader,
            AtomicReaderContext contexts[], String textFieldName, String docLengthFieldName, int histogram[][]) {
        super(dirReader, reader, contexts, textFieldName);
        this.histogram = histogram;
        this.docLengthFieldName = docLengthFieldName;
    }

    @Override
    public int[][] getDocumentSizeHistogram() {
        return histogram;
    }

    @Override
    public IntObjectOpenHashMap<IntArrayList[]> requestWordPositionsInDocuments(String[] words,
            IntIntOpenHashMap docLengths) {
        IntObjectOpenHashMap<IntArrayList[]> positionsInDocs = new IntObjectOpenHashMap<IntArrayList[]>();
        for (int i = 0; i < words.length; ++i) {
            requestDocumentsWithWord(words[i], positionsInDocs, docLengths, i, words.length);
        }
        return positionsInDocs;
    }

    protected void requestDocumentsWithWord(String word, IntObjectOpenHashMap<IntArrayList[]> positionsInDocs,
            IntIntOpenHashMap docLengths, int wordId, int numberOfWords) {
        DocsAndPositionsEnum docPosEnum = null;
        Term term = new Term(fieldName, word);
        int localDocId, globalDocId, baseDocId;
        IntArrayList positions[];
        try {
            for (int i = 0; i < reader.length; i++) {
                docPosEnum = reader[i].termPositionsEnum(term);
                baseDocId = contexts[i].docBase;
                if (docPosEnum != null) {
                    while (docPosEnum.nextDoc() != DocsEnum.NO_MORE_DOCS) {
                        localDocId = docPosEnum.docID();
                        globalDocId = localDocId + baseDocId;
                        // if this is the first word and we found a new document
                        if (!positionsInDocs.containsKey(globalDocId)) {
                            positions = new IntArrayList[numberOfWords];
                            positionsInDocs.put(globalDocId, positions);
                        } else {
                            positions = positionsInDocs.get(globalDocId);
                        }
                        if (positions[wordId] == null) {
                            positions[wordId] = new IntArrayList();
                        }
                        // Go through the positions inside this document
                        for (int p = 0; p < docPosEnum.freq(); ++p) {
                            positions[wordId].add(docPosEnum.nextPosition());
                        }
                        if (!docLengths.containsKey(globalDocId)) {
                            // Get the length of the document
                            docLengths.put(globalDocId, reader[i].document(localDocId).getField(docLengthFieldName)
                                    .numericValue().intValue());
                        }
                    }
                }
            }
        } catch (IOException e) {
            LOGGER.error("Error while requesting documents for word \"" + word
                    + "\".", e);
        }
    }

}
