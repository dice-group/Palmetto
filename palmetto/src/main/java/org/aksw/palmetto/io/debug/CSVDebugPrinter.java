package org.aksw.palmetto.io.debug;

import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.stream.IntStream;

import org.aksw.palmetto.data.SegmentationDefinition;
import org.aksw.palmetto.data.SubsetProbabilities;
import org.apache.commons.io.IOUtils;

/**
 * A simple implementation of the {@link DebugPrinter} interface that is used to
 * store additional information to a CSV file.
 * 
 * @author Michael R&ouml;der (michael.roeder@uni-paderborn.de)
 *
 */
public class CSVDebugPrinter implements DebugPrinter, Closeable {

    /**
     * The name of the file to which the probabilities area printed in case the
     * instance has been created with the {@link #create()} method.
     */
    public static final String PROBABILITIES_FILE_NAME = "probabilities.csv";

    /**
     * The internal stream used to print the data.
     */
    private PrintStream debugStream;

    /**
     * Constructor.
     * 
     * @param debugStream the stream to which the debug information is printed
     */
    public CSVDebugPrinter(PrintStream debugStream) {
        this.debugStream = debugStream;
    }

    @Override
    public void print(String[] wordset, SegmentationDefinition segmentationDefinition,
            SubsetProbabilities subsetProbabilities) {
        // Stream segments, conditions and their combinations
        int[] usedElements = IntStream
                .concat(Arrays.stream(segmentationDefinition.segments),
                        IntStream.concat(
                                Arrays.stream(segmentationDefinition.conditions).flatMapToInt(a -> Arrays.stream(a)),
                                IntStream.range(0, segmentationDefinition.segments.length)
                                        .flatMap(i -> Arrays.stream(segmentationDefinition.conditions[i])
                                                .map(c -> c | segmentationDefinition.segments[i]))))
                .distinct().sorted().toArray();
        for (int i = 0; i < usedElements.length; ++i) {
            if (i > 0) {
                debugStream.print(',');
            }
            debugStream.print("\"P(");
            printBitSet(usedElements[i], wordset, debugStream);
            debugStream.print(")\"");
        }
        debugStream.println();
        for (int i = 0; i < usedElements.length; ++i) {
            if (i > 0) {
                debugStream.print(',');
            }
            debugStream.print(subsetProbabilities.probabilities[usedElements[i]]);
        }
        debugStream.println();
    }

    /**
     * Prints the given bitset as probability label. To this end, the word IDs are
     * extracted from the bitset and the words of the given word set are used as
     * content for a "P(...)" label, which is printed to the given stream. If the
     * given wordset is {@code null} the IDs are printed as numbers.
     * 
     * @param bitset  the probability ID as bitset representation of the probability
     *                for which the label should be printed
     * @param wordset the words that will be used (can be null)
     * @param stream  the stream to which the label will be printed
     */
    public static void printBitSet(int bitset, String[] wordset, PrintStream stream) {
        int bit = 1;
        int pos = 1;
        boolean first = true;
        while (bit <= bitset) {
            if ((bit & bitset) > 0) {
                if (first) {
                    first = false;
                } else {
                    stream.print(',');
                }
                if (wordset != null) {
                    // If the words should be printed
                    stream.print(wordset[pos - 1]);
                } else {
                    // Print the IDs
                    stream.print(pos);
                }
            }
            bit = bit << 1;
            ++pos;
        }
    }

    @Override
    public void close() throws IOException {
        IOUtils.closeQuietly(debugStream);
    }

    /**
     * Creates a CSVDebugPrinter instance by opening a {@link PrintStream} to the
     * file with the name {link {@link #PROBABILITIES_FILE_NAME}.
     * 
     * @return a new CSVDebugPrinter instance
     * @throws FileNotFoundException in case the stream to the file can not be
     *                               opened
     */
    public static CSVDebugPrinter create() throws FileNotFoundException {
        PrintStream debugStream = new PrintStream(PROBABILITIES_FILE_NAME);
        return new CSVDebugPrinter(debugStream);
    }
}
