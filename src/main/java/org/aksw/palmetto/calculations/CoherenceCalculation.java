package org.aksw.palmetto.calculations;

import org.aksw.palmetto.prob.ProbabilitySupplier;

public interface CoherenceCalculation {

    public double[] calculateCoherences(String wordsets[][],
	    ProbabilitySupplier supplier);
}
