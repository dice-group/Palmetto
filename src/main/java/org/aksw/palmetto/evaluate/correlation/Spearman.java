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
package org.aksw.palmetto.evaluate.correlation;

import java.util.Arrays;

public class Spearman implements RankCorrelationCalculator/* , Comparator<ValuePair> */{

    public double calculateRankCorrelation(final double x[], final double y[]) {
        if (x.length != y.length) {
            throw new IllegalArgumentException("The x and y array must have the same size!");
        }

        ValuePair sortedX[] = new ValuePair[x.length];
        ValuePair sortedY[] = new ValuePair[y.length];
        for (int i = 0; i < x.length; ++i) {
            sortedX[i] = new ValuePair(x[i], i);
            sortedY[i] = new ValuePair(y[i], i);
            if (Double.isNaN(x[i]) || Double.isNaN(y[i])) {
                System.out.println("STOP!");
            }
        }
        try {
            // Arrays.sort(sortedX, this);
            Arrays.sort(sortedX);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(Arrays.toString(sortedX));
            return -1;
        }
        try {
            // Arrays.sort(sortedY, this);
            Arrays.sort(sortedY);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(Arrays.toString(sortedY));
            return -1;
        }

        double ranks1[] = createRanks(sortedX);
        double ranks2[] = createRanks(sortedY);

        return calculateRankCorrelationUsingRanks(ranks1, ranks2);
    }

    private double[] createRanks(ValuePair[] sortedPairs) {
        double ranks[] = new double[sortedPairs.length];
        int lowestRank, highestRank = 0;
        double rank;
        while (highestRank < sortedPairs.length) {
            lowestRank = highestRank;
            ++highestRank;
            while ((highestRank < sortedPairs.length)
                    && (sortedPairs[lowestRank].first == sortedPairs[highestRank].first)) {
                ++highestRank;
            }
            rank = (lowestRank + 1 + highestRank) / 2.0;
            for (int i = lowestRank; i < highestRank; ++i) {
                ranks[sortedPairs[i].second] = rank;
            }
        }
        return ranks;
    }

    private double calculateRankCorrelationUsingRanks(final double ranks1[], final double ranks2[]) {
        double sum = 0;
        for (int i = 0; i < ranks1.length; i++) {
            sum += Math.pow(ranks1[i] - ranks2[i], 2);
        }
        return 1 - ((6 * sum) / (ranks1.length * (Math.pow(ranks1.length, 2) - 1)));
    }

    // @Override
    // public int compare(ValuePair pair1, ValuePair pair2) {
    // double diff = pair1.first - pair2.first;
    // if (diff < 0) {
    // return -1;
    // } else if (diff > 0) {
    // return 1;
    // } else {
    // return 0;
    // }
    // }

    protected static class ValuePair implements Comparable<ValuePair> {
        public double first;
        public int second;

        public ValuePair(double first, int second) {
            this.first = first;
            this.second = second;
        }

        @Override
        public int compareTo(ValuePair v) {
            if (Double.isNaN(this.first)) {
                if (Double.isNaN(v.first)) {
                    return 0;
                } else {
                    return -1;
                }
            }
            if (Double.isNaN(v.first)) {
                return 1;
            }
            if (this.first < v.first) {
                return -1;
            } else if (this.first > v.first) {
                return 1;
            } else {
                return 0;
            }
        }

        @Override
        public String toString() {
            return "(" + first + "|" + second + ")";
        }
    }
}
