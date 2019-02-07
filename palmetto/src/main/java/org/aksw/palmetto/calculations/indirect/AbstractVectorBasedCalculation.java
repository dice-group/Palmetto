/**
 * Palmetto - Palmetto is a quality measure tool for topics.
 * Copyright © 2014 Data Science Group (DICE) (michael.roeder@uni-paderborn.de)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.aksw.palmetto.calculations.indirect;

import org.aksw.palmetto.data.SubsetVectors;

import com.carrotsearch.hppc.IntObjectOpenHashMap;

public abstract class AbstractVectorBasedCalculation implements VectorBasedConfirmationMeasure {

    @Override
    public double[] calculateConfirmationValues(SubsetVectors subsetVectors) {
        int pos = 0;
        for (int i = 0; i < subsetVectors.segments.length; ++i) {
            pos += subsetVectors.conditions[i].length;
        }
        double values[] = new double[pos];

        IntObjectOpenHashMap<double[]> vectorCache = new IntObjectOpenHashMap<double[]>();
        for (int i = 0; i < subsetVectors.vectors.length; ++i) {
            vectorCache.put(1 << i, subsetVectors.vectors[i]);
        }
        double segmentVector[],
                conditionVector[];
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
                    vectorCache.put(subsetVectors.conditions[i][j], conditionVector);
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
