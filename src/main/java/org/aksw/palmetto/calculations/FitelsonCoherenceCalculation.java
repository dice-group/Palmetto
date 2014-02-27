package org.aksw.palmetto.calculations;

import org.aksw.palmetto.subsets.SubsetProbabilities;

public class FitelsonCoherenceCalculation implements CoherenceCalculation {

    @Override
    public double[] calculateCoherenceValues(SubsetProbabilities subsetProbabilities) {
        int pos = 0;
        for (int i = 0; i < subsetProbabilities.segments.length; ++i) {
            pos += subsetProbabilities.conditions[i].length;
        }
        double values[] = new double[pos];

        double segmentProbability, intersectionProbability, conditionProbability, conditionalProbability, otherCondProb;
        pos = 0;
        for (int i = 0; i < subsetProbabilities.segments.length; ++i) {
            segmentProbability = subsetProbabilities.probabilities[subsetProbabilities.segments[i]];
            for (int j = 0; j < subsetProbabilities.conditions[i].length; ++j) {
                conditionProbability = subsetProbabilities.probabilities[subsetProbabilities.conditions[i][j]];
                intersectionProbability = subsetProbabilities.probabilities[subsetProbabilities.segments[i]
                        | subsetProbabilities.conditions[i][j]];
                if (conditionProbability > 0) {
                    conditionalProbability = intersectionProbability
                            / subsetProbabilities.probabilities[subsetProbabilities.conditions[i][j]];
                } else {
                    conditionalProbability = 0;
                }
                if (conditionProbability < 1) {
                    otherCondProb = (segmentProbability - intersectionProbability) / (1 - conditionProbability);
                } else {
                    otherCondProb = 0;
                }
                values[pos] = (conditionalProbability - otherCondProb) / (conditionalProbability + otherCondProb);
                ++pos;
            }
        }
        return values;
    }

    @Override
    public String getCalculationName() {
        return "m_f";
    }
}
