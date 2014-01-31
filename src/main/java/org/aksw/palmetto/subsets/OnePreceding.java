package org.aksw.palmetto.subsets;

import com.carrotsearch.hppc.BitSet;

public class OnePreceding implements SubsetCreator {

    public SubsetDefinition getSubsetDefinition(int wordsetSize) {
	/*
	 * Code the combinations of elements not with ids but with bits. 01 is
	 * only the first element, 10 is the second and 11 is the combination of
	 * both.
	 */
	int conditions[][] = new int[wordsetSize][];
	int segments[] = new int[wordsetSize];
	int bit = 1, pos = 0;
	int mask = (1 << wordsetSize) - 1;
	BitSet neededCounts = new BitSet(1 << wordsetSize);
	while (bit < mask) {
	    segments[pos] = bit;
	    neededCounts.set(bit);
	    conditions[pos] = new int[pos];
	    for (int i = 0; i < pos; ++i) {
		neededCounts.set(bit + (1 << i));
		conditions[pos][i] = 1 << i;
	    }
	    bit = bit << 1;
	    ++pos;
	}
	return new SubsetDefinition(segments, conditions, neededCounts);
    }
}
