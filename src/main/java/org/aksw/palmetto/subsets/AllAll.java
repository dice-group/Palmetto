package org.aksw.palmetto.subsets;

import com.carrotsearch.hppc.BitSet;

public class AllAll implements SubsetCreator {

    public SubsetDefinition getSubsetDefinition(int wordsetSize) {
        /*
         * Code the combinations of elements not with ids but with bits. 01 is
         * only the first element, 10 is the second and 11 is the combination of
         * both.
         */
        int mask = (1 << wordsetSize) - 1;
        int conditions[][] = new int[][] { { mask } };
        int segments[] = new int[] { mask };
        BitSet neededCounts = new BitSet(1 << wordsetSize);
        neededCounts.set(mask);
        return new SubsetDefinition(segments, conditions, neededCounts);
    }

    @Override
    public String getName() {
        return "S_all-all";
    }
}
