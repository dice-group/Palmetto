package org.aksw.palmetto.calculations;

import org.aksw.palmetto.prob.ProbabilitySupplier;
import org.aksw.palmetto.subsets.CountedSubsets;
import org.aksw.palmetto.subsets.OnePreceding;
import org.aksw.palmetto.subsets.SubsetCreator;
import org.aksw.palmetto.subsets.SubsetDefinition;
import org.aksw.palmetto.subsets.SubsetProbabilities;

@Deprecated
public class UMassCoherenceCalculation implements CoherenceCalculation {

    protected SubsetCreator subsetCreator;

    public UMassCoherenceCalculation() {
        this.subsetCreator = new OnePreceding();
    }

    public double[] calculateCoherences(String[][] wordsets,
            ProbabilitySupplier supplier) {
        // create subset definitions
        SubsetDefinition definitions[] = new SubsetDefinition[wordsets.length];
        for (int i = 0; i < definitions.length; i++) {
            definitions[i] = subsetCreator
                    .getSubsetDefinition(wordsets[i].length);
        }

        // get the probabilities
        CountedSubsets countedSubsets[] = supplier.getFrequencyDeterminer().determineCounts(wordsets, definitions);
        definitions = null;

        double coherences[] = new double[countedSubsets.length];
        for (int i = 0; i < countedSubsets.length; i++) {
            coherences[i] = calculateCoherence(countedSubsets[i]);
        }
        return coherences;
    }

    private double calculateCoherence(CountedSubsets countedSubsets) {
        int counts[] = countedSubsets.counts;
        double jointDocFreq, precDocFreq;
        double sum = 0;
        for (int i = 0; i < countedSubsets.segments.length; ++i) {
            for (int j = 0; j < countedSubsets.conditions[i].length; ++j) {
                jointDocFreq = counts[countedSubsets.segments[i] | countedSubsets.conditions[i][j]];
                precDocFreq = counts[countedSubsets.conditions[i][j]];
                sum += Math.log((jointDocFreq + 1) / precDocFreq);
            }
        }
        return sum;
    }

    @Override
    public String getCalculationName() {
        return "m_umass";
    }

    @Override
    public double[] calculateCoherenceValues(SubsetProbabilities subsetProbabilities) {
        throw new IllegalAccessError("This method is not supported yet. The complete class will be replaced.");
    }
}
