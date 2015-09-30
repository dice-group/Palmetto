/**
 * This file is part of Palmetto.
 *
 * Palmetto is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Palmetto is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Palmetto.  If not, see <http://www.gnu.org/licenses/>.
 */
							(roeder@informatik.uni-leipzig.de)
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
