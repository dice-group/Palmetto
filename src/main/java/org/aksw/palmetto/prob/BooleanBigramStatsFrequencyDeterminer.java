package org.aksw.palmetto.prob;

import org.aksw.palmetto.corpus.BooleanBigramStatsSupportingAdapter;
import org.aksw.palmetto.subsets.CountedSubsets;
import org.aksw.palmetto.subsets.SubsetDefinition;

public class BooleanBigramStatsFrequencyDeterminer implements FrequencyDeterminer {

    private BooleanBigramStatsSupportingAdapter corpusAdapter;

    public BooleanBigramStatsFrequencyDeterminer(BooleanBigramStatsSupportingAdapter corpusAdapter) {
        this.corpusAdapter = corpusAdapter;
    }

    public double getNumberOfWordCounts() {
        return corpusAdapter.getNumberOfWords();
    }

    public double getNumberOfWordCooccurences() {
        return corpusAdapter.getNumberOfCooccurences();
    }

    public CountedSubsets[] determineCounts(String[][] wordsets,
            SubsetDefinition[] definitions) {
        CountedSubsets countedSubsets[] = new CountedSubsets[definitions.length];
        for (int i = 0; i < definitions.length; ++i) {
            countedSubsets[i] = new CountedSubsets(definitions[i].segments,
                    definitions[i].conditions, createCounts(wordsets[i]));
        }
        return countedSubsets;
    }

    private int[] createCounts(String[] wordset) {
        int counts[] = new int[(1 << wordset.length)];
        int wordId1;
        for (int i = 0; i < wordset.length; ++i) {
            wordId1 = (1 << i);
            counts[wordId1] = corpusAdapter.getCount(wordset[i]);
            for (int j = i + 1; j < wordset.length; ++j) {
                counts[wordId1 | (1 << j)] = corpusAdapter.getCooccurenceCount(wordset[i], wordset[j]);
            }
        }
        return counts;
    }
}
