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
import org.aksw.palmetto.prob.window.BooleanSlidingWindowFrequencyDeterminer;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.carrotsearch.hppc.IntArrayList;
import com.carrotsearch.hppc.IntIntOpenHashMap;
import com.carrotsearch.hppc.IntObjectOpenHashMap;

@RunWith(Parameterized.class)
public class BooleanSlidingWindowFrequencyDeterminerSumCreationTest implements WindowSupportingAdapter {

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays
                .asList(new Object[][] {
                        // one single document containing 7 words
                        { new int[][] { { 7, 1 } }, 3, new long[] { 5, 5, 5 } },
                        // one single document containing 7 words but with a larger window
                        { new int[][] { { 7, 1 } }, 4, new long[] { 4, 4, 4, 4 } },
                        // now we add two documents which are a little bit shorter
                        { new int[][] { { 2, 1 }, { 3, 1 }, { 7, 1 } }, 4, new long[] { 6, 6, 6, 6 } },
                        // lets use higher counts
                        { new int[][] { { 2, 9 }, { 3, 1 }, { 7, 3 } }, 4, new long[] { 22, 22, 22, 22 } } });
    }

    private int[][] histogram;
    private int windowSize;
    private long expectedSums[];

    public BooleanSlidingWindowFrequencyDeterminerSumCreationTest(int[][] histogram, int windowSize, long[] expectedSums) {
        this.histogram = histogram;
        this.windowSize = windowSize;
        this.expectedSums = expectedSums;
    }

    @Test
    public void test() {
        BooleanSlidingWindowFrequencyDeterminer determiner = new BooleanSlidingWindowFrequencyDeterminer(this,
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
    
    @Override
    public void close() {
    }
}
