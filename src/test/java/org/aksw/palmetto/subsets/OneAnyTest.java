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
