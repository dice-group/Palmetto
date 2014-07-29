/**
 * Copyright (C) 2014 Michael Röder (michael.roeder@unister.de)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.aksw.palmetto.calculations.indirect;

import java.util.Arrays;
import java.util.Collection;

import org.aksw.palmetto.calculations.indirect.JaccardConfirmationMeasure;
import org.aksw.palmetto.subsets.OneAny;
import org.aksw.palmetto.subsets.OneOne;
import org.aksw.palmetto.subsets.Segmentator;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class JaccardBasedCoherenceTest extends AbstractVectorBasedCoherenceTest {

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
                 * jaccard(1,2)=6/3/7/3
                 * 
                 * jaccard(1,3)=6/3/7/3
                 * 
                 * jaccard(2,3)=1
                 * 
                 * m_jaccard=1/3*(2*12/13 + 1)
                 */
                {
                        new OneOne(),
                        3,
                        new double[] { 0, 1.0, 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0 },
                        new double[][] { { 1.0, 2.0 / 3.0, 2.0 / 3.0 }, { 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0 },
                                { 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0 } }, (12.0 / 7.0 + 1) / 3.0 },

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
                 * jaccard(1,2)=1/5/3=3/5
                 * 
                 * jaccard(1,3)=1/5/3=3/5
                 * 
                 * jaccard(2,3)=1/5/3=3/5
                 * 
                 * m_jaccard=3/5
                 */{
                        new OneOne(),
                        3,
                        new double[] { 0, 2.0 / 3.0, 2.0 / 3.0, 1.0 / 3.0, 2.0 / 3.0, 1.0 / 3.0, 1.0 / 3.0, 0 },
                        new double[][] { { 2.0 / 3.0, 1.0 / 3.0, 1.0 / 3.0 }, { 1.0 / 3.0, 2.0 / 3.0, 1.0 / 3.0 },
                                { 1.0 / 3.0, 1.0 / 3.0, 2.0 / 3.0 } }, 3.0 / 5.0 },
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
                 * jaccard(1,2)=1/5/3=3/5
                 * 
                 * jaccard(1,3)=1/5/3=3/5
                 * 
                 * jaccard(2,3)=1/5/3=3/5
                 * 
                 * jaccard(1,(2+3))=(4/3)/(8/3)=1/2
                 * 
                 * jaccard(2,(1+3))=(4/3)/(8/3)=1/2
                 * 
                 * jaccard(3,(1+2))=(4/3)/(8/3)=1/2
                 * 
                 * m_jaccard=1/9*(6*3/5 + 3*1/2)
                 */{
                        new OneAny(),
                        3,
                        new double[] { 0, 2.0 / 3.0, 2.0 / 3.0, 1.0 / 3.0, 2.0 / 3.0, 1.0 / 3.0, 1.0 / 3.0, 0 },
                        new double[][] { { 2.0 / 3.0, 1.0 / 3.0, 1.0 / 3.0 }, { 1.0 / 3.0, 2.0 / 3.0, 1.0 / 3.0 },
                                { 1.0 / 3.0, 1.0 / 3.0, 2.0 / 3.0 } }, (18.0 / 5.0 + 1.5) / 9.0 },
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
                 * jaccard(1,2)=(3/4)/1=3/4
                 * 
                 * jaccard(1,3)=(3/4)/1=3/4
                 * 
                 * jaccard(2,3)=(3/4)/5/4=3/5
                 * 
                 * m_jaccard=1/3*(1.5 + 0.6)
                 */
                { new OneOne(), 3, new double[] { 0, 0.25, 0.5, 0.25, 0.5, 0.25, 0.25, 0.25 },
                        new double[][] { { 0.25, 0.25, 0.25 }, { 0.25, 0.5, 0.25 }, { 0.25, 0.25, 0.5 } },
                        0.7 } });
    }

    public JaccardBasedCoherenceTest(Segmentator creator, int wordsetSize, double[] probabilities,
            double[][] vectors, double expectedResult) {
        super(new JaccardConfirmationMeasure(), creator, wordsetSize, probabilities, vectors, expectedResult);
    }

}
