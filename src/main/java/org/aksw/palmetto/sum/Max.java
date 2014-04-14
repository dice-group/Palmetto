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
package org.aksw.palmetto.sum;

public class Max implements Summarization {

    @Override
    @Deprecated
    public double summarize(double[] values) {
        double max = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < values.length; ++i) {
            if (values[i] > max) {
                max = values[i];
            }
        }
        return max;
    }

    @Override
    public String getName() {
        return "sigma_x";
    }

    @Override
    public double summarize(double[] values, double[] weights) {
        double value, max = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < values.length; ++i) {
            if (!Double.isNaN(values[i])) {
                value = values[i] * weights[i];
                if (value > max) {
                    max = value;
                }
            }
        }
        if (Double.isInfinite(max)) {
            return RETURN_VALUE_FOR_UNDEFINED;
        } else {
            return max;
        }
    }

}
