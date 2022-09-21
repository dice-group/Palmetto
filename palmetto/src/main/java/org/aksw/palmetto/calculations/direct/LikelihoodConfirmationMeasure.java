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
package org.aksw.palmetto.calculations.direct;

import org.aksw.palmetto.data.SubsetProbabilities;

/**
 * This confirmation measure calculates the Likelihood similarity between W' and
 * W*. result = P(W'|W*)/(P(W'|¬W*) + e)
 * 
 * The e is defined by {@link LogBasedCalculation#EPSILON}.
 * 
 * @author Michael Röder
 * 
 */
public class LikelihoodConfirmationMeasure extends AbstractUndefinedResultHandlingConfirmationMeasure {

    public LikelihoodConfirmationMeasure() {
        super();
    }

    public LikelihoodConfirmationMeasure(double resultIfCalcUndefined) {
        super(resultIfCalcUndefined);
    }

    @Override
    public double[] calculateConfirmationValues(SubsetProbabilities subsetProbabilities) {
        int pos = 0;
        for (int i = 0; i < subsetProbabilities.segments.length; ++i) {
            pos += subsetProbabilities.conditions[i].length;
        }
        double values[] = new double[pos];

        double segmentProbability,
                conditionProbability,
                intersectionProbability,
                conditionalProbability,
                inverseCondProbability;
        pos = 0;
        for (int i = 0; i < subsetProbabilities.segments.length; ++i) {
            segmentProbability = subsetProbabilities.probabilities[subsetProbabilities.segments[i]];
            if (segmentProbability > 0) {
                for (int j = 0; j < subsetProbabilities.conditions[i].length; ++j) {
                    conditionProbability = subsetProbabilities.probabilities[subsetProbabilities.conditions[i][j]];
                    if (conditionProbability > 0) {
                        intersectionProbability = subsetProbabilities.probabilities[subsetProbabilities.segments[i]
                                | subsetProbabilities.conditions[i][j]];
                        conditionalProbability = intersectionProbability / conditionProbability;
                        if (conditionProbability < 1) {
                            inverseCondProbability = (segmentProbability - intersectionProbability)
                                    / (1 - conditionProbability);
                            values[pos] = conditionalProbability
                                    / (inverseCondProbability + LogBasedCalculation.EPSILON);
                        } else {
                            values[pos] = resultIfCalcUndefined;
                        }
                    } else {
                        values[pos] = resultIfCalcUndefined;
                    }
                    ++pos;
                }
            } else {
                for (int j = 0; j < subsetProbabilities.conditions[i].length; ++j) {
                    values[pos] = resultIfCalcUndefined;
                    ++pos;
                }
            }
        }
        return values;
    }

    @Override
    public String getName() {
        return "m_l";
    }
}
