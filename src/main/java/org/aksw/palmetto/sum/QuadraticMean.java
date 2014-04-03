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

public class QuadraticMean implements Summarization {

    @Override
    @Deprecated
    public double summarize(double[] values) {
        double sum = 0;
        for (int i = 0; i < values.length; ++i) {
            sum += Math.pow(values[i], 2);
        }
        return Math.sqrt(sum / values.length);
    }

    @Override
    public String getName() {
        return "sigma_q";
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public double summarize(double[] values, double[] weights) {
        double sum = 0, weightSum = 0;
        for (int i = 0; i < values.length; ++i) {
            sum += weights[i] * Math.pow(values[i], 2);
            weightSum += weights[i];
        }
        if (weightSum > 0) {
            return Math.sqrt(sum / weightSum);
        } else {
            return 0;
        }
    }
}
