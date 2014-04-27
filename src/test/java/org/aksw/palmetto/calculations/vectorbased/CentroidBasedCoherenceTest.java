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
import org.aksw.palmetto.subsets.OneOne;
import org.aksw.palmetto.subsets.SubsetCreator;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class CentroidBasedCoherenceTest extends AbstractVectorBasedCoherenceTest {

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
                 * centroid 7/3 2 2
                 * 
                 * cos(1,c)=(7/3+4/3+4/3)/(sqrt(17/9)*sqrt(49/9+4+4))
                 * 
                 * cos(2,c)=(14/9+4/3+4/3)/(sqrt(4/3)*sqrt(49/9+4+4))
                 * 
                 * cos(3,c)=(14/9+4/3+4/3)/(sqrt(4/3)*sqrt(49/9+4+4))
                 * 
                 * m_cen=1/3*((15/3)/(sqrt(17/9)*sqrt(121/9)) +
                 * 2*(38/9)/(sqrt(4/3)*sqrt(121/9)))
                 */
                {
                        new OneOne(),
                        3,
                        new double[] { 0, 1.0, 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0 },
                        new double[][] { { 1.0, 2.0 / 3.0, 2.0 / 3.0 }, { 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0 },
                                { 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0 } },
                        ((5.0 / (Math.sqrt(17.0 / 9.0) * Math.sqrt(121.0 / 9.0))) + ((76.0 / 9.0) / (Math
                                .sqrt(4.0 / 3.0) * Math.sqrt(121.0 / 9.0)))) / 3.0 },

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
                 * centroid 4/3 4/3 4/3
                 * 
                 * cos(1,c)=(8/9+4/9+4/9)/(sqrt(6/9)*sqrt(48/9))
                 * 
                 * cos(2,c)=(8/9+4/9+4/9)/(sqrt(6/9)*sqrt(48/9))
                 * 
                 * cos(2,c)=(8/9+4/9+4/9)/(sqrt(6/9)*sqrt(48/9))
                 * 
                 * m_cen=(16/9)/(sqrt(6/9)*sqrt(48/9))
                 */{
                        new OneOne(),
                        3,
                        new double[] { 0, 2.0 / 3.0, 2.0 / 3.0, 1.0 / 3.0, 2.0 / 3.0, 1.0 / 3.0, 1.0 / 3.0, 0 },
                        new double[][] { { 2.0 / 3.0, 1.0 / 3.0, 1.0 / 3.0 }, { 1.0 / 3.0, 2.0 / 3.0, 1.0 / 3.0 },
                                { 1.0 / 3.0, 1.0 / 3.0, 2.0 / 3.0 } },
                        (16.0 / 9.0) / (Math.sqrt(6.0 / 9.0) * Math.sqrt(48.0 / 9.0)) },
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
                 * centroid 4/3 4/3 4/3
                 * 
                 * vec+vec 1 1 2/3
                 * 
                 * cos(1,c)=(8/9+4/9+4/9)/(sqrt(6/9)*sqrt(48/9))
                 * 
                 * cos(2,c)=(8/9+4/9+4/9)/(sqrt(6/9)*sqrt(48/9))
                 * 
                 * cos(3,c)=(8/9+4/9+4/9)/(sqrt(6/9)*sqrt(48/9))
                 * 
                 * cos((1+2),c)=(4/3+4/3+8/9)/(sqrt(22/9)*sqrt(48/9))
                 * 
                 * cos((1+3),c)=(4/3+4/3+8/9)/(sqrt(22/9)*sqrt(48/9))
                 * 
                 * cos((2+3),c)=(4/3+4/3+8/9)/(sqrt(22/9)*sqrt(48/9))
                 * 
                 * m_cen=1/6*(3*(16/9)/(sqrt(6/9)*sqrt(48/9)) +
                 * 3*((32/9)/(sqrt(22/9)*sqrt(48/9))))
                 */{
                        new AnyAny(),
                        3,
                        new double[] { 0, 2.0 / 3.0, 2.0 / 3.0, 1.0 / 3.0, 2.0 / 3.0, 1.0 / 3.0, 1.0 / 3.0, 0 },
                        new double[][] { { 2.0 / 3.0, 1.0 / 3.0, 1.0 / 3.0 }, { 1.0 / 3.0, 2.0 / 3.0, 1.0 / 3.0 },
                                { 1.0 / 3.0, 1.0 / 3.0, 2.0 / 3.0 } },
                        (((48.0 / 9.0) / (Math.sqrt(6.0 / 9.0) * Math.sqrt(48.0 / 9.0))) + ((96.0 / 9.0) / (Math
                                .sqrt(22.0 / 9.0) * Math.sqrt(48.0 / 9.0)))) / 6.0 },
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
                 * centroid 3/4 1 1
                 * 
                 * cos(1,c)=(3/16+1/4+1/4)/(sqrt(3/16)*sqrt(41/16))
                 * 
                 * cos(2,c)=(3/16+1/2+1/4)/(sqrt(6/16)*sqrt(41/16))
                 * 
                 * cos(3,c)=(3/16+1/2+1/4)/(sqrt(6/16)*sqrt(41/16))
                 * 
                 * m_cen=1/3*((11/16)/(sqrt(3/16)*sqrt(41/16)) +
                 * 2*(15/16)/(sqrt(6/16)*sqrt(41/16)))
                 */
                {
                        new OneOne(),
                        3,
                        new double[] { 0, 0.25, 0.5, 0.25, 0.5, 0.25, 0.25, 0.25 },
                        new double[][] { { 0.25, 0.25, 0.25 }, { 0.25, 0.5, 0.25 }, { 0.25, 0.25, 0.5 } },
                        ((11.0 / 16.0) / (Math.sqrt(3.0 / 16.0) * Math.sqrt(41.0 / 16.0)) + (30.0 / 16.0)
                                / (Math.sqrt(6.0 / 16.0) * Math.sqrt(41.0 / 16.0))) / 3.0 } });
    }

    public CentroidBasedCoherenceTest(SubsetCreator creator, int wordsetSize, double[] probabilities,
            double[][] vectors, double expectedResult) {
        super(new CentroidBasedCalculation(), creator, wordsetSize, probabilities, vectors, expectedResult);
    }

}
