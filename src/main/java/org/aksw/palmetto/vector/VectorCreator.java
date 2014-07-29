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
package org.aksw.palmetto.vector;

import org.aksw.palmetto.data.SegmentationDefinition;
import org.aksw.palmetto.data.SubsetVectors;
import org.aksw.palmetto.prob.ProbabilityEstimator;

/**
 * Interface for the vector creation.
 * 
 * @author m.roeder
 * 
 */
public interface VectorCreator {

    /**
     * Creates vectors for the given word sets and their segmentations.
     * 
     * @param wordsets
     * @param definitions
     * @return
     */
    public SubsetVectors[] getVectors(String wordsets[][], SegmentationDefinition definitions[]);

    /**
     * Sets the probability estimator used by the vector creator.
     * 
     * @param supplier
     */
    public void setProbabilityEstimator(ProbabilityEstimator supplier);

    /**
     * Calls {@link ProbabilityEstimator#getName()} of the probability estimator and returns the
     * name of
     * the estimator.
     * 
     * @return
     */
    public String getProbabilityEstimatorName();

    /**
     * Returns the name of the vector space.
     * 
     * @return
     */
    public String getVectorSpaceName();

    /**
     * Returns the name of the direct confirmation measure which is used to create the vectors.
     * 
     * @return
     */
    public String getVectorCreatorName();

    /**
     * Sets the minimum frequency of the probability estimator.
     * 
     * @param minFrequency
     */
    public void setMinFrequency(int minFrequency);
}
