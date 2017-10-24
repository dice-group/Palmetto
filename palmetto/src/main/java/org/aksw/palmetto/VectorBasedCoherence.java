/**
 * Palmetto - Palmetto is a quality measure tool for topics.
 * Copyright © 2014 Data Science Group (DICE) (michael.roeder@uni-paderborn.de)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.aksw.palmetto;

import org.aksw.palmetto.aggregation.Aggregation;
import org.aksw.palmetto.calculations.indirect.VectorBasedConfirmationMeasure;
import org.aksw.palmetto.data.SegmentationDefinition;
import org.aksw.palmetto.data.SubsetVectors;
import org.aksw.palmetto.subsets.Segmentator;
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
    protected Segmentator segmentator;

    /**
     * The vector creator used to determine the vectors for the given words.
     */
    protected VectorCreator vectorCreator;

    /**
     * The confirmation measure used to rate the single subset pairs.
     */
    protected VectorBasedConfirmationMeasure confirmation;

    /**
     * The aggregator used to aggregate the single ratings of the subset pairs.
     */
    protected Aggregation aggregation;

    @Deprecated
    protected Weighter weighter;

    @Deprecated
    public VectorBasedCoherence(Segmentator segmentator, VectorCreator vectorCreator,
            VectorBasedConfirmationMeasure confirmation, Aggregation aggregation, Weighter weighter) {
        this.segmentator = segmentator;
        this.vectorCreator = vectorCreator;
        this.confirmation = confirmation;
        this.aggregation = aggregation;
        this.weighter = weighter;
    }

    public VectorBasedCoherence(Segmentator segmentator, VectorCreator vectorCreator,
            VectorBasedConfirmationMeasure confirmation, Aggregation aggregation) {
        this.segmentator = segmentator;
        this.vectorCreator = vectorCreator;
        this.confirmation = confirmation;
        this.aggregation = aggregation;
    }

    @Override
    public double[] calculateCoherences(String[][] wordsets) {
        // create subset definitions
        SegmentationDefinition definitions[] = new SegmentationDefinition[wordsets.length];
        for (int i = 0; i < definitions.length; i++) {
            definitions[i] = segmentator.getSubsetDefinition(wordsets[i].length);
        }

        // get the probabilities
        SubsetVectors vectors[] = vectorCreator.getVectors(wordsets, definitions);
        definitions = null;

        double coherences[] = new double[vectors.length];
        if (weighter != null) {
            for (int i = 0; i < vectors.length; i++) {
                coherences[i] = aggregation.summarize(confirmation.calculateConfirmationValues(vectors[i]),
                        weighter.createWeights(vectors[i]));
            }
        } else {
            for (int i = 0; i < vectors.length; i++) {
                coherences[i] = aggregation.summarize(confirmation.calculateConfirmationValues(vectors[i]));
            }
        }
        return coherences;
    }

    @Override
    public String getName() {
        StringBuilder builder = new StringBuilder();
        builder.append("C(");
        builder.append(vectorCreator.getProbabilityEstimatorName());
        builder.append(',');
        builder.append(vectorCreator.getVectorSpaceName());
        builder.append(',');
        builder.append(vectorCreator.getVectorCreatorName());
        builder.append(',');
        builder.append(segmentator.getName());
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
