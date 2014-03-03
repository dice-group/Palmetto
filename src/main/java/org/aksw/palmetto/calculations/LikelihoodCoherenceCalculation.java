package org.aksw.palmetto.calculations;

import org.aksw.palmetto.subsets.SubsetProbabilities;

public class LikelihoodCoherenceCalculation implements CoherenceCalculation {

    @Override
    public double[] calculateCoherenceValues(SubsetProbabilities subsetProbabilities) {
        int pos = 0;
        for (int i = 0; i < subsetProbabilities.segments.length; ++i) {
            pos += subsetProbabilities.conditions[i].length;
        }
        double values[] = new double[pos];

        double segmentProbability, conditionProbability, intersectionProbability, conditionalProbability, inverseCondProbability;
        pos = 0;
        for (int i = 0; i < subsetProbabilities.segments.length; ++i) {
            segmentProbability = subsetProbabilities.probabilities[subsetProbabilities.segments[i]];
            if (segmentProbability > 0) {
                for (int j = 0; j < subsetProbabilities.conditions[i].length; ++j) {
                    conditionProbability = subsetProbabilities.probabilities[subsetProbabilities.conditions[i][j]];
                    if (conditionProbability > 0) {
                        intersectionProbability = subsetProbabilities.probabilities[subsetProbabilities.segments[i]
                                | subsetProbabilities.conditions[i][j]];
                        conditionalProbability = intersectionProbability / conditionProbability;
                        inverseCondProbability = (segmentProbability - intersectionProbability)
                                / (1 - conditionProbability);
                        if (inverseCondProbability > 0) {
                            values[pos] = conditionalProbability / inverseCondProbability;
                        }
                    }
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
        return "m_l";
    }
}
