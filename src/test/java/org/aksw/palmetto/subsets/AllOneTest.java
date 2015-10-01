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

import org.junit.Test;

public class AllOneTest extends AbstractSegmentatorTest {

    @Test
    public void testWordSetLength4() {
        int expectedSegments[] = new int[] { 14, 13, 11, 7 };
        int expectedConditions[][] = new int[][] { { 1 }, { 2 }, { 4 }, { 8 } };
        testSubsetCreator(4, new AllOne(), expectedSegments, expectedConditions);
    }
}
