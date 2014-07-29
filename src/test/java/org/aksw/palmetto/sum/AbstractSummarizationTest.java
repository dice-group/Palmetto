/**
 * Copyright (C) 2014 Michael Röder (michael.roeder@unister.de)
 *
 * Licensed under the Creative Commons Attribution-NonCommercial 4.0
 * International Public License (the "License").
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://creativecommons.org/licenses/by-nc/4.0/legalcode
 *
 * Unless required by applicable law or agreed to in writing, a file
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
