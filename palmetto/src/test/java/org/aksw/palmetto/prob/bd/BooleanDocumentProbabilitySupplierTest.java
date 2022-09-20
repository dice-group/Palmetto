/**
 * Palmetto - Palmetto is a quality measure tool for topics.
 * Copyright © 2014 Data Science Group (DICE) (michael.roeder@uni-paderborn.de)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.aksw.palmetto.prob.bd;

import java.util.Arrays;
import java.util.Collection;

import org.aksw.palmetto.data.SegmentationDefinition;
import org.aksw.palmetto.data.SubsetProbabilities;
import org.aksw.palmetto.prob.AbstractBooleanDocumentSupportingAdapterBasedTest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.carrotsearch.hppc.BitSet;

@RunWith(Parameterized.class)
public class BooleanDocumentProbabilitySupplierTest extends AbstractBooleanDocumentSupportingAdapterBasedTest {
    private int minFrequency;
    private double expectedProbabilities[];

    private static final double DOUBLE_PRECISION_DELTA = 0.00000001;

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                /*
                 * word0 1 1 1
                 * 
                 * word1 0 1 1
                 * 
                 * word2 0 0 1
                 */
                { new int[][] { { 0, 1, 2 }, { 1, 2 }, { 2 } }, 3, 1,
                        new double[] { 0, 1.0, 2.0 / 3.0, 2.0 / 3.0, 1.0 / 3.0, 1.0 / 3.0, 1.0 / 3.0, 1.0 / 3.0 } },
                /*
                 * word0 1 1 1
                 * 
                 * word1 0 1 1
                 * 
                 * word2 0 0 1
                 * 
                 * with min freq = 2
                 */
                { new int[][] { { 0, 1, 2 }, { 1, 2 }, { 2 } }, 3, 2,
                        new double[] { 0, 1.0, 2.0 / 3.0, 2.0 / 3.0, 0, 0, 0, 0 } },

                /*
                 * word1 0 0 0 1
                 * 
                 * word2 0 1 0 1
                 * 
                 * word3 0 0 1 1
                 * 
                 * C_d,anyany= 1/12 * ( (P(w_1|w_2)-P(w_1)) +
                 * (P(w_1|w_3)-P(w_1)) + (P(w_1|w_2,w_3)-P(w_1)) +
                 * (P(w_2|w_1)-P(w_2)) + (P(w_2|w_3)-P(w_2)) +
                 * (P(w_2|w_1,w_3)-P(w_2)) + (P(w_3|w_1)-P(w_3)) +
                 * (P(w_3|w_2)-P(w_3)) + (P(w_3|w_1,w_2)-P(w_3)) +
                 * (P(w_1,w_2|w_3)-P(w_1,w_2)) + (P(w_1,w_3|w_2)-P(w_1,w_3)) +
                 * (P(w_2,w_3|w_1)-P(w_2,w_3))) = 1/12 * ((1 - 0.25) + (0.5 -
                 * 0.25) + (0.5 - 0.25) + (1 - 0.5) + (1 - 0.5) + (0.5 - 0.5) +
                 * (1 - 0.5) + (1 - 0.5) + (0.5 - 0.5) + (0.5 - 0.25) + (0.5 -
                 * 0.25) + (1 - 0.25)) = 1/12 * (0.75 + 0.25 + 0.25 + 0.5 + 0.5
                 * + 0 + 0.5 + 0.5 + 0 + 0.25 + 0.25 + 0.75) = 4.5/12
                 */
                { new int[][] { { 3 }, { 1, 3 }, { 2, 3 } }, 4, 1,
                        new double[] { 0, 0.25, 0.5, 0.25, 0.5, 0.25, 0.25, 0.25 } } });
    }

    public BooleanDocumentProbabilitySupplierTest(int[][] wordDocuments, int numberOfDocuments, int minFrequency,
            double[] expectedProbabilities) {
        super(wordDocuments, numberOfDocuments);
        this.minFrequency = minFrequency;
        this.expectedProbabilities = expectedProbabilities;
    }

    @Test
    public void test() {
        String words[] = new String[wordDocuments.length];
        for (int i = 0; i < words.length; i++) {
            words[i] = Integer.toString(i);
        }

        BooleanDocumentProbabilitySupplier probSupplier = BooleanDocumentProbabilitySupplier.create(this);
        probSupplier.setMinFrequency(minFrequency);
        SubsetProbabilities subsetProbs[] = probSupplier
                .getProbabilities(new String[][] { words }, new SegmentationDefinition[] { new SegmentationDefinition(null, null,
                        new BitSet(expectedProbabilities.length - 1)) });

        double probabilities[] = subsetProbs[0].probabilities;
        Assert.assertArrayEquals(expectedProbabilities, probabilities, DOUBLE_PRECISION_DELTA);
    }
}
