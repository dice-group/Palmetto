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
package org.aksw.palmetto.calculations.vectorbased;

import org.aksw.palmetto.data.SubsetVectors;

import com.carrotsearch.hppc.IntObjectOpenHashMap;

public class CentroidBasedCalculation extends CosinusBasedCalculation {

    @Override
    public String getCalculationName() {
        return "m_cen";
    }

    @Override
    public double[] calculateCoherenceValues(SubsetVectors subsetVectors) {
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
