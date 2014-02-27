package org.aksw.palmetto.calculations;

import org.aksw.palmetto.subsets.SubsetProbabilities;

public interface CoherenceCalculation {

    public abstract double[] calculateCoherenceValues(SubsetProbabilities subsetProbabilities);

    public String getCalculationName();
}
