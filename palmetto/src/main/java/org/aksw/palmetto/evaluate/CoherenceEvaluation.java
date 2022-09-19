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
package org.aksw.palmetto.evaluate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.aksw.palmetto.Palmetto;
import org.aksw.palmetto.aggregation.Aggregation;
import org.aksw.palmetto.aggregation.ArithmeticMean;
import org.aksw.palmetto.aggregation.GeometricMean;
import org.aksw.palmetto.aggregation.HarmonicMean;
import org.aksw.palmetto.aggregation.Max;
import org.aksw.palmetto.aggregation.Median;
import org.aksw.palmetto.aggregation.Min;
import org.aksw.palmetto.aggregation.QuadraticMean;
import org.aksw.palmetto.calculations.direct.CondProbConfirmationMeasure;
import org.aksw.palmetto.calculations.direct.DifferenceBasedConfirmationMeasure;
import org.aksw.palmetto.calculations.direct.DirectConfirmationMeasure;
import org.aksw.palmetto.calculations.direct.FitelsonConfirmationMeasure;
import org.aksw.palmetto.calculations.direct.JaccardConfirmationMeasure;
import org.aksw.palmetto.calculations.direct.JointProbabilityConfirmationMeasure;
import org.aksw.palmetto.calculations.direct.LikelihoodConfirmationMeasure;
import org.aksw.palmetto.calculations.direct.LogCondProbConfirmationMeasure;
import org.aksw.palmetto.calculations.direct.LogJaccardConfirmationMeasure;
import org.aksw.palmetto.calculations.direct.LogLikelihoodConfirmationMeasure;
import org.aksw.palmetto.calculations.direct.LogRatioConfirmationMeasure;
import org.aksw.palmetto.calculations.direct.NormalizedLogRatioConfirmationMeasure;
import org.aksw.palmetto.calculations.direct.OlssonsConfirmationMeasure;
import org.aksw.palmetto.calculations.direct.RatioConfirmationMeasure;
import org.aksw.palmetto.calculations.direct.ShogenjisConfirmationMeasure;
import org.aksw.palmetto.calculations.indirect.CosinusConfirmationMeasure;
import org.aksw.palmetto.calculations.indirect.DiceConfirmationMeasure;
import org.aksw.palmetto.calculations.indirect.VectorBasedConfirmationMeasure;
import org.aksw.palmetto.corpus.CorpusAdapter;
import org.aksw.palmetto.corpus.WindowSupportingAdapter;
import org.aksw.palmetto.corpus.lucene.LuceneCorpusAdapter;
import org.aksw.palmetto.corpus.lucene.WindowSupportingLuceneCorpusAdapter;
import org.aksw.palmetto.data.CountedSubsets;
import org.aksw.palmetto.data.SegmentationDefinition;
import org.aksw.palmetto.data.SubsetProbabilities;
import org.aksw.palmetto.data.SubsetVectors;
import org.aksw.palmetto.evaluate.CoherenceEvaluation.ProbSuppConfig.Type;
import org.aksw.palmetto.prob.ProbabilityEstimator;
import org.aksw.palmetto.prob.bd.BooleanDocumentProbabilitySupplier;
import org.aksw.palmetto.prob.window.BooleanSlidingWindowFrequencyDeterminer;
import org.aksw.palmetto.prob.window.ContextWindowFrequencyDeterminer;
import org.aksw.palmetto.prob.window.WindowBasedFrequencyDeterminer;
import org.aksw.palmetto.prob.window.WindowBasedProbabilityEstimator;
import org.aksw.palmetto.subsets.AllOne;
import org.aksw.palmetto.subsets.AnyAny;
import org.aksw.palmetto.subsets.OneAll;
import org.aksw.palmetto.subsets.OneAny;
import org.aksw.palmetto.subsets.OneOne;
import org.aksw.palmetto.subsets.OneOneAndSelf;
import org.aksw.palmetto.subsets.OnePreceding;
import org.aksw.palmetto.subsets.OneSet;
import org.aksw.palmetto.subsets.OneSucceeding;
import org.aksw.palmetto.subsets.Segmentator;
import org.aksw.palmetto.subsets.SetSet;
import org.aksw.palmetto.vector.DirectConfirmationBasedVectorCreator;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.carrotsearch.hppc.IntArrayList;
import com.carrotsearch.hppc.IntIntOpenHashMap;
import com.carrotsearch.hppc.IntObjectOpenHashMap;

public class CoherenceEvaluation {

    private static final Logger LOGGER = LoggerFactory.getLogger(CoherenceEvaluation.class);

    protected static final int MIN_GAMMA = 1;
    protected static final int MAX_GAMMA = 3;

    /**
     * The CSV lines are organized as follows:
     * <ol>
     * <li>Probability estimator type</li>
     * <li>Window size (empty if no window)</li>
     * <li>Segmentation type</li>
     * <li>Indirect measure (empty if not used)</li>
     * <li>Gamma (empty if not used)</li>
     * <li>Direct measure</li>
     * <li>Aggregation</li>
     * <li>Word Set Id</li>
     * <li>Coherence value</li>
     * </ol>
     */
    protected static final int CSV_LINE_LENGTH = 9;
    protected static final int CSV_LINE_PRB_EST_ID = 0;
    protected static final int CSV_LINE_WIN_SIZE_ID = 1;
    protected static final int CSV_LINE_SEG_ID = 2;
    protected static final int CSV_LINE_IN_MEASURE_ID = 3;
    protected static final int CSV_LINE_GAMMA_ID = 4;
    protected static final int CSV_LINE_DIR_MEASURE_ID = 5;
    protected static final int CSV_LINE_AGG_ID = 6;
    protected static final int CSV_LINE_WORD_SET_ID_ID = 7;
    protected static final int CSV_LINE_COHERENCE_ID = 8;

    protected static final AnyAny ANY_ANY = new AnyAny(10, false);
    protected static final OneOneAndSelf OO_AND_SELF_CREATOR = new OneOneAndSelf();

    private static final Segmentator SUBSET_CREATORS[] = new Segmentator[] { new OneAll(), new OneSet(), new AnyAny(),
            new OneAny(), new AllOne(), new OneOne(), new OnePreceding(), new OneSucceeding(), new SetSet() };

    private static final DirectConfirmationMeasure PROBABILITY_BASED_CALCULATIONS[] = new DirectConfirmationMeasure[] {
            new CondProbConfirmationMeasure(), new DifferenceBasedConfirmationMeasure(),
            new FitelsonConfirmationMeasure(), new JaccardConfirmationMeasure(), new LikelihoodConfirmationMeasure(),
            new LogCondProbConfirmationMeasure(), new LogJaccardConfirmationMeasure(),
            new LogLikelihoodConfirmationMeasure(), new LogRatioConfirmationMeasure(),
            new NormalizedLogRatioConfirmationMeasure(), new OlssonsConfirmationMeasure(),
            new RatioConfirmationMeasure(), new ShogenjisConfirmationMeasure(),
            new JointProbabilityConfirmationMeasure() };

    private static final VectorBasedConfirmationMeasure VECTOR_BASED_CALCULATIONS[] = new VectorBasedConfirmationMeasure[] {
            new CosinusConfirmationMeasure(), new DiceConfirmationMeasure(),
            new org.aksw.palmetto.calculations.indirect.JaccardConfirmationMeasure() };

    private static final DirectConfirmationMeasure CALCULATIONS_4_VECTOR_CREATION[] = {
            new CondProbConfirmationMeasure(), new DifferenceBasedConfirmationMeasure(),
            new FitelsonConfirmationMeasure(), new JaccardConfirmationMeasure(), new LikelihoodConfirmationMeasure(),
            new LogCondProbConfirmationMeasure(), new LogJaccardConfirmationMeasure(),
            new LogLikelihoodConfirmationMeasure(), new LogRatioConfirmationMeasure(),
            new NormalizedLogRatioConfirmationMeasure(), new OlssonsConfirmationMeasure(),
            new RatioConfirmationMeasure(), new ShogenjisConfirmationMeasure(),
            new JointProbabilityConfirmationMeasure() };

    private static final Aggregation SUMMARIZERS[] = new Aggregation[] { new ArithmeticMean(), new GeometricMean(),
            new HarmonicMean(), new QuadraticMean(), new Median(), new Min(), new Max() };

    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println(
                    "Wrong Usage!\nCoherenceEvaluation <index-path> <topic-file> <output-file> [basic-prob-type=bd|bp|bs]");
            return;
        }
        String indexPath = args[0];
        File topicFilePath = new File(args[1]);
        File outputFilePath = new File(args[2]);
        Type basicProbType = Type.BD;
        if (args.length > 3) {
            basicProbType = Type.valueOf(args[3].toUpperCase());
        }

        List<String> topicLines;
        try {
            topicLines = FileUtils.readLines(topicFilePath, StandardCharsets.UTF_8);
        } catch (IOException e) {
            LOGGER.error("Couldn't read topics from file.", e);
            return;
        }

        List<String[]> topics = new ArrayList<>();
        for (String topicLine : topicLines) {
            if (topicLine.length() > 5) {
                topics.add(topicLine.split(" "));
            }
        }

        CorpusAdapter luceneAdapter = null;
        try (PrintStream pout = new PrintStream(new FileOutputStream(outputFilePath, true), false, "UTF-8")) {
            if (basicProbType == Type.BD) {
                luceneAdapter = WindowSupportingLuceneCorpusAdapter.create(indexPath,
                        Palmetto.DEFAULT_TEXT_INDEX_FIELD_NAME, Palmetto.DEFAULT_DOCUMENT_LENGTH_INDEX_FIELD_NAME);
            } else {
                luceneAdapter = LuceneCorpusAdapter.create(indexPath, Palmetto.DEFAULT_TEXT_INDEX_FIELD_NAME);
            }

            for (int i = 0; i < topics.size(); ++i) {
                LOGGER.info("Starting topic {}/{}", i, topics.size());
                calculateResultsForTopic(topics.get(i), i, luceneAdapter, basicProbType, pout);
            }
        } catch (IOException e) {
            LOGGER.error("Exception while calculating coherences.", e);
            return;
        } finally {
            luceneAdapter.close();
        }
    }

    public static void calculateResultsForTopic(String[] wordSet, int wordSetId, CorpusAdapter luceneAdapter,
            Type basicProbType, PrintStream pout) throws IOException {
        LOGGER.info("Derive counts...");
        WindowSupportingAdapter tempAdapter = null;
        // Boolean Document
        Stream<ProbSuppConfig> configStream = Stream.of(new ProbSuppConfig(basicProbType, 0));
        if (basicProbType == Type.BD) {
            LOGGER.info("Using window-based approaches");
            tempAdapter = new StaticCorpusAdapter(wordSet, (WindowSupportingAdapter) luceneAdapter);
            // Sliding Window
            configStream = Stream.concat(configStream,
                    IntStream.range(1, 31).map(s -> s * 10).mapToObj(s -> new ProbSuppConfig(Type.SW, s)).parallel());
            // Context Window
            configStream = Stream.concat(configStream,
                    IntStream.range(1, 31).map(s -> s * 5).mapToObj(s -> new ProbSuppConfig(Type.CW, s)).parallel());
        }

        LOGGER.info("Start parallel execution...");
        final WindowSupportingAdapter localAdapter = tempAdapter;
        // Execute the configurations in parallel
        configStream.parallel().forEach(c -> {
            switch (c.type) {
            case BD: // falls through
            case BP:
            case BS: {
                String[] baseCSVLine = new String[CSV_LINE_LENGTH];
                baseCSVLine[CSV_LINE_WORD_SET_ID_ID] = Integer.toString(wordSetId);
                baseCSVLine[CSV_LINE_PRB_EST_ID] = c.type.name().toLowerCase();
                baseCSVLine[CSV_LINE_WIN_SIZE_ID] = "";
                // We have to use the lucene adapter since the local adapter does not support BD
                calculateCoherences(wordSet,
                        BooleanDocumentProbabilitySupplier.create(luceneAdapter, c.type.name().toLowerCase(), true),
                        pout, baseCSVLine);
                return;
            }
            case CW: {
                calculateContextWindowCoherences(wordSet, wordSetId, c.windowSize, localAdapter, pout);
                return;
            }
            case SW: {
                calculateSlidingWindowCoherences(wordSet, wordSetId, c.windowSize, localAdapter, pout);
                return;
            }
            }
        });
    }

    private static void calculateSlidingWindowCoherences(String[] wordSet, int wordSetId, int s,
            WindowSupportingAdapter localAdapter, PrintStream pout) {
        WindowBasedFrequencyDeterminer localFreqDeterminer = new StaticWindowBasedFrequencyDeterminer(
                new BooleanSlidingWindowFrequencyDeterminer(localAdapter, s), wordSet);

        String[] baseCSVLine = new String[CSV_LINE_LENGTH];
        baseCSVLine[CSV_LINE_WORD_SET_ID_ID] = Integer.toString(wordSetId);
        baseCSVLine[CSV_LINE_PRB_EST_ID] = "sw";
        baseCSVLine[CSV_LINE_WIN_SIZE_ID] = Integer.toString(s);

        calculateWindowCoherences(wordSet, localFreqDeterminer,
                WindowBasedProbabilityEstimator.DEFAULT_MIN_FREQUENCY * s, pout, baseCSVLine);
    }

    private static void calculateContextWindowCoherences(String[] wordSet, int wordSetId, int s,
            WindowSupportingAdapter localAdapter, PrintStream pout) {
        WindowBasedFrequencyDeterminer localFreqDeterminer = new StaticWindowBasedFrequencyDeterminer(
                new ContextWindowFrequencyDeterminer(localAdapter, s), wordSet);

        String[] baseCSVLine = new String[CSV_LINE_LENGTH];
        baseCSVLine[CSV_LINE_WORD_SET_ID_ID] = Integer.toString(wordSetId);
        baseCSVLine[CSV_LINE_PRB_EST_ID] = "cw";
        baseCSVLine[CSV_LINE_WIN_SIZE_ID] = Integer.toString(s);

        calculateWindowCoherences(wordSet, localFreqDeterminer, WindowBasedProbabilityEstimator.DEFAULT_MIN_FREQUENCY,
                pout, baseCSVLine);
    }

    private static void calculateWindowCoherences(String[] wordSet, WindowBasedFrequencyDeterminer localFreqDeterminer,
            int minFrequency, PrintStream pout, String[] baseCSVLine) {
        WindowBasedProbabilityEstimator probEstimator = new WindowBasedProbabilityEstimator(localFreqDeterminer);
        probEstimator.setMinFrequency(minFrequency);
        calculateCoherences(wordSet, probEstimator, pout, baseCSVLine);
    }

    private static void calculateCoherences(String[] wordSet, ProbabilityEstimator probEstimator, PrintStream pout,
            String[] baseCSVLine) {
        String[][] wordSets = new String[][] { wordSet };
        SegmentationDefinition[] segDefs = new SegmentationDefinition[1];
        SubsetProbabilities probabilities;

        for (Segmentator segmentator : SUBSET_CREATORS) {
            segDefs[0] = segmentator.getSubsetDefinition(wordSet.length);
            probabilities = probEstimator.getProbabilities(wordSets, segDefs)[0];
            baseCSVLine[CSV_LINE_SEG_ID] = segmentator.getName();
            calculateCoherences(probabilities, wordSet.length, pout, baseCSVLine);
        }
    }

    private static void calculateCoherences(SubsetProbabilities probabilities, int wordSetLength, PrintStream pout,
            String[] baseCSVLine) {
        double[] values;
        List<String[]> csvLines = new ArrayList<String[]>();
        baseCSVLine[CSV_LINE_IN_MEASURE_ID] = "";
        baseCSVLine[CSV_LINE_GAMMA_ID] = "";
        // Direct confirmation measure
        for (DirectConfirmationMeasure confMeasure : PROBABILITY_BASED_CALCULATIONS) {
            values = confMeasure.calculateConfirmationValues(probabilities);
            baseCSVLine[CSV_LINE_DIR_MEASURE_ID] = confMeasure.getName();
            calculateCoherences(values, baseCSVLine, csvLines);
        }

        SubsetVectors vectors;
        SegmentationDefinition oneOneAndSelfDef = OO_AND_SELF_CREATOR.getSubsetDefinition(wordSetLength);
        SubsetProbabilities oneOneAndSelfProbabilities = new SubsetProbabilities(oneOneAndSelfDef.segments,
                oneOneAndSelfDef.conditions, null);
        SegmentationDefinition localDef = new SegmentationDefinition(probabilities.segments, probabilities.conditions,
                null);
        // Direct measure for Indirect confirmation measures
        for (DirectConfirmationMeasure confMeasure : CALCULATIONS_4_VECTOR_CREATION) {
            baseCSVLine[CSV_LINE_DIR_MEASURE_ID] = confMeasure.getName();
            DirectConfirmationBasedVectorCreator vectorCreator = new DirectConfirmationBasedVectorCreator(null,
                    confMeasure, 1);
            // Gamma
            for (int g = MIN_GAMMA; g <= MAX_GAMMA; ++g) {
                vectorCreator.setGamma(g);
                baseCSVLine[CSV_LINE_GAMMA_ID] = Integer.toString(g);
                vectors = vectorCreator.createVectors(wordSetLength, localDef, oneOneAndSelfProbabilities,
                        probabilities);
                for (VectorBasedConfirmationMeasure inConfMeasure : VECTOR_BASED_CALCULATIONS) {
                    values = inConfMeasure.calculateConfirmationValues(vectors);
                    baseCSVLine[CSV_LINE_IN_MEASURE_ID] = inConfMeasure.getName();
                    calculateCoherences(values, baseCSVLine, csvLines);
                }
            }
        }

        printCSVLines(csvLines, pout);
    }

    private static void calculateCoherences(double[] values, String[] baseCSVLine, List<String[]> csvLines) {
        String[] csvResultLine;
        for (Aggregation aggregation : SUMMARIZERS) {
            csvResultLine = Arrays.copyOf(baseCSVLine, CSV_LINE_LENGTH);
            csvResultLine[CSV_LINE_AGG_ID] = aggregation.getName();
            csvResultLine[CSV_LINE_COHERENCE_ID] = Double.toString(aggregation.summarize(values));
            csvLines.add(csvResultLine);
        }
    }

    /**
     * Prints the given CSV lines in a synchronized way using the given print
     * stream. It uses the print stream object to synchronize.
     * 
     * @param csvLines
     * @param pout
     */
    private static void printCSVLines(List<String[]> csvLines, PrintStream pout) {
        synchronized (pout) {
            for (String line[] : csvLines) {
                if (line.length > 0) {
                    pout.print(line[0]);
                }
                for (int i = 1; i < line.length; ++i) {
                    pout.print(',');
                    pout.print(line[i]);
                }
                pout.println();
            }
            pout.flush();
        }
    }

    /**
     * A simple corpus adapter that already contains the results for a single word
     * set. Note that it will throw an {@link IllegalArgumentException} if it is
     * called with a different word set.
     * 
     * @author Michael R&ouml;der (michael.roeder@uni-paderborn.de)
     *
     */
    public static class StaticCorpusAdapter implements WindowSupportingAdapter {

        protected int[][] histogram;
        protected String[] wordSet;
        protected IntIntOpenHashMap docLengths;
        protected IntObjectOpenHashMap<IntArrayList[]> wordPositions;

        public StaticCorpusAdapter(int[][] histogram, String[] wordSet, IntIntOpenHashMap docLengths,
                IntObjectOpenHashMap<IntArrayList[]> wordPositions) {
            this.histogram = histogram;
            this.wordSet = wordSet;
            this.docLengths = docLengths;
            this.wordPositions = wordPositions;
        }

        public StaticCorpusAdapter(String[] wordSet, WindowSupportingAdapter luceneAdapter) {
            this.histogram = luceneAdapter.getDocumentSizeHistogram();
            this.wordSet = wordSet;
            this.docLengths = new IntIntOpenHashMap();
            this.wordPositions = luceneAdapter.requestWordPositionsInDocuments(wordSet, docLengths);
        }

        @Override
        public void close() {
            // nothing to do
        }

        @Override
        public int[][] getDocumentSizeHistogram() {
            return histogram;
        }

        @Override
        public IntObjectOpenHashMap<IntArrayList[]> requestWordPositionsInDocuments(String[] words,
                IntIntOpenHashMap docLengths) {
            if (!Arrays.equals(words, wordSet)) {
                throw new IllegalArgumentException("Got the unexpected word set \"" + Arrays.toString(words)
                        + "\" while \"" + Arrays.toString(wordSet) + "\" was expected");
            }
            // Create a deep copy of the data
            IntObjectOpenHashMap<IntArrayList[]> result = new IntObjectOpenHashMap<>();
            IntArrayList[] orig;
            IntArrayList[] copy;
            for (int i = 0; i < wordPositions.allocated.length; ++i) {
                if (wordPositions.allocated[i]) {
                    orig = (IntArrayList[]) ((Object[]) wordPositions.values)[i];
                    copy = new IntArrayList[orig.length];
                    for (int j = 0; j < copy.length; ++j) {
                        if (orig[j] != null) {
                            copy[j] = orig[j].clone();
                        }
                    }
                    result.put(wordPositions.keys[i], copy);
                }
            }
            docLengths.putAll(this.docLengths);
            return result;
        }
    }

    public static class StaticWindowBasedFrequencyDeterminer implements WindowBasedFrequencyDeterminer {

        protected String[] wordSet;
        protected CountedSubsets counts;
        protected long[] cooccurrenceCounts;
        protected WindowBasedFrequencyDeterminer originalDeterminer;

        public StaticWindowBasedFrequencyDeterminer(WindowBasedFrequencyDeterminer originalDeterminer,
                String[] wordSet) {
            this.wordSet = wordSet;
            cooccurrenceCounts = originalDeterminer.getCooccurrenceCounts();
            counts = originalDeterminer.determineCounts(new String[][] { wordSet },
                    new SegmentationDefinition[] { ANY_ANY.getSubsetDefinition(wordSet.length) })[0];
        }

        @Override
        public CountedSubsets[] determineCounts(String[][] wordsets, SegmentationDefinition[] definitions) {
            if (wordsets.length != 1 || !Arrays.equals(wordsets[0], wordSet)) {
                throw new IllegalArgumentException("Got the unexpected word set \"" + Arrays.toString(wordsets)
                        + "\" while \"" + Arrays.toString(wordSet) + "\" was expected");
            }
            return new CountedSubsets[] {
                    new CountedSubsets(definitions[0].segments, definitions[0].conditions, counts.counts) };
        }

        @Override
        public void setWindowSize(int windowSize) {
            throw new UnsupportedOperationException("The window size is fixed and can't be changed.");
        }

        @Override
        public long[] getCooccurrenceCounts() {
            return cooccurrenceCounts;
        }

        @Override
        public String getSlidingWindowModelName() {
            return originalDeterminer.getSlidingWindowModelName();
        }

        @Override
        public int getWindowSize() {
            return originalDeterminer.getWindowSize();
        }
    }

    public static class ProbSuppConfig {
        public enum Type {
            BD, BP, BS, CW, SW;
        }

        public Type type;
        public int windowSize;

        public ProbSuppConfig(Type type, int windowSize) {
            super();
            this.type = type;
            this.windowSize = windowSize;
        }
    }
}
