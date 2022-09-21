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

import org.junit.Assert;
import org.junit.Test;

public abstract class AbstractVectorBasedCalculationTest {

    private static final double DOUBLE_PRECISION_DELTA = 0.00000001;

    private AbstractVectorBasedCalculation calculation;
    private double vector1[];
    private double vector2[];
    private double expectedResult;

    public AbstractVectorBasedCalculationTest(AbstractVectorBasedCalculation calculation, double[] vector1,
            double[] vector2, double expectedResult) {
        this.calculation = calculation;
        this.vector1 = vector1;
        this.vector2 = vector2;
        this.expectedResult = expectedResult;
    }

    @Test
    public void test() {
        Assert.assertEquals(expectedResult, calculation.calculateSimilarity(vector1, vector2), DOUBLE_PRECISION_DELTA);
    }
}
