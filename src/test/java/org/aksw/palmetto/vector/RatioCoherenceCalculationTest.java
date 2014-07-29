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
package org.aksw.palmetto.vector;

import java.util.Arrays;
import java.util.Collection;

import org.aksw.palmetto.calculations.direct.RatioConfirmationMeasure;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class RatioCoherenceCalculationTest extends AbstractProbCalcBasedVectorCreatorTest {

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
                         * vector1 1 1 1
                         * 
                         * vector2 1 3/2 3/2
                         * 
                         * vector3 1 3/2 3/2
                         */
                        {
                                3,
                                new double[][] { { 0, 1.0, 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0,
                                        2.0 / 3.0 } },
                                new double[][][] { { { 1, 1, 1 }, { 1, (3.0 / 2.0), (3.0 / 2.0) },
                                        { 1, (3.0 / 2.0), (3.0 / 2.0) } } }, "V_r(1)", 1 },

                        /*
                         * word1 0 1 1
                         * 
                         * word2 1 0 1
                         * 
                         * word3 1 1 0
                         * 
                         * vector1 3/2 3/4 3/4
                         * 
                         * vector2 3/4 3/2 3/4
                         * 
                         * vector3 3/4 3/4 3/2
                         */{
                                3,
                                new double[][] { { 0, 2.0 / 3.0, 2.0 / 3.0, 1.0 / 3.0, 2.0 / 3.0, 1.0 / 3.0, 1.0 / 3.0,
                                        0 } },
                                new double[][][] { { { (3.0 / 2.0), (3.0 / 4.0), (3.0 / 4.0) },
                                        { (3.0 / 4.0), (3.0 / 2.0), (3.0 / 4.0) },
                                        { (3.0 / 4.0), (3.0 / 4.0), (3.0 / 2.0) } } }, "V_r(1)", 1 },
                        /*
                         * word1 0 0 0 1
                         * 
                         * word2 0 1 0 1
                         * 
                         * word3 0 0 1 1
                         * 
                         * vector1 4 2 2
                         * 
                         * vector2 2 2 1
                         * 
                         * vector3 2 1 2
                         */
                        { 3, new double[][] { { 0, 0.25, 0.5, 0.25, 0.5, 0.25, 0.25, 0.25 } },
                                new double[][][] { { { 4, 2, 2 }, { 2, 2, 1 }, { 2, 1, 2 } } }, "V_r(1)", 1 },
                        /*
                         * word1 1 0 0 0
                         * 
                         * word2 0 1 0 1
                         * 
                         * word3 0 0 1 1
                         * 
                         * vector1 4 0 0
                         * 
                         * vector2 0 2 1
                         * 
                         * vector3 0 1 2
                         */
                        { 3, new double[][] { { 0, 0.25, 0.5, 0, 0.5, 0, 0.25, 0 } },
                                new double[][][] { { { 4, 0, 0 }, { 0, 2, 1 }, { 0, 1, 2 } } }, "V_r(1)", 1 },
                        // all together
                        {
                                3,
                                new double[][] {
                                        { 0, 1.0, 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0 },
                                        { 0, 2.0 / 3.0, 2.0 / 3.0, 1.0 / 3.0, 2.0 / 3.0, 1.0 / 3.0, 1.0 / 3.0, 0 },
                                        { 0, 0.25, 0.5, 0.25, 0.5, 0.25, 0.25, 0.25 },
                                        { 0, 0.25, 0.5, 0, 0.5, 0, 0.25, 0 } },
                                new double[][][] {
                                        { { 1, 1, 1 }, { 1, (3.0 / 2.0), (3.0 / 2.0) }, { 1, (3.0 / 2.0), (3.0 / 2.0) } },
                                        { { (3.0 / 2.0), (3.0 / 4.0), (3.0 / 4.0) },
                                                { (3.0 / 4.0), (3.0 / 2.0), (3.0 / 4.0) },
                                                { (3.0 / 4.0), (3.0 / 4.0), (3.0 / 2.0) } },
                                        { { 4, 2, 2 }, { 2, 2, 1 }, { 2, 1, 2 } },
                                        { { 4, 0, 0 }, { 0, 2, 1 }, { 0, 1, 2 } } }, "V_r(1)", 1 },
                        /*
                         * word1 1 1 1
                         * 
                         * word2 0 1 1
                         * 
                         * word3 0 1 1
                         * 
                         * vector1 1 1 1
                         * 
                         * vector2 1 3/2 3/2
                         * 
                         * vector3 1 3/2 3/2
                         */
                        {
                                3,
                                new double[][] { { 0, 1.0, 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0,
                                        2.0 / 3.0 } },
                                new double[][][] { { { 1, 1, 1 }, { 1, (9.0 / 4.0), (9.0 / 4.0) },
                                        { 1, (9.0 / 4.0), (9.0 / 4.0) } } }, "V_r(2)", 2 },

                        /*
                         * word1 0 1 1
                         * 
                         * word2 1 0 1
                         * 
                         * word3 1 1 0
                         * 
                         * vector1 3/2 3/4 3/4
                         * 
                         * vector2 3/4 3/2 3/4
                         * 
                         * vector3 3/4 3/4 3/2
                         */{
                                3,
                                new double[][] { { 0, 2.0 / 3.0, 2.0 / 3.0, 1.0 / 3.0, 2.0 / 3.0, 1.0 / 3.0, 1.0 / 3.0,
                                        0 } },
                                new double[][][] { { { (9.0 / 4.0), (9.0 / 16.0), (9.0 / 16.0) },
                                        { (9.0 / 16.0), (9.0 / 4.0), (9.0 / 16.0) },
                                        { (9.0 / 16.0), (9.0 / 16.0), (9.0 / 4.0) } } }, "V_r(2)", 2 },
                        /*
                         * word1 0 0 0 1
                         * 
                         * word2 0 1 0 1
                         * 
                         * word3 0 0 1 1
                         * 
                         * vector1 4 2 2
                         * 
                         * vector2 2 2 1
                         * 
                         * vector3 2 1 2
                         */
                        { 3, new double[][] { { 0, 0.25, 0.5, 0.25, 0.5, 0.25, 0.25, 0.25 } },
                                new double[][][] { { { 16, 4, 4 }, { 4, 4, 1 }, { 4, 1, 4 } } }, "V_r(2)", 2 },
                        /*
                         * word1 1 0 0 0
                         * 
                         * word2 0 1 0 1
                         * 
                         * word3 0 0 1 1
                         * 
                         * vector1 4 0 0
                         * 
                         * vector2 0 2 1
                         * 
                         * vector3 0 1 2
                         */
                        { 3, new double[][] { { 0, 0.25, 0.5, 0, 0.5, 0, 0.25, 0 } },
                                new double[][][] { { { 16, 0, 0 }, { 0, 4, 1 }, { 0, 1, 4 } } }, "V_r(2)", 2 },
                        // all together
                        {
                                3,
                                new double[][] {
                                        { 0, 1.0, 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0 },
                                        { 0, 2.0 / 3.0, 2.0 / 3.0, 1.0 / 3.0, 2.0 / 3.0, 1.0 / 3.0, 1.0 / 3.0, 0 },
                                        { 0, 0.25, 0.5, 0.25, 0.5, 0.25, 0.25, 0.25 },
                                        { 0, 0.25, 0.5, 0, 0.5, 0, 0.25, 0 } },
                                new double[][][] {
                                        { { 1, 1, 1 }, { 1, (9.0 / 4.0), (9.0 / 4.0) }, { 1, (9.0 / 4.0), (9.0 / 4.0) } },
                                        { { (9.0 / 4.0), (9.0 / 16.0), (9.0 / 16.0) },
                                                { (9.0 / 16.0), (9.0 / 4.0), (9.0 / 16.0) },
                                                { (9.0 / 16.0), (9.0 / 16.0), (9.0 / 4.0) } },
                                        { { 16, 4, 4 }, { 4, 4, 1 }, { 4, 1, 4 } },
                                        { { 16, 0, 0 }, { 0, 4, 1 }, { 0, 1, 4 } } }, "V_r(2)", 2 } });
    }

    public RatioCoherenceCalculationTest(int wordsetSize, double[][] probabilities, double[][][] expectedVectors,
            String expectedCreatorName, double gamma) {
        super(new RatioConfirmationMeasure(), wordsetSize, probabilities, expectedVectors, expectedCreatorName, gamma);
    }
}
