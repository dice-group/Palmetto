/**
 * This file is part of Palmetto Web Application.
 *
 * Palmetto Web Application is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Palmetto Web Application is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Palmetto Web Application.  If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * This file is part of General Entity Annotator Benchmark.
 *
 * General Entity Annotator Benchmark is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * General Entity Annotator Benchmark is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with General Entity Annotator Benchmark.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.aksw.palmetto.webapp.config;

import java.util.HashMap;
import java.util.Map;

import org.aksw.palmetto.Coherence;
import org.aksw.palmetto.DirectConfirmationBasedCoherence;
import org.aksw.palmetto.Palmetto;
import org.aksw.palmetto.VectorBasedCoherence;
import org.aksw.palmetto.aggregation.ArithmeticMean;
import org.aksw.palmetto.calculations.direct.FitelsonConfirmationMeasure;
import org.aksw.palmetto.calculations.direct.LogCondProbConfirmationMeasure;
import org.aksw.palmetto.calculations.direct.LogRatioConfirmationMeasure;
import org.aksw.palmetto.calculations.direct.NormalizedLogRatioConfirmationMeasure;
import org.aksw.palmetto.calculations.indirect.CosinusConfirmationMeasure;
import org.aksw.palmetto.corpus.CorpusAdapter;
import org.aksw.palmetto.corpus.WindowSupportingAdapter;
import org.aksw.palmetto.corpus.lucene.WindowSupportingLuceneCorpusAdapter;
import org.aksw.palmetto.prob.bd.BooleanDocumentProbabilitySupplier;
import org.aksw.palmetto.prob.window.BooleanSlidingWindowFrequencyDeterminer;
import org.aksw.palmetto.prob.window.ContextWindowFrequencyDeterminer;
import org.aksw.palmetto.prob.window.WindowBasedProbabilityEstimator;
import org.aksw.palmetto.subsets.OneOne;
import org.aksw.palmetto.subsets.OnePreceding;
import org.aksw.palmetto.subsets.OneSet;
import org.aksw.palmetto.vector.DirectConfirmationBasedVectorCreator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@org.springframework.context.annotation.Configuration
@ComponentScan(basePackages = "org.aksw.palmetto.webapp")
public class RootConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(RootConfig.class);

    private static final String CA_REQUEST_PATH = "ca";
    private static final int CA_DEFAULT_WINDOW_SIZE = 5;
    private static final String CA_WINDOW_SIZE_PROPERTY_KEY = "org.aksw.palmetto.webapp.resources.CAResource.windowSize";

    private static final String CP_REQUEST_PATH = "cp";
    private static final int CP_DEFAULT_WINDOW_SIZE = 70;
    private static final String CP_WINDOW_SIZE_PROPERTY_KEY = "org.aksw.palmetto.webapp.resources.CPResource.windowSize";

    private static final String CV_REQUEST_PATH = "cv";
    private static final int CV_DEFAULT_WINDOW_SIZE = 110;
    private static final String CV_WINDOW_SIZE_PROPERTY_KEY = "org.aksw.palmetto.webapp.resources.CVResource.windowSize";

    private static final String NPMI_REQUEST_PATH = "npmi";
    private static final int NPMI_DEFAULT_WINDOW_SIZE = 10;
    private static final String NPMI_WINDOW_SIZE_PROPERTY_KEY = "org.aksw.palmetto.webapp.resources.NPMIResource.windowSize";

    private static final String UCI_REQUEST_PATH = "uci";
    private static final int UCI_DEFAULT_WINDOW_SIZE = 10;
    private static final String UCI_WINDOW_SIZE_PROPERTY_KEY = "org.aksw.palmetto.webapp.resources.UCIResource.windowSize";

    private static final String UMASS_REQUEST_PATH = "umass";

    private static final String INDEX_PATH_PROPERTY_KEY = "org.aksw.palmetto.webapp.resources.AbstractCoherenceResource.indexPath";

    static @Bean public WindowSupportingAdapter createLuceneAdapter() throws Exception {
        String indexPath = PalmettoConfiguration.getInstance().getString(INDEX_PATH_PROPERTY_KEY);
        if (indexPath == null) {
            String errormsg = "Couldn't load \"" + INDEX_PATH_PROPERTY_KEY + "\" from properties. Aborting.";
            LOGGER.error(errormsg);
            throw new IllegalStateException(errormsg);
        }
        return WindowSupportingLuceneCorpusAdapter.create(indexPath, Palmetto.DEFAULT_TEXT_INDEX_FIELD_NAME,
                Palmetto.DEFAULT_DOCUMENT_LENGTH_INDEX_FIELD_NAME);
    }

    @Bean(name="coherences")
    static public Map<String, Coherence> createCoherences(WindowSupportingAdapter corpusAdapter) {
        Map<String, Coherence> coherences = new HashMap<String, Coherence>();
        coherences.put(CA_REQUEST_PATH, createCACoherence(corpusAdapter));
        coherences.put(CP_REQUEST_PATH, createCPCoherence(corpusAdapter));
        coherences.put(CV_REQUEST_PATH, createCVCoherence(corpusAdapter));
        coherences.put(NPMI_REQUEST_PATH, createNPMICoherence(corpusAdapter));
        coherences.put(UCI_REQUEST_PATH, createUCICoherence(corpusAdapter));
        coherences.put(UMASS_REQUEST_PATH, createUMassCoherence(corpusAdapter));
        return coherences;
    }

    public static Coherence createCACoherence(WindowSupportingAdapter corpusAdapter) {
        int windowSize = CA_DEFAULT_WINDOW_SIZE;
        try {
            windowSize = PalmettoConfiguration.getInstance().getInt(CA_WINDOW_SIZE_PROPERTY_KEY);
        } catch (Exception e) {
            LOGGER.warn("Couldn't load \"{}\" from properties. Using default window size={}.",
                    CA_WINDOW_SIZE_PROPERTY_KEY, CA_DEFAULT_WINDOW_SIZE);
        }
        WindowBasedProbabilityEstimator probEstimator = new WindowBasedProbabilityEstimator(
                new ContextWindowFrequencyDeterminer(corpusAdapter, windowSize));
        probEstimator.setMinFrequency(WindowBasedProbabilityEstimator.DEFAULT_MIN_FREQUENCY * windowSize);
        return new VectorBasedCoherence(new OneOne(),
                new DirectConfirmationBasedVectorCreator(probEstimator, new NormalizedLogRatioConfirmationMeasure()),
                new CosinusConfirmationMeasure(), new ArithmeticMean());
    }

    public static Coherence createCPCoherence(WindowSupportingAdapter corpusAdapter) {
        int windowSize = CP_DEFAULT_WINDOW_SIZE;
        try {
            windowSize = PalmettoConfiguration.getInstance().getInt(CP_WINDOW_SIZE_PROPERTY_KEY);
        } catch (Exception e) {
            LOGGER.warn("Couldn't load \"{}\" from properties. Using default window size={}.",
                    CP_WINDOW_SIZE_PROPERTY_KEY, CP_DEFAULT_WINDOW_SIZE);
        }
        WindowBasedProbabilityEstimator probEstimator = new WindowBasedProbabilityEstimator(
                new BooleanSlidingWindowFrequencyDeterminer(corpusAdapter, windowSize));
        probEstimator.setMinFrequency(WindowBasedProbabilityEstimator.DEFAULT_MIN_FREQUENCY * windowSize);
        return new DirectConfirmationBasedCoherence(new OnePreceding(), probEstimator,
                new FitelsonConfirmationMeasure(), new ArithmeticMean());
    }

    public static Coherence createCVCoherence(WindowSupportingAdapter corpusAdapter) {
        int windowSize = CV_DEFAULT_WINDOW_SIZE;
        try {
            windowSize = PalmettoConfiguration.getInstance().getInt(CV_WINDOW_SIZE_PROPERTY_KEY);
        } catch (Exception e) {
            LOGGER.warn("Couldn't load \"{}\" from properties. Using default window size={}.",
                    CV_WINDOW_SIZE_PROPERTY_KEY, CV_DEFAULT_WINDOW_SIZE);
        }
        WindowBasedProbabilityEstimator probEstimator = new WindowBasedProbabilityEstimator(
                new BooleanSlidingWindowFrequencyDeterminer(corpusAdapter, windowSize));
        probEstimator.setMinFrequency(WindowBasedProbabilityEstimator.DEFAULT_MIN_FREQUENCY * windowSize);
        return new VectorBasedCoherence(new OneSet(),
                new DirectConfirmationBasedVectorCreator(probEstimator, new NormalizedLogRatioConfirmationMeasure()),
                new CosinusConfirmationMeasure(), new ArithmeticMean());
    }

    public static Coherence createNPMICoherence(WindowSupportingAdapter corpusAdapter) {
        int windowSize = NPMI_DEFAULT_WINDOW_SIZE;
        try {
            windowSize = PalmettoConfiguration.getInstance().getInt(NPMI_WINDOW_SIZE_PROPERTY_KEY);
        } catch (Exception e) {
            LOGGER.warn("Couldn't load \"{}\" from properties. Using default window size={}.",
                    NPMI_WINDOW_SIZE_PROPERTY_KEY, NPMI_DEFAULT_WINDOW_SIZE);
        }
        WindowBasedProbabilityEstimator probEstimator = new WindowBasedProbabilityEstimator(
                new BooleanSlidingWindowFrequencyDeterminer(corpusAdapter, windowSize));
        probEstimator.setMinFrequency(WindowBasedProbabilityEstimator.DEFAULT_MIN_FREQUENCY * windowSize);
        return new DirectConfirmationBasedCoherence(new OneOne(), probEstimator,
                new NormalizedLogRatioConfirmationMeasure(), new ArithmeticMean());
    }

    public static Coherence createUCICoherence(WindowSupportingAdapter corpusAdapter) {
        int windowSize = UCI_DEFAULT_WINDOW_SIZE;
        try {
            windowSize = PalmettoConfiguration.getInstance().getInt(UCI_WINDOW_SIZE_PROPERTY_KEY);
        } catch (Exception e) {
            LOGGER.warn("Couldn't load \"{}\" from properties. Using default window size={}.",
                    UCI_WINDOW_SIZE_PROPERTY_KEY, UCI_DEFAULT_WINDOW_SIZE);
        }
        WindowBasedProbabilityEstimator probEstimator = new WindowBasedProbabilityEstimator(
                new BooleanSlidingWindowFrequencyDeterminer(corpusAdapter, windowSize));
        probEstimator.setMinFrequency(WindowBasedProbabilityEstimator.DEFAULT_MIN_FREQUENCY * windowSize);
        return new DirectConfirmationBasedCoherence(new OneOne(), probEstimator, new LogRatioConfirmationMeasure(),
                new ArithmeticMean());
    }

    public static Coherence createUMassCoherence(CorpusAdapter corpusAdapter) {
        return new DirectConfirmationBasedCoherence(new OnePreceding(),
                BooleanDocumentProbabilitySupplier.create(corpusAdapter, "bd", true),
                new LogCondProbConfirmationMeasure(), new ArithmeticMean());
    }

}
