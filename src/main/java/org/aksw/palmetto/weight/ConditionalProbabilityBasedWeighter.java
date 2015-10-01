/**
 * This file is part of Palmetto.
 *
 * Palmetto is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Palmetto is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Palmetto.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.aksw.palmetto.weight;

import org.aksw.palmetto.data.SubsetProbabilities;

@Deprecated
public class ConditionalProbabilityBasedWeighter implements Weighter {

    @Override
    public double[] createWeights(SubsetProbabilities probabilities) {
        int pos = 0;
        for (int i = 0; i < probabilities.segments.length; ++i) {
            pos += probabilities.conditions[i].length;
        }
        double weights[] = new double[pos];

        pos = 0;
        for (int i = 0; i < probabilities.segments.length; ++i) {
            for (int j = 0; j < probabilities.conditions[i].length; ++j) {
                weights[pos] = probabilities.probabilities[probabilities.conditions[i][j]];
                ++pos;
            }
        }

        return weights;
    }

    @Override
    public String getName() {
        return "E_c";
    }
}
