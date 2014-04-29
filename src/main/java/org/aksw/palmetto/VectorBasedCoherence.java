package org.aksw.palmetto;

import org.aksw.palmetto.calculations.vectorbased.VectorBasedCalculation;
import org.aksw.palmetto.data.SubsetDefinition;
import org.aksw.palmetto.data.SubsetVectors;
import org.aksw.palmetto.subsets.SubsetCreator;
import org.aksw.palmetto.sum.Summarization;
import org.aksw.palmetto.vector.VectorCreator;
import org.aksw.palmetto.weight.Weighter;

public class VectorBasedCoherence implements Coherence {

    protected SubsetCreator subsetCreator;
    protected VectorCreator vectorCreator;
    protected VectorBasedCalculation calculation;
    protected Summarization summarizer;
    protected Weighter weighter;
    protected String dataSource = "unkown";

    public VectorBasedCoherence(SubsetCreator subsetCreator, VectorCreator vectorCreator,
            VectorBasedCalculation calculation, Summarization summarizer, Weighter weighter) {
        this.subsetCreator = subsetCreator;
        this.vectorCreator = vectorCreator;
        this.calculation = calculation;
        this.summarizer = summarizer;
        this.weighter = weighter;
    }

    public VectorBasedCoherence(SubsetCreator subsetCreator, VectorCreator vectorCreator,
            VectorBasedCalculation calculation, Summarization summarizer, Weighter weighter, String dataSource) {
        this.subsetCreator = subsetCreator;
        this.vectorCreator = vectorCreator;
        this.calculation = calculation;
        this.summarizer = summarizer;
        this.weighter = weighter;
        this.dataSource = dataSource;
    }

    @Override
    public double[] calculateCoherences(String[][] wordsets) {
        // create subset definitions
        SubsetDefinition definitions[] = new SubsetDefinition[wordsets.length];
        for (int i = 0; i < definitions.length; i++) {
            definitions[i] = subsetCreator.getSubsetDefinition(wordsets[i].length);
        }

        // get the probabilities
        SubsetVectors vectors[] = vectorCreator.getVectors(wordsets, definitions);
        definitions = null;

        double coherences[] = new double[vectors.length];
        for (int i = 0; i < vectors.length; i++) {
            coherences[i] = summarizer.summarize(calculation.calculateCoherenceValues(vectors[i]),
                    weighter.createWeights(vectors[i]));
        }
        return coherences;
    }

    @Override
    public String getName() {
        StringBuilder builder = new StringBuilder();
        builder.append("C(D_");
        builder.append(dataSource);
        builder.append(',');
        builder.append(vectorCreator.getProbabilityModelName());
        builder.append(',');
        builder.append(vectorCreator.getVectorSpaceName());
        builder.append(',');
        builder.append(vectorCreator.getVectorCreatorName());
        builder.append(',');
        builder.append(subsetCreator.getName());
        builder.append(',');
        builder.append(calculation.getCalculationName());
        builder.append(',');
        builder.append(summarizer.getName());
        builder.append(',');
        builder.append(weighter.getName());
        builder.append(')');
        return builder.toString();
    }
}
