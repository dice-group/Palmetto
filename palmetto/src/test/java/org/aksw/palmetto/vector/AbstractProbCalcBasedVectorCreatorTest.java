/**
 * Palmetto - Palmetto is a quality measure tool for topics.
 * Copyright © 2014 Data Science Group (DICE) (michael.roeder@uni-paderborn.de)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
