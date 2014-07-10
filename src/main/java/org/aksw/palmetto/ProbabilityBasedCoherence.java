/**
 * Copyright (C) 2014 Michael Röder (michael.roeder@unister.de)
 *
 * Licensed under the Creative Commons Attribution-NonCommercial 4.0
 * International Public License (the "License").
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://creativecommons.org/licenses/by-nc/4.0/legalcode
 *
 * Unless required by applicable law or agreed to in writing, a file
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.aksw.palmetto;

import org.aksw.palmetto.calculations.probbased.ProbabilityBasedCalculation;
import org.aksw.palmetto.data.SubsetDefinition;
import org.aksw.palmetto.data.SubsetProbabilities;
import org.aksw.palmetto.prob.ProbabilitySupplier;
import org.aksw.palmetto.subsets.SegmentationScheme;
import org.aksw.palmetto.sum.Aggregator;
import org.aksw.palmetto.weight.Weighter;

/**
 * This type of coherence measure is a non-vector-based coherence. It uses only
 * the probabilities derived from the data source to caluclate the coherence.
 * 
 * @author Michael Röder
 * 
 */
@SuppressWarnings("deprecation")
public class ProbabilityBasedCoherence implements Coherence {

    /**
     * The segmentation scheme used to create the subset pairs.
     */
    protected SegmentationScheme subsetScheme;

    /**
     * The probability supplier used to determine the probabilities.
     */
    protected ProbabilitySupplier probSupplier;

    /**
     * The confirmation measure used to rate the single subset pairs.
     */
    protected ProbabilityBasedCalculation calculation;

    /**
     * The aggregator used to aggregate the single ratings of the subset pairs.
     */
    protected Aggregator aggregator;

    @Deprecated
    protected Weighter weighter = null;

    @Deprecated
    public ProbabilityBasedCoherence(SegmentationScheme subsetScheme, ProbabilitySupplier probSupplier,
            ProbabilityBasedCalculation calculation, Aggregator aggregator, Weighter weighter) {
        this.subsetScheme = subsetScheme;
        this.probSupplier = probSupplier;
        this.calculation = calculation;
        this.aggregator = aggregator;
        this.weighter = weighter;
    }

    /**
     * Constructor.
     * 
     * @param subsetScheme
     *            The segmentation scheme used to create the subset pairs.
     * @param probSupplier
     *            The probability supplier used to determine the probabilities.
     * @param calculation
     *            The confirmation measure used to rate the single subset pairs.
     * @param aggregator
     *            The aggregator used to aggregate the single ratings of the
     *            subset pairs.
     */
    public ProbabilityBasedCoherence(SegmentationScheme subsetScheme, ProbabilitySupplier probSupplier,
            ProbabilityBasedCalculation calculation, Aggregator aggregator) {
        this.subsetScheme = subsetScheme;
        this.probSupplier = probSupplier;
        this.calculation = calculation;
        this.aggregator = aggregator;
    }

    @Override
    public double[] calculateCoherences(String[][] wordsets) {
        // create subset definitions
        SubsetDefinition definitions[] = new SubsetDefinition[wordsets.length];
        for (int i = 0; i < definitions.length; i++) {
            definitions[i] = subsetScheme.getSubsetDefinition(wordsets[i].length);
        }

        // get the probabilities
        SubsetProbabilities probabilities[] = probSupplier.getProbabilities(wordsets, definitions);
        definitions = null;

        double coherences[] = new double[probabilities.length];
        for (int i = 0; i < probabilities.length; i++) {
            coherences[i] = aggregator.summarize(calculation.calculateCoherenceValues(probabilities[i]));
            // coherences[i] =
            // aggregator.summarize(calculation.calculateCoherenceValues(probabilities[i]),
            // weighter.createWeights(probabilities[i]));
        }
        return coherences;
    }

    @Override
    public String getName() {
        StringBuilder builder = new StringBuilder();
        builder.append("C(");
        builder.append(probSupplier.getProbabilityModelName());
        builder.append(',');
        builder.append(subsetScheme.getName());
        builder.append(',');
        builder.append(calculation.getCalculationName());
        builder.append(',');
        builder.append(aggregator.getName());
        if (weighter != null) {
            builder.append(',');
            builder.append(weighter.getName());
        }
        builder.append(')');
        return builder.toString();
    }
}
