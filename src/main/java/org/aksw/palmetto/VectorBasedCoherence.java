/**
 * Copyright (C) 2014 Michael Röder (michael.roeder@unister.de)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
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
