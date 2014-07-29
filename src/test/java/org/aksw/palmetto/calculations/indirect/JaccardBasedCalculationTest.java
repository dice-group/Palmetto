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
package org.aksw.palmetto.calculations.indirect;

import java.util.Arrays;
import java.util.Collection;

import org.aksw.palmetto.calculations.indirect.JaccardConfirmationMeasure;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class JaccardBasedCalculationTest extends AbstractVectorBasedCalculationTest {

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays
                .asList(new Object[][] {
                        /*
                         * vector1 1.0 2/3 2/3
                         * 
                         * vector2 2/3 2/3 2/3
                         * 
                         * jaccard=6/3/7/3
                         */
                        { new double[] { 1.0, 2.0 / 3.0, 2.0 / 3.0 }, new double[] { 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0 },
                                6.0 / 7.0 },
                        // The same but with switched vectors
                        { new double[] { 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0 }, new double[] { 1.0, 2.0 / 3.0, 2.0 / 3.0 },
                                6.0 / 7.0 },
                        /*
                         * vector1 2/3 2/3 2/3
                         * 
                         * vector2 2/3 2/3 2/3
                         * 
                         * jaccard=2*2/(2+2)=1
                         */
                        { new double[] { 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0 },
                                new double[] { 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0 }, 1.0 },
                        /*
                         * vector1 0 0 0
                         * 
                         * vector2 2/3 2/3 2/3
                         * 
                         * jaccard=0/2=0
                         */
                        { new double[] { 0, 0, 0 }, new double[] { 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0 }, 0 },
                        /*
                         * vector1 0 0 0
                         * 
                         * vector2 0 0 0
                         * 
                         * jaccard=0/0=0
                         * 
                         * but we define this as 1, because vector1 == vector2
                         */
                        { new double[] { 0, 0, 0 }, new double[] { 0, 0, 0 }, 1.0 },
                        /*
                         * vector1 2/3 1/3 1/3
                         * 
                         * vector2 1/3 2/3 1/3
                         * 
                         * jaccard=1/5/3=3/5
                         */
                        { new double[] { 2.0 / 3.0, 1.0 / 3.0, 1.0 / 3.0 },
                                new double[] { 1.0 / 3.0, 2.0 / 3.0, 1.0 / 3.0 }, 3.0 / 5.0 },
                        /*
                         * 
                         * vector1 2/3 1/3 1/3
                         * 
                         * vector2 2/3 1 1
                         * 
                         * jaccard=(4/3)/(8/3)=1/2
                         */
                        { new double[] { 2.0 / 3.0, 1.0 / 3.0, 1.0 / 3.0 }, new double[] { 2.0 / 3.0, 1.0, 1.0 },
                                1.0 / 2.0 } });
    }

    public JaccardBasedCalculationTest(double[] vector1, double[] vector2, double expectedResult) {
        super(new JaccardConfirmationMeasure(), vector1, vector2, expectedResult);
    }

}
