package org.aksw.palmetto.sum;


public class HarmonicMean implements Summarization {

    @Override
    @Deprecated
    public double summarize(double[] values) {
        double sum = 0;
        for (int i = 0; i < values.length; ++i) {
            if (values[i] == 0) {
                // if one if the values is 0 the harmonic mean goes against 0
                return 0;
            }
            sum += 1.0 / values[i];
        }
        if (sum == 0) {
            return 0;
        } else {
            return values.length / sum;
        }
    }

    @Override
    public String getName() {
        return "sigma_h";
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public double summarize(double[] values, double[] weights) {
        double weightSum = 0, sum = 0;
        for (int i = 0; i < values.length; ++i) {
            if (values[i] == 0) {
                // if one if the values is 0 the harmonic mean goes against 0
                return 0;
            }
            sum += weights[i] / values[i];
            weightSum += weights[i];
        }
        if (sum == 0) {
            return 0;
        } else {
            return weightSum / sum;
        }
    }
}
