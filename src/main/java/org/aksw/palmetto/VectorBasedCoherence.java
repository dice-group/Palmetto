package org.aksw.palmetto;

import org.aksw.palmetto.calculations.vectorbased.VectorBasedCalculation;
import org.aksw.palmetto.data.SubsetDefinition;
import org.aksw.palmetto.data.SubsetVectors;
import org.aksw.palmetto.subsets.SegmentationScheme;
import org.aksw.palmetto.sum.Aggregator;
import org.aksw.palmetto.vector.VectorCreator;
import org.aksw.palmetto.weight.Weighter;

/**
 * This is a vector-based coherence measure.
 * 
 * @author Michael Röder
 * 
 */
@SuppressWarnings("deprecation")
public class VectorBasedCoherence implements Coherence {

    /**
     * The segmentation scheme used to create the subset pairs.
     */
    protected SegmentationScheme subsetScheme;

    /**
     * The vector creator used to determine the vectors for the given words.
     */
    protected VectorCreator vectorCreator;

    /**
     * The confirmation measure used to rate the single subset pairs.
     */
    protected VectorBasedCalculation calculation;

    /**
     * The aggregator used to aggregate the single ratings of the subset pairs.
     */
    protected Aggregator summarizer;

    @Deprecated
    protected Weighter weighter;

    @Deprecated
    public VectorBasedCoherence(SegmentationScheme subsetScheme, VectorCreator vectorCreator,
            VectorBasedCalculation calculation, Aggregator summarizer, Weighter weighter) {
        this.subsetScheme = subsetScheme;
        this.vectorCreator = vectorCreator;
        this.calculation = calculation;
        this.summarizer = summarizer;
        this.weighter = weighter;
    }

    public VectorBasedCoherence(SegmentationScheme subsetScheme, VectorCreator vectorCreator,
            VectorBasedCalculation calculation, Aggregator summarizer) {
        this.subsetScheme = subsetScheme;
        this.vectorCreator = vectorCreator;
        this.calculation = calculation;
        this.summarizer = summarizer;
    }

    @Override
    public double[] calculateCoherences(String[][] wordsets) {
        // create subset definitions
        SubsetDefinition definitions[] = new SubsetDefinition[wordsets.length];
        for (int i = 0; i < definitions.length; i++) {
            definitions[i] = subsetScheme.getSubsetDefinition(wordsets[i].length);
        }

        // get the probabilities
        SubsetVectors vectors[] = vectorCreator.getVectors(wordsets, definitions);
        definitions = null;

        double coherences[] = new double[vectors.length];
        for (int i = 0; i < vectors.length; i++) {
            coherences[i] = summarizer.summarize(calculation.calculateCoherenceValues(vectors[i]));
            // coherences[i] =
            // summarizer.summarize(calculation.calculateCoherenceValues(vectors[i]),
            // weighter.createWeights(vectors[i]));
        }
        return coherences;
    }

    @Override
    public String getName() {
        StringBuilder builder = new StringBuilder();
        builder.append("C(");
        builder.append(vectorCreator.getProbabilityModelName());
        builder.append(',');
        builder.append(vectorCreator.getVectorSpaceName());
        builder.append(',');
        builder.append(vectorCreator.getVectorCreatorName());
        builder.append(',');
        builder.append(subsetScheme.getName());
        builder.append(',');
        builder.append(calculation.getCalculationName());
        builder.append(',');
        builder.append(summarizer.getName());
        if (weighter != null) {
            builder.append(',');
            builder.append(weighter.getName());
        }
        builder.append(')');
        return builder.toString();
    }
}
