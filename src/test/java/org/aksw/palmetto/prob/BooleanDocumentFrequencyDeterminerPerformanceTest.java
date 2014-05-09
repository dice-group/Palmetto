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
import java.util.Random;

import org.aksw.palmetto.data.CountedSubsets;
import org.aksw.palmetto.data.SubsetDefinition;
import org.aksw.palmetto.subsets.AnyAny;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.carrotsearch.hppc.IntOpenHashSet;

@RunWith(Parameterized.class)
public class BooleanDocumentFrequencyDeterminerPerformanceTest extends
        AbstractBooleanDocumentSupportingAdapterBasedTest {

    private static final int NUMBER_OF_TEST_CASES = 3;
    private static final int WORDS_PER_TEST_CASE = 10;
    private static final int MAX_NUMBER_OF_DOCUMENTS_PER_WORD = 100000;
    private static final int DOCUMENT_ID_RANGE = 3 * MAX_NUMBER_OF_DOCUMENTS_PER_WORD;

    @Parameters
    public static Collection<Object[]> data() {
        Object data[][] = new Object[NUMBER_OF_TEST_CASES][1];
        Random random = new Random(System.currentTimeMillis());
        IntOpenHashSet documentsOfWord = new IntOpenHashSet(MAX_NUMBER_OF_DOCUMENTS_PER_WORD);
        int numberOfDocsForWord;
        int wordDocuments[][];
        for (int i = 0; i < data.length; ++i) {
            wordDocuments = new int[WORDS_PER_TEST_CASE][];
            for (int w = 0; w < WORDS_PER_TEST_CASE; ++w) {
                numberOfDocsForWord = random.nextInt(MAX_NUMBER_OF_DOCUMENTS_PER_WORD);
                while (documentsOfWord.assigned < numberOfDocsForWord) {
                    documentsOfWord.add(random.nextInt(DOCUMENT_ID_RANGE));
                }
                wordDocuments[w] = documentsOfWord.toArray();
            }
            data[i][0] = wordDocuments;
        }
        return Arrays.asList(data);
    }

    public BooleanDocumentFrequencyDeterminerPerformanceTest(int[][] wordDocuments) {
        super(wordDocuments, 0);
    }

    @Test
    public void test() {
        long neededTimes[] = new long[2];
        Random random = new Random(System.currentTimeMillis());
        BooleanDocumentFrequencyDeterminer freqDeterminer = new BitSetBasedBooleanDocumentFrequencyDeterminer(this);
        ListBasedBooleanDocumentFrequencyDeterminer listBasedFreqDeterminer = new ListBasedBooleanDocumentFrequencyDeterminer(
                this);
        String words[][] = new String[1][wordDocuments.length];
        for (int i = 0; i < wordDocuments.length; ++i) {
            words[0][i] = Integer.toString(i);
        }
        SubsetDefinition definitions[] = new SubsetDefinition[] { (new AnyAny()).getSubsetDefinition(words.length) };
        CountedSubsets subsets[][] = new CountedSubsets[2][];

        long time;
        if (random.nextBoolean()) {
            time = System.currentTimeMillis();
            subsets[0] = freqDeterminer.determineCounts(words, definitions);
            neededTimes[0] = System.currentTimeMillis() - time;
            time = System.currentTimeMillis();
            subsets[1] = listBasedFreqDeterminer.determineCounts(words, definitions);
            neededTimes[1] = System.currentTimeMillis() - time;
        } else {
            time = System.currentTimeMillis();
            subsets[1] = listBasedFreqDeterminer.determineCounts(words, definitions);
            neededTimes[1] = System.currentTimeMillis() - time;
            time = System.currentTimeMillis();
            subsets[0] = freqDeterminer.determineCounts(words, definitions);
            neededTimes[0] = System.currentTimeMillis() - time;
        }

        System.out.println("BooleanDocument performance test BitSetBased: " + neededTimes[0] + " ms\tListBased: " + neededTimes[1] + " ms");
        Assert.assertArrayEquals(subsets[0][0].counts, subsets[1][0].counts);
    }
}
