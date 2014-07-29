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
package org.aksw.palmetto.calculations.direct;

import org.aksw.palmetto.data.SubsetProbabilities;

/**
 * This confirmation measure calculates the difference of the conditional
 * probability of W' given W* and the conditional probability of W' given ¬W*.
 * The difference is normalized by the sum of these two probabilities. result =
 * (P(W'|W*)-P(W'|¬W*))/(P(W'|W*)+P(W'|¬W*))
 * 
 * @author Michael Röder
 * 
 */
public class FitelsonConfirmationMeasure extends AbstractUndefinedResultHandlingConfirmationMeasure {

    public FitelsonConfirmationMeasure() {
        super();
    }

    public FitelsonConfirmationMeasure(double resultIfCalcUndefined) {
        super(resultIfCalcUndefined);
    }

    @Override
    public double[] calculateConfirmationValues(SubsetProbabilities subsetProbabilities) {
        int pos = 0;
        for (int i = 0; i < subsetProbabilities.segments.length; ++i) {
            pos += subsetProbabilities.conditions[i].length;
        }
        double values[] = new double[pos];

        double segmentProbability, intersectionProbability, conditionProbability, conditionalProbability, otherCondProb;
        pos = 0;
        for (int i = 0; i < subsetProbabilities.segments.length; ++i) {
            segmentProbability = subsetProbabilities.probabilities[subsetProbabilities.segments[i]];
            for (int j = 0; j < subsetProbabilities.conditions[i].length; ++j) {
                conditionProbability = subsetProbabilities.probabilities[subsetProbabilities.conditions[i][j]];
                intersectionProbability = subsetProbabilities.probabilities[subsetProbabilities.segments[i]
                        | subsetProbabilities.conditions[i][j]];
                if (conditionProbability > 0) {
                    conditionalProbability = intersectionProbability
                            / subsetProbabilities.probabilities[subsetProbabilities.conditions[i][j]];
                } else {
                    conditionalProbability = 0;
                }
                if (conditionProbability < 1) {
                    otherCondProb = (segmentProbability - intersectionProbability) / (1 - conditionProbability);
                } else {
                    otherCondProb = 0;
                }
                if ((conditionalProbability > 0) || (otherCondProb > 0)) {
                    values[pos] = (conditionalProbability - otherCondProb) / (conditionalProbability + otherCondProb);
                } else {
                    values[pos] = resultIfCalcUndefined;
                }
                ++pos;
            }
        }
        return values;
    }

    @Override
    public String getName() {
        return "m_f";
    }
}
