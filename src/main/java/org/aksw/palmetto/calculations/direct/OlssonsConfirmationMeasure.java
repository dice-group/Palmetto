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
 * This confirmation measure calculates Olssons coherence.
 * 
 * @author Michael Röder
 * 
 */
public class OlssonsConfirmationMeasure extends AbstractUndefinedResultHandlingConfirmationMeasure {

    public OlssonsConfirmationMeasure() {
        super();
    }

    public OlssonsConfirmationMeasure(double resultIfCalcUndefined) {
        super(resultIfCalcUndefined);
    }

    @Override
    public double[] calculateConfirmationValues(SubsetProbabilities subsetProbabilities) {
        int pos = 0;
        for (int i = 0; i < subsetProbabilities.segments.length; ++i) {
            pos += subsetProbabilities.conditions[i].length;
        }
        double values[] = new double[pos];

        double intersectionProbability, jointProbability;
        pos = 0;
        for (int i = 0; i < subsetProbabilities.segments.length; ++i) {
            for (int j = 0; j < subsetProbabilities.conditions[i].length; ++j) {
                intersectionProbability = subsetProbabilities.probabilities[subsetProbabilities.segments[i]
                        | subsetProbabilities.conditions[i][j]];
                jointProbability = determineJointProbability(subsetProbabilities.segments[i]
                        | subsetProbabilities.conditions[i][j], subsetProbabilities.probabilities);
                if (jointProbability > 0) {
                    values[pos] = intersectionProbability / jointProbability;
                } else {
                    values[pos] = resultIfCalcUndefined;
                }
                ++pos;
            }
        }
        return values;
    }

    private double determineJointProbability(int jointBits, double[] probabilities) {
        double jointProbability = 0;
        int inverseMask = ~jointBits;
        for (int i = 1; i <= jointBits; i++) {
            // if all bits of the current i are elements of the joint set
            if ((inverseMask & i) == 0) {
                // if the number of elements are even
                if ((Integer.bitCount(i) & 1) == 0) {
                    jointProbability -= probabilities[i];
                } else {
                    jointProbability += probabilities[i];
                }
            }
        }
        return jointProbability;
    }

    @Override
    public String getName() {
        return "m_o";
    }
}
