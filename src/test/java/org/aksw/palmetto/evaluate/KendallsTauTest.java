package org.aksw.palmetto.evaluate;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class KendallsTauTest {

    private static final double DOUBLE_PRECISION_DELTA = 0.00000001;

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays
                .asList(new Object[][] {
                        /*
                         * C = 19
                         * D = 5
                         * X = 2
                         * Y = 2
                         * Tau = (19 - 5) / sqrt((19 + 5 + 2) * (19 + 5 + 2)) = 14 / 26
                         */
                        { new double[] { 2.0, 3.0, 3.0, 5.0, 5.5, 8.0, 10.0, 10.0 },
                                new double[] { 1.5, 1.5, 4.0, 3.0, 1.0, 5.0, 5.0, 9.5 }, (14.0 / 26.0) },
                        /*
                         * The same as above but with a changed order
                         */
                        { new double[] { 10.0, 10.0, 8.0, 5.5, 5.0, 3.0, 3.0, 2.0 },
                                new double[] { 9.5, 5.0, 5.0, 1.0, 3.0, 4.0, 1.5, 1.5 }, (14.0 / 26.0) } });
    }

    private double x[];
    private double y[];
    private double expectedCorrelation;

    public KendallsTauTest(double[] x, double[] y, double expectedCorrelation) {
        this.x = x;
        this.y = y;
        this.expectedCorrelation = expectedCorrelation;
    }

    @Test
    public void test() {
        KendallsTau kendallsTau = new KendallsTau();
        Assert.assertEquals(expectedCorrelation, kendallsTau.calculateRankCorrelation(x, y), DOUBLE_PRECISION_DELTA);
    }
}
