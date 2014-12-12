package org.aksw.palmetto.webapp;

import org.aksw.palmetto.Palmetto;
import org.aksw.palmetto.corpus.lucene.WindowSupportingLuceneCorpusAdapter;
import org.aksw.palmetto.webapp.config.PalmettoConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SingletonCorpusAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(SingletonCorpusAdapter.class);

    private static final String INDEX_PATH_PROPERTY_KEY = "org.aksw.palmetto.webapp.resources.AbstractCoherenceResource.indexPath";

    private static WindowSupportingLuceneCorpusAdapter instance = null;

    public static WindowSupportingLuceneCorpusAdapter getInstance() {
        if (instance == null) {
            String indexPath = PalmettoConfiguration.getInstance().getString(INDEX_PATH_PROPERTY_KEY);
            if (indexPath == null) {
                String errormsg = "Couldn't load \"" + INDEX_PATH_PROPERTY_KEY + "\" from properties. Aborting.";
                LOGGER.error(errormsg);
                throw new IllegalStateException(errormsg);
            }
            try {
                instance = WindowSupportingLuceneCorpusAdapter.create(indexPath,
                        Palmetto.DEFAULT_TEXT_INDEX_FIELD_NAME, Palmetto.DEFAULT_DOCUMENT_LENGTH_INDEX_FIELD_NAME);
            } catch (Exception e) {
                return null;
            }
        }
        return instance;
    }
}
