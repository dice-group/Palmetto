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

public class QuadraticMean implements Aggregation {

    @Override
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
        // int count = 0;
        for (int i = 0; i < values.length; ++i) {
            if (!Double.isNaN(values[i])) {
                sum += weights[i] * Math.pow(values[i], 2);
                weightSum += weights[i];
                // ++count;
            }
        }
        if (weightSum > 0) {
            return Math.sqrt(sum / weightSum);
        } else {
            return 0;
        }
    }
}
