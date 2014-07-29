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
