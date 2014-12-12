package org.aksw.palmetto.webapp.coherences;

import java.util.HashMap;
import java.util.Map;

import org.aksw.palmetto.Coherence;
import org.aksw.palmetto.Palmetto;
import org.aksw.palmetto.corpus.lucene.WindowSupportingLuceneCorpusAdapter;
import org.aksw.palmetto.webapp.SingletonCorpusAdapter;
import org.aksw.palmetto.webapp.config.PalmettoConfiguration;
import org.aksw.palmetto.webapp.resources.AbstractCoherenceResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CoherenceManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(SingletonCorpusAdapter.class);

    private static final String INDEX_PATH_PROPERTY_KEY = "org.aksw.palmetto.webapp.resources.AbstractCoherenceResource.indexPath";
    // @SuppressWarnings("unchecked")
    // private static final Class<? extends AbstractCoherenceResource> coherenceResources[] = new Class[] {
    // CAResource.class, CPResource.class, CVResource.class, NPMIResource.class, UCIResource.class,
    // UMassResource.class };

    private static CoherenceManager instance = null;

    public static CoherenceManager getInstance() {
        if (instance == null) {
            String indexPath = PalmettoConfiguration.getInstance().getString(INDEX_PATH_PROPERTY_KEY);
            if (indexPath == null) {
                String errormsg = "Couldn't load \"" + INDEX_PATH_PROPERTY_KEY + "\" from properties. Aborting.";
                LOGGER.error(errormsg);
                throw new IllegalStateException(errormsg);
            }
            WindowSupportingLuceneCorpusAdapter corpusAdapter;
            try {
                corpusAdapter = WindowSupportingLuceneCorpusAdapter.create(indexPath,
                        Palmetto.DEFAULT_TEXT_INDEX_FIELD_NAME, Palmetto.DEFAULT_DOCUMENT_LENGTH_INDEX_FIELD_NAME);
            } catch (Exception e) {
                String errormsg = "Couldn't create Window supporting Lucene corpus adapter. Aborting.";
                LOGGER.error(errormsg);
                throw new IllegalStateException(errormsg);
            }

            // Map<Class<? extends AbstractCoherenceResource>, Coherence> coherences = new HashMap<Class<? extends
            // AbstractCoherenceResource>, Coherence>();
            // AbstractCoherenceResource resource;
            // Coherence coherence;
            // for (int i = 0; i < coherenceResources.length; ++i) {
            // try {
            // resource = coherenceResources[i].newInstance();
            // resource.init(null, null, null);
            //
            // } catch (Exception e) {
            // LOGGER.error("Couldn't create instance of \"" + coherenceResources[i].getCanonicalName() + "\".", e);
            // }
            // }
            instance = new CoherenceManager(corpusAdapter);
        }
        return instance;
    }

    private Map<Class<? extends AbstractCoherenceResource>, Coherence> coherences;
    private WindowSupportingLuceneCorpusAdapter corpusAdapter;

    public CoherenceManager(WindowSupportingLuceneCorpusAdapter corpusAdapter) {
        this.coherences = new HashMap<Class<? extends AbstractCoherenceResource>, Coherence>();
        this.corpusAdapter = corpusAdapter;
    }

    public Coherence getCoherence(Class<? extends AbstractCoherenceResource> clazz) {
        if (coherences.containsKey(clazz)) {
            return coherences.get(clazz);
        } else {
            return null;
        }
    }

    public void addCoherence(AbstractCoherenceResource resource) {
        if (!coherences.containsKey(resource.getClass())) {
            Coherence coherence = resource.createCoherence(corpusAdapter);
            coherences.put(resource.getClass(), coherence);
        }
    }
}
