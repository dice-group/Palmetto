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
