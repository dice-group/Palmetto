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
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.aksw.palmetto.evaluate.rank.Ranker;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

/**
 * Generates summaries for single dimensions using the ranks of coherence
 * calculations. It assumes that the data is a CSV file with the following
 * positions:
 * 
 * <ul>
 * <li>0: Probability estimation type</li>
 * <li>1: Window size (or empty if no window)</li>
 * <li>2: Segmentation type</li>
 * <li>3: Indirect measure type (or empty)</li>
 * <li>4: Gamma value of the indirect measure type (or empty)</li>
 * <li>5: Direct measure type</li>
 * <li>6: Aggregation type</li>
 * <li>7+: Correlation values for datasets</li>
 * <li>last: Average correlation value</li>
 * </ol>
 * 
 * @author Michael R&ouml;der (michael.roeder@uni-paderborn.de)
 *
 */
public class RankBasedSummarizer {

    protected static final int CSV_LINE_CORR_START = 7;
    protected static final int CSV_LINE_CORR_AVG = 13;

    public static void main(String[] args) throws IOException, CloneNotSupportedException {
        RankBasedSummarizer summarizer = new RankBasedSummarizer();

        File inputFile = new File("/home/micha/data/palmetto/sorted-pearson.csv");
        File outputDirectory = new File("/home/micha/data/palmetto/");
        String[] datasetNames = { "20NG", "Genomics", "NYT", "RTLNYT", "RTLWiki", "Movie" };

        summarizer.run(inputFile, outputDirectory, datasetNames);
    }

    public void run(File inputFile, File outputDirectory, String[] datasetNames)
            throws IOException, CloneNotSupportedException {
        Map<String, CoherenceValue> probEstResults = new HashMap<>();
        Map<String, CoherenceValue> segmentResults = new HashMap<>();
        Map<String, CoherenceValue> confirmResults = new HashMap<>();
        Map<String, CoherenceValue> aggregationResults = new HashMap<>();
//        Map<String, CoherenceValue> datasetResults = new HashMap<>();
        Map<String, CoherenceValue> leftOutDatasetResults = new HashMap<>();

        // Read the file
        List<CoherenceValueArray> coherences = readFile(inputFile);

        Ranker ranker = new Ranker();
        double values[] = new double[coherences.size()];
        for (int i = 0; i < datasetNames.length; ++i) {
            System.out.println("Ranking coherences based on " + datasetNames[i]);
            for (int j = 0; j < values.length; ++j) {
                values[j] = coherences.get(j).getValues()[i];
            }
            ranker.rank(values, false);
            // Write ranks back to the coherence objects
            for (int j = 0; j < values.length; ++j) {
                coherences.get(j).getValues()[i] = values[j];
            }
        }
        System.out.println("Ranking finished. Creating summaries...");

        String measurePrefix;
        try (PrintStream pout = new PrintStream(outputDirectory.getAbsolutePath() + "/rankedResults.csv")) {
            for (CoherenceValueArray coherence : coherences) {
                // Set the average rank
                coherence.summarize();
                printCoherence(coherence, "", pout);

                measurePrefix = "".equals(coherence.getLine()[CoherenceEvaluation.CSV_LINE_IN_MEASURE_ID]) ? "d" : "i";
                updateMap(coherence, probEstResults,
                        measurePrefix + coherence.getLine()[CoherenceEvaluation.CSV_LINE_PRB_EST_ID]);
                updateMap(coherence, segmentResults,
                        measurePrefix + coherence.getLine()[CoherenceEvaluation.CSV_LINE_SEG_ID]);
                updateMap(coherence, confirmResults, coherence.getLine()[CoherenceEvaluation.CSV_LINE_IN_MEASURE_ID]
                        + "+" + coherence.getLine()[CoherenceEvaluation.CSV_LINE_DIR_MEASURE_ID]);
                updateMap(coherence, aggregationResults,
                        measurePrefix + coherence.getLine()[CoherenceEvaluation.CSV_LINE_AGG_ID]);

//                 Go over the values of the single datasets
                for (int i = 0; i < datasetNames.length; ++i) {
//                coherence.setCorrelation(Math.abs(Double.parseDouble(line[CSV_LINE_CORR_START + i])));
//                    updateMap(coherence, datasetResults, datasetNames[i]);
                    updateMap(calculateLeftOutAverage(coherence, i), leftOutDatasetResults, datasetNames[i]);
                }
            }
        }
        System.out.println("Summaries created. Writing files... ");
        printResults(probEstResults, outputDirectory, "probResults.csv");
        printResults(segmentResults, outputDirectory, "segResults.csv");
        printResults(confirmResults, outputDirectory, "confMeasureResults.csv");
        printResults(aggregationResults, outputDirectory, "aggregationResults.csv");
//        printResults(datasetResults, outputDirectory, "datasetResults.csv");
        printResults(leftOutDatasetResults, outputDirectory, "leftOutDatasetResults.csv");
    }

//    private void setAverageRank(CoherenceValueArray coherence) {
//        coherence.setCorrelation(Arrays.stream(coherence.getValues()).average().getAsDouble());
//    }

    private static List<CoherenceValueArray> readFile(File inputFile) throws IOException {
        LineIterator iterator = FileUtils.lineIterator(inputFile, "UTF-8");
        String l;
        String[] line;
        List<CoherenceValueArray> coherences = new ArrayList<>();
        while (iterator.hasNext()) {
            l = iterator.next();
            if (!l.isEmpty()) {
                line = l.split(",");
                try {
                    coherences.add(CoherenceValueArray.create(line, CSV_LINE_CORR_START, CSV_LINE_CORR_AVG,
                            CSV_LINE_CORR_AVG));
                } catch (NumberFormatException e) {
                    System.out.println("Couldn't parse line. It will be ignored. Error: " + e.getMessage());
                }
            }
        }
        return coherences;
    }

    private CoherenceValueArray calculateLeftOutAverage(CoherenceValueArray coherence, int leftOutId)
            throws CloneNotSupportedException {
        CoherenceValueArray result = (CoherenceValueArray) coherence.clone();
        double looAverage;
        double looStdDev;
        double leftOutValue = coherence.getValues()[leftOutId];
        int numberOfDatasets = coherence.getValues().length;

        looAverage = coherence.getCorrelation() * numberOfDatasets;
        looAverage -= leftOutValue;
        looAverage /= numberOfDatasets - 1;
        result.setCorrelation(looAverage);
        looStdDev = coherence.getStdDev() * coherence.getStdDev();
        looStdDev *= numberOfDatasets;
        looStdDev -= Math.pow(coherence.getCorrelation() - leftOutValue, 2);
        looStdDev /= numberOfDatasets - 1;
        looStdDev = Math.sqrt(looStdDev);
        result.setStdDev(looStdDev);
        return result;
    }

    protected void updateMap(CoherenceValue newCoherence, Map<String, CoherenceValue> bestResultsMap, String key)
            throws CloneNotSupportedException {
        if (bestResultsMap.containsKey(key)) {
            bestResultsMap.get(key).updateIfBetter(newCoherence, false);
        } else {
            bestResultsMap.put(key, (CoherenceValue) newCoherence.clone());
        }
    }

    private void printResults(Map<String, CoherenceValue> results, File outputDirectory, String outputFileName)
            throws IOException {
        File outputFile = new File(outputDirectory.getAbsolutePath() + File.separator + outputFileName);
        try (PrintStream pout = new PrintStream(outputFile)) {
            results.keySet().stream().sorted().forEach(s -> printCoherence(results.get(s), s, pout));
        }
    }

    private void printCoherence(CoherenceValue coherenceValue, String prefix, PrintStream pout) {
        pout.print(prefix);
        String[] line = coherenceValue.getLine();
        for (int i = 0; i < line.length; ++i) {
            pout.print(',');
            pout.print(line[i]);
        }
        if (coherenceValue instanceof CoherenceValueArray) {
            CoherenceValueArray cva = (CoherenceValueArray) coherenceValue;
            double[] values = cva.getValues();
            for (int i = 0; i < values.length; ++i) {
                pout.print(',');
                pout.print(values[i]);
            }
        }
        pout.print(',');
        pout.print(coherenceValue.getCorrelation());
        if (coherenceValue instanceof CoherenceValueArray) {
            pout.print(',');
            pout.print(((CoherenceValueArray) coherenceValue).getStdDev());
        }
        pout.println();
    }
}
