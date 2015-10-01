/**
 * This file is part of Palmetto.
 *
 * Palmetto is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Palmetto is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Palmetto.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.aksw.palmetto.calculations.indirect;

import java.util.Arrays;
import java.util.Collection;

import org.aksw.palmetto.calculations.indirect.CosinusConfirmationMeasure;
import org.aksw.palmetto.subsets.AnyAny;
import org.aksw.palmetto.subsets.OneAll;
import org.aksw.palmetto.subsets.OneAny;
import org.aksw.palmetto.subsets.OneOne;
import org.aksw.palmetto.subsets.OneSet;
import org.aksw.palmetto.subsets.Segmentator;
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
                        (0.5 / (Math.sqrt(3.0 / 16.0) * Math.sqrt(6.0 / 16.0)) + 5.0 / 6.0) / 3.0 },
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
                 * vector3 1/4 1/4 1/2
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
                        new OneSet(),
                        3,
                        new double[] { 0, 0.25, 0.5, 0.25, 0.5, 0.25, 0.25, 0.25 },
                        new double[][] { { 0.25, 0.25, 0.25 }, { 0.25, 0.5, 0.25 }, { 0.25, 0.25, 0.5 } },
                        ((11.0 / 16.0) / (Math.sqrt(3.0 / 16.0) * Math.sqrt(41.0 / 16.0)) + (30.0 / 16.0)
                                / (Math.sqrt(6.0 / 16.0) * Math.sqrt(41.0 / 16.0))) / 3.0 } });
    }

    public CosinusBasedCoherenceTest(Segmentator creator, int wordsetSize, double[] probabilities,
            double[][] vectors, double expectedResult) {
        super(new CosinusConfirmationMeasure(), creator, wordsetSize, probabilities, vectors, expectedResult);
    }

}
