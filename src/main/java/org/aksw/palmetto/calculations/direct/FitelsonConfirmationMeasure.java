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
package org.aksw.palmetto.calculations.direct;

import org.aksw.palmetto.data.SubsetProbabilities;

/**
 * This confirmation measure calculates the difference of the conditional
 * probability of W' given W* and the conditional probability of W' given ¬W*.
 * The difference is normalized by the sum of these two probabilities. result =
 * (P(W'|W*)-P(W'|¬W*))/(P(W'|W*)+P(W'|¬W*))
 * 
 * @author Michael Röder
 * 
 */
public class FitelsonConfirmationMeasure extends AbstractUndefinedResultHandlingConfirmationMeasure {

    public FitelsonConfirmationMeasure() {
        super();
    }

    public FitelsonConfirmationMeasure(double resultIfCalcUndefined) {
        super(resultIfCalcUndefined);
    }

    @Override
    public double[] calculateConfirmationValues(SubsetProbabilities subsetProbabilities) {
        int pos = 0;
        for (int i = 0; i < subsetProbabilities.segments.length; ++i) {
            pos += subsetProbabilities.conditions[i].length;
        }
        double values[] = new double[pos];

        double segmentProbability, intersectionProbability, conditionProbability, conditionalProbability, otherCondProb;
        pos = 0;
        for (int i = 0; i < subsetProbabilities.segments.length; ++i) {
            segmentProbability = subsetProbabilities.probabilities[subsetProbabilities.segments[i]];
            for (int j = 0; j < subsetProbabilities.conditions[i].length; ++j) {
                conditionProbability = subsetProbabilities.probabilities[subsetProbabilities.conditions[i][j]];
                intersectionProbability = subsetProbabilities.probabilities[subsetProbabilities.segments[i]
                        | subsetProbabilities.conditions[i][j]];
                if (conditionProbability > 0) {
                    conditionalProbability = intersectionProbability
                            / subsetProbabilities.probabilities[subsetProbabilities.conditions[i][j]];
                } else {
                    conditionalProbability = 0;
                }
                if (conditionProbability < 1) {
                    otherCondProb = (segmentProbability - intersectionProbability) / (1 - conditionProbability);
                } else {
                    otherCondProb = 0;
                }
                if ((conditionalProbability > 0) || (otherCondProb > 0)) {
                    values[pos] = (conditionalProbability - otherCondProb) / (conditionalProbability + otherCondProb);
                } else {
                    values[pos] = resultIfCalcUndefined;
                }
                ++pos;
            }
        }
        return values;
    }

    @Override
    public String getName() {
        return "m_f";
    }
}