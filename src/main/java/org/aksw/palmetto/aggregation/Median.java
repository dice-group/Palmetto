/**
 * Copyright (C) 2014 Michael Röder (michael.roeder@unister.de)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
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
