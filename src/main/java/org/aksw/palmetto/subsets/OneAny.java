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

public class OneAny extends AbstractAnyBasedSegmentator {

    public OneAny() {
    }

    public OneAny(int maxSubSetSize, boolean isSubSetUnionSize) {
        super(maxSubSetSize, isSubSetUnionSize);
    }

    @Override
    protected SegmentationDefinition getSubsetDefinitionWithoutRestrictions(int wordsetSize) {
        /*
         * Code the combinations of elements not with ids but with bits. 01 is
         * only the first element, 10 is the second and 11 is the combination of
         * both.
         */
        int mask = (1 << wordsetSize) - 1;
        int posInResult = 0;
        int segments[] = new int[wordsetSize];
        int conditions[][] = new int[segments.length][];
        int bit = 1;
        // Go through all possible probabilities
        while (bit < mask) {
            segments[posInResult] = bit;
            // invert the probability elements to get the possible conditions
            conditions[posInResult] = createConditions(mask - bit);
            bit = bit << 1;
            ++posInResult;
        }

        BitSet neededCounts = new BitSet(1 << wordsetSize);
        neededCounts.set(1, 1 << wordsetSize);
        return new SegmentationDefinition(segments, conditions, neededCounts);
    }

    @Override
    protected SegmentationDefinition getSubsetDefinitionWithRestrictions(int wordsetSize, int maxSingleSubSetSize,
            int maxSubSetUnionSize) {
        /*
         * Code the combinations of elements not with ids but with bits. 01 is
         * only the first element, 10 is the second and 11 is the combination of
         * both.
         */
        int mask = (1 << wordsetSize) - 1;
        int posInResult = 0;
        int segments[] = new int[wordsetSize];
        int conditions[][] = new int[segments.length][];
        int bit = 1;
        int conditionRestriction = maxSubSetUnionSize > maxSingleSubSetSize ? maxSingleSubSetSize
                : (maxSubSetUnionSize - 1);
        // Go through all possible probabilities
        while (bit < mask) {
            segments[posInResult] = bit;
            // invert the probability elements to get the possible conditions
            conditions[posInResult] = createRestrictedConditions(mask - bit, conditionRestriction);
            bit = bit << 1;
            ++posInResult;
        }

        BitSet neededCounts = new BitSet(1 << wordsetSize);
        neededCounts.set(1, 1 << wordsetSize);
        return new SegmentationDefinition(segments, conditions, neededCounts);
    }

    @Override
    public String getName() {
        if (isSingleSubSetSizeRestricted()) {
            return "S^{one}_{any(" + getMaxSingleSubSetSize() + ")}";
        } else {
            return "S^{one}_{any}";
        }
    }
}
