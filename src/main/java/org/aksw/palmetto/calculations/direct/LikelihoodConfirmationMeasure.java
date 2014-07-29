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

import org.aksw.palmetto.calculations.direct.LogBasedCalculation;
import org.aksw.palmetto.data.SubsetProbabilities;

/**
 * This confirmation measure calculates the Likelihood similarity between W' and
 * W*. result = P(W'|W*)/(P(W'|¬W*) + e)
 * 
 * The e is defined by {@link LogBasedCalculation#EPSILON}.
 * 
 * @author Michael Röder
 * 
 */
public class LikelihoodConfirmationMeasure extends AbstractUndefinedResultHandlingConfirmationMeasure {

    public LikelihoodConfirmationMeasure() {
        super();
    }

    public LikelihoodConfirmationMeasure(double resultIfCalcUndefined) {
        super(resultIfCalcUndefined);
    }

    @Override
    public double[] calculateConfirmationValues(SubsetProbabilities subsetProbabilities) {
        int pos = 0;
        for (int i = 0; i < subsetProbabilities.segments.length; ++i) {
            pos += subsetProbabilities.conditions[i].length;
        }
        double values[] = new double[pos];

        double segmentProbability, conditionProbability, intersectionProbability, conditionalProbability, inverseCondProbability;
        pos = 0;
        for (int i = 0; i < subsetProbabilities.segments.length; ++i) {
            segmentProbability = subsetProbabilities.probabilities[subsetProbabilities.segments[i]];
            if (segmentProbability > 0) {
                for (int j = 0; j < subsetProbabilities.conditions[i].length; ++j) {
                    conditionProbability = subsetProbabilities.probabilities[subsetProbabilities.conditions[i][j]];
                    if (conditionProbability > 0) {
                        intersectionProbability = subsetProbabilities.probabilities[subsetProbabilities.segments[i]
                                | subsetProbabilities.conditions[i][j]];
                        conditionalProbability = intersectionProbability / conditionProbability;
                        if (conditionProbability < 1) {
                            inverseCondProbability = (segmentProbability - intersectionProbability)
                                    / (1 - conditionProbability);
                            values[pos] = conditionalProbability
                                    / (inverseCondProbability + LogBasedCalculation.EPSILON);
                        } else {
                            values[pos] = resultIfCalcUndefined;
                        }
                    } else {
                        values[pos] = resultIfCalcUndefined;
                    }
                    ++pos;
                }
            } else {
                for (int j = 0; j < subsetProbabilities.conditions[i].length; ++j) {
                    values[pos] = resultIfCalcUndefined;
                    ++pos;
                }
            }
        }
        return values;
    }

    @Override
    public String getName() {
        return "m_l";
    }
}
