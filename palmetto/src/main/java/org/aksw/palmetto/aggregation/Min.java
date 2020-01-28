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

public class Min implements Aggregation {

    @Override
    public double summarize(double[] values) {
        double min = Double.POSITIVE_INFINITY;
        for (int i = 0; i < values.length; ++i) {
            if (values[i] < min) {
                min = values[i];
            }
        }
        return min;
    }

    @Override
    public String getName() {
        return "sigma_n";
    }

    @Override
    public double summarize(double[] values, double[] weights) {
        double value,
                min = Double.POSITIVE_INFINITY;
        for (int i = 0; i < values.length; ++i) {
            if (!Double.isNaN(values[i])) {
                value = values[i] * weights[i];
                if (value < min) {
                    min = value;
                }
            }
        }
        if (Double.isInfinite(min)) {
            return RETURN_VALUE_FOR_UNDEFINED;
        } else {
            return min;
        }
    }

}
