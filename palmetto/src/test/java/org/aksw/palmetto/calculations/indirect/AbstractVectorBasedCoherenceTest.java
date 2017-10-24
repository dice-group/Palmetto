/**
 * Palmetto - Palmetto is a quality measure tool for topics.
 * Copyright © 2014 Data Science Group (DICE) (michael.roeder@uni-paderborn.de)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.aksw.palmetto.calculations.indirect;

import org.aksw.palmetto.aggregation.ArithmeticMean;
import org.aksw.palmetto.data.SegmentationDefinition;
import org.aksw.palmetto.data.SubsetVectors;
import org.aksw.palmetto.subsets.Segmentator;
import org.junit.Assert;
import org.junit.Test;

public abstract class AbstractVectorBasedCoherenceTest {

    private static final double DOUBLE_PRECISION_DELTA = 0.00000001;

    private VectorBasedConfirmationMeasure calculation;
    private Segmentator subsetCreator;
    private int wordsetSize;
    private double[] probabilities;
    private double vectors[][];
    private double expectedCoherence;

    public AbstractVectorBasedCoherenceTest(VectorBasedConfirmationMeasure calculation, Segmentator subsetCreator,
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
        SegmentationDefinition subsets = subsetCreator.getSubsetDefinition(wordsetSize);
        SubsetVectors subsetVectors = new SubsetVectors(subsets.segments, subsets.conditions, vectors, probabilities);
        Assert.assertEquals(
                expectedCoherence,
                (new ArithmeticMean()).summarize(calculation.calculateConfirmationValues(subsetVectors)),
                DOUBLE_PRECISION_DELTA);
    }
}
