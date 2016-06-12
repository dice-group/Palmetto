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
     * @return the histogram of the document sizes
     */
    public int[][] getDocumentSizeHistogram();

    /**
     * Returns the positions of the given words inside the corpus.
     * 
     * @param words
     *            the words for which the positions inside the documents should
     *            be determined
     * @param docLengths
     *            empty int int map in which the document lengths and counts are
     *            inserted
     * @return the positions of the given words inside the corpus
     */
    public IntObjectOpenHashMap<IntArrayList[]> requestWordPositionsInDocuments(String words[],
            IntIntOpenHashMap docLengths);
}
