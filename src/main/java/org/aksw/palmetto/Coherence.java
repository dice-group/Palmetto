package org.aksw.palmetto;

import org.aksw.palmetto.calculations.CoherenceCalculation;
import org.aksw.palmetto.prob.ProbabilitySupplier;
import org.aksw.palmetto.subsets.SubsetCreator;
import org.aksw.palmetto.subsets.SubsetDefinition;
import org.aksw.palmetto.subsets.SubsetProbabilities;
import org.aksw.palmetto.sum.Summarization;
import org.aksw.palmetto.sum.weighted.AbstractProbabilityBasedWeighter;

public class Coherence {

    protected SubsetCreator subsetCreator;
    protected ProbabilitySupplier probSupplier;
    protected CoherenceCalculation calculation;
    protected Summarization summarizer;
    protected String dataSource = "unkown";

    public Coherence(SubsetCreator subsetCreator, ProbabilitySupplier probSupplier, CoherenceCalculation calculation,
            Summarization summarizer) {
        this.subsetCreator = subsetCreator;
        this.probSupplier = probSupplier;
        this.calculation = calculation;
        this.summarizer = summarizer;
    }

    public Coherence(SubsetCreator subsetCreator, ProbabilitySupplier probSupplier, CoherenceCalculation calculation,
            Summarization summarizer, String dataSource) {
        this.subsetCreator = subsetCreator;
        this.probSupplier = probSupplier;
        this.calculation = calculation;
        this.summarizer = summarizer;
        this.dataSource = dataSource;
    }

    public double[] calculateCoherences(String[][] wordsets) {
        // create subset definitions
        SubsetDefinition definitions[] = new SubsetDefinition[wordsets.length];
        for (int i = 0; i < definitions.length; i++) {
            definitions[i] = subsetCreator
                    .getSubsetDefinition(wordsets[i].length);
        }

        // get the probabilities
        SubsetProbabilities probabilities[] = probSupplier.getProbabilities(
                wordsets, definitions);
        definitions = null;

        double coherences[] = new double[probabilities.length];
        if (summarizer instanceof AbstractProbabilityBasedWeighter) {
            for (int i = 0; i < probabilities.length; i++) {
                coherences[i] = ((AbstractProbabilityBasedWeighter) summarizer).summarize(
                        calculation.calculateCoherenceValues(probabilities[i]), probabilities[i]);
            }
        } else {
            for (int i = 0; i < probabilities.length; i++) {
                coherences[i] = summarizer.summarize(calculation.calculateCoherenceValues(probabilities[i]));
            }
        }
        return coherences;
    }

    public String getName() {
        StringBuilder builder = new StringBuilder();
        builder.append("C(D_");
        builder.append(dataSource);
        builder.append(',');
        builder.append(probSupplier.getProbabilityModelName());
        builder.append(',');
        builder.append(subsetCreator.getName());
        builder.append(',');
        builder.append(calculation.getCalculationName());
        builder.append(',');
        builder.append(summarizer.getName());
        builder.append(')');
        return builder.toString();
    }
}
