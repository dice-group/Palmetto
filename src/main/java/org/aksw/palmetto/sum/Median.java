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

import java.util.Arrays;

import com.carrotsearch.hppc.DoubleArrayList;

public class Median implements Summarization {

    @Override
    @Deprecated
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
