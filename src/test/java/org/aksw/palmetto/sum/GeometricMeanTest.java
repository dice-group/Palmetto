package org.aksw.palmetto.sum;

import java.util.Arrays;
import java.util.Collection;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class GeometricMeanTest extends AbstractSummarizationTest {

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] { { new double[] { 1, 2, 3 }, 1.817120593  },
                { new double[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 }, 0 },
                { new double[] { 0 }, 0 }, { new double[] { -1, 0, 1 }, 0 }, { new double[] { -1, 1 }, 0 } });
    }

    public GeometricMeanTest(double[] values, double expectedSum) {
        super(new GeometricMean(), values, expectedSum);
    }
}
