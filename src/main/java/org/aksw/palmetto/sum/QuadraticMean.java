package org.aksw.palmetto.sum;

import org.aksw.palmetto.sum.weighted.WeightedSummarization;

public class QuadraticMean implements WeightedSummarization {

    @Override
    public double summarize(double[] values) {
        double sum = 0;
        for (int i = 0; i < values.length; ++i) {
            sum += Math.pow(values[i], 2);
        }
        return Math.sqrt(sum / values.length);
    }

    @Override
    public String getName() {
        return "sigma_q";
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public double summarize(double[] values, double[] weights) {
        double sum = 0, weightSum = 0;
        for (int i = 0; i < values.length; ++i) {
            sum += weights[i] * Math.pow(values[i], 2);
            weightSum += weights[i];
        }
        return Math.sqrt(sum / weightSum);
    }
}
