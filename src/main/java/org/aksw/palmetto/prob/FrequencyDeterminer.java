package org.aksw.palmetto.prob;

import org.aksw.palmetto.subsets.CountedSubsets;
import org.aksw.palmetto.subsets.SubsetDefinition;

public interface FrequencyDeterminer {

    public CountedSubsets[] determineCounts(String wordsets[][],
	    SubsetDefinition definitions[]);
}
