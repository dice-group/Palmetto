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

import org.aksw.palmetto.calculations.direct.ShogenjisConfirmationMeasure;
import org.aksw.palmetto.subsets.OneOne;
import org.aksw.palmetto.subsets.Segmentator;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class ShogenjisCoherenceCalculationTest extends AbstractProbabilityBasedCalculationTest {

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
                 * C_s,oneone= 1/6 * (log(P(w_1,w_2)/(P(w_1)^6)) +
                 * log(P(w_1,w_2)/(P(w_2)^6)) + log(P(w_1,w_3)/(P(w_1)^6)) +
                 * log(P(w_1,w_3)/(P(w_3)^6)) + log(P(w_2,w_3)/(P(w_2)^6)) +
                 * log(P(w_2,w_3)/(P(w_3)^6))) = 1/6 * (log(2/3 / 1) + log(2/3 / 64/729)
                 * + log(2/3 / 1) + log(2/3 / 64/729) + log(2/3 / 64/729) + log(2/3 /
                 * 64/729)) = 1/6 * (2*log(2/3) + 4 * log(243 / 32))
                 */
                { new OneOne(), 3,
                        new double[] { 0, 1.0, 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0 },
                        (2 * Math.log(2.0 / 3.0) + 4 * Math.log(243.0 / 32.0)) / 6 },

                /*
                 * word1 0 1 1
                 * 
                 * word2 1 0 1
                 * 
                 * word3 1 1 0
                 * 
                 * C_s,oneone= 1/6 * (log(P(w_1,w_2)/(P(w_1)^6)) +
                 * log(P(w_1,w_2)/(P(w_2)^6)) + log(P(w_1,w_3)/(P(w_1)^6)) +
                 * log(P(w_1,w_3)/(P(w_3)^6)) + log(P(w_2,w_3)/(P(w_2)^6)) +
                 * log(P(w_2,w_3)/(P(w_3)^6))) = 1/6 * (6* log(1/3 / 64/729)) = log(243/64)
                 */{ new OneOne(), 3,
                        new double[] { 0, 2.0 / 3.0, 2.0 / 3.0, 1.0 / 3.0, 2.0 / 3.0, 1.0 / 3.0, 1.0 / 3.0, 0 },
                        Math.log(243.0 / 64.0) },
                /*
                 * word1 0 0 0 1
                 * 
                 * word2 0 1 0 1
                 * 
                 * word3 0 0 1 1
                 * 
                 * C_s,oneone= 1/6 * (log(P(w_1,w_2)/(P(w_1)^6)) +
                 * log(P(w_1,w_2)/(P(w_2)^6)) + log(P(w_1,w_3)/(P(w_1)^6)) +
                 * log(P(w_1,w_3)/(P(w_3)^6)) + log(P(w_2,w_3)/(P(w_2)^6)) +
                 * log(P(w_2,w_3)/(P(w_3)^6))) = 1/6 * (2 * log(1/4 / 1/4096) + 4 *
                 * log(1/4 / 1/64))
                 */
                { new OneOne(), 3, new double[] { 0, 0.25, 0.5, 0.25, 0.5, 0.25, 0.25, 0.25 },
                        (2 * Math.log(1024) + 4 * Math.log(16)) / 6 } });
    }

    public ShogenjisCoherenceCalculationTest(Segmentator subsetCreator, int wordsetSize, double[] probabilities,
            double expectedCoherence) {
        super(new ShogenjisConfirmationMeasure(), subsetCreator, wordsetSize, probabilities, expectedCoherence);
    }
}
