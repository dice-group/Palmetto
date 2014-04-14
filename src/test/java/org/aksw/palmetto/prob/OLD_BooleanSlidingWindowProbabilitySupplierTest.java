package org.aksw.palmetto.prob;

import java.util.Arrays;
import java.util.Collection;

import org.aksw.palmetto.corpus.SlidingWindowSupportingAdapter;
import org.aksw.palmetto.subsets.SubsetDefinition;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.carrotsearch.hppc.IntArrayList;
import com.carrotsearch.hppc.IntIntOpenHashMap;
import com.carrotsearch.hppc.IntObjectOpenHashMap;

@RunWith(Parameterized.class)
public class OLD_BooleanSlidingWindowProbabilitySupplierTest implements SlidingWindowSupportingAdapter {

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
                                new double[] { 0, 2.0 / 7.0, 2.0 / 7.0, 2.0 / 11.0, 0, 0, 0, 0 } },
                        // We ask for A and C with a window size of 3
                        { 7, new int[][] { { 0, 4 }, {}, { 2, 5, 6 } }, 3,
                                new double[] { 0, 2.0 / 7.0, 0, 0, 3.0 / 7.0, 4.0 / 11.0, 0, 0 } },
                        // We ask for B and C with a window size of 3
                        { 7, new int[][] { {}, { 1, 3 }, { 2, 5, 6 } }, 3,
                                new double[] { 0, 0, 2.0 / 7.0, 0, 3.0 / 7.0, 0, 3.0 / 11.0, 0 } },
                        // We ask for A, B and C with a window size of 3
                        {
                                7,
                                new int[][] { { 0, 4 }, { 1, 3 }, { 2, 5, 6 } },
                                3,
                                new double[] { 0, 2.0 / 7.0, 2.0 / 7.0, 2.0 / 11.0, 3.0 / 7.0, 4.0 / 11.0, 3.0 / 11.0,
                                        3.0 / 5.0 } },
                        // We ask for A and B with a window size of 4
                        { 7, new int[][] { { 0, 4 }, { 1, 3 }, {} }, 4,
                                new double[] { 0, 2.0 / 7.0, 2.0 / 7.0, 4.0 / 15.0, 0, 0, 0, 0 } },
                        // We ask for A and C with a window size of 4
                        { 7, new int[][] { { 0, 4 }, {}, { 2, 5, 6 } }, 4,
                                new double[] { 0, 2.0 / 7.0, 0, 0, 3.0 / 7.0, 4.0 / 15.0, 0, 0 } },
                        // We ask for B and C with a window size of 4
                        { 7, new int[][] { {}, { 1, 3 }, { 2, 5, 6 } }, 4,
                                new double[] { 0, 0, 2.0 / 7.0, 0, 3.0 / 7.0, 0, 4.0 / 15.0, 0 } },
                        // We ask for A, B and C with a window size of 4
                        {
                                7,
                                new int[][] { { 0, 4 }, { 1, 3 }, { 2, 5, 6 } },
                                4,
                                new double[] { 0, 2.0 / 7.0, 2.0 / 7.0, 4.0 / 15.0, 3.0 / 7.0, 4.0 / 15.0, 4.0 / 15.0,
                                        6.0 / 13.0 } } });
    }

    private int histogram[][];
    private int docLength;
    private int positions[][];
    private int windowSize;
    private double expectedProbabilities[];

    public OLD_BooleanSlidingWindowProbabilitySupplierTest(int docLength, int[][] positions, int windowSize,
            double expectedProbabilities[]) {
        this.docLength = docLength;
        this.histogram = new int[][] { { docLength, 1 } };
        this.positions = positions;
        this.windowSize = windowSize;
        this.expectedProbabilities = expectedProbabilities;
    }

    @Test
    public void test() {
        OLD_BooleanSlidingWindowFrequencyDeterminer determiner = new OLD_BooleanSlidingWindowFrequencyDeterminer(this,
                windowSize);
        SlidingWindowProbabilitySupplier supplier = new SlidingWindowProbabilitySupplier(determiner);
        supplier.setMinFrequency(1);

        double probabilities[] = supplier.getProbabilities(new String[][] { { "A", "B", "C" } },
                new SubsetDefinition[] { new SubsetDefinition(
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
