package org.aksw.palmetto.sum;

public class Min implements Summarization {

    @Override
    public double summarize(double[] values) {
        double min = Double.MAX_VALUE;
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

}
