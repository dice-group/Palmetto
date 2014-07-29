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

/**
 * This adapter supports window based probability estimation methods.
 * 
 * @author m.roeder
 * 
 */
public interface WindowSupportingAdapter extends CorpusAdapter {

    /**
     * Returns the histogram of the document sizes of the corpus.
     * 
     * @return
     */
    public int[][] getDocumentSizeHistogram();

    /**
     * Returns the positions of the given words inside the corpus.
     * 
     * @param words
     *            the words for which the positions inside the documents should be determined
     * @param docLengths
     *            empty int int map in which the document lengths and counts are inserted
     * @return
     */
    public IntObjectOpenHashMap<IntArrayList[]> requestWordPositionsInDocuments(String words[],
            IntIntOpenHashMap docLengths);
}
