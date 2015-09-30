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
							(roeder@informatik.uni-leipzig.de)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
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
                { new double[] { 0.1, 0.2, 0.3 }, new double[] { 3.0, 1.0, 3.0 }, 0.0 },
                { new double[] { 0.0, 0.0, 0.0 }, new double[] { 3.0, 1.0, 3.0 }, 0.0 },
                { new double[] { 0.0, 0.0, 0.0 }, new double[] { 3.0, 3.0, 3.0 }, 1.0 },
                // example from http://en.wikipedia.org/wiki/Pearson_product-moment_correlation_coefficient
                // y = 0.10 + 0.01*x
                { new double[] { 1, 2, 3, 5, 8 }, new double[] { 0.11, 0.12, 0.13, 0.15, 0.18 }, 1.0 },
                // a more realistic example
                {
                        new double[] { 9.123, 8.746, 10.992, 8.6, 9.945, 9.49, 10.803, 9.789, 9.803, 6.896, 11.539,
                                10.318, 9.481, 10.148, 8.661, 10.949, 11.452, 7.963, 11.407, 8.393, 9.563, 11.075,
                                12.127, 12.06, 8.993, 10.48, 11.045, 12.519, 10.581, 12.348, 8.848, 9.022, 9.278,
                                10.794, 8.845, 9.516, 11.746, 6.494, 8.042, 10.063, 10.862, 9.683, 11.752, 10.24,
                                13.577, 10.835, 9.737, 8.838, 9.395, 11.106, 7.476, 8.139, 10.469, 9.288, 7.604,
                                10.924, 10.529, 8.013, 11.229, 9.701, 9.478, 10.978, 11.679, 8.618, 11.774, 10.001,
                                6.371, 8.427, 11.052, 5.269, 7.349, 10.92, 9.791, 10.222, 8.881, 9.518, 6.346, 11.755,
                                9.956, 11.516, 11.74, 11.232, 8.414, 10.359, 11.18, 11.721, 8.504, 9.396, 11.864,
                                12.516, 12.522, 11.623, 9.568, 9.87, 11.653, 11.128, 6.424, 12.418, 10.066, 11.828 }
                        ,
                        new double[] { 2.3, 1.9, 2.36363636364, 2.1, 2.4, 1.8, 2.09090909091, 2.6, 2.66666666667,
                                1.66666666667, 2.5, 2.18181818182, 2.1, 2.0, 1.55555555556, 2.22222222222, 2.0, 1.8,
                                2.2, 1.77777777778, 2.2, 2.36363636364, 2.0, 1.9, 1.90909090909, 2.1, 1.55555555556,
                                1.7, 1.77777777778, 2.44444444444, 1.88888888889, 1.55555555556, 1.5, 1.33333333333,
                                2.4, 2.0, 2.45454545455, 1.3, 1.81818181818, 2.1, 2.1, 1.88888888889, 2.4, 2.1, 2.9,
                                2.6, 2.375, 2.625, 1.55555555556, 2.09090909091, 1.72727272727, 1.22222222222, 2.5,
                                2.88888888889, 1.5, 2.54545454545, 2.6, 1.33333333333, 2.44444444444, 1.54545454545,
                                1.7, 2.6, 2.36363636364, 2.6, 1.81818181818, 2.7, 1.90909090909, 1.4, 2.6, 1.2, 1.0,
                                2.8, 2.27272727273, 2.5, 1.7, 1.4, 2.09090909091, 2.27272727273, 1.8, 2.09090909091,
                                1.90909090909, 2.54545454545, 2.27272727273, 1.7, 2.7, 2.6, 2.0, 2.1, 2.4, 2.6, 2.2,
                                2.5, 1.6, 1.9, 1.90909090909, 1.72727272727, 1.1, 2.5, 1.6, 2.2 }, 0.5294885471 }
        });
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
