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
package org.aksw.palmetto.calculations.indirect;

public class DiceConfirmationMeasure extends AbstractVectorBasedCalculation {

    @Override
    public String getName() {
        return "m_dice";
    }

    @Override
    protected double calculateSimilarity(double[] vector1, double[] vector2) {
        double minSum = 0;
        double sum = 0;

        for (int i = 0; i < vector1.length; ++i) {
            minSum += vector1[i] < vector2[i] ? vector1[i] : vector2[i];
            sum += vector1[i] + vector2[i];
        }

        if (sum > 0) {
            return 2 * minSum / sum;
        } else {
            // Both vectors have the length 0
            // so both vectors are exactly the zero vector
            return 1;
        }
    }

}
