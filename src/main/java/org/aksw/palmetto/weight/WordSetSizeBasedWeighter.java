package org.aksw.palmetto.weight;

import org.aksw.palmetto.subsets.SubsetProbabilities;

public class WordSetSizeBasedWeighter implements Weighter {

    @Override
    public double[] createWeights(SubsetProbabilities probabilities) {
        // get the number of words the complete word set comprises of
        int numberOfWords = Integer.numberOfTrailingZeros(probabilities.probabilities.length);
        int pos = 0;
        for (int i = 0; i < probabilities.segments.length; ++i) {
            pos += probabilities.conditions[i].length;
        }
        double weights[] = new double[pos];

        pos = 0;
        for (int i = 0; i < probabilities.segments.length; ++i) {
            for (int j = 0; j < probabilities.conditions[i].length; ++j) {
                weights[pos] = ((numberOfWords - Integer.bitCount(probabilities.segments[i]
                        | probabilities.conditions[i][j]))  + 2.0)
                        / numberOfWords;
                ++pos;
            }
        }

        return weights;
    }

    @Override
    public String getName() {
        return "E_l";
    }
}
