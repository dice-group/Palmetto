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

    public ShogenjisCoherenceCalculationTest(SubsetCreator subsetCreator, int wordsetSize, double[] probabilities,
            double expectedCoherence) {
        super(new ShogenjisCoherenceCalculation(), subsetCreator, wordsetSize, probabilities, expectedCoherence);
    }
}
