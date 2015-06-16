/**
 * The MIT License
 * Copyright (c) 2014 Agile Knowledge Engineering and Semantic Web (AKSW)
							(roeder@informatik.uni-leipzig.de)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
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
