package org.aksw.palmetto.sum;


public class Min implements Summarization {

    @Override
    @Deprecated
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
        double value, min = Double.POSITIVE_INFINITY;
        for (int i = 0; i < values.length; ++i) {
            value = values[i] * weights[i];
            if (value < min) {
                min = value;
            }
        }
        return min;
    }

}
