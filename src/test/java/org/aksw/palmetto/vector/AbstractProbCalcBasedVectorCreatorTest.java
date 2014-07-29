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
package org.aksw.palmetto.vector;

import junit.framework.Assert;

import org.aksw.palmetto.calculations.direct.DirectConfirmationMeasure;
import org.aksw.palmetto.subsets.OneOne;
import org.junit.Test;

public abstract class AbstractProbCalcBasedVectorCreatorTest extends AbstractVectorCreatorTest {

    protected String expectedCreatorName;

    public AbstractProbCalcBasedVectorCreatorTest(DirectConfirmationMeasure calculation, int wordsetSize,
            double[][] probabilities, double[][][] expectedVectors, String expectedCreatorName, double gamma) {
        super(new DirectConfirmationBasedVectorCreator(null, calculation, gamma), new OneOne(), wordsetSize, probabilities,
                expectedVectors);
        this.expectedCreatorName = expectedCreatorName;
    }

    @Test
    public void testCreatorName() {
        Assert.assertEquals(expectedCreatorName, this.vectorCreator.getVectorCreatorName());
    }

}
