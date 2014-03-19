package org.aksw.palmetto.weight;

import java.util.Arrays;

import org.aksw.palmetto.subsets.SubsetProbabilities;

public class EqualWeighter implements Weighter {

    @Override
    public double[] createWeights(SubsetProbabilities probabilities) {
        int pos = 0;
        for (int i = 0; i < probabilities.segments.length; ++i) {
            pos += probabilities.conditions[i].length;
        }
        double weights[] = new double[pos];
        Arrays.fill(weights, 1.0);
        return weights;
    }

    @Override
    public String getName() {
        return "E_e";
    }
}
