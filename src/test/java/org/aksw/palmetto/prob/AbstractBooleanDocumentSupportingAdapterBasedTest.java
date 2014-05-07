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
    public int getNumberOfDocuments() {
        return numberOfDocuments;
    }
}
