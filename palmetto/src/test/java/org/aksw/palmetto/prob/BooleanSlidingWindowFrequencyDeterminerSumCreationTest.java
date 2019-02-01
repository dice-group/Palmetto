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
        // nothing to do
    }
}
