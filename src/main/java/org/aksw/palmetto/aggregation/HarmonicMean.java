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
