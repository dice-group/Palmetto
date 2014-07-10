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
package org.aksw.palmetto.calculations.vectorbased;

import java.util.Arrays;
import java.util.Collection;

import org.aksw.palmetto.subsets.OneAny;
import org.aksw.palmetto.subsets.OneOne;
import org.aksw.palmetto.subsets.SegmentationScheme;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class DiceBasedCoherenceTest extends AbstractVectorBasedCoherenceTest {

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
                 * vector1 1.0 2/3 2/3
                 * 
                 * vector2 2/3 2/3 2/3
                 * 
                 * vector2 2/3 2/3 2/3
                 * 
                 * dice(1,2)=2*2/(13/3)=12/13
                 * 
                 * dice(1,3)=2*2/(13/3)=12/13
                 * 
                 * dice(2,3)=1
                 * 
                 * m_dice=1/3*(2*12/13 + 1)
                 */
                {
                        new OneOne(),
                        3,
                        new double[] { 0, 1.0, 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0 },
                        new double[][] { { 1.0, 2.0 / 3.0, 2.0 / 3.0 }, { 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0 },
                                { 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0 } }, (24.0 / 13.0 + 1) / 3.0 },

                /*
                 * word1 0 1 1
                 * 
                 * word2 1 0 1
                 * 
                 * word3 1 1 0
                 * 
                 * vector1 2/3 1/3 1/3
                 * 
                 * vector2 1/3 2/3 1/3
                 * 
                 * vector2 1/3 1/3 2/3
                 * 
                 * dice(1,2)=2*1/(4/3+4/3)=6/8
                 * 
                 * dice(1,3)=2*1/(4/3+4/3)=6/8
                 * 
                 * dice(2,3)=2*1/(4/3+4/3)=6/8
                 * 
                 * m_dice=6/8
                 */{
                        new OneOne(),
                        3,
                        new double[] { 0, 2.0 / 3.0, 2.0 / 3.0, 1.0 / 3.0, 2.0 / 3.0, 1.0 / 3.0, 1.0 / 3.0, 0 },
                        new double[][] { { 2.0 / 3.0, 1.0 / 3.0, 1.0 / 3.0 }, { 1.0 / 3.0, 2.0 / 3.0, 1.0 / 3.0 },
                                { 1.0 / 3.0, 1.0 / 3.0, 2.0 / 3.0 } }, 6.0 / 8.0 },
                /*
                 * word1 0 1 1
                 * 
                 * word2 1 0 1
                 * 
                 * word3 1 1 0
                 * 
                 * vector1 2/3 1/3 1/3
                 * 
                 * vector2 1/3 2/3 1/3
                 * 
                 * vector2 1/3 1/3 2/3
                 * 
                 * dice(1,2)=2*1/(4/3+4/3)=6/8
                 * 
                 * dice(1,3)=2*1/(4/3+4/3)=6/8
                 * 
                 * dice(2,3)=2*1/(4/3+4/3)=6/8
                 * 
                 * dice(1,(2+3))=(8/3)/(4/3+8/3)=8/12
                 * 
                 * dice(2,(1+3))=(8/3)/(4/3+8/3)=8/12
                 * 
                 * dice(3,(1+2))=(8/3)/(4/3+8/3)=8/12
                 * 
                 * m_dice=1/9*(6*6/8 + 3*8/12)
                 */{
                        new OneAny(),
                        3,
                        new double[] { 0, 2.0 / 3.0, 2.0 / 3.0, 1.0 / 3.0, 2.0 / 3.0, 1.0 / 3.0, 1.0 / 3.0, 0 },
                        new double[][] { { 2.0 / 3.0, 1.0 / 3.0, 1.0 / 3.0 }, { 1.0 / 3.0, 2.0 / 3.0, 1.0 / 3.0 },
                                { 1.0 / 3.0, 1.0 / 3.0, 2.0 / 3.0 } }, (36.0 / 8.0 + 2.0) / 9.0 },
                /*
                 * word1 0 0 0 1
                 * 
                 * word2 0 1 0 1
                 * 
                 * word3 0 0 1 1
                 * 
                 * vector1 1/4 1/4 1/4
                 * 
                 * vector2 1/4 1/2 1/4
                 * 
                 * vector2 1/4 1/4 1/2
                 * 
                 * dice(1,2)=2*(3/4)/(3/4+1)=6/7
                 * 
                 * dice(1,3)=2*(3/4)/(3/4+1)=6/7
                 * 
                 * dice(2,3)=2*(3/4)/2=3/4
                 * 
                 * m_dice=1/3*(0.5/(sqrt(3/16)*sqrt(6/16)) + 5/6)
                 */
                { new OneOne(), 3, new double[] { 0, 0.25, 0.5, 0.25, 0.5, 0.25, 0.25, 0.25 },
                        new double[][] { { 0.25, 0.25, 0.25 }, { 0.25, 0.5, 0.25 }, { 0.25, 0.25, 0.5 } },
                        (12.0 / 7.0 + 0.75) / 3.0 } });
    }

    public DiceBasedCoherenceTest(SegmentationScheme creator, int wordsetSize, double[] probabilities, double[][] vectors,
            double expectedResult) {
        super(new DiceBasedCalculation(), creator, wordsetSize, probabilities, vectors, expectedResult);
    }

}
