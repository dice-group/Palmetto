/**
 * The MIT License
 * Copyright (c) 2014 Agile Knowledge Engineering and Semantic Web (AKSW)
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

import org.aksw.palmetto.corpus.BooleanDocumentSupportingAdapter;
import org.aksw.palmetto.data.CountedSubsets;
import org.aksw.palmetto.data.SegmentationDefinition;

import com.carrotsearch.hppc.BitSet;
import com.carrotsearch.hppc.IntOpenHashSet;
import com.carrotsearch.hppc.ObjectObjectOpenHashMap;

public class BitSetBasedBooleanDocumentFrequencyDeterminer implements BooleanDocumentFrequencyDeterminer {

    private BooleanDocumentSupportingAdapter corpusAdapter;

    public BitSetBasedBooleanDocumentFrequencyDeterminer(BooleanDocumentSupportingAdapter corpusAdapter) {
        this.corpusAdapter = corpusAdapter;
    }

    public int getNumberOfDocuments() {
        return corpusAdapter.getNumberOfDocuments();
    }

    public CountedSubsets[] determineCounts(String[][] wordsets,
            SegmentationDefinition[] definitions) {
        ObjectObjectOpenHashMap<String, IntOpenHashSet> wordDocMapping = new ObjectObjectOpenHashMap<String, IntOpenHashSet>();
        for (int i = 0; i < wordsets.length; ++i) {
            for (int j = 0; j < wordsets[i].length; ++j) {
                if (!wordDocMapping.containsKey(wordsets[i][j])) {
                    wordDocMapping.put(wordsets[i][j], new IntOpenHashSet());
                }
            }
        }

        corpusAdapter.getDocumentsWithWordsAsSet(wordDocMapping);

        CountedSubsets countedSubsets[] = new CountedSubsets[definitions.length];
        for (int i = 0; i < definitions.length; ++i) {
            countedSubsets[i] = new CountedSubsets(definitions[i].segments,
                    definitions[i].conditions, createCounts(
                            createBitSets(wordDocMapping, wordsets[i]),
                            definitions[i].neededCounts));
        }
        return countedSubsets;
    }

    private BitSet[] createBitSets(
            ObjectObjectOpenHashMap<String, IntOpenHashSet> wordDocMapping,
            String[] wordset) {
        IntOpenHashSet hashSets[] = new IntOpenHashSet[wordset.length];
        IntOpenHashSet mergedHashSet = new IntOpenHashSet();
        for (int i = 0; i < hashSets.length; ++i) {
            hashSets[i] = wordDocMapping.get(wordset[i]);
            mergedHashSet.addAll(hashSets[i]);
        }
        return createBitSets(hashSets, mergedHashSet);
    }

    private BitSet[] createBitSets(IntOpenHashSet hashSets[],
            IntOpenHashSet mergedHashSet) {
        BitSet bitSets[] = new BitSet[hashSets.length];
        for (int i = 0; i < bitSets.length; ++i) {
            bitSets[i] = new BitSet(mergedHashSet.size());
        }

        int pos = 0;
        for (int i = 0; i < mergedHashSet.keys.length; i++) {
            if (mergedHashSet.allocated[i]) {
                for (int j = 0; j < bitSets.length; ++j) {
                    if (hashSets[j].contains(mergedHashSet.keys[i])) {
                        bitSets[j].set(pos);
                    }
                }
                ++pos;
            }
        }

        return bitSets;
    }

    private int[] createCounts(BitSet bitsets[], BitSet neededCounts) {
        // TODO use the neededCounts bit set to avoid the creation of bit sets which are not needed
        // TODO Check the minimum frequency at this stage --> all BitSets with a lower cardinality can be set to null
        // and all following don't have to be created.
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
        int cardinalities[] = new int[combinations.length];
        for (int i = 1; i < combinations.length; ++i) {
            cardinalities[i] = (int) combinations[i].cardinality();
        }
        return cardinalities;
    }
}
