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
package org.aksw.palmetto.calculations.indirect;

import java.util.Arrays;
import java.util.Collection;

import org.aksw.palmetto.calculations.indirect.CosinusConfirmationMeasure;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class CosinusBasedCalculationTest extends AbstractVectorBasedCalculationTest {

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                /*
                 * vector1 1.0 2/3 2/3
                 * 
                 * vector2 2/3 2/3 2/3
                 * 
                 * cos=(14/9)/(sqrt(17/9)*sqrt(4/3))
                 */
                { new double[] { 1.0, 2.0 / 3.0, 2.0 / 3.0 }, new double[] { 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0 },
                        (14.0 / 9.0) / (Math.sqrt(17.0 / 9.0) * Math.sqrt(4.0 / 3.0)) },
                // The same but with switched vectors
                { new double[] { 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0 }, new double[] { 1.0, 2.0 / 3.0, 2.0 / 3.0 },
                        (14.0 / 9.0) / (Math.sqrt(17.0 / 9.0) * Math.sqrt(4.0 / 3.0)) },
                /*
                 * vector1 2/3 2/3 2/3
                 * 
                 * vector2 2/3 2/3 2/3
                 * 
                 * cos=(4/3)/(sqrt(4/3)*sqrt(4/3))=1
                 */
                { new double[] { 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0 }, new double[] { 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0 },
                        1.0 },
                /*
                 * vector1 0 0 0
                 * 
                 * vector2 2/3 2/3 2/3
                 * 
                 * cos=0/(sqrt(0)*sqrt(4/3))=0
                 */
                { new double[] { 0, 0, 0 }, new double[] { 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0 }, 0 },
                /*
                 * vector1 0 0 0
                 * 
                 * vector2 0 0 0
                 * 
                 * cos=0/(sqrt(0)*sqrt(0))=0
                 * 
                 * but we define this as 1, because vector1 == vector2
                 */
                { new double[] { 0, 0, 0 }, new double[] { 0, 0, 0 }, 1.0 },
                /*
                 * vector1 2/3 1/3 1/3
                 * 
                 * vector2 1/3 2/3 1/3
                 * 
                 * cos=(5/9)/(sqrt(6/9)*sqrt(6/9))=5/9 / 6/9
                 */
                { new double[] { 2.0 / 3.0, 1.0 / 3.0, 1.0 / 3.0 }, new double[] { 1.0 / 3.0, 2.0 / 3.0, 1.0 / 3.0 },
                        5.0 / 6.0 },
                /*
                 * 
                 * vector1 2/3 1/3 1/3
                 * 
                 * vector2 2/3 1 1
                 * 
                 * cos=(10/9)/(sqrt(6/9)*sqrt(22/9))
                 */
                { new double[] { 2.0 / 3.0, 1.0 / 3.0, 1.0 / 3.0 }, new double[] { 2.0 / 3.0, 1.0, 1.0 },
                        (10.0 / 9.0) / (Math.sqrt(6.0 / 9.0) * Math.sqrt(22.0 / 9.0)) } });
    }

    public CosinusBasedCalculationTest(double[] vector1, double[] vector2, double expectedResult) {
        super(new CosinusConfirmationMeasure(), vector1, vector2, expectedResult);
    }

}
