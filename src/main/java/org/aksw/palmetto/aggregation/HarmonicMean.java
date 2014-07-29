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
