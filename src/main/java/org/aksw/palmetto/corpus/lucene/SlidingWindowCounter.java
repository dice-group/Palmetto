package org.aksw.palmetto.corpus.lucene;

import com.carrotsearch.hppc.IntArrayList;

public interface SlidingWindowCounter {

    public void setWindowSize(int windowSize);

    public int determineCount(IntArrayList[] positions);

    /**
     * This method returns the sum of word sets which would be counted if one would go over the complete corpus using
     * the sliding window. <b>Note</b> that if the given word set length is 1 this method must return the sum of all
     * terms inside the corpus.
     * 
     * @param wordSetLength
     * @return
     */
    public long getWordSetCountSum(int wordSetLength);
}
