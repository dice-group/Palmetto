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
     * @param wordDocMapping
     */
    public void getDocumentsWithWordsAsSet(ObjectObjectOpenHashMap<String, IntOpenHashSet> wordDocMapping);

    /**
     * Determines the documents containing the words used as key in the given map. The resulting int arrays contain the
     * ids of the documents and are inserted into the map.
     * 
     * @param wordDocMapping
     */
    public void getDocumentsWithWords(ObjectObjectOpenHashMap<String, IntArrayList> wordDocMapping);

    /**
     * Returns the number of documents the corpus contains.
     * 
     * @return
     */
    public int getNumberOfDocuments();
}
