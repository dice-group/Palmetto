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
import com.carrotsearch.hppc.IntObjectOpenHashMap;

@RunWith(Parameterized.class)
public class BooleanSlidingWindowFrequencyDeterminerCountingTest implements SlidingWindowSupportingAdapter {

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays
                .asList(new Object[][] {
                        /*
                         * Lets start with a simple test document
                         * A B C B A C C
                         */
                        // We ask for A and B with a window size of 3
                        { new int[][] { { 7, 1 } }, new int[][] { { 0, 4 }, { 1, 3 }, {} }, 3,
                                new int[] { 0, 2, 2, 2, 0, 0, 0, 0 } },
                        // We ask for A and C with a window size of 3
                        { new int[][] { { 7, 1 } }, new int[][] { { 0, 4 }, {}, { 2, 5, 6 } }, 3,
                                new int[] { 0, 2, 0, 0, 3, 4, 0, 0 } },
                        // We ask for B and C with a window size of 3
                        { new int[][] { { 7, 1 } }, new int[][] { {}, { 1, 3 }, { 2, 5, 6 } }, 3,
                                new int[] { 0, 0, 2, 0, 3, 0, 3, 0 } },
                        // We ask for A, B and C with a window size of 3
                        { new int[][] { { 7, 1 } }, new int[][] { { 0, 4 }, { 1, 3 }, { 2, 5, 6 } }, 3,
                                new int[] { 0, 2, 2, 2, 3, 4, 3, 3 } },
                        // We ask for A and B with a window size of 4
                        { new int[][] { { 7, 1 } }, new int[][] { { 0, 4 }, { 1, 3 }, {} }, 4,
                                new int[] { 0, 2, 2, 4, 0, 0, 0, 0 } },
                        // We ask for A and C with a window size of 4
                        { new int[][] { { 7, 1 } }, new int[][] { { 0, 4 }, {}, { 2, 5, 6 } }, 4,
                                new int[] { 0, 2, 0, 0, 3, 4, 0, 0 } },
                        // We ask for B and C with a window size of 4
                        { new int[][] { { 7, 1 } }, new int[][] { {}, { 1, 3 }, { 2, 5, 6 } }, 4,
                                new int[] { 0, 0, 2, 0, 3, 0, 4, 0 } },
                        // We ask for A, B and C with a window size of 3
                        { new int[][] { { 7, 1 } }, new int[][] { { 0, 4 }, { 1, 3 }, { 2, 5, 6 } }, 4,
                                new int[] { 0, 2, 2, 4, 3, 4, 4, 6 } } });
    }

    private int histogram[][];
    private int positions[][];
    private int windowSize;
    private int expectedCounts[];

    public BooleanSlidingWindowFrequencyDeterminerCountingTest(int[][] histogram, int[][] positions, int windowSize,
            int expectedCounts[]) {
        this.histogram = histogram;
        this.positions = positions;
        this.windowSize = windowSize;
        this.expectedCounts = expectedCounts;
    }

    @Test
    public void test() {
        BooleanSlidingWindowFrequencyDeterminer determiner = new BooleanSlidingWindowFrequencyDeterminer(this,
                windowSize);
        IntArrayList lists[] = new IntArrayList[positions.length];
        for (int i = 0; i < lists.length; ++i) {
            lists[i] = new IntArrayList(positions[i].length);
            lists[i].add(positions[i]);
        }
        int counts[] = determiner.determineCounts(new String[][] { { "A", "B", "C" } },
                new SubsetDefinition[] { new SubsetDefinition(
                        new int[0], new int[0][0], null) })[0].counts;
        Assert.assertArrayEquals(expectedCounts, counts);
    }

    @Override
    public int[][] getDocumentSizeHistogram() {
        return histogram;
    }

    @Override
    public IntObjectOpenHashMap<IntArrayList[]> requestWordPositionsInDocuments(String[] words) {
        IntObjectOpenHashMap<IntArrayList[]> positionsInDocuments = new IntObjectOpenHashMap<IntArrayList[]>();
        IntArrayList[] positionsInDocument = new IntArrayList[positions.length];
        for (int i = 0; i < positionsInDocument.length; ++i) {
            if (positions[i].length > 0) {
                positionsInDocument[i] = new IntArrayList();
                positionsInDocument[i].add(positions[i]);
            }
        }
        positionsInDocuments.put(0, positionsInDocument);
        return positionsInDocuments;
    }
}
