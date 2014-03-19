package org.aksw.palmetto.calculations;

import java.util.Arrays;
import java.util.Collection;

import org.aksw.palmetto.subsets.OneOne;
import org.aksw.palmetto.subsets.SubsetCreator;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class RatioCoherenceCalculationTest extends AbstractCalculationTest {

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
                 * C_d,oneone= 1/3 * ((P(w_1,w_2)/(P(w_1)*P(w_2))) +
                 * (P(w_1,w_3)/(P(w_1)*P(w_3))) + (P(w_2,w_3)/(P(w_2)*P(w_3))))
                 * = 1/3 * (2/3 / (1 * 2/3)) + (2/3 / (1 * 2/3)) + (2/3 / (2/3 *
                 * 2/3))) = 1/3 * (1 + 1 + 3/2) = 7/6
                 */
                { new OneOne(), 3,
                        new double[] { 0, 1.0, 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0 },
                        7.0 / 6.0 },

                /*
                 * word1 0 1 1
                 * 
                 * word2 1 0 1
                 * 
                 * word3 1 1 0
                 * 
                 * C_d,oneone= 1/3 * ((P(w_1,w_2)/(P(w_1)*P(w_2))) +
                 * (P(w_1,w_3)/(P(w_1)*P(w_3))) + (P(w_2,w_3)/(P(w_2)*P(w_3))))
                 * = 1/3 * ((1/3 / (2/3 * 2/3)) + (1/3 / (2/3 * 2/3)) + (1/3 /
                 * (2/3 * 2/3))) = 3/4
                 */{ new OneOne(), 3,
                        new double[] { 0, 2.0 / 3.0, 2.0 / 3.0, 1.0 / 3.0, 2.0 / 3.0, 1.0 / 3.0, 1.0 / 3.0, 0 },
                        3.0 / 4.0 },
                /*
                 * word1 0 0 0 1
                 * 
                 * word2 0 1 0 1
                 * 
                 * word3 0 0 1 1
                 * 
                 * C_d,oneone= 1/3 * ((P(w_1,w_2)/(P(w_1)*P(w_2))) +
                 * (P(w_1,w_3)/(P(w_1)*P(w_3))) + (P(w_2,w_3)/(P(w_2)*P(w_3))))
                 * = 1/3 * ((1/4 / (1/4 * 1/2)) + (1/4 / (1/4 * 1/2)) + (1/4 /
                 * (1/2 * 1/2))) = 1/3 * (2 + 2 + 1) = 5/3
                 */
                { new OneOne(), 3, new double[] { 0, 0.25, 0.5, 0.25, 0.5, 0.25, 0.25, 0.25 }, 5.0 / 3.0 } });
    }

    public RatioCoherenceCalculationTest(SubsetCreator subsetCreator, int wordsetSize, double[] probabilities,
            double expectedCoherence) {
        super(new RatioCoherenceCalculation(), subsetCreator, wordsetSize, probabilities, expectedCoherence);
    }
}
