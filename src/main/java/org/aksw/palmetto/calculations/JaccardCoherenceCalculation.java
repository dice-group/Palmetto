package org.aksw.palmetto.calculations;

import org.aksw.palmetto.subsets.SubsetProbabilities;

public class JaccardCoherenceCalculation implements CoherenceCalculation {

    @Override
    public double[] calculateCoherenceValues(SubsetProbabilities subsetProbabilities) {
        int pos = 0;
        for (int i = 0; i < subsetProbabilities.segments.length; ++i) {
            pos += subsetProbabilities.conditions[i].length;
        }
        double values[] = new double[pos];

        double segmentProbability, intersectionProbability, joinProbability;
        pos = 0;
        for (int i = 0; i < subsetProbabilities.segments.length; ++i) {
            segmentProbability = subsetProbabilities.probabilities[subsetProbabilities.segments[i]];
            for (int j = 0; j < subsetProbabilities.conditions[i].length; ++j) {
                joinProbability = segmentProbability
                        + subsetProbabilities.probabilities[subsetProbabilities.conditions[i][j]];
                intersectionProbability = subsetProbabilities.probabilities[subsetProbabilities.segments[i]
                        | subsetProbabilities.conditions[i][j]];
                joinProbability -= intersectionProbability;
                if (joinProbability > 0) {
                    values[pos] = intersectionProbability / joinProbability;
                }
                ++pos;
            }
        }
        return values;
    }

    @Override
    public String getCalculationName() {
        return "m_j";
    }
}
