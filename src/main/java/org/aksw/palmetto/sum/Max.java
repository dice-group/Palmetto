package org.aksw.palmetto.sum;

import org.aksw.palmetto.sum.weighted.WeightedSummarization;

public class Max implements WeightedSummarization {

    @Override
    public double summarize(double[] values) {
        double max = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < values.length; ++i) {
            if (values[i] > max) {
                max = values[i];
            }
        }
        return max;
    }

    @Override
    public String getName() {
        return "sigma_x";
    }

    @Override
    public double summarize(double[] values, double[] weights) {
        double value, weightSum = 0, max = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < values.length; ++i) {
            value = values[i] * weights[i];
            weightSum += weights[i];
            if (value > max) {
                max = value;
            }
        }
        return max / weightSum;
    }

}
