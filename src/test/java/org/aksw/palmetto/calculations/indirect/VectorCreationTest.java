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

import org.aksw.palmetto.calculations.indirect.AbstractVectorBasedCalculation;
import org.aksw.palmetto.calculations.indirect.CosinusConfirmationMeasure;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class VectorCreationTest {

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

    private double vectors[][];
    private int vectorId;
    private double expectedVector[];

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
