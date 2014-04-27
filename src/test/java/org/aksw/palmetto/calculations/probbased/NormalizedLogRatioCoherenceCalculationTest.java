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
package org.aksw.palmetto.calculations.probbased;

import java.util.Arrays;
import java.util.Collection;

import org.aksw.palmetto.calculations.probbased.LogBasedCalculation;
import org.aksw.palmetto.calculations.probbased.NormalizedLogRatioCoherenceCalculation;
import org.aksw.palmetto.subsets.OneOne;
import org.aksw.palmetto.subsets.SubsetCreator;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class NormalizedLogRatioCoherenceCalculationTest extends AbstractProbabilityBasedCalculationTest {

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays
                .asList(new Object[][] {
                        /*
                         * word1 1 1 1
                         * 
                         * word2 0 1 1
                         * 
                         * word3 0 1 1
                         * 
                         * C_nlr,oneone = 1/3 *
                         * (log(P(w_1,w_2)/(P(w_1)*P(w_2)))/-log(P(w_1,w_2)) +
                         * log(P(w_1,w_3)/(P(w_1)*P(w_3)))/-log(P(w_1,w_3)) +
                         * log(P(w_2,w_3)/(P(w_2)*P(w_3)))/-log(P(w_2,w_3))) =
                         * 1/3 * (log(2/3 / (1 * 2/3))/-log(2/3) + log(2/3 / (1
                         * * 2/3))/-log(2/3) + log(2/3 / (2/3 * 2/3))/-log(2/3))
                         * = 1/3 * (log(1)/-log(2/3) + log(1)/-log(2/3) +
                         * log(3/2)/-log(2/3)) = 1/3 * (log(3/2)/-log(2/3))
                         */
                        {
                                new OneOne(),
                                3,
                                new double[] { 0, 1.0, 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0 },
                                Math.log(3.0 / 2.0) / (-3 * Math.log(2.0 / 3.0)) },

                        /*
                         * word1 0 1 1
                         * 
                         * word2 1 0 1
                         * 
                         * word3 1 1 0
                         * 
                         * C_nlr,oneone = 1/3 *
                         * (log(P(w_1,w_2)/(P(w_1)*P(w_2)))/-log(P(w_1,w_2)) +
                         * log(P(w_1,w_3)/(P(w_1)*P(w_3)))/-log(P(w_1,w_3)) +
                         * log(P(w_2,w_3)/(P(w_2)*P(w_3)))/-log(P(w_2,w_3))) =
                         * 1/3 * (log(1/3 / (2/3 * 2/3))/-log(1/3) + log(1/3 /
                         * (2/3 * 2/3))/-log(1/3) + log(1/3 / (2/3 *
                         * 2/3))/-log(1/3)) = log(3/4)/-log(1/3)
                         */{
                                new OneOne(),
                                3,
                                new double[] { 0, 2.0 / 3.0, 2.0 / 3.0, 1.0 / 3.0, 2.0 / 3.0, 1.0 / 3.0, 1.0 / 3.0, 0 },
                                Math.log(3.0 / 4.0) / -Math.log(1.0 / 3.0) },
                        /*
                         * word1 0 0 0 1
                         * 
                         * word2 0 1 0 1
                         * 
                         * word3 0 0 1 1
                         * 
                         * C_nlr,oneone = 1/3 *
                         * (log(P(w_1,w_2)/(P(w_1)*P(w_2)))/-log(P(w_1,w_2)) +
                         * log(P(w_1,w_3)/(P(w_1)*P(w_3)))/-log(P(w_1,w_3)) +
                         * log(P(w_2,w_3)/(P(w_2)*P(w_3)))/-log(P(w_2,w_3))) =
                         * 1/3 * (log(1/4 / (1/4 * 1/2))/-log(1/4) + log(1/4 /
                         * (1/4 * 1/2))/-log(1/4) + log(1/4 / (1/2 *
                         * 1/2))/-log(1/4)) = 1/3 * (log(2)/-log(1/4) +
                         * log(2)/-log(1/4))
                         */
                        { new OneOne(), 3, new double[] { 0, 0.25, 0.5, 0.25, 0.5, 0.25, 0.25, 0.25 },
                                2 * Math.log(2.0) / (-3.0 * Math.log(1.0 / 4.0)) },
                        /*
                         * word1 1 0 0 0
                         * 
                         * word2 0 1 0 1
                         * 
                         * word3 0 0 1 1
                         * 
                         * C_nlr,oneone = 1/3 *
                         * (log(P(w_1,w_2)/(P(w_1)*P(w_2)))/-log(P(w_1,w_2)) +
                         * log(P(w_1,w_3)/(P(w_1)*P(w_3)))/-log(P(w_1,w_3)) +
                         * log(P(w_2,w_3)/(P(w_2)*P(w_3)))/-log(P(w_2,w_3))) =
                         * 1/3 * (log(eps / (1/4 * 1/2))/-log(eps) + log(eps /
                         * (1/4 * 1/2))/-log(eps) + log(1/4 / (1/2 *
                         * 1/2))/-log(1/4)) = 1/3 * (2*(log(8*eps)/-log(eps)))
                         */
                        {
                                new OneOne(),
                                3,
                                new double[] { 0, 0.25, 0.5, 0, 0.5, 0, 0.25, 0 },
                                (2 * (Math.log(8 * LogBasedCalculation.EPSILON) / -Math
                                        .log(LogBasedCalculation.EPSILON))) / 3.0 } });
    }

    public NormalizedLogRatioCoherenceCalculationTest(SubsetCreator subsetCreator, int wordsetSize,
            double[] probabilities, double expectedCoherence) {
        super(new NormalizedLogRatioCoherenceCalculation(), subsetCreator, wordsetSize, probabilities,
                expectedCoherence);
    }
}
