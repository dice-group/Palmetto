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
}
