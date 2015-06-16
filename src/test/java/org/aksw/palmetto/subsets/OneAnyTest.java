/**
 * The MIT License
 * Copyright (c) 2014 Agile Knowledge Engineering and Semantic Web (AKSW)
							(roeder@informatik.uni-leipzig.de)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.aksw.palmetto.subsets;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class OneAnyTest extends AbstractSegmentatorTest {

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                { new OneAny(), 4, new int[] { 1, 2, 4, 8 }, new int[][] { { 2, 4, 8, 6, 10, 12, 14 },
                { 1, 4, 8, 5, 9, 12, 13 }, { 1, 2, 3, 8, 9, 10, 11 }, { 1, 2, 3, 4, 5, 6, 7 } } },
                { new OneAny(2, false), 4, new int[] { 1, 2, 4, 8 }, new int[][] { { 2, 4, 8, 6, 10, 12 },
                { 1, 4, 8, 5, 9, 12 }, { 1, 2, 3, 8, 9, 10 }, { 1, 2, 3, 4, 5, 6 } } },
                { new OneAny(1, false), 4, new int[] { 1, 2, 4, 8 }, new int[][] { { 2, 4, 8 },
                { 1, 4, 8 }, { 1, 2, 8 }, { 1, 2, 4 } } },
                { new OneAny(3, true), 4, new int[] { 1, 2, 4, 8 }, new int[][] { { 2, 4, 8, 6, 10, 12 },
                { 1, 4, 8, 5, 9, 12 }, { 1, 2, 3, 8, 9, 10 }, { 1, 2, 3, 4, 5, 6 } } },
                { new OneAny(2, true), 4, new int[] { 1, 2, 4, 8 }, new int[][] { { 2, 4, 8 },
                { 1, 4, 8 }, { 1, 2, 8 }, { 1, 2, 4 } } } });
    }

    private OneAny subsetCreator;
    private int wordSetSize;
    private int expectedSegments[];
    private int expectedConditions[][];

    public OneAnyTest(OneAny subsetCreator, int wordSetSize, int[] expectedSegments, int[][] expectedConditions) {
        this.subsetCreator = subsetCreator;
        this.wordSetSize = wordSetSize;
        this.expectedSegments = expectedSegments;
        this.expectedConditions = expectedConditions;
    }

    @Test
    public void test() {
        testSubsetCreator(wordSetSize, subsetCreator, expectedSegments, expectedConditions);
    }
}
