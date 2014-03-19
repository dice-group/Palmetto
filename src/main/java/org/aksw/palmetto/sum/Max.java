package org.aksw.palmetto.sum;


public class Max implements Summarization {

    @Override
    @Deprecated
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
        double value, max = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < values.length; ++i) {
            value = values[i] * weights[i];
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

}
