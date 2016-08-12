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
