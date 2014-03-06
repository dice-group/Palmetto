package org.aksw.palmetto.sum.weighted;

import org.aksw.palmetto.subsets.SubsetProbabilities;

public class SimpleProbabilityBasedWeighter extends AbstractProbabilityBasedWeighter {

    public SimpleProbabilityBasedWeighter(WeightedSummarization summarizer) {
        super(summarizer);
    }

    @Override
    protected double[] createWeights(SubsetProbabilities probabilities) {
        int pos = 0;
        for (int i = 0; i < probabilities.segments.length; ++i) {
            pos += probabilities.conditions[i].length;
        }
        double weights[] = new double[pos];

        double segmentProbability;
        pos = 0;
        for (int i = 0; i < probabilities.segments.length; ++i) {
            segmentProbability = probabilities.probabilities[probabilities.segments[i]];
            for (int j = 0; j < probabilities.conditions[i].length; ++j) {
                weights[pos] = segmentProbability + probabilities.probabilities[probabilities.conditions[i][j]];
                ++pos;
            }
        }

        return weights;
    }

    @Override
    public String getName() {
        return summarizer.getName() + "p";
    }
}
