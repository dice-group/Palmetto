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
package org.aksw.palmetto.corpus;

import com.carrotsearch.hppc.IntArrayList;
import com.carrotsearch.hppc.IntIntOpenHashMap;
import com.carrotsearch.hppc.IntObjectOpenHashMap;

public interface SlidingWindowSupportingAdapter extends CorpusAdapter {

    // public void setWindowSize(int windowSize);

    /**
     * Returns the number of cooccurrences of the given words. <b>Note</b> that if the given array contains one single
     * word this method must return the number of occurrence of this single word.
     * 
     * @param words
     * @return
     */
    // public int getCooccurrenceCounts(String words[]);

    // public long getWordSetCountSum(int wordSetLength);

    public int[][] getDocumentSizeHistogram();

    public IntObjectOpenHashMap<IntArrayList[]> requestWordPositionsInDocuments(String words[], IntIntOpenHashMap docLengths);
}
