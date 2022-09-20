/**
 * Palmetto - Palmetto is a quality measure tool for topics.
 * Copyright © 2014 Data Science Group (DICE) (michael.roeder@uni-paderborn.de)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.aksw.palmetto.subsets;

import org.junit.Test;

@Deprecated
public class AllAllTest extends AbstractSegmentatorTest {

    @Test
    public void testWordSetLength4() {
        int expectedSegments[] = new int[] { 15 };
        int expectedConditions[][] = new int[][] { { 15 } };
        testSubsetCreator(4, new AllAll(), expectedSegments, expectedConditions);
    }
}
