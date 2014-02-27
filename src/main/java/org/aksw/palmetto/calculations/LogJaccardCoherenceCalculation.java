package org.aksw.palmetto.calculations;

import org.aksw.palmetto.subsets.SubsetCreator;
import org.aksw.palmetto.subsets.SubsetProbabilities;

public class LogJaccardCoherenceCalculation extends AbstractSubsetCreatorBasedCoherenceCalculation {

    public LogJaccardCoherenceCalculation(SubsetCreator subsetCreator) {
        super(subsetCreator);
    }

    @Override
    protected double calculateCoherence(SubsetProbabilities subsetProbabilities) {
        double segmentProbability, intersectionProbability, joinProbability;
        double coherence = 0;
        int count = 0;
        for (int i = 0; i < subsetProbabilities.segments.length; ++i) {
            segmentProbability = subsetProbabilities.probabilities[subsetProbabilities.segments[i]];
            if (segmentProbability > 0) {
                for (int j = 0; j < subsetProbabilities.conditions[i].length; ++j) {
                    joinProbability = segmentProbability + subsetProbabilities.probabilities[subsetProbabilities.conditions[i][j]];
                    intersectionProbability = subsetProbabilities.probabilities[subsetProbabilities.segments[i]
                            | subsetProbabilities.conditions[i][j]];
                    joinProbability -= intersectionProbability;
                    if (joinProbability > 0) {
                        coherence += Math.log(intersectionProbability / joinProbability);
                    }
                }
            }
            count += subsetProbabilities.conditions[i].length;
        }
        return coherence / count;
    }
}
