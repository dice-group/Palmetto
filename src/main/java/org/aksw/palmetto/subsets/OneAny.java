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

public class OneAny extends AbstractAnyBasedSubsetCreator {

    public OneAny() {
    }

    public OneAny(int maxSubSetSize, boolean isSubSetUnionSize) {
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
        return new SubsetDefinition(segments, conditions, neededCounts);
    }

    @Override
    protected SubsetDefinition getSubsetDefinitionWithRestrictions(int wordsetSize, int maxSingleSubSetSize,
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
        return new SubsetDefinition(segments, conditions, neededCounts);
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
