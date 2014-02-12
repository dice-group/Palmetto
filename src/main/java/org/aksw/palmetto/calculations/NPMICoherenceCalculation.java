package org.aksw.palmetto.calculations;

import org.aksw.palmetto.subsets.SubsetCreator;
import org.aksw.palmetto.subsets.SubsetProbabilities;

public class NPMICoherenceCalculation extends AbstractSubsetCreatorBasedCoherenceCalculation {

    public NPMICoherenceCalculation(SubsetCreator subsetCreator) {
        super(subsetCreator);
    }

    @Override
    protected double calculateCoherence(SubsetProbabilities subsetProbabilities) {
        double segmentProbability, conditionProbability, jointProbability;
        double coherence = 0;
        int count = 0;
        for (int i = 0; i < subsetProbabilities.segments.length; ++i) {
            segmentProbability = subsetProbabilities.probabilities[subsetProbabilities.segments[i]];
            if (segmentProbability > 0) {
                for (int j = 0; j < subsetProbabilities.conditions[i].length; ++j) {
                    conditionProbability = subsetProbabilities.probabilities[subsetProbabilities.conditions[i][j]];
                    jointProbability = subsetProbabilities.probabilities[subsetProbabilities.segments[i]
                            | subsetProbabilities.conditions[i][j]];
                    if ((conditionProbability > 0) && (jointProbability > 0)) {
                        coherence += Math.log(jointProbability / (segmentProbability * conditionProbability))
                                / -Math.log(jointProbability);
                    }
                }
            }
            count += subsetProbabilities.conditions[i].length;
        }
        return coherence / count;
    }
}
