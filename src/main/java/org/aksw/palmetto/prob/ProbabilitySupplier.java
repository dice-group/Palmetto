package org.aksw.palmetto.prob;

import org.aksw.palmetto.subsets.SubsetDefinition;
import org.aksw.palmetto.subsets.SubsetProbabilities;

public interface ProbabilitySupplier {

    public SubsetProbabilities[] getProbabilities(String wordsets[][],
            SubsetDefinition definitions[]);

    public FrequencyDeterminer getFrequencyDeterminer();
}
