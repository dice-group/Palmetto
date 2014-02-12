package org.aksw.palmetto.evaluate;

import java.util.Arrays;
import java.util.Comparator;

import org.aksw.palmetto.evaluate.AbstractRankCorrelationCalculator.ValuePair;

public abstract class AbstractRankCorrelationCalculator implements RankCorrelationCalculator, Comparator<ValuePair> {

    public double calculateRankCorrelation(final double x[], final double y[]) {
        if (x.length != y.length) {
            throw new IllegalArgumentException("The x and y array have to be of the same size!");
        }

        ValuePair pairs[] = new ValuePair[x.length];
        for (int i = 0; i < x.length; ++i) {
            pairs[i] = new ValuePair(x[i], y[i]);
        }
        Arrays.sort(pairs, this);

        return calculateRankCorrelation(pairs);
    }

    protected abstract double calculateRankCorrelation(ValuePair[] pairs);

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
        public double second;

        public ValuePair(double first, double second) {
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
