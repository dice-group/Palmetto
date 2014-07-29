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
package org.aksw.palmetto.data;

/**
 * This class contains the segmentations and probabilities for a word set.
 * 
 * @author m.roeder
 * 
 */
public class SubsetProbabilities {

    public int segments[];
    public int conditions[][];
    public double probabilities[];

    public SubsetProbabilities(int[] segments, int[][] conditions, double[] segmentProbabilities) {
        this.segments = segments;
        this.conditions = conditions;
        this.probabilities = segmentProbabilities;
    }

    /**
     * @return the segmentProbabilities
     */
    public double[] getSegmentProbabilities() {
        return probabilities;
    }

    /**
     * @param segmentProbabilities
     *            the segmentProbabilities to set
     */
    public void setSegmentProbabilities(double[] segmentProbabilities) {
        this.probabilities = segmentProbabilities;
    }
}
