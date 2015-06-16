/**
 * The MIT License
 * Copyright (c) 2014 Agile Knowledge Engineering and Semantic Web (AKSW)
							(roeder@informatik.uni-leipzig.de)
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

        double coherences[] = coherence.calculateCoherences(wordsets);
        corpusAdapter.close();

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
