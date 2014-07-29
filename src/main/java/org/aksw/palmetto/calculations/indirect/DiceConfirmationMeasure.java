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
package org.aksw.palmetto.calculations.indirect;

public class DiceConfirmationMeasure extends AbstractVectorBasedCalculation {

    @Override
    public String getName() {
        return "m_dice";
    }

    @Override
    protected double calculateSimilarity(double[] vector1, double[] vector2) {
        double minSum = 0, sum = 0;
        for (int i = 0; i < vector1.length; ++i) {
            minSum += vector1[i] < vector2[i] ? vector1[i] : vector2[i];
            sum += vector1[i] + vector2[i];
        }
        if (sum > 0) {
            return 2 * minSum / sum;
        } else {
            // Both vectors have the length 0
            // so both vectors are exactly the zero vector
            return 1;
        }
    }

}
