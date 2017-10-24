/**
 * Palmetto - Palmetto is a quality measure tool for topics.
 * Copyright © 2014 Data Science Group (DICE) (michael.roeder@uni-paderborn.de)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
