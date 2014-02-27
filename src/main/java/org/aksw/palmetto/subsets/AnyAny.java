package org.aksw.palmetto.subsets;

import com.carrotsearch.hppc.BitSet;

public class AnyAny implements SubsetCreator {

    public SubsetDefinition getSubsetDefinition(int wordsetSize) {
        /*
         * Code the combinations of elements not with ids but with bits. 01 is
         * only the first element, 10 is the second and 11 is the combination of
         * both.
         */
        int mask = (1 << wordsetSize) - 1;
        int posInResult = 0;
        int segments[] = new int[mask - 1];
        int conditions[][] = new int[segments.length][];
        // Go through all possible probabilities
        for (int i = 1; i < mask; ++i) {
            segments[posInResult] = i;
            // invert the probability elements to get the possible conditions
            conditions[posInResult] = createConditions(mask - i);
            ++posInResult;
        }
        BitSet neededCounts = new BitSet(1 << wordsetSize);
        neededCounts.set(1, 1 << wordsetSize);
        return new SubsetDefinition(segments, conditions, neededCounts);
    }

    protected int[] createConditions(int condition) {
        int card = Integer.bitCount(condition);
        // create all combinations of condition including the one which contains
        // all
        int conditions[] = new int[(1 << card) - 1];
        int bit = 1, count = 0, pos, pos2, j;
        while (count < card) {
            if ((bit & condition) > 0) {
                pos = (1 << count) - 1;
                conditions[pos] = bit;
                pos2 = pos + 1;
                for (j = 0; j < pos; j++) {
                    conditions[pos2] = conditions[j] | bit;
                    ++pos2;
                }
                ++count;
            }
            bit = bit << 1;
        }
        return conditions;
    }

    @Override
    public String getName() {
        return "S_any-any";
    }
}
