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
 * This confirmation measure calculates Shogenjis coherence.
 * 
 * @author Michael Röder
 * 
 */
public class ShogenjisConfirmationMeasure extends AbstractUndefinedResultHandlingConfirmationMeasure {

    public ShogenjisConfirmationMeasure() {
        super();
    }

    public ShogenjisConfirmationMeasure(double resultIfCalcUndefined) {
        super(resultIfCalcUndefined);
    }

    @Override
    public double[] calculateConfirmationValues(SubsetProbabilities subsetProbabilities) {
        int numberOfPairs = 0;
        for (int i = 0; i < subsetProbabilities.segments.length; ++i) {
            numberOfPairs += subsetProbabilities.conditions[i].length;
        }
        double values[] = new double[numberOfPairs];

        double conditionProbability,
                intersectionProbability;
        int pos = 0;
        for (int i = 0; i < subsetProbabilities.segments.length; ++i) {
            // if (subsetProbabilities.probabilities[subsetProbabilities.segments[i]] > 0) {
            for (int j = 0; j < subsetProbabilities.conditions[i].length; ++j) {
                conditionProbability = subsetProbabilities.probabilities[subsetProbabilities.conditions[i][j]];
                intersectionProbability = subsetProbabilities.probabilities[subsetProbabilities.segments[i]
                        | subsetProbabilities.conditions[i][j]];
                // if (conditionProbability > 0) {
                values[pos] = Math.log(intersectionProbability + LogBasedCalculation.EPSILON) - numberOfPairs
                        * Math.log(conditionProbability + LogBasedCalculation.EPSILON);
                // } else {
                // values[pos] = resultIfCalcUndefined;
                // }
                ++pos;
            }
            // } else {
            // for (int j = 0; j < subsetProbabilities.conditions[i].length; ++j) {
            // values[pos] = resultIfCalcUndefined;
            // ++pos;
            // }
            // }
        }
        return values;
    }

    @Override
    public String getName() {
        return "m_ls";
    }
}
