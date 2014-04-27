/**
 * Copyright (C) 2014 Michael Röder (michael.roeder@unister.de)
 *
 * Licensed under the Creative Commons Attribution-NonCommercial 4.0
 * International Public License (the "License").
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://creativecommons.org/licenses/by-nc/4.0/legalcode
 *
 * Unless required by applicable law or agreed to in writing, a file
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.aksw.palmetto.subsets;

import org.aksw.palmetto.data.SubsetDefinition;

import com.carrotsearch.hppc.BitSet;

public class AnyAny extends AbstractAnyBasedSubsetCreator {

    public AnyAny() {
    }

    public AnyAny(int maxSubSetSize, boolean isSubSetUnionSize) {
        super(maxSubSetSize, isSubSetUnionSize);
    }

    @Override
    protected SubsetDefinition getSubsetDefinitionWithoutRestrictions(int wordsetSize) {
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

    @Override
    protected SubsetDefinition getSubsetDefinitionWithRestrictions(int wordsetSize, int maxSingleSubSetSize,
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
        return new SubsetDefinition(segments, conditions, neededCounts);
    }

    @Override
    public String getName() {
        if (isSingleSubSetSizeRestricted()) {
            if (isSubSetUnionSizeRestricted()) {
                return "S_any(" + getMaxSingleSubSetSize() + ")-any(" + getMaxSingleSubSetSize() + ")_("
                        + getMaxSubSetUnionSize() + ")";
            } else {
                return "S_any(" + getMaxSingleSubSetSize() + ")-any(" + getMaxSingleSubSetSize() + ")";
            }
        } else {
            if (isSubSetUnionSizeRestricted()) {
                return "S_any-any_(" + getMaxSubSetUnionSize() + ")";
            } else {
                return "S_any-any";
            }
        }
    }
}
