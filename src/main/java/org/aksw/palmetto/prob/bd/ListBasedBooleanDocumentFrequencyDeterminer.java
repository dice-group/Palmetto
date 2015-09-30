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
package org.aksw.palmetto.prob.bd;

import java.util.Arrays;

import org.aksw.palmetto.corpus.BooleanDocumentSupportingAdapter;
import org.aksw.palmetto.data.CountedSubsets;
import org.aksw.palmetto.data.SegmentationDefinition;

import com.carrotsearch.hppc.IntArrayList;
import com.carrotsearch.hppc.ObjectObjectOpenHashMap;

public class ListBasedBooleanDocumentFrequencyDeterminer implements BooleanDocumentFrequencyDeterminer {

    private BooleanDocumentSupportingAdapter corpusAdapter;

    public ListBasedBooleanDocumentFrequencyDeterminer(BooleanDocumentSupportingAdapter corpusAdapter) {
        this.corpusAdapter = corpusAdapter;
    }

    public int getNumberOfDocuments() {
        return corpusAdapter.getNumberOfDocuments();
    }

    public CountedSubsets[] determineCounts(String[][] wordsets,
            SegmentationDefinition[] definitions) {
        ObjectObjectOpenHashMap<String, IntArrayList> wordDocMapping = new ObjectObjectOpenHashMap<String, IntArrayList>();
        for (int i = 0; i < wordsets.length; ++i) {
            for (int j = 0; j < wordsets[i].length; ++j) {
                if (!wordDocMapping.containsKey(wordsets[i][j])) {
                    wordDocMapping.put(wordsets[i][j], new IntArrayList());
                }
            }
        }

        corpusAdapter.getDocumentsWithWords(wordDocMapping);

        CountedSubsets countedSubsets[] = new CountedSubsets[definitions.length];
        int counts[];
        for (int i = 0; i < definitions.length; ++i) {
            counts = createCounts(wordDocMapping, wordsets[i]);
            addCountsOfSubsets(counts);
            countedSubsets[i] = new CountedSubsets(definitions[i].segments,
                    definitions[i].conditions, counts);
        }
        return countedSubsets;
    }

    private void addCountsOfSubsets(int[] counts) {
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

    private int[] createCounts(ObjectObjectOpenHashMap<String, IntArrayList> wordDocMapping, String[] wordset) {
        int counts[] = new int[(1 << wordset.length)];
        IntArrayList wordDocuments[] = new IntArrayList[wordset.length];
        for (int i = 0; i < wordDocuments.length; ++i) {
            wordDocuments[i] = wordDocMapping.get(wordset[i]);
            Arrays.sort(wordDocuments[i].buffer, 0, wordDocuments[i].elementsCount);
        }

        int posInList[] = new int[wordDocuments.length];
        int nextDocId;
        int documentSignature = 0;
        counts[0] = -1;
        do {
            ++counts[documentSignature];
            nextDocId = Integer.MAX_VALUE;
            for (int i = 0; i < wordDocuments.length; ++i) {
                if ((posInList[i] < wordDocuments[i].elementsCount)
                        && (wordDocuments[i].buffer[posInList[i]] <= nextDocId)) {
                    if (wordDocuments[i].buffer[posInList[i]] < nextDocId) {
                        nextDocId = wordDocuments[i].buffer[posInList[i]];
                        documentSignature = 0;
                    }
                    documentSignature |= 1 << i;
                }
            }
            for (int i = 0; i < posInList.length; ++i) {
                if ((documentSignature & (1 << i)) > 0) {
                    ++posInList[i];
                }
            }
        } while (nextDocId != Integer.MAX_VALUE);
        return counts;
    }
}
