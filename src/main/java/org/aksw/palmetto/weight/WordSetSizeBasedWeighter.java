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
package org.aksw.palmetto.weight;

import org.aksw.palmetto.subsets.SubsetProbabilities;

public class WordSetSizeBasedWeighter implements Weighter {

    @Override
    public double[] createWeights(SubsetProbabilities probabilities) {
        // get the number of words the complete word set comprises of
        int numberOfWords = Integer.numberOfTrailingZeros(probabilities.probabilities.length);
        int pos = 0;
        for (int i = 0; i < probabilities.segments.length; ++i) {
            pos += probabilities.conditions[i].length;
        }
        double weights[] = new double[pos];

        pos = 0;
        for (int i = 0; i < probabilities.segments.length; ++i) {
            for (int j = 0; j < probabilities.conditions[i].length; ++j) {
                weights[pos] = ((numberOfWords - Integer.bitCount(probabilities.segments[i]
                        | probabilities.conditions[i][j]))  + 2.0)
                        / numberOfWords;
                ++pos;
            }
        }

        return weights;
    }

    @Override
    public String getName() {
        return "E_l";
    }
}
