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
package org.aksw.palmetto.prob;

import org.aksw.palmetto.corpus.BooleanBigramStatsSupportingAdapter;
import org.aksw.palmetto.corpus.CorpusAdapter;
import org.aksw.palmetto.subsets.CountedSubsets;
import org.aksw.palmetto.subsets.SubsetProbabilities;

@Deprecated
public class BooleanBigramStatsProbabilitySupplier extends AbstractProbabilitySupplier {

    public static BooleanBigramStatsProbabilitySupplier create(CorpusAdapter adapter) {
        BooleanBigramStatsFrequencyDeterminer determiner = createFrequencyDeterminer(adapter);
        if (determiner != null) {
            return new BooleanBigramStatsProbabilitySupplier(determiner);
        }
        return null;
    }

    protected static BooleanBigramStatsFrequencyDeterminer createFrequencyDeterminer(CorpusAdapter adapter) {
        if (adapter instanceof BooleanBigramStatsSupportingAdapter) {
            return new BooleanBigramStatsFrequencyDeterminer((BooleanBigramStatsSupportingAdapter) adapter);
        }
        return null;
    }

    private double numberOfWordCounts;
    private double numberOfWordCooccurenceCounts;

    protected BooleanBigramStatsProbabilitySupplier(BooleanBigramStatsFrequencyDeterminer freqDeterminer) {
        super(freqDeterminer);
        numberOfWordCounts = freqDeterminer.getWordCountsSum();
        numberOfWordCooccurenceCounts = freqDeterminer.getWordCooccurenceCountsSum();
    }

    @Override
    protected SubsetProbabilities getProbabilities(CountedSubsets countedSubsets) {
        double probabilities[] = new double[countedSubsets.counts.length];
        int wordPair;
        for (int i = 1; i < probabilities.length; i = i << 1) {
            if (countedSubsets.counts[i] >= minFrequency) {
                probabilities[i] = countedSubsets.counts[i] / numberOfWordCounts;
                for (int j = i << 1; j < probabilities.length; j = j << 1) {
                    wordPair = i | j;
                    if (countedSubsets.counts[wordPair] >= minFrequency)
                        probabilities[wordPair] = countedSubsets.counts[wordPair] / numberOfWordCooccurenceCounts;
                }
            }
        }
        return new SubsetProbabilities(countedSubsets.segments, countedSubsets.conditions, probabilities);
    }

    @Override
    public String getProbabilityModelName() {
        return "P_sw";
    }

}
