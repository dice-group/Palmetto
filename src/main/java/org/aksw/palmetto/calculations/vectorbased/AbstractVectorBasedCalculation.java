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

public abstract class AbstractVectorBasedCalculation implements VectorBasedCalculation {

    @Override
    public double[] calculateCoherenceValues(SubsetVectors subsetVectors) {
        int pos = 0;
        for (int i = 0; i < subsetVectors.segments.length; ++i) {
            pos += subsetVectors.conditions[i].length;
        }
        double values[] = new double[pos];

        IntObjectOpenHashMap<double[]> vectorCache = new IntObjectOpenHashMap<double[]>();
        for (int i = 0; i < subsetVectors.vectors.length; ++i) {
            vectorCache.put(1 << i, subsetVectors.vectors[i]);
        }
        double segmentVector[], conditionVector[];
        pos = 0;
        for (int i = 0; i < subsetVectors.segments.length; ++i) {
            if (vectorCache.containsKey(subsetVectors.segments[i])) {
                segmentVector = vectorCache.lget();
            } else {
                segmentVector = createVector(subsetVectors.segments[i], subsetVectors.vectors);
                vectorCache.put(subsetVectors.segments[i], segmentVector);
            }
            for (int j = 0; j < subsetVectors.conditions[i].length; ++j) {
                if (vectorCache.containsKey(subsetVectors.conditions[i][j])) {
                    conditionVector = vectorCache.lget();
                } else {
                    conditionVector = createVector(subsetVectors.conditions[i][j], subsetVectors.vectors);
                    vectorCache.put(subsetVectors.conditions[i][j], segmentVector);
                }
                values[pos] = calculateSimilarity(segmentVector, conditionVector);
                ++pos;
            }
        }
        return values;
    }

    protected abstract double calculateSimilarity(double[] vector1, double[] vector2);

    protected double[] createVector(int id, double[][] vectors) {
        int vectorCount = Integer.bitCount(id);
        if (vectorCount == 1) {
            for (int i = 0; i < vectors.length; ++i) {
                if (((1 << i) & id) > 0) {
                    return vectors[i];
                }
            }
        } else {
            double vectorSummands[][] = new double[vectorCount][];
            int pos = 0;
            for (int i = 0; i < vectors.length; ++i) {
                if (((1 << i) & id) > 0) {
                    vectorSummands[pos] = vectors[i];
                    ++pos;
                }
            }
            double vector[] = new double[vectorSummands[0].length];
            for (int i = 0; i < vector.length; ++i) {
                for (int j = 0; j < vectorSummands.length; ++j) {
                    vector[i] += vectorSummands[j][i];
                }
            }
            return vector;
        }
        // This return statement shouldn't be reached
        return null;
    }

}
