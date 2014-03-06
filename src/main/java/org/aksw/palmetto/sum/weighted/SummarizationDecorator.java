package org.aksw.palmetto.sum.weighted;

import org.aksw.palmetto.sum.Summarization;

public interface SummarizationDecorator extends Summarization {

    public Summarization getDecoratedSummarization();
}
