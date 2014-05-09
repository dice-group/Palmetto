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

import org.aksw.palmetto.corpus.SlidingWindowSupportingAdapter;
import org.aksw.palmetto.data.CountedSubsets;
import org.aksw.palmetto.data.SubsetDefinition;

import com.carrotsearch.hppc.BitSet;
import com.carrotsearch.hppc.IntArrayList;
import com.carrotsearch.hppc.IntIntOpenHashMap;
import com.carrotsearch.hppc.IntObjectOpenHashMap;

@Deprecated
public class BitSetBasedBooleanSlidingWindowFrequencyDeterminer implements SlidingWindowFrequencyDeterminer {

    private SlidingWindowSupportingAdapter corpusAdapter;
    private int windowSize;
    private long wordSetCountSums[];

    public BitSetBasedBooleanSlidingWindowFrequencyDeterminer(SlidingWindowSupportingAdapter corpusAdapter, int windowSize) {
        this.corpusAdapter = corpusAdapter;
        setWindowSize(windowSize);
    }

    @Override
    public CountedSubsets[] determineCounts(String[][] wordsets, SubsetDefinition[] definitions) {
        CountedSubsets countedSubsets[] = new CountedSubsets[definitions.length];
        for (int i = 0; i < definitions.length; ++i) {
            countedSubsets[i] = new CountedSubsets(definitions[i].segments,
                    definitions[i].conditions, determineCounts(wordsets[i]));
        }
        return countedSubsets;
    }

    private int[] determineCounts(String wordset[]) {
        int counts[] = new int[(1 << wordset.length)];
        IntArrayList positions[];
        IntIntOpenHashMap docLengths = new IntIntOpenHashMap();
        IntObjectOpenHashMap<IntArrayList[]> positionsInDocs = corpusAdapter.requestWordPositionsInDocuments(wordset,
                docLengths);
        for (int i = 0; i < positionsInDocs.keys.length; ++i) {
            if (positionsInDocs.allocated[i]) {
                positions = ((IntArrayList[]) ((Object[]) positionsInDocs.values)[i]);
                addCountsFromDocument(positions, counts, docLengths.get(positionsInDocs.keys[i]));
            }
        }
        return counts;
    }

    private void addCounts(BitSet bitsets[], int counts[]) {
        BitSet[] combinations = new BitSet[(1 << bitsets.length)];
        int pos, pos2;
        for (int i = 0; i < bitsets.length; ++i) {
            pos = (1 << i);
            combinations[pos] = bitsets[i];
            pos2 = pos + 1;
            for (int j = 1; j < pos; ++j) {
                combinations[pos2] = ((BitSet) bitsets[i].clone());
                combinations[pos2].intersect(combinations[j]);
                ++pos2;
            }
        }
        for (int i = 1; i < combinations.length; ++i) {
            counts[i] += (int) combinations[i].cardinality();
        }
    }

    private void addCountsFromDocument(IntArrayList[] positions, int[] counts, int docLength) {
        if (docLength <= windowSize) {
            addCountsFromSmallDocument(positions, counts);
            return;
        }
        BitSet bitsets[] = new BitSet[positions.length];
        int smallerWindowSize = windowSize - 1;
        int maxPos, start, end, lastWindowPos = docLength - smallerWindowSize;
        for (int i = 0; i < positions.length; ++i) {
            if ((positions[i] != null) && (positions[i].size() > 0)) {
                maxPos = positions[i].buffer[positions[i].elementsCount - 1];
                bitsets[i] = new BitSet(maxPos >= lastWindowPos ? lastWindowPos : maxPos + 1);
                for (int j = 0; j < positions[i].elementsCount; ++j) {
                    start = positions[i].buffer[j] - smallerWindowSize;
                    // end has to be +1 because the set method is excluding it
                    end = positions[i].buffer[j] + 1;
                    bitsets[i].set(start < 0 ? 0 : start,
                            end > lastWindowPos ? lastWindowPos : end);
                }
            } else {
                bitsets[i] = new BitSet(0);
            }
        }
        addCounts(bitsets, counts);
    }

    private void addCountsFromSmallDocument(IntArrayList[] positions, int[] counts) {
        BitSet bitsets[] = new BitSet[positions.length];
        for (int i = 0; i < positions.length; ++i) {
            bitsets[i] = new BitSet(1);
            if ((positions[i] != null) && (positions[i].size() > 0)) {
                bitsets[i].set(0);
            }
        }
        addCounts(bitsets, counts);
    }

    @Override
    public void setWindowSize(int windowSize) {
        if (windowSize > Long.SIZE) {
            throw new IllegalArgumentException("This class only supports a window size up to " + Long.SIZE + ".");
        }
        this.windowSize = windowSize;
        determineWordSetCountSum();
    }

    @Override
    public long[] getCooccurrenceCounts() {
        return wordSetCountSums;
    }

    @Override
    public String getSlidingWindowModelName() {
        return "P_sw" + windowSize;
    }

    private boolean checkWordsInsideSingleWindow(int[] posInText) {
        int min = Integer.MAX_VALUE, max = -1;
        for (int i = 0; i < posInText.length; ++i) {
            if (posInText[i] > max) {
                max = posInText[i];
            }
            if (posInText[i] < min) {
                min = posInText[i];
            }
        }
        return (max - min) < this.windowSize;
    }

    protected void determineWordSetCountSum() {
        // For determining the sum of the counts we rely on a histogram of documents length and the window size
        wordSetCountSums = new long[this.windowSize];

        // Go through the histogram, count the number of windows
        int numberOfWindowsInDocs = 0;
        int histogram[][] = corpusAdapter.getDocumentSizeHistogram();
        for (int i = 0; i < histogram.length; ++i) {
            // If this document is shorter than the window
            if (histogram[i][0] < this.windowSize) {
                numberOfWindowsInDocs += histogram[i][1];
            } else {
                numberOfWindowsInDocs += histogram[i][1] * (histogram[i][0] - (this.windowSize - 1));
            }
        }

        // Determine how many word sets would have been counted using the number of windows
        for (int i = 0; i < wordSetCountSums.length; ++i) {
            wordSetCountSums[i] = numberOfWindowsInDocs;
        }
    }

    protected int determineCount(IntArrayList[] positions) {
        for (int i = 0; i < positions.length; ++i) {
            if ((positions[i] == null) || (positions[i].size() == 0)) {
                return 0;
            }
        }
        int posInList[] = new int[positions.length];
        int posInText[] = new int[positions.length];
        // determine the first positions of the words
        for (int i = 0; i < positions.length; ++i) {
            posInText[i] = positions[i].buffer[0];
        }
        // Go through all combinations of words and test if they are inside a single window
        int currentWordId, count = 0;
        while (posInList[0] < positions[0].size()) {
            if (checkWordsInsideSingleWindow(posInText)) {
                ++count;
            }
            currentWordId = positions.length - 1;
            ++posInList[currentWordId];
            while ((currentWordId > 0) && (posInList[currentWordId] >= positions[currentWordId].size())) {
                posInList[currentWordId] = 0;
                posInText[currentWordId] = positions[currentWordId].buffer[0];
                --currentWordId;
                ++posInList[currentWordId];
            }
            if (posInList[currentWordId] < positions[currentWordId].size()) {
                posInText[currentWordId] = positions[currentWordId].buffer[posInList[currentWordId]];
            }
        }
        return count;
    }

    @Override
    public int getWindowSize() {
        return windowSize;
    }

}
