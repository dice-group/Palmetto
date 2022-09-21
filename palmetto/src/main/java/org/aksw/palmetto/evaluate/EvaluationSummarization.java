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
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.DoubleStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.math3.stat.correlation.KendallsCorrelation;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math3.stat.correlation.SpearmansCorrelation;

import com.carrotsearch.hppc.DoubleArrayList;

/**
 * Summarizes the result files created by the {@link CoherenceEvaluation} class
 * and compares the values with the gold standard files.
 * 
 * @author Michael R&ouml;der (michael.roeder@uni-paderborn.de)
 *
 */
public class EvaluationSummarization {

    public static void main(String[] args) throws IOException {
        if (args.length < 3) {
            System.out.println("Wrong Usage!\nEvaluationSummarization <coherence-file> <gold-file> <output-file>");
            return;
        }
        File inputFile = new File(args[0]);
        File goldStdFile = new File(args[1]);
        File outputFile = new File(args[2]);

        EvaluationSummarization summarization = new EvaluationSummarization();
        summarization.run(inputFile, goldStdFile, outputFile);
    }

    public void run(File inputFile, File goldStdFile, File outputFile) throws IOException {
        // read goldStd
        double[] goldStd = readHumanRatings(goldStdFile);
        // read coherence data and print correlations
        PearsonsCorrelation pCorrelation = new PearsonsCorrelation();
        SpearmansCorrelation sCorrelation = new SpearmansCorrelation();
        KendallsCorrelation kCorrelation = new KendallsCorrelation();
        try (PrintStream pout = new PrintStream(new FileOutputStream(outputFile), true, "UTF-8");
                CoherenceDataIterator iterator = CoherenceDataIterator.create(inputFile, goldStd.length)) {
            while (iterator.hasNext()) {
                CoherenceData data = iterator.next();
                pout.print(data.metaData[0]);
                for (int i = 1; i < data.metaData.length; ++i) {
                    pout.print(',');
                    pout.print(data.metaData[i]);
                }
                if (containsNaN(data.coherenceValues)) {
                    pout.println(",NaN,NaN,NaN");
                } else {
                    pout.print(',');
                    pout.print(pCorrelation.correlation(goldStd, data.coherenceValues));
                    pout.print(',');
                    pout.print(sCorrelation.correlation(goldStd, data.coherenceValues));
                    pout.print(',');
                    pout.println(kCorrelation.correlation(goldStd, data.coherenceValues));
                }
            }
        }
    }

    private boolean containsNaN(double[] values) {
        return DoubleStream.of(values).filter(Double::isNaN).findAny().isPresent();
    }

    public double[] readHumanRatings(File goldStdFile) throws IOException {
        List<String> lines = FileUtils.readLines(goldStdFile, StandardCharsets.UTF_8);

        DoubleArrayList humanRatings = new DoubleArrayList();
        for (String line : lines) {
            humanRatings.add(Double.parseDouble(line));
        }
        return humanRatings.toArray();
    }

    public static class CoherenceData {
        public String[] metaData;
        public double[] coherenceValues;

        public CoherenceData(String[] metaData, double[] coherenceValues) {
            super();
            this.metaData = metaData;
            this.coherenceValues = coherenceValues;
        }

    }

    public static class CoherenceDataIterator implements AutoCloseable, Iterator<CoherenceData> {
        private LineIterator lineIterator;
        private String[] lastLine = null;
        private CoherenceData next = null;
        private int numTopics;

        private CoherenceDataIterator(LineIterator lineIterator, int numTopics) {
            super();
            this.lineIterator = lineIterator;
            this.numTopics = numTopics;
        }

        @Override
        public boolean hasNext() {
            if (next != null) {
                return true;
            }
            String line;
            // check whether we can read something
            if ((lastLine == null) && (lineIterator.hasNext())) {
                // read the line and put it into lastLine
                line = lineIterator.next();
                lastLine = line.split(",");
            }
            if (lastLine != null) {
                // Create the new CoherenceData object by copying the first part of the line
                // into the metadata array. The word set ID is the first field that _does not_
                // belong to the coherence metadata
                next = new CoherenceData(Arrays.copyOf(lastLine, CoherenceEvaluation.CSV_LINE_WORD_SET_ID_ID),
                        new double[numTopics]);
                do {
                    // Add the coherence value from this particular line to the coherence data
                    addValueFromLine(next, lastLine, CoherenceEvaluation.CSV_LINE_WORD_SET_ID_ID,
                            CoherenceEvaluation.CSV_LINE_COHERENCE_ID);
                    // read the line and put it into lastLine
                    lastLine = null;
                    if (lineIterator.hasNext()) {
                        line = lineIterator.next();
                        lastLine = line.split(",");
                    }
                    // Make sure that this line still belongs to the current coherence
                } while (coherencesEqual(next, lastLine, 0, CoherenceEvaluation.CSV_LINE_WORD_SET_ID_ID));
            }
            // If we were able to read something, there is a next object
            return next != null;
        }

        private void addValueFromLine(CoherenceData data, String line[], int csvLineWordSetIdId,
                int csvLineCoherenceId) {
            data.coherenceValues[Integer.parseInt(line[csvLineWordSetIdId])] = Double
                    .parseDouble(line[csvLineCoherenceId]);
        }

        @Override
        public CoherenceData next() {
            CoherenceData result = null;
            if (hasNext()) {
                result = next;
                next = null;
            }
            return result;
        }

        @Override
        public void close() {
            if (lineIterator != null) {
                try {
                    lineIterator.close();
                } catch (Exception e) {
                }
            }
        }

        public boolean coherencesEqual(CoherenceData coherence, String[] metadataArray, int startId, int endId) {
            return subArraysEqual(coherence.metaData, 0, coherence.metaData.length, metadataArray, startId, endId);
        }

        public static CoherenceDataIterator create(File inputFile, int numTopics) throws IOException {
            return new CoherenceDataIterator(FileUtils.lineIterator(inputFile, "UTF-8"), numTopics);
        }
    }

    public static <T> boolean subArraysEqual(T[] array1, int start1, int end1, T[] array2, int start2, int end2) {
        if (array1 == null) {
            return array2 == null;
        } else if (array2 == null) {
            return false;
        }
        int length = end1 - start1;
        if (length != end2 - start2) {
            // The length of the arrays differ
            return false;
        }
        // Compare the elements of the arrays
        for (int i = 0; i < length; ++i) {
            if (array1[i + start1] == null) {
                if (array2[i + start2] != null) {
                    // Element 1 is null but the other isn't
                    return false;
                }
            } else {
                if (!array1[i + start1].equals(array2[i + start2])) {
                    return false;
                }
            }
        }
        return true;
    }
}
