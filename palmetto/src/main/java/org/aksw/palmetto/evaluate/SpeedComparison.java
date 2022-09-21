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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.aksw.palmetto.Coherence;
import org.aksw.palmetto.DirectConfirmationBasedCoherence;
import org.aksw.palmetto.Palmetto;
import org.aksw.palmetto.VectorBasedCoherence;
import org.aksw.palmetto.aggregation.Aggregation;
import org.aksw.palmetto.aggregation.ArithmeticMean;
import org.aksw.palmetto.calculations.direct.DifferenceBasedConfirmationMeasure;
import org.aksw.palmetto.calculations.direct.FitelsonConfirmationMeasure;
import org.aksw.palmetto.calculations.direct.LogCondProbConfirmationMeasure;
import org.aksw.palmetto.calculations.direct.LogRatioConfirmationMeasure;
import org.aksw.palmetto.calculations.direct.NormalizedLogRatioConfirmationMeasure;
import org.aksw.palmetto.calculations.indirect.CosinusConfirmationMeasure;
import org.aksw.palmetto.calculations.indirect.VectorBasedConfirmationMeasure;
import org.aksw.palmetto.corpus.WindowSupportingAdapter;
import org.aksw.palmetto.corpus.lucene.LuceneCorpusAdapter;
import org.aksw.palmetto.corpus.lucene.WindowSupportingLuceneCorpusAdapter;
import org.aksw.palmetto.data.SegmentationDefinition;
import org.aksw.palmetto.data.SubsetProbabilities;
import org.aksw.palmetto.data.SubsetVectors;
import org.aksw.palmetto.io.SimpleWordSetReader;
import org.aksw.palmetto.prob.FrequencyDeterminer;
import org.aksw.palmetto.prob.ProbabilityEstimator;
import org.aksw.palmetto.prob.bd.BooleanDocumentProbabilitySupplier;
import org.aksw.palmetto.prob.window.ContextWindowFrequencyDeterminer;
import org.aksw.palmetto.prob.window.WindowBasedProbabilityEstimator;
import org.aksw.palmetto.subsets.AnyAny;
import org.aksw.palmetto.subsets.OneAll;
import org.aksw.palmetto.subsets.OneAny;
import org.aksw.palmetto.subsets.OneOne;
import org.aksw.palmetto.subsets.OnePreceding;
import org.aksw.palmetto.subsets.OneSet;
import org.aksw.palmetto.subsets.OneSucceeding;
import org.aksw.palmetto.subsets.Segmentator;
import org.aksw.palmetto.subsets.SetSet;
import org.aksw.palmetto.vector.DirectConfirmationBasedVectorCreator;
import org.apache.lucene.index.CorruptIndexException;

/**
 * This class implements the runtime comparison of complete coherence
 * calculations as well as single parts of a coherence.
 * 
 * @author Michael R&ouml;der (michael.roeder@uni-paderborn.de)
 *
 */
public class SpeedComparison {

    private Random random;
    private final int numberOfWarmups;
    private final int numberOfRuns;

    public SpeedComparison(long seed, int numberOfWarmups, int numberOfRuns) {
        random = new Random(seed);
        this.numberOfWarmups = numberOfWarmups;
        this.numberOfRuns = numberOfRuns;
    }

    public void run(Coherence coherences[], List<AbstractTimeMeasuringModule> modules, String wordSets[][]) {
        Map<String, long[]> times = new HashMap<>();
        for (int i = 0; i < coherences.length; ++i) {
            times.put(coherences[i].getName(), new long[numberOfRuns]);
        }
        // Warmup
        for (int i = 0; i < numberOfWarmups; ++i) {
            System.out.println("Warm up run #" + i + "...");
            executeCoherenceCalculations(coherences, wordSets, null, 0);
            for (AbstractTimeMeasuringModule module : modules) {
                module.increaseRunId();
            }
        }
        // Runs
        for (int i = 0; i < numberOfRuns; ++i) {
            System.out.println("Run #" + i + "...");
            executeCoherenceCalculations(coherences, wordSets, times, i);
            for (AbstractTimeMeasuringModule module : modules) {
                module.increaseRunId();
            }
        }
        // Calculate statistics
        for (Entry<String, long[]> entry : times.entrySet()) {
            printStatistics(entry.getKey(), entry.getValue());
        }
    }

    protected void executeCoherenceCalculations(Coherence coherences[], String wordSets[][], Map<String, long[]> times,
            int runId) {
        int[] order = getRandomOrder(coherences.length);
        Coherence coherence;
        long time;
        for (int i = 0; i < order.length; ++i) {
            coherence = coherences[order[i]];
            System.out.println(" " + coherence.getName());
            time = executeCoherenceCalculation(coherence, wordSets);
            if (times != null) {
                times.get(coherence.getName())[runId] = time;
            }
        }
    }

    protected long executeCoherenceCalculation(Coherence coherence, String wordSets[][]) {
        long time = System.currentTimeMillis();
        coherence.calculateCoherences(wordSets);
        return System.currentTimeMillis() - time;
    }

    protected int[] getRandomOrder(int n) {
        int[] ids = new int[n];
        for (int i = 0; i < ids.length; ++i) {
            ids[i] = i;
        }
        /*
         * Durstenfelds version of Fisher-Yates shuffling.
         * https://en.wikipedia.org/wiki/Fisher%E2%80%93Yates_shuffle#
         * The_modern_algorithm
         */
        int id;
        int temp;
        for (int j = ids.length - 1; j > 0; --j) {
            // choose id that should be swapped with j
            id = random.nextInt(j);
            // swap
            temp = ids[j];
            ids[j] = ids[id];
            ids[id] = temp;
        }
        return ids;
    }

    protected static void printStatistics(String label, long[] values) {
        double avg = Arrays.stream(values).average().getAsDouble();
        double stdDev = Arrays.stream(values).mapToDouble(t -> Math.pow(avg - t, 2.0)).average().getAsDouble();
        stdDev = Math.sqrt(stdDev);
        System.out.print(label);
        System.out.print("\t");
        System.out.print(avg);
        System.out.print("\t(");
        System.out.print(stdDev);
        System.out.print(")\t");
        System.out.println(Arrays.toString(values));
    }

    public static void main(String[] args) throws CorruptIndexException, IOException {
        long seed = 38759812345L;
        int numberOfWarmups = 1;
        int numberOfRuns = 5;
        String bdIndexPath = "/home/micha/data/wikipedia_bd";
        String bd2IndexPath = "/home/micha/Downloads/temp/Wikipedia_bd";
        String bsIndexPath = "/home/micha/Downloads/temp/Wikipedia_bs";
        String bpIndexPath = "/home/micha/Downloads/temp/Wikipedia_bp";
        boolean testMainCoherences = false;
        boolean testSegmentations = false;
        boolean testProbEstimations = true;

        String inputFile = "topicsNYT.txt"; // TODO
//        String inputFile = "temp-test.txt"; // TODO
        SimpleWordSetReader reader = new SimpleWordSetReader();
        String wordsets[][] = reader.readWordSets(inputFile);

        // Create coherences
        WindowSupportingAdapter adapter = WindowSupportingLuceneCorpusAdapter.create(bdIndexPath,
                Palmetto.DEFAULT_TEXT_INDEX_FIELD_NAME, Palmetto.DEFAULT_DOCUMENT_LENGTH_INDEX_FIELD_NAME);

        WindowBasedProbabilityEstimator probEstimatorCA = new WindowBasedProbabilityEstimator(
                new ContextWindowFrequencyDeterminer((WindowSupportingAdapter) adapter, 5));
        probEstimatorCA.setMinFrequency(WindowBasedProbabilityEstimator.DEFAULT_MIN_FREQUENCY * 5);

        DirectConfirmationBasedVectorCreator caCreator = new DirectConfirmationBasedVectorCreator(probEstimatorCA,
                new NormalizedLogRatioConfirmationMeasure(), 1);
        DirectConfirmationBasedVectorCreator cv2Creator = new DirectConfirmationBasedVectorCreator(
                Palmetto.getSWProbabilityEstimator(110, adapter), new NormalizedLogRatioConfirmationMeasure(), 1);

        List<AbstractTimeMeasuringModule> modules = new ArrayList<>();

        Coherence[] coherences;
        // *********** Main Coherences for experiments with their overall runtime
        if (testMainCoherences) {
            coherences = new Coherence[] {
                    // UMass
                    new DirectConfirmationBasedCoherence(new OnePreceding(),
                            BooleanDocumentProbabilitySupplier.create(adapter, "bd", true),
                            new LogCondProbConfirmationMeasure(), new ArithmeticMean()),
                    // UCI
                    new DirectConfirmationBasedCoherence(new OneOne(), Palmetto.getSWProbabilityEstimator(10, adapter),
                            new LogRatioConfirmationMeasure(), new ArithmeticMean()),
                    // NPMI
                    new DirectConfirmationBasedCoherence(new OneOne(), Palmetto.getSWProbabilityEstimator(10, adapter),
                            new NormalizedLogRatioConfirmationMeasure(), new ArithmeticMean()),
                    // C_A
                    new VectorBasedCoherence(new OneOne(), caCreator, new CosinusConfirmationMeasure(),
                            new ArithmeticMean()),
                    // C_P
                    new DirectConfirmationBasedCoherence(new OnePreceding(),
                            Palmetto.getSWProbabilityEstimator(70, adapter), new FitelsonConfirmationMeasure(),
                            new ArithmeticMean()),
                    // C_V
                    new VectorBasedCoherence(new OneAll(), cv2Creator, new CosinusConfirmationMeasure(),
                            new ArithmeticMean()),
                    // OneAny
                    new DirectConfirmationBasedCoherence(new OneAny(),
                            BooleanDocumentProbabilitySupplier.create(adapter, "bd", true),
                            new DifferenceBasedConfirmationMeasure(), new ArithmeticMean()) };

            SpeedComparison comparison = new SpeedComparison(seed, numberOfWarmups, numberOfRuns);
            comparison.run(coherences, modules, wordsets);
        }

//        // *********** Runtime influence of segmentators on confirmation measure and aggregation
        if (testSegmentations) {
            Segmentator segmentators[] = { new SetSet(), new OneSet(), new OneAll(), new OnePreceding(),
                    new OneSucceeding(), new OneOne(), new OneAny(), new AnyAny() };
            coherences = new Coherence[segmentators.length];
            for (int i = 0; i < segmentators.length; ++i) {
                TimeMeasuringConfirmation conf = new TimeMeasuringConfirmation(new CosinusConfirmationMeasure(),
                        "conf_" + segmentators[i].getName(), numberOfWarmups, numberOfRuns);
                modules.add(conf);
                TimeMeasuringAggregation agg = new TimeMeasuringAggregation(new ArithmeticMean(),
                        "agg_" + segmentators[i].getName(), numberOfWarmups, numberOfRuns);
                modules.add(agg);
                coherences[i] = new VectorBasedCoherence(segmentators[i], cv2Creator, conf, agg);
            }

            SpeedComparison comparison = new SpeedComparison(seed, numberOfWarmups, numberOfRuns);
            comparison.run(coherences, modules, wordsets);
            for (AbstractTimeMeasuringModule module : modules) {
                module.printStatistics();
            }
        }
        modules.clear();

        // ********** Runtime influence of probability Estimations
        if (testProbEstimations) {
            SpeedComparison comparison = new SpeedComparison(seed, numberOfWarmups, numberOfRuns);
            TimeMeasuringProbabilitySupplier measuringModul;
            DirectConfirmationBasedVectorCreator creator;
            LuceneCorpusAdapter adapter2;

//            // SW / CW
//            measuringModul = new TimeMeasuringProbabilitySupplier(Palmetto.getSWProbabilityEstimator(10, adapter),
//                    "sw(10)", numberOfWarmups, numberOfRuns);
//            modules.add(measuringModul);
//            creator = new DirectConfirmationBasedVectorCreator(measuringModul,
//                    new NormalizedLogRatioConfirmationMeasure());
//            creator.setGamma(1.0);
//            measuringModul = new TimeMeasuringProbabilitySupplier(Palmetto.getSWProbabilityEstimator(110, adapter),
//                    "sw(110)", numberOfWarmups, numberOfRuns);
//            modules.add(measuringModul);
//            cv2Creator = new DirectConfirmationBasedVectorCreator(measuringModul,
//                    new NormalizedLogRatioConfirmationMeasure());
//            cv2Creator.setGamma(1.0);
//            measuringModul = new TimeMeasuringProbabilitySupplier(probEstimatorCA, "cw(5)", numberOfWarmups,
//                    numberOfRuns);
//            modules.add(measuringModul);
//            caCreator = new DirectConfirmationBasedVectorCreator(measuringModul,
//                    new NormalizedLogRatioConfirmationMeasure());
//            caCreator.setGamma(1.0);
//            coherences = new Coherence[] {
//                    new VectorBasedCoherence(new OneAll(), creator, new CosinusConfirmationMeasure(),
//                            new ArithmeticMean()),
//                    new VectorBasedCoherence(new OneAll(), cv2Creator, new CosinusConfirmationMeasure(),
//                            new ArithmeticMean()),
//                    new VectorBasedCoherence(new OneAll(), caCreator, new CosinusConfirmationMeasure(),
//                            new ArithmeticMean()), };
//            comparison.run(coherences, modules, wordsets);
//            for (AbstractTimeMeasuringModule module : modules) {
//                module.printStatistics();
//            }
//            modules.clear();
//            adapter.close();
//
            // BD
            adapter2 = LuceneCorpusAdapter.create(bd2IndexPath, Palmetto.DEFAULT_TEXT_INDEX_FIELD_NAME);
            measuringModul = new TimeMeasuringProbabilitySupplier(
                    BooleanDocumentProbabilitySupplier.create(adapter2, "bd", true), "bd", numberOfWarmups,
                    numberOfRuns);
            modules.add(measuringModul);
            creator = new DirectConfirmationBasedVectorCreator(measuringModul,
                    new NormalizedLogRatioConfirmationMeasure(), 1);
            comparison.run(new Coherence[] { new VectorBasedCoherence(new OneAll(), creator,
                    new CosinusConfirmationMeasure(), new ArithmeticMean()) }, modules, wordsets);
            measuringModul.printStatistics();
            adapter2.close();
            modules.clear();

            // BP
            adapter2 = LuceneCorpusAdapter.create(bpIndexPath, Palmetto.DEFAULT_TEXT_INDEX_FIELD_NAME);
            measuringModul = new TimeMeasuringProbabilitySupplier(
                    BooleanDocumentProbabilitySupplier.create(adapter2, "bp", true), "bp", numberOfWarmups,
                    numberOfRuns);
            modules.add(measuringModul);
            creator = new DirectConfirmationBasedVectorCreator(measuringModul,
                    new NormalizedLogRatioConfirmationMeasure(), 1);
            comparison.run(new Coherence[] { new VectorBasedCoherence(new OneAll(), creator,
                    new CosinusConfirmationMeasure(), new ArithmeticMean()) }, modules, wordsets);
            measuringModul.printStatistics();
            adapter2.close();
            modules.clear();

            // BS
            adapter2 = LuceneCorpusAdapter.create(bsIndexPath, Palmetto.DEFAULT_TEXT_INDEX_FIELD_NAME);
            measuringModul = new TimeMeasuringProbabilitySupplier(
                    BooleanDocumentProbabilitySupplier.create(adapter2, "bs", true), "bs", numberOfWarmups,
                    numberOfRuns);
            modules.add(measuringModul);
            creator = new DirectConfirmationBasedVectorCreator(measuringModul,
                    new NormalizedLogRatioConfirmationMeasure(), 1);
            comparison.run(new Coherence[] { new VectorBasedCoherence(new OneAll(), creator,
                    new CosinusConfirmationMeasure(), new ArithmeticMean()) }, modules, wordsets);
            measuringModul.printStatistics();
            adapter2.close();
            modules.clear();
        }
    }

    /**
     * Measures in nano seconds!
     * 
     * @author Michael R&ouml;der (michael.roeder@uni-paderborn.de)
     *
     */
    public static abstract class AbstractTimeMeasuringModule {
        private final long[] times;
        private int runId;
        private String label;

        public AbstractTimeMeasuringModule(String label, int numberOfWarmups, int numberOfRuns) {
            this.times = new long[numberOfRuns];
            this.runId = -numberOfWarmups;
            this.label = label;
        }

        public void addTimeMeasurement(long time) {
            if ((runId >= 0) && (runId < times.length)) {
                times[runId] += time;
            }
        }

        public void increaseRunId() {
            ++runId;
        }

        public void printStatistics() {
            SpeedComparison.printStatistics(label, times);
        }
    }

    public static class TimeMeasuringAggregation extends AbstractTimeMeasuringModule implements Aggregation {

        protected Aggregation decorated;

        public TimeMeasuringAggregation(Aggregation decorated, int numberOfWarmups, int numberOfRuns) {
            this(decorated, decorated.getName(), numberOfWarmups, numberOfRuns);
        }

        public TimeMeasuringAggregation(Aggregation decorated, String name, int numberOfWarmups, int numberOfRuns) {
            super(name, numberOfWarmups, numberOfRuns);
            this.decorated = decorated;
        }

        @Override
        public double summarize(double[] values) {
            double result = 0;
            long time = System.nanoTime();
            result = decorated.summarize(values);
            time = System.nanoTime() - time;
            addTimeMeasurement(time);
            return result;
        }

        @Override
        public double summarize(double[] values, double[] weights) {
            double result = 0;
            long time = System.nanoTime();
            result = decorated.summarize(values, weights);
            time = System.nanoTime() - time;
            addTimeMeasurement(time);
            return result;
        }

        @Override
        public String getName() {
            return decorated.getName();
        }
    }

    public static class TimeMeasuringConfirmation extends AbstractTimeMeasuringModule
            implements VectorBasedConfirmationMeasure {

        protected VectorBasedConfirmationMeasure decorated;

        public TimeMeasuringConfirmation(VectorBasedConfirmationMeasure decorated, int numberOfWarmups,
                int numberOfRuns) {
            this(decorated, decorated.getName(), numberOfWarmups, numberOfRuns);
        }

        public TimeMeasuringConfirmation(VectorBasedConfirmationMeasure decorated, String name, int numberOfWarmups,
                int numberOfRuns) {
            super(name, numberOfWarmups, numberOfRuns);
            this.decorated = decorated;
        }

        @Override
        public String getName() {
            return decorated.getName();
        }

        @Override
        public double[] calculateConfirmationValues(SubsetVectors subsetVectors) {
            double[] results;
            long time = System.nanoTime();
            results = decorated.calculateConfirmationValues(subsetVectors);
            time = System.nanoTime() - time;
            addTimeMeasurement(time);
            return results;
        }
    }

    public static class TimeMeasuringProbabilitySupplier extends AbstractTimeMeasuringModule
            implements ProbabilityEstimator {

        protected ProbabilityEstimator decorated;

        public TimeMeasuringProbabilitySupplier(String label, int numberOfWarmups, int numberOfRuns) {
            super(label, numberOfWarmups, numberOfRuns);
        }

        public TimeMeasuringProbabilitySupplier(ProbabilityEstimator decorated, int numberOfWarmups, int numberOfRuns) {
            this(decorated, decorated.getName(), numberOfWarmups, numberOfRuns);
        }

        public TimeMeasuringProbabilitySupplier(ProbabilityEstimator decorated, String name, int numberOfWarmups,
                int numberOfRuns) {
            super(name, numberOfWarmups, numberOfRuns);
            this.decorated = decorated;
        }

        @Override
        public String getName() {
            return decorated.getName();
        }

        @Override
        public SubsetProbabilities[] getProbabilities(String[][] wordsets, SegmentationDefinition[] definitions) {
            SubsetProbabilities[] results;
            long time = System.nanoTime();
            results = decorated.getProbabilities(wordsets, definitions);
            time = System.nanoTime() - time;
            addTimeMeasurement(time);
            return results;
        }

        @Override
        public FrequencyDeterminer getFrequencyDeterminer() {
            return decorated.getFrequencyDeterminer();
        }

        @Override
        public void setFrequencyDeterminer(FrequencyDeterminer determiner) {
            decorated.setFrequencyDeterminer(determiner);
        }

        @Override
        public void setMinFrequency(int minFrequency) {
            decorated.setMinFrequency(minFrequency);
        }

    }
}
