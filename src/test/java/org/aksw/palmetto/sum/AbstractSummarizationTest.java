package org.aksw.palmetto.sum;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

public abstract class AbstractSummarizationTest {

    private static final double DOUBLE_PRECISION_DELTA = 0.00000001;

    private Summarization summarizer;
    private double values[];
    private double expectedSum;

    public AbstractSummarizationTest(Summarization summarizer, double[] values, double expectedSum) {
        this.summarizer = summarizer;
        this.values = values;
        this.expectedSum = expectedSum;
    }

    @Test
    public void test() {
        double weights[] = new double[values.length];
        Arrays.fill(weights, 1.0);
        Assert.assertEquals(expectedSum, summarizer.summarize(values, weights), DOUBLE_PRECISION_DELTA);
    }
}
