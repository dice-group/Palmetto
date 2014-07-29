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
