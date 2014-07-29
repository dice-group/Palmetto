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

import org.aksw.palmetto.aggregation.Aggregation;
import org.aksw.palmetto.calculations.direct.DirectConfirmationMeasure;
import org.aksw.palmetto.data.SegmentationDefinition;
import org.aksw.palmetto.data.SubsetProbabilities;
import org.aksw.palmetto.prob.ProbabilityEstimator;
import org.aksw.palmetto.subsets.Segmentator;
import org.aksw.palmetto.weight.Weighter;

/**
 * This type of coherence measure is a non-vector-based coherence. It uses only
 * the probabilities derived from the data source to caluclate the coherence.
 * 
 * @author Michael Röder
 * 
 */
@SuppressWarnings("deprecation")
public class DirectConfirmationBasedCoherence implements Coherence {

    /**
     * The segmentation used to create the subset pairs.
     */
    protected Segmentator segmentation;

    /**
     * The probability estimator used to determine the probabilities.
     */
    protected ProbabilityEstimator probEstimator;

    /**
     * The confirmation measure used to rate the single subset pairs.
     */
    protected DirectConfirmationMeasure confirmation;

    /**
     * The aggregator used to aggregate the single ratings of the subset pairs.
     */
    protected Aggregation aggregation;

    @Deprecated
    protected Weighter weighter;

    @Deprecated
    public DirectConfirmationBasedCoherence(Segmentator segmentation, ProbabilityEstimator probEstimator,
            DirectConfirmationMeasure confirmation, Aggregation aggregation, Weighter weighter) {
        this.segmentation = segmentation;
        this.probEstimator = probEstimator;
        this.confirmation = confirmation;
        this.aggregation = aggregation;
        this.weighter = weighter;
    }

    public DirectConfirmationBasedCoherence(Segmentator segmentation, ProbabilityEstimator probEstimator,
            DirectConfirmationMeasure confirmation, Aggregation aggregation) {
        this.segmentation = segmentation;
        this.probEstimator = probEstimator;
        this.confirmation = confirmation;
        this.aggregation = aggregation;
    }

    @Override
    public double[] calculateCoherences(String[][] wordsets) {
        // create subset definitions
        SegmentationDefinition definitions[] = new SegmentationDefinition[wordsets.length];
        for (int i = 0; i < definitions.length; i++) {
            definitions[i] = segmentation.getSubsetDefinition(wordsets[i].length);
        }

        // get the probabilities
        SubsetProbabilities probabilities[] = probEstimator.getProbabilities(wordsets, definitions);
        definitions = null;

        double coherences[] = new double[probabilities.length];
        if (weighter != null) {
            for (int i = 0; i < probabilities.length; i++) {
                coherences[i] = aggregation.summarize(confirmation.calculateConfirmationValues(probabilities[i]),
                        weighter.createWeights(probabilities[i]));
            }
        } else {
            for (int i = 0; i < probabilities.length; i++) {
                coherences[i] = aggregation.summarize(confirmation.calculateConfirmationValues(probabilities[i]));
            }
        }
        return coherences;
    }

    @Override
    public String getName() {
        StringBuilder builder = new StringBuilder();
        builder.append("C(");
        builder.append(probEstimator.getName());
        builder.append(',');
        builder.append(segmentation.getName());
        builder.append(',');
        builder.append(confirmation.getName());
        builder.append(',');
        builder.append(aggregation.getName());
        if (weighter != null) {
            builder.append(',');
            builder.append(weighter.getName());
        }
        builder.append(')');
        return builder.toString();
    }
}
