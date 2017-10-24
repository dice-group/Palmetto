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
package org.aksw.palmetto.aggregation;

public class GeometricMean implements Aggregation {

    @Override
    public double summarize(double[] values) {
        double prod = 1;
        for (int i = 0; i < values.length; ++i) {
            if (values[i] <= 0) {
                // the geometric mean is not defined for negative numbers
                return 0;
            }
            prod *= values[i];
        }
        return Math.pow(prod, 1.0 / values.length);
    }

    @Override
    public String getName() {
        return "sigma_g";
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public double summarize(double[] values, double[] weights) {
        double weightSum = 0, prod = 0;
        for (int i = 0; i < values.length; ++i) {
            if (!Double.isNaN(values[i])) {
                if (values[i] <= 0) {
                    // the geometric mean is not defined for negative numbers
                    return 0;
                }

                prod += weights[i] * Math.log(values[i]);
                weightSum += weights[i];
            }
        }

        if (weightSum > 0) {
            return Math.exp(prod / weightSum);
        } else {
            return 0;
        }
    }
}
