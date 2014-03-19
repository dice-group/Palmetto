package org.aksw.palmetto.calculations;

import org.aksw.palmetto.subsets.SubsetProbabilities;

public class OlssonsCoherenceCalculation implements CoherenceCalculation {

    @Override
    public double[] calculateCoherenceValues(SubsetProbabilities subsetProbabilities) {
        int pos = 0;
        for (int i = 0; i < subsetProbabilities.segments.length; ++i) {
            pos += subsetProbabilities.conditions[i].length;
        }
        double values[] = new double[pos];

        double intersectionProbability, jointProbability;
        pos = 0;
        for (int i = 0; i < subsetProbabilities.segments.length; ++i) {
            for (int j = 0; j < subsetProbabilities.conditions[i].length; ++j) {
                intersectionProbability = subsetProbabilities.probabilities[subsetProbabilities.segments[i]
                        | subsetProbabilities.conditions[i][j]];
                if (intersectionProbability > 0) {
                    jointProbability = determineJointProbability(subsetProbabilities.segments[i]
                            | subsetProbabilities.conditions[i][j], subsetProbabilities.probabilities);
                    if (jointProbability > 0) {
                        values[pos] = intersectionProbability / jointProbability;
                    }
                }
                ++pos;
            }
        }
        return values;
    }

    private double determineJointProbability(int jointBits, double[] probabilities) {
        double jointProbability = 0;
        int inverseMask = ~jointBits;
        for (int i = 1; i <= jointBits; i++) {
            // if all bits of the current i are elements of the joint set
            if ((inverseMask & i) == 0) {
                // if the number of elements are even
                if ((Integer.bitCount(i) & 1) == 0) {
                    jointProbability -= probabilities[i];
                } else {
                    jointProbability += probabilities[i];
                }
            }
        }
        return jointProbability;
    }

    @Override
    public String getCalculationName() {
        return "m_o";
    }
}
