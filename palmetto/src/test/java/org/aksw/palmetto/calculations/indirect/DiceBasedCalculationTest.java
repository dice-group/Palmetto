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
