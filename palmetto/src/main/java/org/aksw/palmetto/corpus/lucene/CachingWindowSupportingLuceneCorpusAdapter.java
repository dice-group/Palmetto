package org.aksw.palmetto.corpus.lucene;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.lucene.index.AtomicReader;
import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.DocsAndPositionsEnum;
import org.apache.lucene.index.DocsEnum;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.NIOFSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.carrotsearch.hppc.IntArrayList;
import com.carrotsearch.hppc.IntIntOpenHashMap;
import com.carrotsearch.hppc.IntObjectOpenHashMap;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

/**
 * An Extension of the {@link WindowSupportingLuceneCorpusAdapter} that caches
 * word positions of the single documents. Preliminary tests show that it can
 * improve the performance if the same words are requested very often. However,
 * it reduces the performance if this is not the case.
 * 
 * @author Michael R&ouml;der (michael.roeder@uni-paderborn.de)
 *
 */
public class CachingWindowSupportingLuceneCorpusAdapter extends WindowSupportingLuceneCorpusAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(CachingWindowSupportingLuceneCorpusAdapter.class);

    protected LoadingCache<String, CachedWordData> cache;

    protected CachingWindowSupportingLuceneCorpusAdapter(DirectoryReader dirReader, AtomicReader[] reader,
            AtomicReaderContext[] contexts, String textFieldName, String docLengthFieldName, int[][] histogram,
            int maxCacheSize) {
        super(dirReader, reader, contexts, textFieldName, docLengthFieldName, histogram);
        this.cache = CacheBuilder.newBuilder().maximumSize(maxCacheSize)
                .build(new CacheLoader<String, CachedWordData>() {
                    @Override
                    public CachedWordData load(String key) throws Exception {
                        return requestDocumentsWithWord(key);
                    }
                });
    }

    @Override
    protected void requestDocumentsWithWord(String word, IntObjectOpenHashMap<IntArrayList[]> positionsInDocs,
            IntIntOpenHashMap docLengths, int wordId, int numberOfWords) {
        try {
            CachedWordData wordData = cache.get(word);
            IntObjectOpenHashMap<IntArrayList> cachedPositions = wordData.getWordPositions();
            int docId;
            IntArrayList positions[];
            for (int i = 0; i < cachedPositions.allocated.length; ++i) {
                if (cachedPositions.allocated[i]) {
                    docId = cachedPositions.keys[i];
                    if (!positionsInDocs.containsKey(docId)) {
                        positions = new IntArrayList[numberOfWords];
                        positionsInDocs.put(docId, positions);
                    } else {
                        positions = positionsInDocs.get(docId);
                    }
                    positions[wordId] = (IntArrayList) ((Object[]) cachedPositions.values)[i];
                }
            }
            docLengths.putAll(wordData.getDocumentLengths());
        } catch (ExecutionException e) {
            throw new IllegalStateException(e);
        }
    }

    protected CachedWordData requestDocumentsWithWord(String word) {
        System.out.println(word);
        DocsAndPositionsEnum docPosEnum = null;
        Term term = new Term(fieldName, word);
        int localDocId;
        int globalDocId;
        int baseDocId;
        IntIntOpenHashMap docLengths = new IntIntOpenHashMap();
        IntObjectOpenHashMap<IntArrayList> wordPositions = new IntObjectOpenHashMap<IntArrayList>();
        IntArrayList positions;
        try {
            for (int i = 0; i < reader.length; i++) {
                docPosEnum = reader[i].termPositionsEnum(term);
                baseDocId = contexts[i].docBase;
                if (docPosEnum != null) {
                    while (docPosEnum.nextDoc() != DocsEnum.NO_MORE_DOCS) {
                        localDocId = docPosEnum.docID();
                        globalDocId = localDocId + baseDocId;
                        positions = new IntArrayList();
                        gatherWordPositions(docPosEnum, positions);
                        addDocLength(docLengths, globalDocId, localDocId, reader[i]);
                        wordPositions.put(globalDocId, positions);
                    }
                }
            }
        } catch (IOException e) {
            LOGGER.error("Error while requesting documents for word \"" + word + "\".", e);
        }
        return new CachedWordData(wordPositions, docLengths);
    }

    public static CachingWindowSupportingLuceneCorpusAdapter create(String indexPath, String textFieldName,
            String docLengthFieldName, int maxCacheSize) throws CorruptIndexException, IOException {
        DirectoryReader dirReader = DirectoryReader.open(new NIOFSDirectory(new File(indexPath)));
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

        return new CachingWindowSupportingLuceneCorpusAdapter(dirReader, reader, contexts, textFieldName,
                docLengthFieldName, histogram, maxCacheSize);
    }

    protected class CachedWordData {
        protected IntObjectOpenHashMap<IntArrayList> wordPositions;
        protected IntIntOpenHashMap documentLengths;

        public CachedWordData(IntObjectOpenHashMap<IntArrayList> wordPositions, IntIntOpenHashMap documentLengths) {
            super();
            this.wordPositions = wordPositions;
            this.documentLengths = documentLengths;
        }

        /**
         * @return the wordPositions
         */
        public IntObjectOpenHashMap<IntArrayList> getWordPositions() {
            return wordPositions;
        }

        /**
         * @param wordPositions the wordPositions to set
         */
        public void setWordPositions(IntObjectOpenHashMap<IntArrayList> wordPositions) {
            this.wordPositions = wordPositions;
        }

        /**
         * @return the documentLengths
         */
        public IntIntOpenHashMap getDocumentLengths() {
            return documentLengths;
        }

        /**
         * @param documentLengths the documentLengths to set
         */
        public void setDocumentLengths(IntIntOpenHashMap documentLengths) {
            this.documentLengths = documentLengths;
        }
    }
}
