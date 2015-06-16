/**
 * The MIT License
 * Copyright (c) 2014 Agile Knowledge Engineering and Semantic Web (AKSW)
							(roeder@informatik.uni-leipzig.de)
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
package org.aksw.palmetto.calculations.direct;

import java.util.Arrays;
import java.util.Collection;

import org.aksw.palmetto.calculations.direct.LogBasedCalculation;
import org.aksw.palmetto.calculations.direct.LogLikelihoodConfirmationMeasure;
import org.aksw.palmetto.subsets.OneOne;
import org.aksw.palmetto.subsets.Segmentator;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class LogLikelihoodCoherenceCalculationTest extends AbstractProbabilityBasedCalculationTest {

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
                 * C_ll,oneone= 1/3 * (log(P(w_1|w_2)/P(w_1|-w_2)) +
                 * log(P(w_1|w_3)/P(w_1|-w_3)) + log(P(w_2|w_1)/P(w_2|-w_1)) +
                 * log(P(w_2|w_3)/P(w_2|-w_3)) + log(P(w_3|w_1)/P(w_3|-w_1)) +
                 * log(P(w_3|w_1)/P(w_3|-w_2))) = 1/6 * (log(1) + log(1) + 0 +
                 * log(1/eps) + 0 + log(1/eps) = log(1/eps) / 3
                 * 
                 * only divided by 2 because the NaN are not used for the mean
                 * calculation
                 */
                { new OneOne(), 3,
                        new double[] { 0, 1.0, 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0 },
                        (Math.log(1.0 / LogBasedCalculation.EPSILON)) / 3.0 },

                /*
                 * word1 0 1 1
                 * 
                 * word2 1 0 1
                 * 
                 * word3 1 1 0
                 * 
                 * C_ll,oneone= 1/3 * (log(P(w_1|w_2)/P(w_1|-w_2)) +
                 * log(P(w_1|w_3)/P(w_1|-w_3)) + log(P(w_2|w_1)/P(w_2|-w_1)) +
                 * log(P(w_2|w_3)/P(w_2|-w_3)) + log(P(w_3|w_1)/P(w_3|-w_1)) +
                 * log(P(w_3|w_1)/P(w_3|-w_2))) = 1/6 * (6*log(0.5)) = log(0.5)
                 */
                { new OneOne(), 3,
                        new double[] { 0, 2.0 / 3.0, 2.0 / 3.0, 1.0 / 3.0, 2.0 / 3.0, 1.0 / 3.0, 1.0 / 3.0, 0 },
                        Math.log(0.5) },
                /*
                 * word1 0 0 0 1
                 * 
                 * word2 0 1 0 1
                 * 
                 * word3 0 0 1 1
                 * 
                 * C_ll,oneone= 1/3 * (log(P(w_1|w_2)/P(w_1|-w_2)) +
                 * log(P(w_1|w_3)/P(w_1|-w_3)) + log(P(w_2|w_1)/P(w_2|-w_1)) +
                 * log(P(w_2|w_3)/P(w_2|-w_3)) + log(P(w_3|w_1)/P(w_3|-w_1)) +
                 * log(P(w_3|w_1)/P(w_3|-w_2))) = 1/6 * (2*log(0.5/eps) +
                 * 2*log(3) + 2*log(1))
                 */
                { new OneOne(), 3, new double[] { 0, 0.25, 0.5, 0.25, 0.5, 0.25, 0.25, 0.25 },
                        (Math.log(0.5 / LogBasedCalculation.EPSILON) + Math.log(3.0)) / 3.0 } });
    }

    public LogLikelihoodCoherenceCalculationTest(Segmentator subsetCreator, int wordsetSize, double[] probabilities,
            double expectedCoherence) {
        super(new LogLikelihoodConfirmationMeasure(), subsetCreator, wordsetSize, probabilities, expectedCoherence);
    }
}
