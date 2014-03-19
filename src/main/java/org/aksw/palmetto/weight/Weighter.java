package org.aksw.palmetto.weight;

import org.aksw.palmetto.subsets.SubsetProbabilities;

public interface Weighter {

    public double[] createWeights(SubsetProbabilities probabilities);
    
    public String getName();
}
