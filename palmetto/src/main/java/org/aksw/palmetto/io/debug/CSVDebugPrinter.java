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

public class CSVDebugPrinter implements DebugPrinter, Closeable {

    public static final String PROBABILITIES_FILE_NAME = "probabilities.csv";

    private PrintStream debugStream;

    private int[] usedElements = null;

    public CSVDebugPrinter(PrintStream debugStream) {
        this.debugStream = debugStream;
    }

    @Override
    public void print(SegmentationDefinition segmentationDefinition, SubsetProbabilities subsetProbabilities) {
        if (usedElements == null) {
            // Stream segments, conditions and their combinations
            usedElements = IntStream
                    .concat(Arrays.stream(segmentationDefinition.segments), IntStream.concat(
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
                printBitSet(usedElements[i], debugStream);
                debugStream.print(")\"");
            }
            debugStream.println();
        }
        for (int i = 0; i < usedElements.length; ++i) {
            if (i > 0) {
                debugStream.print(',');
            }
            debugStream.print(subsetProbabilities.probabilities[usedElements[i]]);
        }
        debugStream.println();
    }

    public static void printBitSet(int bitset, PrintStream stream) {
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
                stream.print(pos);
            }
            bit = bit << 1;
            ++pos;
        }
    }

    @Override
    public void close() throws IOException {
        IOUtils.closeQuietly(debugStream);
    }

    public static CSVDebugPrinter create() throws FileNotFoundException {
        PrintStream debugStream = new PrintStream(PROBABILITIES_FILE_NAME);
        return new CSVDebugPrinter(debugStream);
    }
}
