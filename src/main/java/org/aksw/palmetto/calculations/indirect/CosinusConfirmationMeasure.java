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
package org.aksw.palmetto.calculations.indirect;

public class CosinusConfirmationMeasure extends AbstractVectorBasedCalculation {

    @Override
    public String getName() {
        return "m_cos";
    }

    @Override
    protected double calculateSimilarity(double[] vector1, double[] vector2) {
        double length1 = 0, length2 = 0, sum = 0;
        for (int i = 0; i < vector1.length; ++i) {
            sum += vector1[i] * vector2[i];
            length1 += Math.pow(vector1[i], 2);
            length2 += Math.pow(vector2[i], 2);
        }
        if ((length1 > 0) && (length2 > 0)) {
            return sum / (Math.sqrt(length1) * Math.sqrt(length2));
        } else {
            if ((length1 == 0) && (length2 == 0)) {
                return 1;
            } else {
                return 0;
            }
        }
    }

}
