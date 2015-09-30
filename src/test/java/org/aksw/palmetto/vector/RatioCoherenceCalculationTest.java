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
package org.aksw.palmetto.vector;

import java.util.Arrays;
import java.util.Collection;

import org.aksw.palmetto.calculations.direct.RatioConfirmationMeasure;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class RatioCoherenceCalculationTest extends AbstractProbCalcBasedVectorCreatorTest {

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays
                .asList(new Object[][] {
                        /*
                         * word1 1 1 1
                         * 
                         * word2 0 1 1
                         * 
                         * word3 0 1 1
                         * 
                         * vector1 1 1 1
                         * 
                         * vector2 1 3/2 3/2
                         * 
                         * vector3 1 3/2 3/2
                         */
                        {
                                3,
                                new double[][] { { 0, 1.0, 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0,
                                        2.0 / 3.0 } },
                                new double[][][] { { { 1, 1, 1 }, { 1, (3.0 / 2.0), (3.0 / 2.0) },
                                        { 1, (3.0 / 2.0), (3.0 / 2.0) } } }, "V_r(1)", 1 },

                        /*
                         * word1 0 1 1
                         * 
                         * word2 1 0 1
                         * 
                         * word3 1 1 0
                         * 
                         * vector1 3/2 3/4 3/4
                         * 
                         * vector2 3/4 3/2 3/4
                         * 
                         * vector3 3/4 3/4 3/2
                         */{
                                3,
                                new double[][] { { 0, 2.0 / 3.0, 2.0 / 3.0, 1.0 / 3.0, 2.0 / 3.0, 1.0 / 3.0, 1.0 / 3.0,
                                        0 } },
                                new double[][][] { { { (3.0 / 2.0), (3.0 / 4.0), (3.0 / 4.0) },
                                        { (3.0 / 4.0), (3.0 / 2.0), (3.0 / 4.0) },
                                        { (3.0 / 4.0), (3.0 / 4.0), (3.0 / 2.0) } } }, "V_r(1)", 1 },
                        /*
                         * word1 0 0 0 1
                         * 
                         * word2 0 1 0 1
                         * 
                         * word3 0 0 1 1
                         * 
                         * vector1 4 2 2
                         * 
                         * vector2 2 2 1
                         * 
                         * vector3 2 1 2
                         */
                        { 3, new double[][] { { 0, 0.25, 0.5, 0.25, 0.5, 0.25, 0.25, 0.25 } },
                                new double[][][] { { { 4, 2, 2 }, { 2, 2, 1 }, { 2, 1, 2 } } }, "V_r(1)", 1 },
                        /*
                         * word1 1 0 0 0
                         * 
                         * word2 0 1 0 1
                         * 
                         * word3 0 0 1 1
                         * 
                         * vector1 4 0 0
                         * 
                         * vector2 0 2 1
                         * 
                         * vector3 0 1 2
                         */
                        { 3, new double[][] { { 0, 0.25, 0.5, 0, 0.5, 0, 0.25, 0 } },
                                new double[][][] { { { 4, 0, 0 }, { 0, 2, 1 }, { 0, 1, 2 } } }, "V_r(1)", 1 },
                        // all together
                        {
                                3,
                                new double[][] {
                                        { 0, 1.0, 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0 },
                                        { 0, 2.0 / 3.0, 2.0 / 3.0, 1.0 / 3.0, 2.0 / 3.0, 1.0 / 3.0, 1.0 / 3.0, 0 },
                                        { 0, 0.25, 0.5, 0.25, 0.5, 0.25, 0.25, 0.25 },
                                        { 0, 0.25, 0.5, 0, 0.5, 0, 0.25, 0 } },
                                new double[][][] {
                                        { { 1, 1, 1 }, { 1, (3.0 / 2.0), (3.0 / 2.0) }, { 1, (3.0 / 2.0), (3.0 / 2.0) } },
                                        { { (3.0 / 2.0), (3.0 / 4.0), (3.0 / 4.0) },
                                                { (3.0 / 4.0), (3.0 / 2.0), (3.0 / 4.0) },
                                                { (3.0 / 4.0), (3.0 / 4.0), (3.0 / 2.0) } },
                                        { { 4, 2, 2 }, { 2, 2, 1 }, { 2, 1, 2 } },
                                        { { 4, 0, 0 }, { 0, 2, 1 }, { 0, 1, 2 } } }, "V_r(1)", 1 },
                        /*
                         * word1 1 1 1
                         * 
                         * word2 0 1 1
                         * 
                         * word3 0 1 1
                         * 
                         * vector1 1 1 1
                         * 
                         * vector2 1 3/2 3/2
                         * 
                         * vector3 1 3/2 3/2
                         */
                        {
                                3,
                                new double[][] { { 0, 1.0, 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0,
                                        2.0 / 3.0 } },
                                new double[][][] { { { 1, 1, 1 }, { 1, (9.0 / 4.0), (9.0 / 4.0) },
                                        { 1, (9.0 / 4.0), (9.0 / 4.0) } } }, "V_r(2)", 2 },

                        /*
                         * word1 0 1 1
                         * 
                         * word2 1 0 1
                         * 
                         * word3 1 1 0
                         * 
                         * vector1 3/2 3/4 3/4
                         * 
                         * vector2 3/4 3/2 3/4
                         * 
                         * vector3 3/4 3/4 3/2
                         */{
                                3,
                                new double[][] { { 0, 2.0 / 3.0, 2.0 / 3.0, 1.0 / 3.0, 2.0 / 3.0, 1.0 / 3.0, 1.0 / 3.0,
                                        0 } },
                                new double[][][] { { { (9.0 / 4.0), (9.0 / 16.0), (9.0 / 16.0) },
                                        { (9.0 / 16.0), (9.0 / 4.0), (9.0 / 16.0) },
                                        { (9.0 / 16.0), (9.0 / 16.0), (9.0 / 4.0) } } }, "V_r(2)", 2 },
                        /*
                         * word1 0 0 0 1
                         * 
                         * word2 0 1 0 1
                         * 
                         * word3 0 0 1 1
                         * 
                         * vector1 4 2 2
                         * 
                         * vector2 2 2 1
                         * 
                         * vector3 2 1 2
                         */
                        { 3, new double[][] { { 0, 0.25, 0.5, 0.25, 0.5, 0.25, 0.25, 0.25 } },
                                new double[][][] { { { 16, 4, 4 }, { 4, 4, 1 }, { 4, 1, 4 } } }, "V_r(2)", 2 },
                        /*
                         * word1 1 0 0 0
                         * 
                         * word2 0 1 0 1
                         * 
                         * word3 0 0 1 1
                         * 
                         * vector1 4 0 0
                         * 
                         * vector2 0 2 1
                         * 
                         * vector3 0 1 2
                         */
                        { 3, new double[][] { { 0, 0.25, 0.5, 0, 0.5, 0, 0.25, 0 } },
                                new double[][][] { { { 16, 0, 0 }, { 0, 4, 1 }, { 0, 1, 4 } } }, "V_r(2)", 2 },
                        // all together
                        {
                                3,
                                new double[][] {
                                        { 0, 1.0, 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0 },
                                        { 0, 2.0 / 3.0, 2.0 / 3.0, 1.0 / 3.0, 2.0 / 3.0, 1.0 / 3.0, 1.0 / 3.0, 0 },
                                        { 0, 0.25, 0.5, 0.25, 0.5, 0.25, 0.25, 0.25 },
                                        { 0, 0.25, 0.5, 0, 0.5, 0, 0.25, 0 } },
                                new double[][][] {
                                        { { 1, 1, 1 }, { 1, (9.0 / 4.0), (9.0 / 4.0) }, { 1, (9.0 / 4.0), (9.0 / 4.0) } },
                                        { { (9.0 / 4.0), (9.0 / 16.0), (9.0 / 16.0) },
                                                { (9.0 / 16.0), (9.0 / 4.0), (9.0 / 16.0) },
                                                { (9.0 / 16.0), (9.0 / 16.0), (9.0 / 4.0) } },
                                        { { 16, 4, 4 }, { 4, 4, 1 }, { 4, 1, 4 } },
                                        { { 16, 0, 0 }, { 0, 4, 1 }, { 0, 1, 4 } } }, "V_r(2)", 2 } });
    }

    public RatioCoherenceCalculationTest(int wordsetSize, double[][] probabilities, double[][][] expectedVectors,
            String expectedCreatorName, double gamma) {
        super(new RatioConfirmationMeasure(), wordsetSize, probabilities, expectedVectors, expectedCreatorName, gamma);
    }
}
