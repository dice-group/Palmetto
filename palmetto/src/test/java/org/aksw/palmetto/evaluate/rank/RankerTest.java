package org.aksw.palmetto.evaluate.rank;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class RankerTest {

    private double[] values;
    private double[] expectedRanksAsc;
    private double[] expectedRanksDesc;

    public RankerTest(double[] values, double[] expectedRanksAsc, double[] expectedRanksDesc) {
        this.values = values;
        this.expectedRanksAsc = expectedRanksAsc;
        this.expectedRanksDesc = expectedRanksDesc;
    }

    @Test
    public void test() {
        // Test ascending
        Ranker ranker = new Ranker();
        double[] localValues = Arrays.copyOf(values, values.length);
        double[] ranks = ranker.rank(localValues, true);
        try {
            Assert.assertArrayEquals(expectedRanksAsc, ranks, 0.00001);
        } catch (AssertionError e) {
            System.err.println("Expected " + Arrays.toString(expectedRanksAsc) + " but got " + Arrays.toString(ranks));
            throw e;
        }
        // Test descending
        System.arraycopy(values, 0, localValues, 0, values.length);
        ranks = ranker.rank(localValues, false);
        try {
            Assert.assertArrayEquals(expectedRanksDesc, ranks, 0.00001);
        } catch (AssertionError e) {
            System.err.println("Expected " + Arrays.toString(expectedRanksDesc) + " but got " + Arrays.toString(ranks));
            throw e;
        }
    }

    @Parameters
    public static Collection<Object[]> data() {
        List<Object[]> testCases = new ArrayList<>();
        testCases.add(new Object[] { new double[] { 1, 2, 3 }, new double[] { 1, 2, 3 }, new double[] { 3, 2, 1 } });
        testCases.add(
                new Object[] { new double[] { -99, 0, 3354 }, new double[] { 1, 2, 3 }, new double[] { 3, 2, 1 } });
        testCases.add(
                new Object[] { new double[] { 1, 2, 2 }, new double[] { 1, 2.5, 2.5 }, new double[] { 3, 1.5, 1.5 } });
        testCases.add(new Object[] { new double[] { 1, 1, 1 }, new double[] { 2, 2, 2 }, new double[] { 2, 2, 2 } });
        testCases.add(
                new Object[] { new double[] { 1, 2, Double.NaN }, new double[] { 1, 2, 3 }, new double[] { 2, 1, 3 } });
        testCases.add(new Object[] { new double[] { 1, Double.NaN, Double.NaN }, new double[] { 1, 2.5, 2.5 },
                new double[] { 1, 2.5, 2.5 } });
        testCases.add(new Object[] { new double[] { 1, Double.NaN, Double.parseDouble("NaN"), Double.NaN, 2 },
                new double[] { 1, 4, 4, 4, 2 }, new double[] { 2, 4, 4, 4, 1 } });
        testCases.add(new Object[] { new double[] { Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NaN },
                new double[] { 2, 1, 3 }, new double[] { 1, 2, 3 } });
        return testCases;
    }

}
