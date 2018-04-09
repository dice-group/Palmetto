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
                 */
                { new double[] { 0, 0, 0 }, new double[] { 0, 0, 0 }, 0 },
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
