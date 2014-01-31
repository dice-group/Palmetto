package org.aksw.palmetto.corpus;

import com.carrotsearch.hppc.IntOpenHashSet;
import com.carrotsearch.hppc.ObjectObjectOpenHashMap;

public interface BooleanDocumentSupportingAdapter extends CorpusAdapter {

    public void getDocumentsWithWords(
	    ObjectObjectOpenHashMap<String, IntOpenHashSet> wordDocMapping);

    public int getNumberOfDocuments();
}
