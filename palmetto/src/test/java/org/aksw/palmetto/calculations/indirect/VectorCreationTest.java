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

import java.util.Arrays;
import java.util.Collection;

import org.aksw.palmetto.calculations.indirect.AbstractVectorBasedCalculation;
import org.aksw.palmetto.calculations.indirect.CosinusConfirmationMeasure;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class VectorCreationTest {
    private double vectors[][];
    private int vectorId;
    private double expectedVector[];

    private static final double DOUBLE_PRECISION_DELTA = 0.00000001;

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                { new double[][] { { 1, 0, -1 }, { 1, 2, 3 }, { 1, 1, 1 } }, 1, new double[] { 1, 0, -1 } },
                { new double[][] { { 1, 0, -1 }, { 1, 2, 3 }, { 1, 1, 1 } }, 2, new double[] { 1, 2, 3 } },
                { new double[][] { { 1, 0, -1 }, { 1, 2, 3 }, { 1, 1, 1 } }, 4, new double[] { 1, 1, 1 } },
                { new double[][] { { 1, 0, -1 }, { 1, 2, 3 }, { 1, 1, 1 } }, 3, new double[] { 2, 2, 2 } },
                { new double[][] { { 1, 0, -1 }, { 1, 2, 3 }, { 1, 1, 1 } }, 5, new double[] { 2, 1, 0 } },
                { new double[][] { { 1, 0, -1 }, { 1, 2, 3 }, { 1, 1, 1 } }, 6, new double[] { 2, 3, 4 } },
                { new double[][] { { 1, 0, -1 }, { 1, 2, 3 }, { 1, 1, 1 } }, 7, new double[] { 3, 3, 3 } } });
    }

    public VectorCreationTest(double[][] vectors, int vectorId, double[] expectedVector) {
        this.vectors = vectors;
        this.vectorId = vectorId;
        this.expectedVector = expectedVector;
    }

    @Test
    public void test() {
        AbstractVectorBasedCalculation calculation = new CosinusConfirmationMeasure();
        Assert.assertArrayEquals(expectedVector, calculation.createVector(vectorId, vectors), DOUBLE_PRECISION_DELTA);
    }
}
