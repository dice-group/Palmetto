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
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

/**
 * Generates summaries for single dimensions. It assumes that the data is a CSV
 * file with the following positions:
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
public class SingleDimensionSummarizer {

    protected static final int CSV_LINE_CORR_START = 7;
    protected static final int CSV_LINE_CORR_AVG = 13;

    public static void main(String[] args) throws IOException {
        SingleDimensionSummarizer summarizer = new SingleDimensionSummarizer();

        File inputFile = new File("/home/micha/data/palmetto/sorted-pearson.csv");
        File outputDirectory = new File("/home/micha/data/palmetto/");
        String[] datasetNames = { "20NG", "Genomics", "NYT", "RTLNYT", "RTLWiki", "Movie" };

        summarizer.run(inputFile, outputDirectory, datasetNames);
    }

    public void run(File inputFile, File outputDirectory, String[] datasetNames) throws IOException {
        Map<String, AbsoluteCoherenceValues> probEstResults = new HashMap<>();
        Map<String, AbsoluteCoherenceValues> segmentResults = new HashMap<>();
        Map<String, AbsoluteCoherenceValues> confirmResults = new HashMap<>();
        Map<String, AbsoluteCoherenceValues> aggregationResults = new HashMap<>();
        Map<String, AbsoluteCoherenceValues> datasetResults = new HashMap<>();
        Map<String, AbsoluteCoherenceValues> leftOutDatasetResults = new HashMap<>();

        LineIterator iterator = FileUtils.lineIterator(inputFile, "UTF-8");
        String l;
        String measurePrefix;
        String[] line;
        AbsoluteCoherenceValues coherence = null;
        double averageValue;
        while (iterator.hasNext()) {
            l = iterator.next();
            if (!l.isEmpty()) {
                line = l.split(",");
                try {
                    if (coherence == null) {
                        coherence = AbsoluteCoherenceValues.create(line, CSV_LINE_CORR_START, CSV_LINE_CORR_AVG,
                                CSV_LINE_CORR_AVG);
                    } else {
                        coherence.update(line, CSV_LINE_CORR_START, CSV_LINE_CORR_AVG, CSV_LINE_CORR_AVG);
                    }
                    measurePrefix = "".equals(line[CoherenceEvaluation.CSV_LINE_IN_MEASURE_ID]) ? "d" : "i";
                    updateMap(coherence, probEstResults, measurePrefix + line[CoherenceEvaluation.CSV_LINE_PRB_EST_ID]);
                    updateMap(coherence, segmentResults, measurePrefix + line[CoherenceEvaluation.CSV_LINE_SEG_ID]);
                    updateMap(coherence, confirmResults, line[CoherenceEvaluation.CSV_LINE_IN_MEASURE_ID] + "+"
                            + line[CoherenceEvaluation.CSV_LINE_DIR_MEASURE_ID]);
                    updateMap(coherence, aggregationResults, measurePrefix + line[CoherenceEvaluation.CSV_LINE_AGG_ID]);
                    
                    averageValue = coherence.correlation;
                    // Go over the values of the single datasets
                    for (int i = 0; i < datasetNames.length; ++i) {
                        coherence.setAbsoluteCorrelation(Math.abs(Double.parseDouble(line[CSV_LINE_CORR_START + i])));
                        updateMap(coherence, datasetResults, datasetNames[i]);
                        coherence.setAbsoluteCorrelation(calculateLeftOutCorrelation(coherence, i,
                                CSV_LINE_CORR_START, CSV_LINE_CORR_AVG, averageValue));
                        updateMap(coherence, leftOutDatasetResults, datasetNames[i]);
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Couldn't parse line. It will be ignored. Error: " + e.getMessage());
                }
            }
        }
        printResults(probEstResults, outputDirectory, "probResults.csv");
        printResults(segmentResults, outputDirectory, "segResults.csv");
        printResults(confirmResults, outputDirectory, "confMeasureResults.csv");
        printResults(aggregationResults, outputDirectory, "aggregationResults.csv");
        printResults(datasetResults, outputDirectory, "datasetResults.csv");
        printResults(leftOutDatasetResults, outputDirectory, "leftOutDatasetResults.csv");
    }

    private double calculateLeftOutCorrelation(CoherenceValue coherence, int leftOutId, int startCorrValues,
            int numberOfDatasets, double averageValue) {
        double result = 0;
        try {
            result = averageValue * numberOfDatasets;
            result -= Double.parseDouble(coherence.line[startCorrValues + leftOutId]);
            result /= numberOfDatasets - 1;
        } catch (NumberFormatException e) {
            return 0;
        }
        if (Double.isNaN(result)) {
            result = 0;
        }
        return result;
    }

    protected void updateMap(AbsoluteCoherenceValues newCoherence, Map<String, AbsoluteCoherenceValues> bestResultsMap, String key) {
        if (bestResultsMap.containsKey(key)) {
            bestResultsMap.get(key).updateIfBetter(newCoherence);
        } else {
            bestResultsMap.put(key, new AbsoluteCoherenceValues(newCoherence));
        }
    }

    private void printResults(Map<String, AbsoluteCoherenceValues> results, File outputDirectory, String outputFileName)
            throws IOException {
        File outputFile = new File(outputDirectory.getAbsolutePath() + File.separator + outputFileName);
        try (PrintStream pout = new PrintStream(outputFile)) {
            results.keySet().stream().sorted().forEach(s -> printCoherence(results.get(s), s, pout));
        }
    }

    private void printCoherence(CoherenceValue coherenceValues, String prefix, PrintStream pout) {
        pout.print(prefix);
        String[] line = coherenceValues.line;
        for (int i = 0; i < line.length; ++i) {
            pout.print(',');
            pout.print(line[i]);
        }
        pout.println();
    }

}
