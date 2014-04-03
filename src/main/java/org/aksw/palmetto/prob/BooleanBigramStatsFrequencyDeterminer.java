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
import org.aksw.palmetto.subsets.CountedSubsets;
import org.aksw.palmetto.subsets.SubsetDefinition;

@Deprecated
public class BooleanBigramStatsFrequencyDeterminer implements FrequencyDeterminer {

    private BooleanBigramStatsSupportingAdapter corpusAdapter;

    public BooleanBigramStatsFrequencyDeterminer(BooleanBigramStatsSupportingAdapter corpusAdapter) {
        this.corpusAdapter = corpusAdapter;
    }

    public double getWordCountsSum() {
        return corpusAdapter.getWordCountsSum();
    }

    public double getWordCooccurenceCountsSum() {
        return corpusAdapter.getWordCooccurenceCountsSum();
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
