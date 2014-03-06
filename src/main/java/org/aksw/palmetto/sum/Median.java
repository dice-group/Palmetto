package org.aksw.palmetto.sum;

import java.util.Arrays;

import org.aksw.palmetto.sum.weighted.WeightedSummarization;

public class Median implements WeightedSummarization {

    @Override
    public double summarize(double[] values) {
        if (values.length == 0) {
            throw new IllegalArgumentException(
                    "The given array has to have at least one element to determine the modus.");
        }
        Arrays.sort(values);
        if ((values.length & 1) > 0) {
            return values[values.length / 2];
        } else {
            return (values[values.length / 2] + values[(values.length / 2) - 1]) / 2;
        }
    }

    @Override
    public String getName() {
        return "sigma_m";
    }

    @Override
    public double summarize(double[] values, double[] weights) {
        if (values.length == 0) {
            throw new IllegalArgumentException(
                    "The given array has to have at least one element to determine the modus.");
        }
        double weightedValues[] = new double[values.length];
        double weightSum = 0;
        for (int i = 0; i < weightedValues.length; ++i) {
            weightedValues[i] = weights[i] * values[i];
            weightSum += weights[i];
        }
        Arrays.sort(weightedValues);
        if ((weightedValues.length & 1) > 0) {
            return weightedValues[weightedValues.length / 2] / weightSum;
        } else {
            return (weightedValues[weightedValues.length / 2] + weightedValues[(weightedValues.length / 2) - 1])
                    / (2 * weightSum);
        }
    }

}
