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
 * This vector creator uses the marginal probabilities for the vector creation.
 * 
 * @author m.roeder
 * 
 */
public class ProbabilityBasedVectorCreator extends AbstractVectorCreator {

    private static final String VECTOR_CREATOR_NAME = "V_p";

    public ProbabilityBasedVectorCreator(ProbabilityEstimator supplier) {
        super(supplier);
    }

    @Override
    public String getVectorCreatorName() {
        return VECTOR_CREATOR_NAME;
    }

    @Override
    protected SubsetVectors[] createVectors(String[][] wordsets, SegmentationDefinition[] definitions,
            SubsetProbabilities[] probabilities) {
        SubsetVectors vectors[] = new SubsetVectors[wordsets.length];
        double currentVectors[][];
        int bit1;
        for (int w = 0; w < wordsets.length; ++w) {
            currentVectors = new double[wordsets[w].length][wordsets[w].length];
            for (int i = 0; i < wordsets[w].length; ++i) {
                bit1 = 1 << i;
                currentVectors[i][i] = probabilities[w].probabilities[bit1];
                for (int j = i + 1; j < wordsets[w].length; ++j) {
                    currentVectors[i][j] = probabilities[w].probabilities[bit1 | (1 << j)];
                    currentVectors[j][i] = currentVectors[i][j];
                }
            }
            vectors[w] = new SubsetVectors(definitions[w].segments, definitions[w].conditions, currentVectors,
                    probabilities[w].probabilities);
        }
        return vectors;
    }

}
