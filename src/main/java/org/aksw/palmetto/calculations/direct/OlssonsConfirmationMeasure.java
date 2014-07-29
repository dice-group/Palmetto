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
