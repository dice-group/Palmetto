package org.aksw.palmetto.sum;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class MedianTest {

    private static final double DOUBLE_PRECISION_DELTA = 0.00000001;

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] { { new double[] { 1, 2, 3 }, 2 },
                { new double[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 }, 4.5 },
                { new double[] { 0 }, 0 }, { new double[] { -1, 0, 1 }, 0 }, { new double[] { -1, 1 }, 0 } });
    }

    private double values[];
    private double expectedMean;

    public MedianTest(double[] values, double expectedMean) {
        super();
        this.values = values;
        this.expectedMean = expectedMean;
    }

    @Test
    public void test() {
        Summarization summarizer = new Median();
        Assert.assertEquals(expectedMean, summarizer.summarize(values), DOUBLE_PRECISION_DELTA);
    }
}
