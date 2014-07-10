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

public abstract class AbstractAnyBasedSubsetCreator implements SegmentationScheme {

    private int maxSingleSubSetSize = Integer.SIZE;
    private int maxSubSetUnionSize = Integer.SIZE;
    private int factorials[];

    public AbstractAnyBasedSubsetCreator() {
    }

    public AbstractAnyBasedSubsetCreator(int maxSubSetSize, boolean isSubSetUnionSize) {
        if (isSubSetUnionSize) {
            maxSubSetUnionSize = maxSubSetSize;
        } else {
            maxSingleSubSetSize = maxSubSetSize;
        }
        createOrIncreaseFactorials(maxSubSetSize);
    }

    private void createOrIncreaseFactorials(int subSetSize) {
        int startId;
        if (factorials == null) {
            factorials = new int[subSetSize + 1];
            factorials[0] = 1;
            startId = 1;
        } else {
            // the factorials array has to grow
            int temp[] = new int[subSetSize + 1];
            System.arraycopy(factorials, 0, temp, 0, factorials.length);
            startId = factorials.length;
            factorials = temp;
        }
        // create all needed factorials
        for (int i = startId; i < factorials.length; ++i) {
            factorials[i] = factorials[i - 1] * i;
        }
    }

    @Override
    public SubsetDefinition getSubsetDefinition(int wordsetSize) {
        if ((maxSingleSubSetSize < (wordsetSize - 1)) || (maxSubSetUnionSize < wordsetSize)) {
            return getSubsetDefinitionWithRestrictions(wordsetSize, maxSingleSubSetSize, maxSubSetUnionSize);
        } else {
            return getSubsetDefinitionWithoutRestrictions(wordsetSize);
        }
    }

    protected abstract SubsetDefinition getSubsetDefinitionWithoutRestrictions(int wordsetSize);

    protected abstract SubsetDefinition getSubsetDefinitionWithRestrictions(int wordsetSize, int maxSingleSubSetSize,
            int maxSubSetUnionSize);

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

    protected int[] createRestrictedConditions(int condition, int maxConditionSize) {
        int wordsAvailable = Integer.bitCount(condition);
        if (wordsAvailable <= maxConditionSize) {
            return createConditions(condition);
        }
        int numberOfConditions = getNumberOfCombinations(wordsAvailable, maxConditionSize);
        // create all combinations of condition which have less or equal the number of maximum words
        int conditions[] = new int[numberOfConditions];
        int bit = 1, pos = 0, pos2, j;
        while (bit <= condition) {
            if ((bit & condition) > 0) {
                conditions[pos] = bit;
                pos2 = pos;
                ++pos;
                for (j = 0; j < pos2; ++j) {
                    if (Integer.bitCount(conditions[j]) < maxConditionSize) {
                        conditions[pos] = conditions[j] | bit;
                        ++pos;
                    }
                }
            }
            bit = bit << 1;
        }
        return conditions;
    }

    protected int getNumberOfCombinations(int elementCount, int maxElementsPerCombination) {
        if (factorials.length <= elementCount) {
            createOrIncreaseFactorials(elementCount);
        }
        int numberOfCombinations = elementCount;
        for (int i = 2; i <= maxElementsPerCombination; ++i) {
            numberOfCombinations += factorials[elementCount] / (factorials[i] * factorials[elementCount - i]);
        }
        return numberOfCombinations;
    }

    protected boolean isSingleSubSetSizeRestricted() {
        return maxSingleSubSetSize != Integer.SIZE;
    }

    protected boolean isSubSetUnionSizeRestricted() {
        return maxSubSetUnionSize != Integer.SIZE;
    }

    public int getMaxSingleSubSetSize() {
        return maxSingleSubSetSize;
    }

    public int getMaxSubSetUnionSize() {
        return maxSubSetUnionSize;
    }
}
