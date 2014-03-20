/**
 * Copyright (C) 2014 Michael Röder (michael.roeder@unister.de)
 *
 * Licensed under the Creative Commons Attribution-NonCommercial 4.0
 * International Public License (the "License").
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://creativecommons.org/licenses/by-nc/4.0/legalcode
 *
 * Unless required by applicable law or agreed to in writing, a file
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.aksw.palmetto.prob;

import java.util.Arrays;
import java.util.Collection;

import org.aksw.palmetto.corpus.BooleanBigramStatsSupportingAdapter;
import org.aksw.palmetto.subsets.AnyAny;
import org.aksw.palmetto.subsets.CountedSubsets;
import org.aksw.palmetto.subsets.SubsetDefinition;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class BooleanBigramStatsFrequencyDeterminerTest implements BooleanBigramStatsSupportingAdapter {

    private static final double DOUBLE_PRECISION_DELTA = 0.00000001;

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays
                .asList(new Object[][] {
                        /*
                         * cooccurence matrix
                         * 
                         * ----- 0 1 2
                         * 
                         * word0 5 2 3
                         * 
                         * word1 2 2 1
                         * 
                         * word2 3 1 6
                         */

                        { new int[][] { { 5, 2, 3 }, { 2, 2, 1 }, { 3, 1, 6 } }, 6.0, 13.0,
                                new int[] { 0, 5, 2, 2, 6, 3, 1, 0 } },

                        /*
                         * 
                         * ----- 0 1 2
                         * 
                         * word0 1 0 0
                         * 
                         * word1 0 1 1
                         * 
                         * word2 0 1 1
                         */

                        { new int[][] { { 1, 0, 0 }, { 0, 1, 1 }, { 0, 1, 1 } }, 3.0, 1.0,
                                new int[] { 0, 1, 1, 0, 1, 0, 1, 0 } },
                });
    }

    private int wordCooccurences[][];
    private int expectedCounts[];
    private double numberOfCooccurenceCounts;
    private double numberOfWordCounts;

    public BooleanBigramStatsFrequencyDeterminerTest(int[][] wordCooccurences, double numberOfCooccurenceCounts,
            double numberOfWordCounts, int[] expectedCounts) {
        this.wordCooccurences = wordCooccurences;
        this.expectedCounts = expectedCounts;
        this.numberOfCooccurenceCounts = numberOfCooccurenceCounts;
        this.numberOfWordCounts = numberOfWordCounts;
    }

    @Test
    public void test() {
        String words[] = new String[wordCooccurences.length];
        for (int i = 0; i < words.length; i++) {
            words[i] = Integer.toString(i);
        }

        BooleanBigramStatsFrequencyDeterminer freqDeterminer = new BooleanBigramStatsFrequencyDeterminer(this);
        CountedSubsets countedSubsets[] = freqDeterminer.determineCounts(new String[][] { words },
                new SubsetDefinition[] { (new AnyAny()).getSubsetDefinition(words.length) });

        int counts[] = countedSubsets[0].counts;
        Assert.assertArrayEquals(expectedCounts, counts);
        Assert.assertEquals(numberOfWordCounts, freqDeterminer.getNumberOfWordCounts(), DOUBLE_PRECISION_DELTA);
        Assert.assertEquals(numberOfCooccurenceCounts, freqDeterminer.getNumberOfWordCooccurences(),
                DOUBLE_PRECISION_DELTA);
    }

    @Override
    public int getCount(String word1) {
        int id = Integer.parseInt(word1);
        return wordCooccurences[id][id];
    }

    @Override
    public double getNumberOfWords() {
        return numberOfWordCounts;
    }

    @Override
    public int getCooccurenceCount(String word1, String word2) {
        int id1 = Integer.parseInt(word1);
        int id2 = Integer.parseInt(word2);
        return wordCooccurences[id1][id2];
    }

    @Override
    public double getNumberOfCooccurences() {
        return numberOfCooccurenceCounts;
    }
}
