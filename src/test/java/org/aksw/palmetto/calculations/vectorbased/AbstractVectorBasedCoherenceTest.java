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
package org.aksw.palmetto.calculations.vectorbased;

import org.aksw.palmetto.data.SubsetDefinition;
import org.aksw.palmetto.data.SubsetVectors;
import org.aksw.palmetto.subsets.SubsetCreator;
import org.aksw.palmetto.sum.ArithmeticMean;
import org.aksw.palmetto.weight.EqualWeighter;
import org.aksw.palmetto.weight.Weighter;
import org.junit.Assert;
import org.junit.Test;

public abstract class AbstractVectorBasedCoherenceTest {

    private static final double DOUBLE_PRECISION_DELTA = 0.00000001;

    private VectorBasedCalculation calculation;
    private SubsetCreator subsetCreator;
    private int wordsetSize;
    private double[] probabilities;
    private double vectors[][];
    private double expectedCoherence;

    public AbstractVectorBasedCoherenceTest(VectorBasedCalculation calculation, SubsetCreator subsetCreator,
            int wordsetSize, double[] probabilities, double[][] vectors, double expectedCoherence) {
        this.calculation = calculation;
        this.probabilities = probabilities;
        this.vectors = vectors;
        this.wordsetSize = wordsetSize;
        this.subsetCreator = subsetCreator;
        this.expectedCoherence = expectedCoherence;
    }

    @Test
    public void test() {
        SubsetDefinition subsets = subsetCreator.getSubsetDefinition(wordsetSize);
        SubsetVectors subsetVectors = new SubsetVectors(subsets.segments, subsets.conditions, vectors, probabilities);
        Weighter weighter = new EqualWeighter();
        Assert.assertEquals(
                expectedCoherence,
                (new ArithmeticMean()).summarize(calculation.calculateCoherenceValues(subsetVectors),
                        weighter.createWeights(subsetVectors)), DOUBLE_PRECISION_DELTA);
    }
}
