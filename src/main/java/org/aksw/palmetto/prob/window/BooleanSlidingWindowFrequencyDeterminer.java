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
package org.aksw.palmetto.prob.window;

import java.util.Arrays;

import org.aksw.palmetto.corpus.WindowSupportingAdapter;
import org.aksw.palmetto.data.CountedSubsets;
import org.aksw.palmetto.data.SegmentationDefinition;

import com.carrotsearch.hppc.IntArrayList;
import com.carrotsearch.hppc.IntIntOpenHashMap;
import com.carrotsearch.hppc.IntObjectOpenHashMap;

public class BooleanSlidingWindowFrequencyDeterminer implements WindowBasedFrequencyDeterminer {

    protected WindowSupportingAdapter corpusAdapter;
    protected int windowSize;
    protected long wordSetCountSums[];

    public BooleanSlidingWindowFrequencyDeterminer(WindowSupportingAdapter corpusAdapter, int windowSize) {
        this.corpusAdapter = corpusAdapter;
        setWindowSize(windowSize);
    }

    @Override
    public CountedSubsets[] determineCounts(String[][] wordsets, SegmentationDefinition[] definitions) {
        CountedSubsets countedSubsets[] = new CountedSubsets[definitions.length];
        for (int i = 0; i < definitions.length; ++i) {
            countedSubsets[i] = new CountedSubsets(definitions[i].segments,
                    definitions[i].conditions, determineCounts(wordsets[i]));
        }
        return countedSubsets;
    }

    protected int[] determineCounts(String wordset[]) {
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
        addCountsOfSubsets(counts);
        return counts;
    }

    protected void addCountsFromDocument(IntArrayList[] positions, int[] counts, int docLength) {
        if (docLength <= windowSize) {
            addCountsFromSmallDocument(positions, counts);
            return;
        }
        int posInList[] = new int[positions.length + 1];
        int nextWordId = 0, nextWordPos = Integer.MAX_VALUE;
        int wordCount = 0;
        // determine the first token which we should look at
        for (int i = 0; i < positions.length; ++i) {
            if (positions[i] != null) {
                Arrays.sort(positions[i].buffer, 0, positions[i].elementsCount);
                if (positions[i].buffer[0] < nextWordPos) {
                    nextWordPos = positions[i].buffer[0];
                    nextWordId = i;
                }
                wordCount += positions[i].elementsCount;
            }
        }

        IntArrayList wordIdsInWindow = new IntArrayList(wordCount < windowSize ? wordCount : windowSize);
        IntArrayList wordPositionsInWindow = new IntArrayList(wordCount < windowSize ? wordCount : windowSize);
        int romaveableWordsPosId = posInList.length - 1;
        int windowWords = 0;
        int lastWordPos, wordEndPos;
        boolean countingEnabled = false;
        while (nextWordPos < docLength) {
            // create (or udpate) a signature containing a 1 for every word type inside this window
            // check whether a word will be removed
            if (nextWordId == positions.length) {
                windowWords &= ~(1 << wordIdsInWindow.buffer[posInList[romaveableWordsPosId]]);
                ++posInList[romaveableWordsPosId];
            } else {
                // if this word is already inside the window
                if ((windowWords & (1 << nextWordId)) > 0) {
                    // we have to remove its first occurrence from the list
                    for (int i = posInList[romaveableWordsPosId]; i < wordIdsInWindow.elementsCount; ++i) {
                        if (wordIdsInWindow.buffer[i] == nextWordId) {
                            wordIdsInWindow.remove(i);
                            wordPositionsInWindow.remove(i);
                        }
                    }
                } else {
                    // add the word
                    windowWords |= (1 << nextWordId);
                }
                // add its position to the list of tokens that should be removed (if this would be inside this document)
                wordEndPos = nextWordPos + windowSize;
                if (wordEndPos < docLength) {
                    wordIdsInWindow.add(nextWordId);
                    wordPositionsInWindow.add(wordEndPos);
                }
                // check if on the same position a word should be removed
                if ((posInList[romaveableWordsPosId] < wordPositionsInWindow.elementsCount)
                        && (wordPositionsInWindow.buffer[posInList[romaveableWordsPosId]] == nextWordPos)) {
                    windowWords &= ~(1 << wordIdsInWindow.buffer[posInList[romaveableWordsPosId]]);
                    ++posInList[romaveableWordsPosId];
                }
                ++posInList[nextWordId];
            }

            // Find the next position we should look at
            lastWordPos = nextWordPos;
            nextWordPos = Integer.MAX_VALUE;
            for (int i = 0; i < positions.length; ++i) {
                if ((positions[i] != null) && (posInList[i] < positions[i].elementsCount)
                        && (positions[i].buffer[posInList[i]] < nextWordPos)) {
                    nextWordPos = positions[i].buffer[posInList[i]];
                    nextWordId = i;
                }
            }
            if ((posInList[romaveableWordsPosId] < wordPositionsInWindow.elementsCount)
                    && (wordPositionsInWindow.buffer[posInList[romaveableWordsPosId]] < nextWordPos)) {
                nextWordPos = wordPositionsInWindow.buffer[posInList[romaveableWordsPosId]];
                nextWordId = positions.length;
            }
            // Make sure that counting only starts if the first window is complete
            if ((!countingEnabled) && (nextWordPos >= windowSize)) {
                if (lastWordPos < windowSize) {
                    lastWordPos = windowSize - 1;
                }
                countingEnabled = true;
            }
            if ((countingEnabled) && (windowWords != 0)) {
                // increase counts
                if (nextWordPos < docLength) {
                    counts[windowWords] += nextWordPos - lastWordPos;
                } else {
                    counts[windowWords] += docLength - lastWordPos;
                }
            }
        }
    }

    protected void addCountsFromSmallDocument(IntArrayList[] positions, int[] counts) {
        int signature = 0;
        for (int i = 0; i < positions.length; ++i) {
            if ((positions[i] != null) && (positions[i].size() > 0)) {
                signature |= 1 << i;
            }
        }
        ++counts[signature];
    }

    protected void addCountsOfSubsets(int[] counts) {
        // until now the counts contain only the windows which have exactly the matching word combination
        // --> we have to add the counts of the larger word sets to their subsets
        for (int i = 1; i < counts.length; ++i) {
            for (int j = i + 1; j < counts.length; ++j) {
                // if i is a subset of j
                if ((i & j) == i) {
                    counts[i] += counts[j];
                }
            }
        }
    }

    @Override
    public void setWindowSize(int windowSize) {
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

    // private boolean checkWordsInsideSingleWindow(int[] posInText) {
    // int min = Integer.MAX_VALUE, max = -1;
    // for (int i = 0; i < posInText.length; ++i) {
    // if (posInText[i] > max) {
    // max = posInText[i];
    // }
    // if (posInText[i] < min) {
    // min = posInText[i];
    // }
    // }
    // return (max - min) < this.windowSize;
    // }

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

    // protected int determineCount(IntArrayList[] positions) {
    // for (int i = 0; i < positions.length; ++i) {
    // if ((positions[i] == null) || (positions[i].size() == 0)) {
    // return 0;
    // }
    // }
    // int posInList[] = new int[positions.length];
    // int posInText[] = new int[positions.length];
    // // determine the first positions of the words
    // for (int i = 0; i < positions.length; ++i) {
    // posInText[i] = positions[i].buffer[0];
    // }
    // // Go through all combinations of words and test if they are inside a single window
    // int currentWordId, count = 0;
    // while (posInList[0] < positions[0].size()) {
    // if (checkWordsInsideSingleWindow(posInText)) {
    // ++count;
    // }
    // currentWordId = positions.length - 1;
    // ++posInList[currentWordId];
    // while ((currentWordId > 0) && (posInList[currentWordId] >= positions[currentWordId].size())) {
    // posInList[currentWordId] = 0;
    // posInText[currentWordId] = positions[currentWordId].buffer[0];
    // --currentWordId;
    // ++posInList[currentWordId];
    // }
    // if (posInList[currentWordId] < positions[currentWordId].size()) {
    // posInText[currentWordId] = positions[currentWordId].buffer[posInList[currentWordId]];
    // }
    // }
    // return count;
    // }

    @Override
    public int getWindowSize() {
        return windowSize;
    }

}
