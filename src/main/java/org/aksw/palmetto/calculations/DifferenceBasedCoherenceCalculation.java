package org.aksw.palmetto.calculations;

import org.aksw.palmetto.subsets.SubsetProbabilities;

public class DifferenceBasedCoherenceCalculation implements CoherenceCalculation {

    @Override
    public double[] calculateCoherenceValues(SubsetProbabilities subsetProbabilities) {
        int pos = 0;
        for (int i = 0; i < subsetProbabilities.segments.length; ++i) {
            pos += subsetProbabilities.conditions[i].length;
        }
        double values[] = new double[pos];

        double marginalProbability, conditionalProbability;
        pos = 0;
        for (int i = 0; i < subsetProbabilities.segments.length; ++i) {
            marginalProbability = subsetProbabilities.probabilities[subsetProbabilities.segments[i]];
            if (marginalProbability > 0) {
                for (int j = 0; j < subsetProbabilities.conditions[i].length; ++j) {
                    if (subsetProbabilities.probabilities[subsetProbabilities.conditions[i][j]] > 0) {
                        conditionalProbability = subsetProbabilities.probabilities[subsetProbabilities.segments[i]
                                | subsetProbabilities.conditions[i][j]]
                                / subsetProbabilities.probabilities[subsetProbabilities.conditions[i][j]];
                    } else {
                        conditionalProbability = 0;
                    }
                    values[pos] = conditionalProbability - marginalProbability;
                    ++pos;
                }
            } else {
                pos += subsetProbabilities.conditions[i].length;
            }
        }
        return values;
    }

    @Override
    public String getCalculationName() {
        return "m_d";
    }
}
