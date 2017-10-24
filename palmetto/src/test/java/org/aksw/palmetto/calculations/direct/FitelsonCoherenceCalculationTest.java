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
package org.aksw.palmetto.calculations.direct;

import java.util.Arrays;
import java.util.Collection;

import org.aksw.palmetto.calculations.direct.FitelsonConfirmationMeasure;
import org.aksw.palmetto.subsets.OneOne;
import org.aksw.palmetto.subsets.Segmentator;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class FitelsonCoherenceCalculationTest extends AbstractProbabilityBasedCalculationTest {

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                /*
                 * word1 1 1 1
                 * 
                 * word2 0 1 1
                 * 
                 * word3 0 1 1
                 * 
                 * C_f,oneone= 1/6 *
                 * (((P(w_1|w_2)-P(w_1|-w_2))/(P(w_1|w_2)+P(w_1|-w_2))) +
                 * ((P(w_1|w_3)-P(w_1|-w_3))/(P(w_1|w_3)+P(w_1|-w_3))) +
                 * ((P(w_2|w_1)-P(w_2|-w_1))/(P(w_2|w_1)+P(w_2|-w_1))) +
                 * ((P(w_2|w_3)-P(w_2|-w_3))/(P(w_2|w_3)+P(w_2|-w_3))) +
                 * ((P(w_3|w_1)-P(w_3|-w_1))/(P(w_3|w_1)+P(w_3|-w_1))) +
                 * ((P(w_3|w_2)-P(w_3|-w_2))/(P(w_3|w_2)+P(w_3|-w_2)))) =
                 * 1/6*((1-1/2)+(1-1/2)+(2/3/2/3)+(1/1)+(2/3/2/3)+(1/1)) = 2/3
                 */
                { new OneOne(), 3,
                        new double[] { 0, 1.0, 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0 },
                        2.0 / 3.0 },

                /*
                 * word1 0 1 1
                 * 
                 * word2 1 0 1
                 * 
                 * word3 1 1 0
                 * 
                 * C_f,oneone= 1/6 *
                 * (((P(w_1|w_2)-P(w_1|-w_2))/(P(w_1|w_2)+P(w_1|-w_2))) +
                 * ((P(w_1|w_3)-P(w_1|-w_3))/(P(w_1|w_3)+P(w_1|-w_3))) +
                 * ((P(w_2|w_1)-P(w_2|-w_1))/(P(w_2|w_1)+P(w_2|-w_1))) +
                 * ((P(w_2|w_3)-P(w_2|-w_3))/(P(w_2|w_3)+P(w_2|-w_3))) +
                 * ((P(w_3|w_1)-P(w_3|-w_1))/(P(w_3|w_1)+P(w_3|-w_1))) +
                 * ((P(w_3|w_2)-P(w_3|-w_2))/(P(w_3|w_2)+P(w_3|-w_2)))) =
                 * 1/6*(6*((1/2-1)/(1/2+1))) = -1/3
                 */{ new OneOne(), 3,
                        new double[] { 0, 2.0 / 3.0, 2.0 / 3.0, 1.0 / 3.0, 2.0 / 3.0, 1.0 / 3.0, 1.0 / 3.0, 0 },
                        -1.0 / 3.0 },
                /*
                 * word1 0 0 0 1
                 * 
                 * word2 0 1 0 1
                 * 
                 * word3 0 0 1 1
                 * 
                 * C_f,oneone= 1/6 *
                 * (((P(w_1|w_2)-P(w_1|-w_2))/(P(w_1|w_2)+P(w_1|-w_2))) +
                 * ((P(w_1|w_3)-P(w_1|-w_3))/(P(w_1|w_3)+P(w_1|-w_3))) +
                 * ((P(w_2|w_1)-P(w_2|-w_1))/(P(w_2|w_1)+P(w_2|-w_1))) +
                 * ((P(w_2|w_3)-P(w_2|-w_3))/(P(w_2|w_3)+P(w_2|-w_3))) +
                 * ((P(w_3|w_1)-P(w_3|-w_1))/(P(w_3|w_1)+P(w_3|-w_1))) +
                 * ((P(w_3|w_2)-P(w_3|-w_2))/(P(w_3|w_2)+P(w_3|-w_2)))) =
                 * 1/6*((1/1)+(1/1)+(1-1/3/1+1/3)+(0/1)+(1-1/3/1+1/3)+(0/1)) =
                 * 1/2
                 */
                { new OneOne(), 3, new double[] { 0, 0.25, 0.5, 0.25, 0.5, 0.25, 0.25, 0.25 }, 1.0 / 2.0 } });
    }

    public FitelsonCoherenceCalculationTest(Segmentator subsetCreator, int wordsetSize, double[] probabilities,
            double expectedCoherence) {
        super(new FitelsonConfirmationMeasure(), subsetCreator, wordsetSize, probabilities, expectedCoherence);
    }
}
