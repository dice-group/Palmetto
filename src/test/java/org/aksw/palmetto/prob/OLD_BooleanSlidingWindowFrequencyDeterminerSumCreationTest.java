package org.aksw.palmetto.prob;

import java.util.Arrays;
import java.util.Collection;

import org.aksw.palmetto.corpus.SlidingWindowSupportingAdapter;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.carrotsearch.hppc.IntArrayList;
import com.carrotsearch.hppc.IntIntOpenHashMap;
import com.carrotsearch.hppc.IntObjectOpenHashMap;

@RunWith(Parameterized.class)
public class OLD_BooleanSlidingWindowFrequencyDeterminerSumCreationTest implements SlidingWindowSupportingAdapter {

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays
                .asList(new Object[][] {
                        // one single document containing 7 words
                        { new int[][] { { 7, 1 } }, 3, new long[] { 7, 11, 5 } },
                        // one single document containing 7 words but with a larger window
                        { new int[][] { { 7, 1 } }, 4, new long[] { 7, 15, 13, 4 } },
                        // now we add two documents which are a little bit shorter
                        { new int[][] { { 2, 1 }, { 3, 1 }, { 7, 1 } }, 4, new long[] { 12, 19, 14, 4 } },
                        // lets use higher counts
                        { new int[][] { { 2, 9 }, { 3, 1 }, { 7, 3 } }, 4, new long[] { 42, 57, 40, 12 } } });
    }

    private int[][] histogram;
    private int windowSize;
    private long expectedSums[];

    public OLD_BooleanSlidingWindowFrequencyDeterminerSumCreationTest(int[][] histogram, int windowSize, long[] expectedSums) {
        this.histogram = histogram;
        this.windowSize = windowSize;
        this.expectedSums = expectedSums;
    }

    @Test
    public void test() {
        OLD_BooleanSlidingWindowFrequencyDeterminer determiner = new OLD_BooleanSlidingWindowFrequencyDeterminer(this,
                windowSize);
        Assert.assertArrayEquals(expectedSums, determiner.getCooccurrenceCounts());
    }

    @Override
    public int[][] getDocumentSizeHistogram() {
        return histogram;
    }

    @Override
    public IntObjectOpenHashMap<IntArrayList[]> requestWordPositionsInDocuments(String[] words,
            IntIntOpenHashMap docLengths) {
        return null;
    }
}
