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

public class HarmonicMean implements Aggregation {

    @Override
    public double summarize(double[] values) {
        double sum = 0;
        for (int i = 0; i < values.length; ++i) {
            if (values[i] == 0) {
                // if one if the values is 0 the harmonic mean goes against 0
                return 0;
            }
            sum += 1.0 / values[i];
        }
        if (sum == 0) {
            return 0;
        } else {
            return values.length / sum;
        }
    }

    @Override
    public String getName() {
        return "sigma_h";
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public double summarize(double[] values, double[] weights) {
        double weightSum = 0, sum = 0;
        for (int i = 0; i < values.length; ++i) {
            if (!Double.isNaN(values[i])) {
                if (values[i] == 0) {
                    // if one if the values is 0 the harmonic mean goes against 0
                    return 0;
                }
                sum += weights[i] / values[i];
                weightSum += weights[i];
            }
        }
        if (sum == 0) {
            return 0;
        } else {
            return weightSum / sum;
        }
    }
}
