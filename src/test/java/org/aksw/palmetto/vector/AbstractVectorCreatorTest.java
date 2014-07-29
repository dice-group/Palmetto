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
import org.aksw.palmetto.prob.FrequencyDeterminer;
import org.aksw.palmetto.prob.ProbabilityEstimator;
import org.aksw.palmetto.subsets.Segmentator;
import org.junit.Assert;
import org.junit.Test;

public abstract class AbstractVectorCreatorTest implements ProbabilityEstimator {

    private static final double DOUBLE_PRECISION_DELTA = 0.00000001;

    protected VectorCreator vectorCreator;
    protected Segmentator subsetCreator;
    protected int wordsetSize;
    protected double probabilities[][];
    protected double expectedVectors[][][];

    public AbstractVectorCreatorTest(VectorCreator vectorCreator, Segmentator subsetCreator, int wordsetSize,
            double[][] probabilities, double expectedVectors[][][]) {
        this.vectorCreator = vectorCreator;
        vectorCreator.setProbabilityEstimator(this);
        this.probabilities = probabilities;
        this.wordsetSize = wordsetSize;
        this.subsetCreator = subsetCreator;
        this.expectedVectors = expectedVectors;
    }

    @Test
    public void test() {
        SegmentationDefinition definitions[] = new SegmentationDefinition[probabilities.length];
        for (int i = 0; i < definitions.length; ++i) {
            definitions[i] = subsetCreator.getSubsetDefinition(wordsetSize);
        }
        SubsetVectors vectors[] = vectorCreator.getVectors(new String[probabilities.length][wordsetSize], definitions);
        for (int i = 0; i < probabilities.length; ++i) {
            Assert.assertEquals(expectedVectors[i].length, vectors[i].vectors.length);
            for (int j = 0; j < expectedVectors[i].length; ++j) {
                Assert.assertArrayEquals("Vector " + j + " of wordset " + i + " is not equal to the expected one.",
                        expectedVectors[i][j], vectors[i].vectors[j], DOUBLE_PRECISION_DELTA);
            }
        }
    }

    @Override
    public SubsetProbabilities[] getProbabilities(String[][] wordsets, SegmentationDefinition[] definitions) {
        SubsetProbabilities subsetProbs[] = new SubsetProbabilities[wordsets.length];
        for (int i = 0; i < wordsets.length; ++i) {
            subsetProbs[i] = new SubsetProbabilities(definitions[i].segments, definitions[i].conditions,
                    probabilities[i]);
        }
        return subsetProbs;
    }

    @Override
    public FrequencyDeterminer getFrequencyDeterminer() {
        throw new IllegalAccessError("This method shouldn't be accessed. Therefore it have not been implemented.");
    }

    @Override
    public String getName() {
        throw new IllegalAccessError("This method shouldn't be accessed. Therefore it have not been implemented.");
    }

    @Override
    public void setMinFrequency(int minFrequency) {
        throw new IllegalAccessError("This method shouldn't be accessed. Therefore it have not been implemented.");
    }

    @Override
    public void setFrequencyDeterminer(FrequencyDeterminer determiner) {
        throw new IllegalAccessError("This method shouldn't be accessed. Therefore it have not been implemented.");
    }
}
