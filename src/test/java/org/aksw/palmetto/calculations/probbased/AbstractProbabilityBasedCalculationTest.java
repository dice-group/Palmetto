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
package org.aksw.palmetto.calculations.probbased;

import org.aksw.palmetto.calculations.probbased.ProbabilityBasedCalculation;
import org.aksw.palmetto.data.SubsetDefinition;
import org.aksw.palmetto.data.SubsetProbabilities;
import org.aksw.palmetto.subsets.SegmentationScheme;
import org.aksw.palmetto.sum.ArithmeticMean;
import org.aksw.palmetto.weight.EqualWeighter;
import org.aksw.palmetto.weight.Weighter;
import org.junit.Assert;
import org.junit.Test;

public abstract class AbstractProbabilityBasedCalculationTest {

    private static final double DOUBLE_PRECISION_DELTA = 0.00000001;

    private ProbabilityBasedCalculation calculation;
    private SegmentationScheme subsetCreator;
    private int wordsetSize;
    private double probabilities[];
    private double expectedCoherence;

    public AbstractProbabilityBasedCalculationTest(ProbabilityBasedCalculation calculation, SegmentationScheme subsetCreator, int wordsetSize,
            double[] probabilities, double expectedCoherence) {
        this.calculation = calculation;
        this.probabilities = probabilities;
        this.wordsetSize = wordsetSize;
        this.subsetCreator = subsetCreator;
        this.expectedCoherence = expectedCoherence;
    }

    @Test
    public void test() {
        SubsetDefinition subsets = subsetCreator.getSubsetDefinition(wordsetSize);
        SubsetProbabilities subProbs = new SubsetProbabilities(subsets.segments, subsets.conditions, probabilities);
        Weighter weighter = new EqualWeighter();
        Assert.assertEquals(
                expectedCoherence,
                (new ArithmeticMean()).summarize(calculation.calculateCoherenceValues(subProbs),
                        weighter.createWeights(subProbs)), DOUBLE_PRECISION_DELTA);
    }
}
