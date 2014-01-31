package org.aksw.palmetto.prob;

import org.aksw.palmetto.subsets.CountedSubsets;
import org.aksw.palmetto.subsets.SubsetDefinition;
import org.aksw.palmetto.subsets.SubsetProbabilities;

public abstract class AbstractProbabilitySupplier implements
        ProbabilitySupplier {

    public static final int DEFAULT_MIN_FREQUENCY = 10;

    protected FrequencyDeterminer freqDeterminer;
    protected int minFrequency = DEFAULT_MIN_FREQUENCY;

    protected AbstractProbabilitySupplier(FrequencyDeterminer freqDeterminer) {
        this.freqDeterminer = freqDeterminer;
    }

    public SubsetProbabilities[] getProbabilities(String wordsets[][],
            SubsetDefinition definitions[]) {
        CountedSubsets subsets[] = freqDeterminer.determineCounts(wordsets,
                definitions);
        SubsetProbabilities probabilities[] = new SubsetProbabilities[subsets.length];
        for (int i = 0; i < subsets.length; i++) {
            probabilities[i] = getProbabilities(subsets[i]);
        }
        return probabilities;
    }

    protected abstract SubsetProbabilities getProbabilities(
            CountedSubsets countedSubsets);

    public void setMinFrequency(int minFrequency) {
        this.minFrequency = minFrequency;
    }

    public int getMinFrequency() {
        return minFrequency;
    }

    public FrequencyDeterminer getFrequencyDeterminer() {
        return freqDeterminer;
    }

    public void setFrequencyDeterminer(FrequencyDeterminer freqDeterminer) {
        this.freqDeterminer = freqDeterminer;
    }
}
