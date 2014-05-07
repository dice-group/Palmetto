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

import org.aksw.palmetto.corpus.BooleanDocumentSupportingAdapter;
import org.aksw.palmetto.data.CountedSubsets;
import org.aksw.palmetto.data.SubsetDefinition;

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
            SubsetDefinition[] definitions) {
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
