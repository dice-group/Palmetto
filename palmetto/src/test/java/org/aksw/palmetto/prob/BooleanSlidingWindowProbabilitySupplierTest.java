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
package org.aksw.palmetto.prob;

import java.util.Arrays;
import java.util.Collection;

import org.aksw.palmetto.corpus.WindowSupportingAdapter;
import org.aksw.palmetto.data.SegmentationDefinition;
import org.aksw.palmetto.prob.window.BooleanSlidingWindowFrequencyDeterminer;
import org.aksw.palmetto.prob.window.WindowBasedProbabilityEstimator;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.carrotsearch.hppc.IntArrayList;
import com.carrotsearch.hppc.IntIntOpenHashMap;
import com.carrotsearch.hppc.IntObjectOpenHashMap;

@RunWith(Parameterized.class)
public class BooleanSlidingWindowProbabilitySupplierTest implements WindowSupportingAdapter {

    private static final double DOUBLE_PRECISION_DELTA = 0.00000001;

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays
                .asList(new Object[][] {
                        /*
                         * Lets start with a simple test document
                         * A B C B A C C
                         */
                        // We ask for A and B with a window size of 3
                        { 7, new int[][] { { 0, 4 }, { 1, 3 }, {} }, 3,
                                new double[] { 0, 4.0 / 5.0, 4.0 / 5.0, 3.0 / 5.0, 0, 0, 0, 0 } },
                        // We ask for A and C with a window size of 3
                        { 7, new int[][] { { 0, 4 }, {}, { 2, 5, 6 } }, 3,
                                new double[] { 0, 4.0 / 5.0, 0, 0, 1.0, 4.0 / 5.0, 0, 0 } },
                        // We ask for B and C with a window size of 3
                        { 7, new int[][] { {}, { 1, 3 }, { 2, 5, 6 } }, 3,
                                new double[] { 0, 0, 4.0 / 5.0, 0, 1.0, 0, 4.0 / 5.0, 0 } },
                        // We ask for A, B and C with a window size of 3
                        {
                                7,
                                new int[][] { { 0, 4 }, { 1, 3 }, { 2, 5, 6 } },
                                3,
                                new double[] { 0, 4.0 / 5.0, 4.0 / 5.0, 3.0 / 5.0, 1.0, 4.0 / 5.0, 4.0 / 5.0,
                                        3.0 / 5.0 } },
                        // We ask for A and B with a window size of 4
                        { 7, new int[][] { { 0, 4 }, { 1, 3 }, {} }, 4,
                                new double[] { 0, 1.0, 1.0, 1.0, 0, 0, 0, 0 } },
                        // We ask for A and C with a window size of 4
                        { 7, new int[][] { { 0, 4 }, {}, { 2, 5, 6 } }, 4,
                                new double[] { 0, 1.0, 0, 0, 1.0, 1.0, 0, 0 } },
                        // We ask for B and C with a window size of 4
                        { 7, new int[][] { {}, { 1, 3 }, { 2, 5, 6 } }, 4,
                                new double[] { 0, 0, 1.0, 0, 1.0, 0, 1.0, 0 } },
                        // We ask for A, B and C with a window size of 4
                        {
                                7,
                                new int[][] { { 0, 4 }, { 1, 3 }, { 2, 5, 6 } },
                                4,
                                new double[] { 0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0 } } });
    }

    private int histogram[][];
    private int docLength;
    private int positions[][];
    private int windowSize;
    private double expectedProbabilities[];

    public BooleanSlidingWindowProbabilitySupplierTest(int docLength, int[][] positions, int windowSize,
            double expectedProbabilities[]) {
        this.docLength = docLength;
        this.histogram = new int[][] { { docLength, 1 } };
        this.positions = positions;
        this.windowSize = windowSize;
        this.expectedProbabilities = expectedProbabilities;
    }

    @Test
    public void test() {
        BooleanSlidingWindowFrequencyDeterminer determiner = new BooleanSlidingWindowFrequencyDeterminer(this,
                windowSize);
        WindowBasedProbabilityEstimator supplier = new WindowBasedProbabilityEstimator(determiner);
        supplier.setMinFrequency(1);

        double probabilities[] = supplier.getProbabilities(new String[][] { { "A", "B", "C" } },
                new SegmentationDefinition[] { new SegmentationDefinition(
                        new int[0], new int[0][0], null) })[0].probabilities;
        Assert.assertArrayEquals(expectedProbabilities, probabilities, DOUBLE_PRECISION_DELTA);
    }

    @Override
    public int[][] getDocumentSizeHistogram() {
        return histogram;
    }

    @Override
    public IntObjectOpenHashMap<IntArrayList[]> requestWordPositionsInDocuments(String[] words,
            IntIntOpenHashMap docLengths) {
        IntObjectOpenHashMap<IntArrayList[]> positionsInDocuments = new IntObjectOpenHashMap<IntArrayList[]>();
        IntArrayList[] positionsInDocument = new IntArrayList[positions.length];
        for (int i = 0; i < positionsInDocument.length; ++i) {
            if (positions[i].length > 0) {
                positionsInDocument[i] = new IntArrayList();
                positionsInDocument[i].add(positions[i]);
            }
        }
        positionsInDocuments.put(0, positionsInDocument);
        docLengths.put(0, docLength);
        return positionsInDocuments;
    }
    
    @Override
    public void close() {
    }
}
