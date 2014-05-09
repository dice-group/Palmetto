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

import org.aksw.palmetto.corpus.SlidingWindowSupportingAdapter;
import org.aksw.palmetto.data.CountedSubsets;
import org.aksw.palmetto.data.SubsetDefinition;
import org.aksw.palmetto.subsets.AnyAny;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.carrotsearch.hppc.IntArrayList;
import com.carrotsearch.hppc.IntIntOpenHashMap;
import com.carrotsearch.hppc.IntObjectOpenHashMap;

@Deprecated
@RunWith(Parameterized.class)
public class BooleanSlidingWindowFrequencyDeterminerPerformanceTest implements SlidingWindowSupportingAdapter {

    private static final int NUMBER_OF_TEST_CASES = 3;
    private static final int WORDS_PER_TEST_CASE = 10;
    private static final int MAX_NUMBER_OF_DOCUMENTS_PER_WORD = 1000;
    private static final int DOCUMENT_ID_RANGE = 3 * MAX_NUMBER_OF_DOCUMENTS_PER_WORD;
    private static final int MAX_DOCUMENT_LENGTH = 1000;
    private static final int MAX_TOKENS_OF_WORD_PER_DOCUMENT = 10;

    @Parameters
    public static Collection<Object[]> data() {
        Object data[][] = new Object[NUMBER_OF_TEST_CASES][3];
        Random random = new Random(System.currentTimeMillis());
        int numberOfDocsForWord;
        IntObjectOpenHashMap<IntArrayList[]> positionsInDocuments;
        IntIntOpenHashMap docLengths, freeWordsInDoc, tempHistogram;
        int histogram[][];
        int countOfDocumentsForThisWord, pos;
        int documentId, documentLength, wordsInDocument, wordPosition, freePositions;
        boolean wordPositionOk;
        IntArrayList[] positionsInCurrentDocument;
        for (int i = 0; i < data.length; ++i) {
            positionsInDocuments = new IntObjectOpenHashMap<IntArrayList[]>();
            docLengths = new IntIntOpenHashMap();
            freeWordsInDoc = new IntIntOpenHashMap();
            for (int w = 0; w < WORDS_PER_TEST_CASE; ++w) {
                numberOfDocsForWord = random.nextInt(MAX_NUMBER_OF_DOCUMENTS_PER_WORD);
                countOfDocumentsForThisWord = 0;
                while (countOfDocumentsForThisWord < numberOfDocsForWord) {
                    documentId = random.nextInt(DOCUMENT_ID_RANGE);
                    if (positionsInDocuments.containsKey(documentId)) {
                        positionsInCurrentDocument = positionsInDocuments.lget();
                        documentLength = docLengths.get(documentId);
                        freePositions = freeWordsInDoc.get(documentId);
                    } else {
                        positionsInCurrentDocument = new IntArrayList[WORDS_PER_TEST_CASE];
                        positionsInDocuments.put(documentId, positionsInCurrentDocument);
                        documentLength = random.nextInt(MAX_DOCUMENT_LENGTH - MAX_TOKENS_OF_WORD_PER_DOCUMENT)
                                + MAX_TOKENS_OF_WORD_PER_DOCUMENT;
                        docLengths.put(documentId, documentLength);
                        freeWordsInDoc.put(documentId, documentLength);
                        freePositions = documentLength;
                    }
                    // If this word hasn't already been added to this document
                    if ((positionsInCurrentDocument[w] == null) && (freePositions > 0)) {
                        wordsInDocument = random
                                .nextInt((freePositions < MAX_TOKENS_OF_WORD_PER_DOCUMENT ? freePositions
                                        : (MAX_TOKENS_OF_WORD_PER_DOCUMENT - 1))) + 1;
                        positionsInCurrentDocument[w] = new IntArrayList(wordsInDocument);
                        while (positionsInCurrentDocument[w].elementsCount < wordsInDocument) {
                            wordPosition = random.nextInt(documentLength);
                            wordPositionOk = true;
                            for (int j = 0; wordPositionOk && (j < positionsInCurrentDocument.length); ++j) {
                                if (positionsInCurrentDocument[j] != null) {
                                    for (int j2 = 0; wordPositionOk
                                            && (j2 < positionsInCurrentDocument[j].elementsCount); ++j2) {
                                        wordPositionOk = positionsInCurrentDocument[j].buffer[j2] != wordPosition;
                                    }
                                }
                            }
                            if (wordPositionOk) {
                                positionsInCurrentDocument[w].add(wordPosition);
                                freeWordsInDoc.addTo(documentId, -1);
                            }
                        }
                        ++countOfDocumentsForThisWord;
                    }
                }
            }
            // create the histogram
            tempHistogram = new IntIntOpenHashMap();
            for (int j = 0; j < docLengths.keys.length; ++j) {
                if (docLengths.allocated[j]) {
                    tempHistogram.putOrAdd(docLengths.values[j], 1, 1);
                }
            }
            histogram = new int[tempHistogram.assigned][2];
            pos = 0;
            for (int j = 0; j < tempHistogram.keys.length; ++j) {
                if (tempHistogram.allocated[j]) {
                    histogram[pos][0] = tempHistogram.keys[j];
                    histogram[pos][1] = tempHistogram.values[j];
                }
            }

            data[i][0] = positionsInDocuments;
            data[i][1] = docLengths;
            data[i][2] = histogram;
        }
        return Arrays.asList(data);
    }

    private IntObjectOpenHashMap<IntArrayList[]> positionsInDocuments;
    private IntIntOpenHashMap docLengths;
    private int histogram[][];

    public BooleanSlidingWindowFrequencyDeterminerPerformanceTest(
            IntObjectOpenHashMap<IntArrayList[]> positionsInDocuments, IntIntOpenHashMap docLengths, int histogram[][]) {
        this.positionsInDocuments = positionsInDocuments;
        this.docLengths = docLengths;
        this.histogram = histogram;
    }

    @Test
    public void test() {
        long neededTimes[] = new long[2];
        Random random = new Random(System.currentTimeMillis());
        BitSetBasedBooleanSlidingWindowFrequencyDeterminer bitSetBasedFreqDeterminer = new BitSetBasedBooleanSlidingWindowFrequencyDeterminer(
                this, 10);
        BooleanSlidingWindowFrequencyDeterminer freqDeterminer = new BooleanSlidingWindowFrequencyDeterminer(this, 10);
        String words[][] = new String[1][WORDS_PER_TEST_CASE];
        for (int i = 0; i < WORDS_PER_TEST_CASE; ++i) {
            words[0][i] = Integer.toString(i);
        }
        SubsetDefinition definitions[] = new SubsetDefinition[] { (new AnyAny()).getSubsetDefinition(words.length) };
        CountedSubsets subsets[][] = new CountedSubsets[2][];

        long time;
        if (random.nextBoolean()) {
            time = System.currentTimeMillis();
            subsets[0] = bitSetBasedFreqDeterminer.determineCounts(words, definitions);
            neededTimes[0] = System.currentTimeMillis() - time;
            time = System.currentTimeMillis();
            subsets[1] = freqDeterminer.determineCounts(words, definitions);
            neededTimes[1] = System.currentTimeMillis() - time;
        } else {
            time = System.currentTimeMillis();
            subsets[1] = freqDeterminer.determineCounts(words, definitions);
            neededTimes[1] = System.currentTimeMillis() - time;
            time = System.currentTimeMillis();
            subsets[0] = bitSetBasedFreqDeterminer.determineCounts(words, definitions);
            neededTimes[0] = System.currentTimeMillis() - time;
        }

        System.out.println("SlidingWindow performance test: BitSetBased: " + neededTimes[0] + " ms\tListBased: "
                + neededTimes[1] + " ms");
        Assert.assertArrayEquals(subsets[0][0].counts, subsets[1][0].counts);
    }

    @Override
    public int[][] getDocumentSizeHistogram() {
        return histogram;
    }

    @Override
    public IntObjectOpenHashMap<IntArrayList[]> requestWordPositionsInDocuments(String[] words,
            IntIntOpenHashMap docLengths) {
        docLengths.putAll(this.docLengths);
        return positionsInDocuments;
    }
}
