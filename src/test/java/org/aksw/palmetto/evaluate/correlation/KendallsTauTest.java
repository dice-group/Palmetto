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

import org.aksw.palmetto.evaluate.correlation.KendallsTau;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class KendallsTauTest {

    private static final double DOUBLE_PRECISION_DELTA = 0.00000001;

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays
                .asList(new Object[][] {
                        /*
                         * C = 19
                         * D = 5
                         * X = 2
                         * Y = 2
                         * Tau = (19 - 5) / sqrt((19 + 5 + 2) * (19 + 5 + 2)) = 14 / 26
                         */
                        { new double[] { 2.0, 3.0, 3.0, 5.0, 5.5, 8.0, 10.0, 10.0 },
                                new double[] { 1.5, 1.5, 4.0, 3.0, 1.0, 5.0, 5.0, 9.5 }, (14.0 / 26.0) },
                        /*
                         * The same as above but with a changed order
                         */
                        { new double[] { 10.0, 10.0, 8.0, 5.5, 5.0, 3.0, 3.0, 2.0 },
                                new double[] { 9.5, 5.0, 5.0, 1.0, 3.0, 4.0, 1.5, 1.5 }, (14.0 / 26.0) } });
    }

    private double x[];
    private double y[];
    private double expectedCorrelation;

    public KendallsTauTest(double[] x, double[] y, double expectedCorrelation) {
        this.x = x;
        this.y = y;
        this.expectedCorrelation = expectedCorrelation;
    }

    @Test
    public void test() {
        KendallsTau kendallsTau = new KendallsTau();
        Assert.assertEquals(expectedCorrelation, kendallsTau.calculateRankCorrelation(x, y), DOUBLE_PRECISION_DELTA);
    }
}
