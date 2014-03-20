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
public class SpearmanTest {

    private static final double DOUBLE_PRECISION_DELTA = 0.00000001;

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                /*
                 * Correlation = 1 - (6*26 / 8 * (8² - 1)) = 1 - (156/504)
                 */
                { new double[] { 2.0, 3.0, 3.0, 5.0, 5.5, 8.0, 10.0, 10.0 },
                        new double[] { 1.5, 1.5, 4.0, 3.0, 1.0, 5.0, 5.0, 9.5 }, 1.0 - (156.0 / 504.0) },
                /*
                 * The same as above but with a changed order
                 */
                { new double[] { 10.0, 10.0, 8.0, 5.5, 5.0, 3.0, 3.0, 2.0 },
                        new double[] { 9.5, 5.0, 5.0, 1.0, 3.0, 4.0, 1.5, 1.5 }, 1.0 - (156.0 / 504.0) },
                /*
                 * Correlation = 1 - (6*194 / 10 * (10² - 1)) = 1 - (1164/990)
                 */
                { new double[] { 106.0, 86.0, 100.0, 101.0, 99.0, 103.0, 97.0, 113.0, 112.0, 110.0 },
                        new double[] { 7.0, 0.0, 27.0, 50.0, 28.0, 29.0, 20.0, 12.0, 6.0, 17.0 },
                        1.0 - (1164.0 / 990.0) } });
    }

    private double x[];
    private double y[];
    private double expectedCorrelation;

    public SpearmanTest(double[] x, double[] y, double expectedCorrelation) {
        this.x = x;
        this.y = y;
        this.expectedCorrelation = expectedCorrelation;
    }

    @Test
    public void test() {
        Spearman correlation = new Spearman();
        Assert.assertEquals(expectedCorrelation, correlation.calculateRankCorrelation(x, y), DOUBLE_PRECISION_DELTA);
    }
}
