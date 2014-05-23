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

import org.aksw.palmetto.subsets.AnyAny;
import org.aksw.palmetto.subsets.OneAll;
import org.aksw.palmetto.subsets.OneAny;
import org.aksw.palmetto.subsets.OneOne;
import org.aksw.palmetto.subsets.SubsetCreator;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class CosinusBasedCoherenceTest extends AbstractVectorBasedCoherenceTest {

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
                 * cos(1,2)=(14/9)/(sqrt(17/9)*sqrt(4/3))
                 * 
                 * cos(1,3)=(14/9)/(sqrt(17/9)*sqrt(4/3))
                 * 
                 * cos(2,3)=(4/3)/(sqrt(4/3)*sqrt(4/3))=1
                 * 
                 * m_cos=1/3*(2*(14/9)/(sqrt(17/9)*sqrt(4/3)) + 1)
                 */
                {
                        new OneOne(),
                        3,
                        new double[] { 0, 1.0, 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0 },
                        new double[][] { { 1.0, 2.0 / 3.0, 2.0 / 3.0 }, { 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0 },
                        { 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0 } },
                        (2 * (14.0 / 9.0) / (Math.sqrt(17.0 / 9.0) * Math.sqrt(4.0 / 3.0)) + 1) / 3.0 },

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
                 * cos(1,2)=(5/9)/(sqrt(6/9)*sqrt(6/9))
                 * 
                 * cos(1,3)=(5/9)/(sqrt(6/9)*sqrt(6/9))
                 * 
                 * cos(2,3)=(5/9)/(sqrt(6/9)*sqrt(6/9))
                 * 
                 * m_cos=5/6
                 */{
                        new OneOne(),
                        3,
                        new double[] { 0, 2.0 / 3.0, 2.0 / 3.0, 1.0 / 3.0, 2.0 / 3.0, 1.0 / 3.0, 1.0 / 3.0, 0 },
                        new double[][] { { 2.0 / 3.0, 1.0 / 3.0, 1.0 / 3.0 }, { 1.0 / 3.0, 2.0 / 3.0, 1.0 / 3.0 },
                        { 1.0 / 3.0, 1.0 / 3.0, 2.0 / 3.0 } }, 5.0 / 6.0 },
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
                 * cos(1,(2 + 3))=(10/9)/(sqrt(6/9)*sqrt(22/9))
                 * 
                 * cos(2,(1 + 3))=(10/9)/(sqrt(6/9)*sqrt(22/9))
                 * 
                 * cos(3,(1 + 2))=(10/9)/(sqrt(6/9)*sqrt(22/9))
                 * 
                 * m_cos=3*(11/9)/(3*sqrt(6/9)*sqrt(22/9))
                 */{
                        new OneAll(),
                        3,
                        new double[] { 0, 2.0 / 3.0, 2.0 / 3.0, 1.0 / 3.0, 2.0 / 3.0, 1.0 / 3.0, 1.0 / 3.0, 0 },
                        new double[][] { { 2.0 / 3.0, 1.0 / 3.0, 1.0 / 3.0 }, { 1.0 / 3.0, 2.0 / 3.0, 1.0 / 3.0 },
                        { 1.0 / 3.0, 1.0 / 3.0, 2.0 / 3.0 } },
                        (10.0 / 9.0) / (Math.sqrt(2.0 / 3.0) * Math.sqrt(22.0 / 9.0)) },
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
                 * cos(1,2)=(5/9)/(sqrt(6/9)*sqrt(6/9))
                 * 
                 * cos(1,3)=(5/9)/(sqrt(6/9)*sqrt(6/9))
                 * 
                 * cos(2,3)=(5/9)/(sqrt(6/9)*sqrt(6/9))
                 * 
                 * cos(1,(2 + 3))=(10/9)/(sqrt(6/9)*sqrt(22/9))
                 * 
                 * cos(2,(1 + 3))=(10/9)/(sqrt(6/9)*sqrt(22/9))
                 * 
                 * cos(3,(1 + 2))=(10/9)/(sqrt(6/9)*sqrt(22/9))
                 * 
                 * m_cos=((6*5/6) + 6*(11/9)/(sqrt(6/9)*sqrt(22/9)))/12
                 */{
                        new AnyAny(),
                        3,
                        new double[] { 0, 2.0 / 3.0, 2.0 / 3.0, 1.0 / 3.0, 2.0 / 3.0, 1.0 / 3.0, 1.0 / 3.0, 0 },
                        new double[][] { { 2.0 / 3.0, 1.0 / 3.0, 1.0 / 3.0 }, { 1.0 / 3.0, 2.0 / 3.0, 1.0 / 3.0 },
                        { 1.0 / 3.0, 1.0 / 3.0, 2.0 / 3.0 } },
                        (5.0 + 6.0 * (10.0 / 9.0) / (Math.sqrt(2.0 / 3.0) * Math.sqrt(22.0 / 9.0))) / 12.0 },
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
                 * cos(1,2)=(5/9)/(sqrt(6/9)*sqrt(6/9))
                 * 
                 * cos(1,3)=(5/9)/(sqrt(6/9)*sqrt(6/9))
                 * 
                 * cos(2,3)=(5/9)/(sqrt(6/9)*sqrt(6/9))
                 * 
                 * cos(1,(2+3))=(10/9)/(sqrt(6/9)*sqrt(22/9))
                 * 
                 * cos(2,(1+3))=(10/9)/(sqrt(6/9)*sqrt(22/9))
                 * 
                 * cos(3,(1+2))=(10/9)/(sqrt(6/9)*sqrt(22/9))
                 * 
                 * m_cos=1/9*(6*4/5 + 3*((10/9)/(sqrt(6/9)*sqrt(22/9))))
                 */{
                        new OneAny(),
                        3,
                        new double[] { 0, 2.0 / 3.0, 2.0 / 3.0, 1.0 / 3.0, 2.0 / 3.0, 1.0 / 3.0, 1.0 / 3.0, 0 },
                        new double[][] { { 2.0 / 3.0, 1.0 / 3.0, 1.0 / 3.0 }, { 1.0 / 3.0, 2.0 / 3.0, 1.0 / 3.0 },
                        { 1.0 / 3.0, 1.0 / 3.0, 2.0 / 3.0 } },
                        (5.0 + 3.0 * ((10.0 / 9.0) / (Math.sqrt(6.0 / 9.0) * Math.sqrt(22.0 / 9.0)))) / 9.0 },
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
                 * cos(1,2)=(1/4)/(sqrt(3/16)*sqrt(6/16))
                 * 
                 * cos(1,3)=(1/4)/(sqrt(3/16)*sqrt(6/16))
                 * 
                 * cos(2,3)=(5/16)/(sqrt(6/16)*sqrt(6/16))
                 * 
                 * m_cos=1/3*(0.5/(sqrt(3/16)*sqrt(6/16)) + 5/6)
                 */
                { new OneOne(), 3, new double[] { 0, 0.25, 0.5, 0.25, 0.5, 0.25, 0.25, 0.25 },
                        new double[][] { { 0.25, 0.25, 0.25 }, { 0.25, 0.5, 0.25 }, { 0.25, 0.25, 0.5 } },
                        (0.5 / (Math.sqrt(3.0 / 16.0) * Math.sqrt(6.0 / 16.0)) + 5.0 / 6.0) / 3.0 } });
    }

    public CosinusBasedCoherenceTest(SubsetCreator creator, int wordsetSize, double[] probabilities,
            double[][] vectors, double expectedResult) {
        super(new CosinusBasedCalculation(), creator, wordsetSize, probabilities, vectors, expectedResult);
    }

}
