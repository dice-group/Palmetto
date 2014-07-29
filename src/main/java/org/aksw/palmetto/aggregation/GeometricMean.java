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
