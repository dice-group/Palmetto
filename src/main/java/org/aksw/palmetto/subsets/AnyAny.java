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

public class AnyAny extends AbstractAnyBasedSegmentator {

    public AnyAny() {
    }

    public AnyAny(int maxSubSetSize, boolean isSubSetUnionSize) {
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
        return new SegmentationDefinition(segments, conditions, neededCounts);
    }

    @Override
    protected SegmentationDefinition getSubsetDefinitionWithRestrictions(int wordsetSize, int maxSingleSubSetSize,
            int maxSubSetUnionSize) {
        int maxSegmentSize = maxSubSetUnionSize > maxSingleSubSetSize ? maxSingleSubSetSize
                : (maxSubSetUnionSize - 1);
        int mask = (1 << wordsetSize) - 1;
        int posInResult = 0, segmentBitCount;
        int segments[] = new int[getNumberOfCombinations(wordsetSize, maxSegmentSize)];
        int conditions[][] = new int[segments.length][];
        // Go through all possible probabilities
        for (int i = 1; i < mask; ++i) {
            segmentBitCount = Integer.bitCount(i);
            if (segmentBitCount <= maxSegmentSize) {
                segments[posInResult] = i;
                // invert the probability elements to get the possible conditions
                conditions[posInResult] = createRestrictedConditions(mask - i,
                        Math.min(maxSingleSubSetSize, maxSubSetUnionSize - segmentBitCount));
                ++posInResult;
            }
        }
        BitSet neededCounts = new BitSet(1 << wordsetSize);
        neededCounts.set(1, 1 << wordsetSize);
        return new SegmentationDefinition(segments, conditions, neededCounts);
    }

    @Override
    public String getName() {
        if (isSingleSubSetSizeRestricted()) {
            return "S^{any(" + getMaxSingleSubSetSize() + ")}_{any(" + getMaxSingleSubSetSize() + ")}";
        } else {
            if (isSubSetUnionSizeRestricted()) {
                return "S^{any}_{any}(" + getMaxSubSetUnionSize() + ")";
            } else {
                return "S^{any}_{any}";
            }
        }
    }
}
