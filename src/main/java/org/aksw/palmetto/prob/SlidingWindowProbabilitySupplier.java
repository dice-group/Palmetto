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

import org.aksw.palmetto.subsets.CountedSubsets;
import org.aksw.palmetto.subsets.SubsetProbabilities;

public class SlidingWindowProbabilitySupplier extends AbstractProbabilitySupplier {

    private long cooccurrenceCountsSums[];

    public SlidingWindowProbabilitySupplier(SlidingWindowFrequencyDeterminer freqDeterminer) {
        super(freqDeterminer);
        cooccurrenceCountsSums = freqDeterminer.getCooccurrenceCounts();
    }

    @Override
    protected SubsetProbabilities getProbabilities(CountedSubsets countedSubsets) {
        double probabilities[] = new double[countedSubsets.counts.length];
        int wordSet;
        for (int i = 1; i < probabilities.length; i = i << 1) {
            if (countedSubsets.counts[i] >= minFrequency) {
                probabilities[i] = countedSubsets.counts[i] / (double) cooccurrenceCountsSums[0];
                for (int j = 1; j < i; ++j) {
                    wordSet = i | j;
                    if (countedSubsets.counts[wordSet] >= minFrequency)
                        probabilities[wordSet] = countedSubsets.counts[wordSet]
                                / (double) cooccurrenceCountsSums[Integer.bitCount(wordSet) - 1];
                }
            }
        }
        return new SubsetProbabilities(countedSubsets.segments, countedSubsets.conditions, probabilities);
    }

    @Override
    public String getProbabilityModelName() {
        return ((SlidingWindowFrequencyDeterminer) freqDeterminer).getSlidingWindowModelName();
    }

    @Deprecated
    @Override
    public double getInverseProbability(int wordSetDef, int invertingWordSet, double[] probabilities) {
        int windowSize = ((SlidingWindowFrequencyDeterminer) this.freqDeterminer).getWindowSize();
        int wordSetSize = Integer.bitCount(wordSetDef);
        int invertWordSetSize = Integer.bitCount(invertingWordSet);
        int intersectionWordSetSize = wordSetSize + invertWordSetSize;
        double intersectionCount = probabilities[wordSetDef | invertingWordSet]
                * cooccurrenceCountsSums[intersectionWordSetSize - 1];

        // we will need factorial numbers for this step
        int factorials[] = new int[windowSize + 1];
        factorials[0] = 1;
        for (int i = 1; i < factorials.length; ++i) {
            factorials[i] = factorials[i - 1] * i;
        }

        // determine the number of word sets with the size of the intersection and containing the wordSetDef part
        double wordSetCount = probabilities[wordSetDef] * cooccurrenceCountsSums[wordSetSize - 1];
        // determine the number of combinations of the word set and other sets that have the size of the inverting word
        // set inside a single window
        double maxIntersectionCount = (factorials[windowSize - wordSetSize]
                / (factorials[invertWordSetSize] * factorials[windowSize - invertWordSetSize]));
        // add the number of word sets which would be created by moving the window
        maxIntersectionCount += wordSetCount * (windowSize - (wordSetSize + 1));
        return (maxIntersectionCount - intersectionCount) / cooccurrenceCountsSums[intersectionWordSetSize - 1];
    }
}
