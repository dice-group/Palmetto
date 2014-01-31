package org.aksw.palmetto.calculations;

import org.aksw.palmetto.prob.ProbabilitySupplier;
import org.aksw.palmetto.subsets.SubsetCreator;
import org.aksw.palmetto.subsets.SubsetDefinition;
import org.aksw.palmetto.subsets.SubsetProbabilities;

public abstract class AbstractSubsetCreatorBasedCoherenceCalculation implements
	CoherenceCalculation {

    protected SubsetCreator subsetCreator;

    public AbstractSubsetCreatorBasedCoherenceCalculation(
	    SubsetCreator subsetCreator) {
	this.subsetCreator = subsetCreator;
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
	SubsetProbabilities probabilities[] = supplier.getProbabilities(
		wordsets, definitions);
	definitions = null;

	double coherences[] = new double[probabilities.length];
	for (int i = 0; i < probabilities.length; i++) {
	    coherences[i] = calculateCoherence(probabilities[i]);
	}
	return coherences;
    }

    protected abstract double calculateCoherence(
	    SubsetProbabilities subsetProbabilities);
}
