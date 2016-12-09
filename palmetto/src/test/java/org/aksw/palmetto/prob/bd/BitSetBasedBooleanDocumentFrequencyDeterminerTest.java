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
package org.aksw.palmetto.prob.bd;

import java.util.Arrays;
import java.util.Collection;

import org.aksw.palmetto.data.CountedSubsets;
import org.aksw.palmetto.data.SegmentationDefinition;
import org.aksw.palmetto.prob.AbstractBooleanDocumentSupportingAdapterBasedTest;
import org.aksw.palmetto.subsets.AnyAny;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class BitSetBasedBooleanDocumentFrequencyDeterminerTest extends AbstractBooleanDocumentSupportingAdapterBasedTest {

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays
                .asList(new Object[][] {
                        /*
                         * word0 1 1 1
                         * 
                         * word1 0 1 1
                         * 
                         * word2 0 0 1
                         */

                        { new int[][] { { 0, 1, 2 }, { 1, 2 }, { 2 } }, new int[] { 0, 3, 2, 2, 1, 1, 1, 1 } },

                        /*
                         * word0 1 1 0 0
                         * 
                         * word1 0 1 1 1
                         * 
                         * word2 0 0 1 1
                         */

                        { new int[][] { { 0, 1 }, { 1, 2, 3 }, { 2, 3 } }, new int[] { 0, 2, 3, 1, 2, 0, 2, 0 } },

                        /*
                         * word0 1 1 0 0
                         * 
                         * word1 0 1 1 0
                         * 
                         * word2 0 0 1 1
                         * 
                         * word3 1 1 0 1
                         */

                        { new int[][] { { 0, 1 }, { 1, 2 }, { 2, 3 }, { 0, 1, 3 } },
                                new int[] { 0, 2, 2, 1, 2, 0, 1, 0, 3, 2, 1, 1, 1, 0, 0, 0 } } });
    }

    private int wordDocuments[][];
    private int expectedCounts[];

    public BitSetBasedBooleanDocumentFrequencyDeterminerTest(int[][] wordDocuments, int[] expectedCounts) {
        super(wordDocuments, 0);
        this.wordDocuments = wordDocuments;
        this.expectedCounts = expectedCounts;
    }

    @Test
    public void test() {
        String words[] = new String[wordDocuments.length];
        for (int i = 0; i < words.length; i++) {
            words[i] = Integer.toString(i);
        }

        BooleanDocumentFrequencyDeterminer freqDeterminer = new BitSetBasedBooleanDocumentFrequencyDeterminer(this);
        CountedSubsets countedSubsets[] = freqDeterminer.determineCounts(new String[][] { words },
                new SegmentationDefinition[] { (new AnyAny()).getSubsetDefinition(words.length) });

        int counts[] = countedSubsets[0].counts;
        Assert.assertArrayEquals(expectedCounts, counts);
    }
}
