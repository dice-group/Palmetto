package org.aksw.palmetto.sum;

public class Max implements Summarization {

    @Override
    public double summarize(double[] values) {
        double max = -Double.MAX_VALUE;
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

}
