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

public class CosinusConfirmationMeasure extends AbstractVectorBasedCalculation {

    @Override
    public String getName() {
        return "m_cos";
    }

    @Override
    protected double calculateSimilarity(double[] vector1, double[] vector2) {
        double length1 = 0,
                length2 = 0,
                sum = 0;
        for (int i = 0; i < vector1.length; ++i) {
            sum += vector1[i] * vector2[i];
            length1 += Math.pow(vector1[i], 2);
            length2 += Math.pow(vector2[i], 2);
        }
        if ((length1 > 0) && (length2 > 0)) {
            return sum / (Math.sqrt(length1) * Math.sqrt(length2));
        } else {
            return 0;
        }
    }

}
