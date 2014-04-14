package org.aksw.palmetto.corpus.lucene;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;

import org.aksw.palmetto.corpus.SlidingWindowSupportingAdapter;
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

public class LuceneCorpusAdapterForSlidingWindows extends LuceneCorpusAdapter implements SlidingWindowSupportingAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(LuceneCorpusAdapterForSlidingWindows.class);

    public static final String HISTOGRAM_FILE_SUFFIX = ".histogram";

    public static LuceneCorpusAdapterForSlidingWindows create(String indexPath, String textFieldName,
            String docLengthFieldName)
            throws CorruptIndexException, IOException {
        DirectoryReader dirReader = DirectoryReader.open(new SimpleFSDirectory(
                new File(indexPath)));
        List<AtomicReaderContext> leaves = dirReader.leaves();
        AtomicReader reader[] = new AtomicReader[leaves.size()];
        for (int i = 0; i < reader.length; i++) {
            reader[i] = leaves.get(i).reader();
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

        return new LuceneCorpusAdapterForSlidingWindows(dirReader, reader, textFieldName, docLengthFieldName, histogram);
    }

    protected int histogram[][];
    protected String docLengthFieldName;

    protected LuceneCorpusAdapterForSlidingWindows(DirectoryReader dirReader, AtomicReader[] reader,
            String textFieldName, String docLengthFieldName, int histogram[][]) {
        super(dirReader, reader, textFieldName);
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
        int docId;
        IntArrayList positions[];
        try {
            for (int i = 0; i < reader.length; i++) {
                docPosEnum = reader[i].termPositionsEnum(term);
                if (docPosEnum != null) {
                    while (docPosEnum.nextDoc() != DocsEnum.NO_MORE_DOCS) {
                        docId = docPosEnum.docID();
                        // if this is the first word and we found a new document
                        if (!positionsInDocs.containsKey(docId)) {
                            positions = new IntArrayList[numberOfWords];
                            positionsInDocs.put(docId, positions);
                        } else {
                            positions = positionsInDocs.get(docId);
                        }
                        if (positions[wordId] == null) {
                            positions[wordId] = new IntArrayList();
                        }
                        // Go through the positions inside this document
                        for (int p = 0; p < docPosEnum.freq(); ++p) {
                            positions[wordId].add(docPosEnum.nextPosition());
                        }
                        if (!docLengths.containsKey(docId)) {
                            // Get the length of the document
                            docLengths.put(docId, reader[i].document(docId).getField(docLengthFieldName).numericValue()
                                    .intValue());
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
