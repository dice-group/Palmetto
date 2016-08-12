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
