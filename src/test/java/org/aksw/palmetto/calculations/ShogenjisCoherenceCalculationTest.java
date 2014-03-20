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
package org.aksw.palmetto.calculations;

import java.util.Arrays;
import java.util.Collection;

import org.aksw.palmetto.subsets.OneOne;
import org.aksw.palmetto.subsets.SubsetCreator;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class ShogenjisCoherenceCalculationTest extends AbstractCalculationTest {

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
                 * C_s,oneone= 1/6 * ((P(w_1,w_2)/(P(w_1)^6)) +
                 * (P(w_1,w_2)/(P(w_2)^6)) + (P(w_1,w_3)/(P(w_1)^6)) +
                 * (P(w_1,w_3)/(P(w_3)^6)) + (P(w_2,w_3)/(P(w_2)^6)) +
                 * (P(w_2,w_3)/(P(w_3)^6))) = 1/6 * ((2/3 / 1) + (2/3 / 64/729)
                 * + (2/3 / 1) + (2/3 / 64/729) + (2/3 / 64/729) + (2/3 /
                 * 64/729)) = 1/6 * (4/3 + 4 * (243 / 32)) = 1/6 * (4/3 + 243/8)
                 * = 761/144
                 */
                { new OneOne(), 3,
                        new double[] { 0, 1.0, 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0 },
                        761.0 / 144.0 },

                /*
                 * word1 0 1 1
                 * 
                 * word2 1 0 1
                 * 
                 * word3 1 1 0
                 * 
                 * C_s,oneone= 1/6 * ((P(w_1,w_2)/(P(w_1)^6)) +
                 * (P(w_1,w_2)/(P(w_2)^6)) + (P(w_1,w_3)/(P(w_1)^6)) +
                 * (P(w_1,w_3)/(P(w_3)^6)) + (P(w_2,w_3)/(P(w_2)^6)) +
                 * (P(w_2,w_3)/(P(w_3)^6))) = 1/6 * (6* (1/3 / 64/729)) = 243/64
                 */{ new OneOne(), 3,
                        new double[] { 0, 2.0 / 3.0, 2.0 / 3.0, 1.0 / 3.0, 2.0 / 3.0, 1.0 / 3.0, 1.0 / 3.0, 0 },
                        243.0 / 64.0 },
                /*
                 * word1 0 0 0 1
                 * 
                 * word2 0 1 0 1
                 * 
                 * word3 0 0 1 1
                 * 
                 * C_s,oneone= 1/6 * ((P(w_1,w_2)/(P(w_1)^6)) +
                 * (P(w_1,w_2)/(P(w_2)^6)) + (P(w_1,w_3)/(P(w_1)^6)) +
                 * (P(w_1,w_3)/(P(w_3)^6)) + (P(w_2,w_3)/(P(w_2)^6)) +
                 * (P(w_2,w_3)/(P(w_3)^6))) = 1/6 * (2 * (1/4 / 1/4096) + 4 *
                 * (1/4 / 1/64)) = 1/6 * (2048 + 64) = 352
                 */
                { new OneOne(), 3, new double[] { 0, 0.25, 0.5, 0.25, 0.5, 0.25, 0.25, 0.25 }, 352 } });
    }

    public ShogenjisCoherenceCalculationTest(SubsetCreator subsetCreator, int wordsetSize, double[] probabilities,
            double expectedCoherence) {
        super(new ShogenjisCoherenceCalculation(), subsetCreator, wordsetSize, probabilities, expectedCoherence);
    }
}
