/**
 * Copyright (C) 2014 Michael Röder (michael.roeder@unister.de)
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
