/**
 * This file is part of Palmetto.
 *
 * Palmetto is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Palmetto is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Palmetto.  If not, see <http://www.gnu.org/licenses/>.
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
