/**
 * Copyright (C) 2014 Michael Röder (michael.roeder@unister.de)
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
