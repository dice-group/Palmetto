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
public class AnyAnyTest extends AbstractSegmentatorTest {

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                { new AnyAny(), 4, new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14 },
                        new int[][] { { 2, 4, 6, 8, 10, 12, 14 },
                        { 1, 4, 8, 5, 9, 12, 13 }, { 4, 8, 12 },
                        { 1, 2, 3, 8, 9, 10, 11 }, { 2, 8, 10 }, { 1, 8, 9 }, { 8 },
                        { 1, 2, 3, 4, 5, 6, 7 }, { 2, 4, 6 }, { 1, 4, 5 }, { 4 },
                        { 1, 2, 3 }, { 2 }, { 1 } } },
                { new AnyAny(2, false), 4, new int[] { 1, 2, 3, 4, 5, 6, 8, 9, 10, 12 },
                        new int[][] { { 2, 4, 6, 8, 10, 12 },
                        { 1, 4, 8, 5, 9, 12 }, { 4, 8, 12 },
                        { 1, 2, 3, 8, 9, 10 }, { 2, 8, 10 }, { 1, 8, 9 },
                        { 1, 2, 3, 4, 5, 6 }, { 2, 4, 6 }, { 1, 4, 5 },
                        { 1, 2, 3 } } },
                { new AnyAny(1, false), 4, new int[] { 1, 2, 4, 8 }, new int[][] { { 2, 4, 8 },
                { 1, 4, 8 }, { 1, 2, 8 }, { 1, 2, 4 } } },
                { new AnyAny(3, true), 4, new int[] { 1, 2, 3, 4, 5, 6, 8, 9, 10, 12 },
                        new int[][] { { 2, 4, 8, 6, 10, 12 },
                        { 1, 4, 8, 5, 9, 12 }, { 4, 8 },
                        { 1, 2, 3, 8, 9, 10 }, { 2, 8 }, { 1, 8 },
                        { 1, 2, 3, 4, 5, 6 }, { 2, 4 }, { 1, 4 },
                        { 1, 2 } } },
                { new AnyAny(2, true), 4, new int[] { 1, 2, 4, 8 }, new int[][] { { 2, 4, 8 },
                { 1, 4, 8 }, { 1, 2, 8 }, { 1, 2, 4 } } } });
    }

    private AnyAny subsetCreator;
    private int wordSetSize;
    private int expectedSegments[];
    private int expectedConditions[][];

    public AnyAnyTest(AnyAny subsetCreator, int wordSetSize, int[] expectedSegments, int[][] expectedConditions) {
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
