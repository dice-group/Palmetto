package org.aksw.palmetto.calculations;

import org.aksw.palmetto.subsets.SubsetCreator;
import org.aksw.palmetto.subsets.SubsetProbabilities;

public class DifferenceBasedCoherenceCalculation extends AbstractSubsetCreatorBasedCoherenceCalculation {

    public DifferenceBasedCoherenceCalculation(SubsetCreator subsetCreator) {
        super(subsetCreator);
    }

    @Override
    protected double calculateCoherence(SubsetProbabilities subsetProbabilities) {
        double marginalProbability, conditionalProbability;
        double coherence = 0;
        int count = 0;
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
                    coherence += conditionalProbability - marginalProbability;
                }
            }
            count += subsetProbabilities.conditions[i].length;
        }
        return coherence / count;
    }
}
