package org.aksw.palmetto.sum.weighted;

import org.aksw.palmetto.subsets.SubsetProbabilities;

public abstract class AbstractProbabilityBasedWeighter implements SummarizationDecorator {

    protected WeightedSummarization summarizer;

    public AbstractProbabilityBasedWeighter(WeightedSummarization summarizer) {
        this.summarizer = summarizer;
    }

    @Override
    public double summarize(double[] values) {
        return summarizer.summarize(values);
    }

    @Override
    public WeightedSummarization getDecoratedSummarization() {
        return summarizer;
    }

    public double summarize(double[] values, SubsetProbabilities probabilities) {
        return summarizer.summarize(values, createWeights(probabilities));
    }

    protected abstract double[] createWeights(SubsetProbabilities probabilities);

}
