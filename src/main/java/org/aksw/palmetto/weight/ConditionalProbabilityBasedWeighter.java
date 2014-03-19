package org.aksw.palmetto.weight;

import org.aksw.palmetto.subsets.SubsetProbabilities;

public class ConditionalProbabilityBasedWeighter implements Weighter {

    @Override
    public double[] createWeights(SubsetProbabilities probabilities) {
        int pos = 0;
        for (int i = 0; i < probabilities.segments.length; ++i) {
            pos += probabilities.conditions[i].length;
        }
        double weights[] = new double[pos];

        pos = 0;
        for (int i = 0; i < probabilities.segments.length; ++i) {
            for (int j = 0; j < probabilities.conditions[i].length; ++j) {
                weights[pos] = probabilities.probabilities[probabilities.conditions[i][j]];
                ++pos;
            }
        }

        return weights;
    }

    @Override
    public String getName() {
        return "E_c";
    }
}
