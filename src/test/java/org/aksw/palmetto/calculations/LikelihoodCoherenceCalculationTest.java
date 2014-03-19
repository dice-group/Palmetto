package org.aksw.palmetto.calculations;

import java.util.Arrays;
import java.util.Collection;

import org.aksw.palmetto.subsets.OneOne;
import org.aksw.palmetto.subsets.SubsetCreator;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class LikelihoodCoherenceCalculationTest extends AbstractCalculationTest {

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
                         * C_l,oneone= 1/3 * ((P(w_1|w_2)/P(w_1|-w_2)) +
                         * (P(w_1|w_3)/P(w_1|-w_3)) + (P(w_2|w_1)/P(w_2|-w_1)) +
                         * (P(w_2|w_3)/P(w_2|-w_3)) + (P(w_3|w_1)/P(w_3|-w_1)) +
                         * (P(w_3|w_1)/P(w_3|-w_2))) = 1/6 * ((1 / 1) + (1 / 1) + (2/3/eps) + (1/eps) + (2/3/eps) +
                         * (1/eps))
                         */
                        {
                                new OneOne(),
                                3,
                                new double[] { 0, 1.0, 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0 },
                                (2 + 2 * ((2.0 / 3.0) / LogBasedCalculation.EPSILON) + 2 * (1.0 / LogBasedCalculation.EPSILON)) / 6.0 },

                        /*
                         * word1 0 1 1
                         * 
                         * word2 1 0 1
                         * 
                         * word3 1 1 0
                         * 
                         * C_l,oneone= 1/3 * ((P(w_1|w_2)/P(w_1|-w_2)) +
                         * (P(w_1|w_3)/P(w_1|-w_3)) + (P(w_2|w_1)/P(w_2|-w_1)) +
                         * (P(w_2|w_3)/P(w_2|-w_3)) + (P(w_3|w_1)/P(w_3|-w_1)) +
                         * (P(w_3|w_1)/P(w_3|-w_2))) = 1/6 * (6*0.5) = 0.5
                         */{
                                new OneOne(),
                                3,
                                new double[] { 0, 2.0 / 3.0, 2.0 / 3.0, 1.0 / 3.0, 2.0 / 3.0, 1.0 / 3.0, 1.0 / 3.0, 0 },
                                0.5 },
                        /*
                         * word1 0 0 0 1
                         * 
                         * word2 0 1 0 1
                         * 
                         * word3 0 0 1 1
                         * 
                         * C_l,oneone= 1/3 * ((P(w_1|w_2)/P(w_1|-w_2)) +
                         * (P(w_1|w_3)/P(w_1|-w_3)) + (P(w_2|w_1)/P(w_2|-w_1)) +
                         * (P(w_2|w_3)/P(w_2|-w_3)) + (P(w_3|w_1)/P(w_3|-w_1)) +
                         * (P(w_3|w_1)/P(w_3|-w_2))) = 1/6 * (0.5/eps + 0.5/eps + 3 + 1 + 3 + 1)
                         * = 4/3
                         */
                        { new OneOne(), 3, new double[] { 0, 0.25, 0.5, 0.25, 0.5, 0.25, 0.25, 0.25 },
                                ((1.0 / LogBasedCalculation.EPSILON) + 8.0) / 6.0 } });
    }

    public LikelihoodCoherenceCalculationTest(SubsetCreator subsetCreator, int wordsetSize, double[] probabilities,
            double expectedCoherence) {
        super(new LikelihoodCoherenceCalculation(), subsetCreator, wordsetSize, probabilities, expectedCoherence);
    }
}
