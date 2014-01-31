package org.aksw.palmetto.calculations;

import org.aksw.palmetto.subsets.SubsetCreator;
import org.aksw.palmetto.subsets.SubsetProbabilities;

public class DifferenceBasedCoherenceCalculation extends
        AbstractSubsetCreatorBasedCoherenceCalculation {

    public DifferenceBasedCoherenceCalculation(SubsetCreator subsetCreator) {
        super(subsetCreator);
    }

    @Override
    protected double calculateCoherence(SubsetProbabilities subsetProbabilities) {
        double coherence = 0;
        int count = 0;
        for (int i = 0; i < subsetProbabilities.segmentProbabilities.length; ++i) {
            for (int j = 0; j < subsetProbabilities.conditionProbabilities[i].length; ++j) {
                coherence += subsetProbabilities.conditionProbabilities[i][j]
                        - subsetProbabilities.segmentProbabilities[i];
                ++count;
            }
        }
        return coherence / count;
    }
}
