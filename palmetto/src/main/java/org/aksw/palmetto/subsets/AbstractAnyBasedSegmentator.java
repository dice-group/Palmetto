/**
 * Palmetto - Palmetto is a quality measure tool for topics.
 * Copyright © 2014 Data Science Group (DICE) (michael.roeder@uni-paderborn.de)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.aksw.palmetto.subsets;

import org.aksw.palmetto.data.SegmentationDefinition;

public abstract class AbstractAnyBasedSegmentator implements Segmentator {

    private int maxSingleSubSetSize = Integer.SIZE;
    private int maxSubSetUnionSize = Integer.SIZE;
    private int factorials[];

    public AbstractAnyBasedSegmentator() {
    }

    public AbstractAnyBasedSegmentator(int maxSubSetSize, boolean isSubSetUnionSize) {
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
    public SegmentationDefinition getSubsetDefinition(int wordsetSize) {
        if ((maxSingleSubSetSize < (wordsetSize - 1)) || (maxSubSetUnionSize < wordsetSize)) {
            return getSubsetDefinitionWithRestrictions(wordsetSize, maxSingleSubSetSize, maxSubSetUnionSize);
        } else {
            return getSubsetDefinitionWithoutRestrictions(wordsetSize);
        }
    }

    protected abstract SegmentationDefinition getSubsetDefinitionWithoutRestrictions(int wordsetSize);

    protected abstract SegmentationDefinition getSubsetDefinitionWithRestrictions(int wordsetSize, int maxSingleSubSetSize,
            int maxSubSetUnionSize);

    protected int[] createConditions(int condition) {
        int card = Integer.bitCount(condition);
        // create all combinations of condition including the one which contains
        // all
        int conditions[] = new int[(1 << card) - 1];
        int bit = 1,
            count = 0,
            pos,
            pos2,
            j;
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
        int bit = 1,
            pos = 0,
            pos2, j;
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
