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
public class FitelsonCoherenceCalculationTest extends AbstractCalculationTest {

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

    public FitelsonCoherenceCalculationTest(SubsetCreator subsetCreator, int wordsetSize, double[] probabilities,
            double expectedCoherence) {
        super(new FitelsonCoherenceCalculation(), subsetCreator, wordsetSize, probabilities, expectedCoherence);
    }
}
