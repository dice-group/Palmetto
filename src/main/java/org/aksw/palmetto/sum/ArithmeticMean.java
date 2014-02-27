package org.aksw.palmetto.sum;

public class ArithmeticMean implements Summarization {

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
}
