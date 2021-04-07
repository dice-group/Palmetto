package org.aksw.palmetto.io.debug;

import org.aksw.palmetto.data.SegmentationDefinition;
import org.aksw.palmetto.data.SubsetProbabilities;

/**
 * Interface of a class that is used to print additional data from the coherence calculation process.
 * 
 * @author Michael R&ouml;der (michael.roeder@uni-paderborn.de)
 *
 */
public interface DebugPrinter {

    void print(SegmentationDefinition segmentationDefinition, SubsetProbabilities subsetProbabilities);

}
