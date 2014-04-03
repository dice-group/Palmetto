package org.aksw.palmetto.prob;

public interface SlidingWindowFrequencyDeterminer extends FrequencyDeterminer {

    public void setWindowSize(int windowSize);
    
    public long[] getCooccurrenceCounts();

    public String getSlidingWindowModelName();
}
