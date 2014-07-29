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
        double value, min = Double.POSITIVE_INFINITY;
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
