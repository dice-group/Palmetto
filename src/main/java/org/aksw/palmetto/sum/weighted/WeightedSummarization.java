package org.aksw.palmetto.sum.weighted;

import org.aksw.palmetto.sum.Summarization;

public interface WeightedSummarization extends Summarization {

    public double summarize(double values[], double weights[]);
}
