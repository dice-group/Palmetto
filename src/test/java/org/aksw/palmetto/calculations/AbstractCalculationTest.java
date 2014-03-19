package org.aksw.palmetto.calculations;

import org.aksw.palmetto.subsets.SubsetCreator;
import org.aksw.palmetto.subsets.SubsetDefinition;
import org.aksw.palmetto.subsets.SubsetProbabilities;
import org.aksw.palmetto.sum.ArithmeticMean;
import org.aksw.palmetto.weight.EqualWeighter;
import org.aksw.palmetto.weight.Weighter;
import org.junit.Assert;
import org.junit.Test;

public abstract class AbstractCalculationTest {

    private static final double DOUBLE_PRECISION_DELTA = 0.00000001;

    private CoherenceCalculation calculation;
    private SubsetCreator subsetCreator;
    private int wordsetSize;
    private double probabilities[];
    private double expectedCoherence;

    public AbstractCalculationTest(CoherenceCalculation calculation, SubsetCreator subsetCreator, int wordsetSize,
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
