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
package org.aksw.palmetto.evaluate.correlation;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class PearsonsSampleCorrelationCoefficientTest {

    private static final double DOUBLE_PRECISION_DELTA = 0.00000001;

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] { { new double[] { 1.0, 0.5, 0.0 }, new double[] { 1.0, 0.5, 0.0 }, 1.0 },
                { new double[] { 0.1, 0.2, 0.3 }, new double[] { 1.0, 2.0, 3.0 }, 1.0 },
                { new double[] { 0.2, 0.2, 0.3 }, new double[] { 1.0, 2.0, 3.0 }, 0.8660254038 },
                { new double[] { 0.1, 0.2, 0.3 }, new double[] { 3.0, 2.0, 1.0 }, -1.0 },
                { new double[] { 0.1, 0.2, 0.3 }, new double[] { 3.0, 1.0, 3.0 }, 0.0 }});
    }

    private double x[];
    private double y[];
    private double expectedCorrelation;

    public PearsonsSampleCorrelationCoefficientTest(double[] x, double[] y, double expectedCorrelation) {
        this.x = x;
        this.y = y;
        this.expectedCorrelation = expectedCorrelation;
    }

    @Test
    public void test() {
        PearsonsSampleCorrelationCoefficient correl = new PearsonsSampleCorrelationCoefficient();
        Assert.assertEquals(expectedCorrelation, correl.calculateRankCorrelation(x, y), DOUBLE_PRECISION_DELTA);
    }
}
