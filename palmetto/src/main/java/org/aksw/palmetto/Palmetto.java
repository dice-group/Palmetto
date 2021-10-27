/**
 * Palmetto - Palmetto is a quality measure tool for topics.
 * Copyright © 2014 Data Science Group (DICE) (michael.roeder@uni-paderborn.de)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
import org.aksw.palmetto.prob.window.ContextWindowFrequencyDeterminer;
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

        CorpusAdapter corpusAdapter = getCorpusAdapter(calcType, indexPath);
        if (corpusAdapter == null) {
            return;
        }

        Coherence coherence = getCoherence(calcType, corpusAdapter);
        if (coherence == null) {
            return;
        }

        SimpleWordSetReader reader = new SimpleWordSetReader();
        String wordsets[][] = reader.readWordSets(inputFile);
        LOGGER.info("Read " + wordsets.length + " from file.");

        long time = System.currentTimeMillis();
        double coherences[] = coherence.calculateCoherences(wordsets);
        corpusAdapter.close();
        time = System.currentTimeMillis() - time;
        System.out.println(time);

        printCoherences(coherences, wordsets, System.out);
    }

    public static CorpusAdapter getCorpusAdapter(String calcType, String indexPath) {
        try {
            if ("umass".equals(calcType)) {
                return LuceneCorpusAdapter.create(indexPath, DEFAULT_TEXT_INDEX_FIELD_NAME);
            } else {
                return WindowSupportingLuceneCorpusAdapter.create(indexPath,
                        DEFAULT_TEXT_INDEX_FIELD_NAME, DEFAULT_DOCUMENT_LENGTH_INDEX_FIELD_NAME);
            }
        } catch (Exception e) {
            LOGGER.error("Couldn't open lucene index. Aborting.", e);
            return null;
        }
    }

    public static Coherence getCoherence(String calcType, CorpusAdapter corpusAdapter) {
        if ("umass".equals(calcType)) {
            return new DirectConfirmationBasedCoherence(new OnePreceding(),
                    BooleanDocumentProbabilitySupplier.create(corpusAdapter, "bd", true),
                    new LogCondProbConfirmationMeasure(), new ArithmeticMean());
        }

        if ("uci".equals(calcType)) {
            return new DirectConfirmationBasedCoherence(
                    new OneOne(), getWindowBasedProbabilityEstimator(10, (WindowSupportingAdapter) corpusAdapter),
                    new LogRatioConfirmationMeasure(), new ArithmeticMean());
        }

        if ("npmi".equals(calcType)) {
            return new DirectConfirmationBasedCoherence(
                    new OneOne(), getWindowBasedProbabilityEstimator(10, (WindowSupportingAdapter) corpusAdapter),
                    new NormalizedLogRatioConfirmationMeasure(), new ArithmeticMean());
        }

        if ("c_a".equals(calcType)) {
            int windowSize = 5;
            WindowBasedProbabilityEstimator probEstimator = new WindowBasedProbabilityEstimator(
                    new ContextWindowFrequencyDeterminer((WindowSupportingAdapter) corpusAdapter, windowSize));
            probEstimator.setMinFrequency(WindowBasedProbabilityEstimator.DEFAULT_MIN_FREQUENCY * windowSize);
            return new VectorBasedCoherence(
                    new OneOne(), new DirectConfirmationBasedVectorCreator(probEstimator,
                            new NormalizedLogRatioConfirmationMeasure()), new CosinusConfirmationMeasure(),
                    new ArithmeticMean());
        }

        if ("c_p".equals(calcType)) {
            return new DirectConfirmationBasedCoherence(
                    new OnePreceding(),
                    getWindowBasedProbabilityEstimator(70, (WindowSupportingAdapter) corpusAdapter),
                    new FitelsonConfirmationMeasure(), new ArithmeticMean());
        }

        if ("c_v".equals(calcType)) {
            return new VectorBasedCoherence(new OneSet(),
                    new DirectConfirmationBasedVectorCreator(
                            getWindowBasedProbabilityEstimator(110, (WindowSupportingAdapter) corpusAdapter),
                            new NormalizedLogRatioConfirmationMeasure()),
                    new CosinusConfirmationMeasure(), new ArithmeticMean());
        }

        StringBuilder msg = new StringBuilder();
        msg.append("Unknown calculation type \"");
        msg.append(calcType);
        msg.append("\". Supported types are:\nUMass\nUCI\nNPMI\nC_A\nC_P\nC_V\n\nAborting.");
        LOGGER.error(msg.toString());
        return null;
    }

    public static WindowBasedProbabilityEstimator getWindowBasedProbabilityEstimator(int windowSize,
            WindowSupportingAdapter corpusAdapter) {
        WindowBasedProbabilityEstimator probEstimator = new WindowBasedProbabilityEstimator(
                new BooleanSlidingWindowFrequencyDeterminer(
                        corpusAdapter, windowSize));
        probEstimator.setMinFrequency(WindowBasedProbabilityEstimator.DEFAULT_MIN_FREQUENCY * windowSize);
        return probEstimator;
    }

    public static void printCoherences(double[] coherences, String[][] wordsets, PrintStream out) {
        for (int i = 0; i < wordsets.length; i++) {
            out.format("%5d\t%3.5f\t%s%n", new Object[] { i, coherences[i], Arrays.toString(wordsets[i]) });
        }
    }
}
