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
package org.aksw.palmetto.prob.window;

import org.aksw.palmetto.data.CountedSubsets;
import org.aksw.palmetto.data.SubsetProbabilities;
import org.aksw.palmetto.prob.AbstractProbabilitySupplier;

public class WindowBasedProbabilityEstimator extends AbstractProbabilitySupplier {

    private long cooccurrenceCountsSums[];

    public WindowBasedProbabilityEstimator(WindowBasedFrequencyDeterminer freqDeterminer) {
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
    public String getName() {
        return ((WindowBasedFrequencyDeterminer) freqDeterminer).getSlidingWindowModelName();
    }
}
