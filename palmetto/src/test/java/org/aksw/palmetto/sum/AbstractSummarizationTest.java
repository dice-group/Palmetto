/**
 * This file is part of Palmetto.
 *
 * Palmetto is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Palmetto is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Palmetto.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.aksw.palmetto.sum;

import java.util.Arrays;

import org.aksw.palmetto.aggregation.Aggregation;
import org.junit.Assert;
import org.junit.Test;

public abstract class AbstractSummarizationTest {

    private static final double DOUBLE_PRECISION_DELTA = 0.00000001;

    private Aggregation summarizer;
    private double values[];
    private double expectedSum;

    public AbstractSummarizationTest(Aggregation summarizer, double[] values, double expectedSum) {
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
