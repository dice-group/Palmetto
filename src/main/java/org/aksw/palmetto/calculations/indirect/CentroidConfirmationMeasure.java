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
package org.aksw.palmetto.calculations.indirect;

import org.aksw.palmetto.data.SubsetVectors;

import com.carrotsearch.hppc.IntObjectOpenHashMap;

@Deprecated
public class CentroidConfirmationMeasure extends CosinusConfirmationMeasure {

    @Override
    public String getName() {
        return "m_cen";
    }

    @Override
    public double[] calculateConfirmationValues(SubsetVectors subsetVectors) {
        double values[] = new double[subsetVectors.segments.length];

        double centroid[] = new double[subsetVectors.vectors[0].length];
        IntObjectOpenHashMap<double[]> vectorCache = new IntObjectOpenHashMap<double[]>();
        for (int i = 0; i < subsetVectors.vectors.length; ++i) {
            vectorCache.put(1 << i, subsetVectors.vectors[i]);
            for (int j = 0; j < centroid.length; j++) {
                centroid[j] += subsetVectors.vectors[i][j];
            }
        }
        // for (int j = 0; j < centroid.length; j++) {
        // centroid[j] /= subsetVectors.vectors.length;
        // }
        double segmentVector[];
        for (int i = 0; i < subsetVectors.segments.length; ++i) {
            if (vectorCache.containsKey(subsetVectors.segments[i])) {
                segmentVector = vectorCache.lget();
            } else {
                segmentVector = createVector(subsetVectors.segments[i], subsetVectors.vectors);
                vectorCache.put(subsetVectors.segments[i], segmentVector);
            }
            values[i] = calculateSimilarity(segmentVector, centroid);
        }
        return values;
    }

}
