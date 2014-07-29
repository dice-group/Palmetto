/**
 * Copyright (C) 2014 Michael Röder (michael.roeder@unister.de)
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
package org.aksw.palmetto.prob;

import org.aksw.palmetto.data.SegmentationDefinition;
import org.aksw.palmetto.data.SubsetProbabilities;

/**
 * This interface defines the methods of a class that estimates the probabilities of a given word set.
 * 
 * @author m.roeder
 * 
 */
public interface ProbabilityEstimator {

    /**
     * Estimates the probabilities for the given word sets and the given segmentations.
     * 
     * @param wordsets
     * @param definitions
     * @return
     */
    public SubsetProbabilities[] getProbabilities(String wordsets[][],
            SegmentationDefinition definitions[]);

    /**
     * Returns the frequency determiner used by this estimator.
     * 
     * @return
     */
    public FrequencyDeterminer getFrequencyDeterminer();

    /**
     * Sets the frequency determiner that should be used by this estimator.
     * 
     * @param determiner
     */
    public void setFrequencyDeterminer(FrequencyDeterminer determiner);

    /**
     * Returns the name of this probability estimator.
     * 
     * @return
     */
    public String getName();

    /**
     * Sets the minimum frequency a word (or word set) must have to get a probability > 0.
     * 
     * @param minFrequency
     */
    public void setMinFrequency(int minFrequency);
}
