package org.aksw.palmetto.subsets;

import com.carrotsearch.hppc.BitSet;

public class OneOne implements SubsetCreator {

    public SubsetDefinition getSubsetDefinition(int wordsetSize) {
        /*
         * Code the combinations of elements not with ids but with bits. 01 is
         * only the first element, 10 is the second and 11 is the combination of
         * both.
         */
        int conditions[][] = new int[wordsetSize][wordsetSize - 1];
        int segments[] = new int[wordsetSize];
        int condBit, condPos, bit = 1, pos = 0;
        int mask = (1 << wordsetSize) - 1;
        BitSet neededCounts = new BitSet(1 << wordsetSize);
        while (bit < mask) {
            segments[pos] = bit;
            neededCounts.set(bit);
            condBit = 1;
            condPos = 0;
            while (condBit < mask) {
                if (condBit != bit) {
                    neededCounts.set(bit + condBit);
                    conditions[pos][condPos] = condBit;
                    ++condPos;
                }
                condBit = condBit << 1;
            }
            bit = bit << 1;
            ++pos;
        }
        return new SubsetDefinition(segments, conditions, neededCounts);
    }

    @Override
    public String getName() {
        return "S_one-one";
    }
}
