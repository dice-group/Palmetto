package org.aksw.palmetto.sum;

public interface Summarization {

    @Deprecated
    public double summarize(double values[]);

    public double summarize(double values[], double weights[]);

    public String getName();
}
