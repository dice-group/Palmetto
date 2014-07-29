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
