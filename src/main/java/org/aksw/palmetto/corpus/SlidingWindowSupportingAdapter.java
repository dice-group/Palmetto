package org.aksw.palmetto.corpus;

import com.carrotsearch.hppc.IntArrayList;
import com.carrotsearch.hppc.IntIntOpenHashMap;
import com.carrotsearch.hppc.IntObjectOpenHashMap;

public interface SlidingWindowSupportingAdapter extends CorpusAdapter {

    // public void setWindowSize(int windowSize);

    /**
     * Returns the number of cooccurrences of the given words. <b>Note</b> that if the given array contains one single
     * word this method must return the number of occurrence of this single word.
     * 
     * @param words
     * @return
     */
    // public int getCooccurrenceCounts(String words[]);

    // public long getWordSetCountSum(int wordSetLength);

    public int[][] getDocumentSizeHistogram();

    public IntObjectOpenHashMap<IntArrayList[]> requestWordPositionsInDocuments(String words[], IntIntOpenHashMap docLengths);
}
