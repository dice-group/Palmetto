package org.aksw.palmetto.io;

import java.io.File;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.mallet.types.Alphabet;
import cc.mallet.types.FeatureSequence;
import cc.mallet.types.Instance;
import cc.mallet.types.InstanceList;

public class MalletBasedDocumentTextSupplier implements DocumentTextSupplier {

    private static final Logger LOGGER = LoggerFactory.getLogger(MalletBasedDocumentTextSupplier.class);

    public static MalletBasedDocumentTextSupplier create(File inputFile) {
        MalletBasedDocumentTextSupplier supplier = null;
        try {
            InstanceList instanceList = InstanceList.load(inputFile);
            supplier = new MalletBasedDocumentTextSupplier(instanceList);
        } catch (Exception e) {
            LOGGER.error("Couldn't read Mallet instance list from file. Returning null.", e);
        }
        return supplier;
    }

    private Iterator<Instance> iterator;
    private Alphabet alphabet;

    public MalletBasedDocumentTextSupplier(InstanceList instanceList) {
        iterator = instanceList.iterator();
        alphabet = instanceList.getAlphabet();
    }

    public String getNextDocumentText() {
        String text = null;
        if (iterator != null) {
            if (iterator.hasNext()) {
                text = generateText(iterator.next());
            } else {
                // destroy the iterator and the alphabet
                iterator = null;
                alphabet = null;
            }
        }
        return text;
    }

    private String generateText(Instance instance) {
        int wordIds[] = ((FeatureSequence) instance.getData()).getFeatures();
        Object words[] = alphabet.lookupObjects(wordIds);
        StringBuilder builder = new StringBuilder();
        if (words.length > 0) {
            builder.append(words[0]);
            for (int i = 1; i < wordIds.length; ++i) {
                builder.append(' ');
                builder.append(words[i]);
            }
        }
        return builder.toString();
    }
}
