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

import java.util.Arrays;

import com.carrotsearch.hppc.DoubleArrayList;

public class Median implements Aggregation {

    @Override
    public double summarize(double[] values) {
        if (values.length == 0) {
            throw new IllegalArgumentException(
                    "The given array has to have at least one element to determine the modus.");
        }
        Arrays.sort(values);
        if ((values.length & 1) > 0) {
            return values[values.length / 2];
        } else {
            return (values[values.length / 2] + values[(values.length / 2) - 1]) / 2;
        }
    }

    @Override
    public String getName() {
        return "sigma_m";
    }

    @Override
    public double summarize(double[] values, double[] weights) {
        if (values.length == 0) {
            throw new IllegalArgumentException(
                    "The given array has to have at least one element to determine the modus.");
        }
        DoubleArrayList weightedValues = new DoubleArrayList(values.length);
        for (int i = 0; i < values.length; ++i) {
            if (!Double.isNaN(values[i])) {
                weightedValues.add(weights[i] * values[i]);
            }
        }
        if (weightedValues.size() == 0) {
            return 0;
        }
        double weightedValuesAsArray[] = weightedValues.toArray();
        Arrays.sort(weightedValuesAsArray);
        if ((weightedValuesAsArray.length & 1) > 0) {
            return weightedValuesAsArray[weightedValuesAsArray.length / 2];
        } else {
            return (weightedValuesAsArray[weightedValuesAsArray.length / 2] + weightedValuesAsArray[(weightedValuesAsArray.length / 2) - 1]) / 2.0;
        }
    }

}
