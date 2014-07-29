/**
 * Copyright (C) 2014 Michael Röder (michael.roeder@unister.de)
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

import org.aksw.palmetto.calculations.indirect.DiceConfirmationMeasure;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class DiceBasedCalculationTest extends AbstractVectorBasedCalculationTest {

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays
                .asList(new Object[][] {
                        /*
                         * vector1 1.0 2/3 2/3
                         * 
                         * vector2 2/3 2/3 2/3
                         * 
                         * dice=2*2/(13/3)
                         */
                        { new double[] { 1.0, 2.0 / 3.0, 2.0 / 3.0 }, new double[] { 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0 },
                                12.0 / 13.0 },
                        // The same but with switched vectors
                        { new double[] { 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0 }, new double[] { 1.0, 2.0 / 3.0, 2.0 / 3.0 },
                                12.0 / 13.0 },
                        /*
                         * vector1 2/3 2/3 2/3
                         * 
                         * vector2 2/3 2/3 2/3
                         * 
                         * dice=2*2/(2+2)=1
                         */
                        { new double[] { 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0 },
                                new double[] { 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0 }, 1.0 },
                        /*
                         * vector1 0 0 0
                         * 
                         * vector2 2/3 2/3 2/3
                         * 
                         * dice=0/(0+2)=0
                         */
                        { new double[] { 0, 0, 0 }, new double[] { 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0 }, 0 },
                        /*
                         * vector1 0 0 0
                         * 
                         * vector2 0 0 0
                         * 
                         * dice=0/0=0
                         * 
                         * but we define this as 1, because vector1 == vector2
                         */
                        { new double[] { 0, 0, 0 }, new double[] { 0, 0, 0 }, 1.0 },
                        /*
                         * vector1 2/3 1/3 1/3
                         * 
                         * vector2 1/3 2/3 1/3
                         * 
                         * dice=2*1/(4/3+4/3)=6/8
                         */
                        { new double[] { 2.0 / 3.0, 1.0 / 3.0, 1.0 / 3.0 },
                                new double[] { 1.0 / 3.0, 2.0 / 3.0, 1.0 / 3.0 }, 6.0 / 8.0 },
                        /*
                         * 
                         * vector1 2/3 1/3 1/3
                         * 
                         * vector2 2/3 1 1
                         * 
                         * dice=(8/3)/(4/3+8/3)=8/12
                         */
                        { new double[] { 2.0 / 3.0, 1.0 / 3.0, 1.0 / 3.0 }, new double[] { 2.0 / 3.0, 1.0, 1.0 },
                                2.0 / 3.0 } });
    }

    public DiceBasedCalculationTest(double[] vector1, double[] vector2, double expectedResult) {
        super(new DiceConfirmationMeasure(), vector1, vector2, expectedResult);
    }

}
