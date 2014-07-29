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
import org.aksw.palmetto.data.SubsetProbabilities;
import org.aksw.palmetto.data.SubsetVectors;
import org.aksw.palmetto.prob.ProbabilityEstimator;

/**
 * Abstract class containing the process of the vector creation.
 * 
 * @author m.roeder
 *
 */
public abstract class AbstractVectorCreator implements VectorCreator {

    private static final String VECTOR_SPACE_NAME = "V^Top";

    private ProbabilityEstimator supplier;

    public AbstractVectorCreator(ProbabilityEstimator supplier) {
        this.supplier = supplier;
    }

    @Override
    public SubsetVectors[] getVectors(String[][] wordsets, SegmentationDefinition[] definitions) {
        SubsetProbabilities probabilities[] = supplier.getProbabilities(wordsets, definitions);
        return createVectors(wordsets, definitions, probabilities);
    }

    protected abstract SubsetVectors[] createVectors(String[][] wordsets, SegmentationDefinition[] definitions,
            SubsetProbabilities[] probabilities);

    @Override
    public String getProbabilityEstimatorName() {
        return supplier.getName();
    }

    @Override
    public String getVectorSpaceName() {
        return VECTOR_SPACE_NAME;
    }

    @Override
    public void setMinFrequency(int minFrequency) {
        supplier.setMinFrequency(minFrequency);
    }

    public void setProbabilityEstimator(ProbabilityEstimator supplier) {
        this.supplier = supplier;
    }
}
