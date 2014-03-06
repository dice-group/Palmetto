package org.aksw.palmetto.sum;

import org.aksw.palmetto.sum.weighted.WeightedSummarization;

public class ArithmeticMean implements WeightedSummarization {

    @Override
    public double summarize(double[] values) {
        double sum = 0;
        for (int i = 0; i < values.length; ++i) {
            sum += values[i];
        }
        return sum / (double) values.length;
    }

    @Override
    public String getName() {
        return "sigma_a";
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public double summarize(double[] values, double[] weights) {
        double weightSum = 0, sum = 0;
        for (int i = 0; i < values.length; ++i) {
            sum += weights[i] * values[i];
            weightSum += weights[i];
        }
        return sum / weightSum;
    }
}
