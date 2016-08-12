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
import com.carrotsearch.hppc.IntOpenHashSet;
import com.carrotsearch.hppc.ObjectObjectOpenHashMap;

/**
 * This is an interface for an adapter that makes boolean document word counts available. Note that this interface is
 * used for boolean paragraph and boolean sentence probability estimation methods, too, since the difference between
 * these methods relies in the preprocessing of the corpus.
 * 
 * @author m.roeder
 * 
 */
public interface BooleanDocumentSupportingAdapter extends CorpusAdapter {

    /**
     * Determines the documents containing the words used as key in the given map. The resulting sets contain the ids of
     * the documents and are inserted into the map.
     * 
     * @param wordDocMapping a mapping of words to documents in which the results are stored
     */
    public void getDocumentsWithWordsAsSet(ObjectObjectOpenHashMap<String, IntOpenHashSet> wordDocMapping);

    /**
     * Determines the documents containing the words used as key in the given map. The resulting int arrays contain the
     * ids of the documents and are inserted into the map.
     * 
     * @param wordDocMapping a mapping of words to documents in which the results are stored
     */
    public void getDocumentsWithWords(ObjectObjectOpenHashMap<String, IntArrayList> wordDocMapping);

    /**
     * Returns the number of documents the corpus contains.
     * 
     * @return the number of documents
     */
    public int getNumberOfDocuments();
}
