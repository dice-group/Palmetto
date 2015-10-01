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

import org.aksw.palmetto.data.SegmentationDefinition;

import com.carrotsearch.hppc.BitSet;

public class OneAll implements Segmentator {

    public SegmentationDefinition getSubsetDefinition(int wordsetSize) {
	/*
	 * Code the combinations of elements not with ids but with bits. 01 is
	 * only the first element, 10 is the second and 11 is the combination of
	 * both.
	 */
	int conditions[][] = new int[wordsetSize][1];
	int segments[] = new int[wordsetSize];
	int bit = 1, pos = 0;
	int mask = (1 << wordsetSize) - 1;
	BitSet neededCounts = new BitSet(1 << wordsetSize);
	while (bit < mask) {
	    segments[pos] = bit;
	    neededCounts.set(bit);
	    conditions[pos] = new int[] { mask - bit };
	    bit = bit << 1;
	    ++pos;
	}
	neededCounts.set(mask);
	return new SegmentationDefinition(segments, conditions, neededCounts);
    }

    @Override
    public String getName() {
        return "S^{one}_{all}";
    }
}
