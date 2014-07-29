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
package org.aksw.palmetto;

import java.io.PrintStream;
import java.util.Arrays;

import org.aksw.palmetto.aggregation.ArithmeticMean;
import org.aksw.palmetto.calculations.direct.FitelsonConfirmationMeasure;
import org.aksw.palmetto.calculations.direct.LogCondProbConfirmationMeasure;
import org.aksw.palmetto.calculations.direct.LogRatioConfirmationMeasure;
import org.aksw.palmetto.calculations.direct.NormalizedLogRatioConfirmationMeasure;
import org.aksw.palmetto.calculations.indirect.CosinusConfirmationMeasure;
import org.aksw.palmetto.corpus.CorpusAdapter;
import org.aksw.palmetto.corpus.WindowSupportingAdapter;
import org.aksw.palmetto.corpus.lucene.LuceneCorpusAdapter;
import org.aksw.palmetto.corpus.lucene.WindowSupportingLuceneCorpusAdapter;
import org.aksw.palmetto.io.SimpleWordSetReader;
import org.aksw.palmetto.prob.bd.BooleanDocumentProbabilitySupplier;
import org.aksw.palmetto.prob.window.BooleanSlidingWindowFrequencyDeterminer;
import org.aksw.palmetto.prob.window.WindowBasedProbabilityEstimator;
import org.aksw.palmetto.subsets.OneOne;
import org.aksw.palmetto.subsets.OnePreceding;
import org.aksw.palmetto.subsets.OneSet;
import org.aksw.palmetto.vector.DirectConfirmationBasedVectorCreator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Palmetto {

    private static final Logger LOGGER = LoggerFactory.getLogger(Palmetto.class);

    private static final String USAGE = "palmetto.jar <index-directory> <coherence-name> <input-file>";

    public static final String DEFAULT_TEXT_INDEX_FIELD_NAME = "text";
    public static final String DEFAULT_DOCUMENT_LENGTH_INDEX_FIELD_NAME = "length";

    public static void main(String[] args) {
        if (args.length < 3) {
            LOGGER.error("Wrong number of arguments. Usage:\n" + USAGE);
            return;
        }
        String indexPath = args[0];
        String calcType = args[1].toLowerCase();
        String inputFile = args[2];

        Coherence coherence = getCoherence(calcType, indexPath);
        if (coherence == null) {
            return;
        }

        SimpleWordSetReader reader = new SimpleWordSetReader();
        String wordsets[][] = reader.readWordSets(inputFile);
        LOGGER.info("Read " + wordsets.length + " from file.");

        double coherences[] = coherence.calculateCoherences(wordsets);

        printCoherences(coherences, wordsets, System.out);
    }

    private static Coherence getCoherence(String calcType, String indexPath) {
        if ("umass".equals(calcType)) {
            CorpusAdapter corpusAdapter;
            try {
                corpusAdapter = LuceneCorpusAdapter.create(indexPath, DEFAULT_TEXT_INDEX_FIELD_NAME);
            } catch (Exception e) {
                LOGGER.error("Couldn't open lucene index. Aborting.", e);
                return null;
            }
            return new DirectConfirmationBasedCoherence(new OnePreceding(),
                    BooleanDocumentProbabilitySupplier.create(corpusAdapter, "bd", true),
                    new LogCondProbConfirmationMeasure(), new ArithmeticMean());
        }

        // All other calculations are using sliding window. Thus, we already can create the corpus adapter for them.
        WindowSupportingAdapter corpusAdapter;
        try {
            corpusAdapter = WindowSupportingLuceneCorpusAdapter.create(indexPath,
                    DEFAULT_TEXT_INDEX_FIELD_NAME, DEFAULT_DOCUMENT_LENGTH_INDEX_FIELD_NAME);
        } catch (Exception e) {
            LOGGER.error("Couldn't open lucene index. Aborting.", e);
            return null;
        }
        if(corpusAdapter == null) {
            LOGGER.error("Couldn't open lucene index. Aborting.");
            return null;
        }
        
        if ("uci".equals(calcType)) {
            return new DirectConfirmationBasedCoherence(
                    new OneOne(), getWindowBasedProbabilityEstimator(10, corpusAdapter),
                    new LogRatioConfirmationMeasure(), new ArithmeticMean());
        }

        if ("npmi".equals(calcType)) {
            return new DirectConfirmationBasedCoherence(
                    new OneOne(), getWindowBasedProbabilityEstimator(10, corpusAdapter),
                    new NormalizedLogRatioConfirmationMeasure(), new ArithmeticMean());
        }

        if ("c_p".equals(calcType)) {
            return new DirectConfirmationBasedCoherence(
                    new OnePreceding(), getWindowBasedProbabilityEstimator(70, corpusAdapter),
                    new FitelsonConfirmationMeasure(), new ArithmeticMean());
        }

        if ("c_v".equals(calcType)) {
            return new VectorBasedCoherence(new OneSet(),
                    new DirectConfirmationBasedVectorCreator(
                            getWindowBasedProbabilityEstimator(110, corpusAdapter),
                            new NormalizedLogRatioConfirmationMeasure()),
                    new CosinusConfirmationMeasure(), new ArithmeticMean());
        }

        StringBuilder msg = new StringBuilder();
        msg.append("Unknown calculation type \"");
        msg.append(calcType);
        msg.append("\". Supported types are:\nUMass\nUCI\nNPMI\nC_P\nC_V\n\nAborting.");
        return null;
    }

    private static WindowBasedProbabilityEstimator getWindowBasedProbabilityEstimator(int windowSize,
            WindowSupportingAdapter corpusAdapter) {
        WindowBasedProbabilityEstimator probEstimator = new WindowBasedProbabilityEstimator(
                new BooleanSlidingWindowFrequencyDeterminer(
                        corpusAdapter, windowSize));
        probEstimator.setMinFrequency(WindowBasedProbabilityEstimator.DEFAULT_MIN_FREQUENCY * windowSize);
        return probEstimator;
    }

    private static void printCoherences(double[] coherences, String[][] wordsets, PrintStream out) {
        for (int i = 0; i < wordsets.length; i++) {
            out.format("%5d\t%3.5f\t%s%n", new Object[] { i, coherences[i], Arrays.toString(wordsets[i]) });
        }
    }
}
