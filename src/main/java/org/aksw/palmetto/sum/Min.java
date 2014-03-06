package org.aksw.palmetto.sum;

import org.aksw.palmetto.sum.weighted.WeightedSummarization;

public class Min implements WeightedSummarization {

    @Override
    public double summarize(double[] values) {
        double min = Double.POSITIVE_INFINITY;
        for (int i = 0; i < values.length; ++i) {
            if (values[i] < min) {
                min = values[i];
            }
        }
        return min;
    }

    @Override
    public String getName() {
        return "sigma_n";
    }

    @Override
    public double summarize(double[] values, double[] weights) {
        double value, weightSum = 0, min = Double.POSITIVE_INFINITY;
        for (int i = 0; i < values.length; ++i) {
            value = values[i] * weights[i];
            weightSum += weights[i];
            if (value < min) {
                min = value;
            }
        }
        return min / weightSum;
    }

}
