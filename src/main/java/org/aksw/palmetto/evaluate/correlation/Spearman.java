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
package org.aksw.palmetto.evaluate.correlation;

import java.util.Arrays;
import java.util.Comparator;

import org.aksw.palmetto.evaluate.correlation.Spearman.ValuePair;

public class Spearman implements RankCorrelationCalculator, Comparator<ValuePair> {

    public double calculateRankCorrelation(final double x[], final double y[]) {
        if (x.length != y.length) {
            throw new IllegalArgumentException("The x and y array must have the same size!");
        }

        ValuePair sortedX[] = new ValuePair[x.length];
        ValuePair sortedY[] = new ValuePair[y.length];
        for (int i = 0; i < x.length; ++i) {
            sortedX[i] = new ValuePair(x[i], i);
            sortedY[i] = new ValuePair(y[i], i);
        }
        Arrays.sort(sortedX, this);
        Arrays.sort(sortedY, this);

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

    @Override
    public int compare(ValuePair pair1, ValuePair pair2) {
        double diff = pair1.first - pair2.first;
        if (diff < 0) {
            return -1;
        } else if (diff > 0) {
            return 1;
        } else {
            return 0;
        }
    }

    protected static class ValuePair implements Comparable<ValuePair> {
        public double first;
        public int second;

        public ValuePair(double first, int second) {
            this.first = first;
            this.second = second;
        }

        @Override
        public int compareTo(ValuePair v) {
            double diff = this.first - v.first;
            if (diff < 0) {
                return -1;
            } else if (diff > 0) {
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
