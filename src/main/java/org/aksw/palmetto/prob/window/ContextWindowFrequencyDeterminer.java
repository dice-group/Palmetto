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
							(roeder@informatik.uni-leipzig.de)
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
package org.aksw.palmetto.prob.window;

import java.util.Arrays;

import org.aksw.palmetto.corpus.WindowSupportingAdapter;
import org.aksw.palmetto.data.CountedSubsets;
import org.aksw.palmetto.data.SegmentationDefinition;

import com.carrotsearch.hppc.IntArrayList;
import com.carrotsearch.hppc.IntIntOpenHashMap;
import com.carrotsearch.hppc.IntObjectOpenHashMap;

public class ContextWindowFrequencyDeterminer implements WindowBasedFrequencyDeterminer {

    private WindowSupportingAdapter corpusAdapter;
    private int windowSize;
    private long wordSetCountSums[];

    public ContextWindowFrequencyDeterminer(WindowSupportingAdapter corpusAdapter, int windowSize) {
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

    private void addCountsFromDocument(IntArrayList[] positions, int[] counts, int docLength) {
        int posInList[] = new int[positions.length];
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
        // create two large arrays for this document
        int wordIds[] = new int[wordCount];
        int wordPositions[] = new int[wordCount];
        wordCount = 0;
        while (nextWordPos < docLength) {
            wordIds[wordCount] = nextWordId;
            wordPositions[wordCount] = nextWordPos;
            ++posInList[nextWordId];
            ++wordCount;
            nextWordPos = Integer.MAX_VALUE;
            for (int i = 0; i < positions.length; ++i) {
                if ((positions[i] != null) && (posInList[i] < positions[i].elementsCount)
                        && (positions[i].buffer[posInList[i]] < nextWordPos)) {
                    nextWordPos = positions[i].buffer[posInList[i]];
                    nextWordId = i;
                }
            }
        }

        int windowStartPos, windowEndPos/* , windowWordSet */; // start inclusive, end exclusive
        int currentWordBit;
        for (int i = 0; i < wordIds.length; ++i) {
            windowStartPos = i;
            windowEndPos = i + 1;
            // search the beginning of the window
            while ((windowStartPos > 0) && (wordPositions[windowStartPos - 1] >= (wordPositions[i] - windowSize))) {
                --windowStartPos;
            }
            // search the end of the window
            while ((windowEndPos < wordPositions.length)
                    && (wordPositions[windowEndPos] <= (wordPositions[i] + windowSize))) {
                ++windowEndPos;
            }
            // windowWordSet = 0;
            currentWordBit = 1 << wordIds[i];
            for (int j = windowStartPos; j < windowEndPos; ++j) {
                // windowWordSet |= wordIds[j];
                if (wordIds[i] < wordIds[j]) {
                    ++counts[currentWordBit | (1 << wordIds[j])];
                }
            }
            ++counts[currentWordBit];
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
        return "P_cw" + windowSize;
    }

    protected void determineWordSetCountSum() {
        // For determining the sum of the counts we rely on a histogram of documents length and the window size
        wordSetCountSums = new long[this.windowSize];

        // Go through the histogram, count the number of windows
        int numberOfWindowsInDocs = 0;
        int histogram[][] = corpusAdapter.getDocumentSizeHistogram();
        for (int i = 0; i < histogram.length; ++i) {
            numberOfWindowsInDocs += histogram[i][1] * (histogram[i][0] - (this.windowSize - 1));
        }

        // Determine how many word sets would have been counted using the number of windows
        for (int i = 0; i < wordSetCountSums.length; ++i) {
            wordSetCountSums[i] = numberOfWindowsInDocs;
        }
    }

    @Override
    public int getWindowSize() {
        return windowSize;
    }

}
