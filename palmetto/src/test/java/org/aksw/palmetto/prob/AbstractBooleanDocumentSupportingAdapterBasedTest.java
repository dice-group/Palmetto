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
package org.aksw.palmetto.prob;

import org.aksw.palmetto.corpus.BooleanDocumentSupportingAdapter;

import com.carrotsearch.hppc.IntArrayList;
import com.carrotsearch.hppc.IntOpenHashSet;
import com.carrotsearch.hppc.ObjectObjectOpenHashMap;

public abstract class AbstractBooleanDocumentSupportingAdapterBasedTest implements BooleanDocumentSupportingAdapter {

    protected int wordDocuments[][];
    protected int numberOfDocuments;

    public AbstractBooleanDocumentSupportingAdapterBasedTest(int[][] wordDocuments, int numberOfDocuments) {
        this.wordDocuments = wordDocuments;
        this.numberOfDocuments = numberOfDocuments;
    }

    @Override
    public void getDocumentsWithWordsAsSet(ObjectObjectOpenHashMap<String, IntOpenHashSet> wordDocMapping) {
        Object keys[] = (Object[]) wordDocMapping.keys;
        Object values[] = (Object[]) wordDocMapping.values;
        for (int i = 0; i < wordDocMapping.allocated.length; ++i) {
            if (wordDocMapping.allocated[i]) {
                ((IntOpenHashSet) values[i]).add(wordDocuments[Integer.parseInt((String) keys[i])]);
            }
        }
    }

    @Override
    public void getDocumentsWithWordAsSet(String word, IntOpenHashSet documents) {
        documents.add(wordDocuments[Integer.parseInt(word)]);
    }

    @Override
    public void getDocumentsWithWords(ObjectObjectOpenHashMap<String, IntArrayList> wordDocMapping) {
        Object keys[] = (Object[]) wordDocMapping.keys;
        Object values[] = (Object[]) wordDocMapping.values;
        for (int i = 0; i < wordDocMapping.allocated.length; ++i) {
            if (wordDocMapping.allocated[i]) {
                ((IntArrayList) values[i]).add(wordDocuments[Integer.parseInt((String) keys[i])]);
            }
        }
    }

    @Override
    public void getDocumentsWithWord(String word, IntArrayList documents) {
        documents.add(wordDocuments[Integer.parseInt(word)]);
    }

    @Override
    public int getNumberOfDocuments() {
        return numberOfDocuments;
    }

    @Override
    public void close() {
    }
}
