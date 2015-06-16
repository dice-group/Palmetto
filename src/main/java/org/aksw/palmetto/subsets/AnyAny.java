/**
 * The MIT License
 * Copyright (c) 2014 Agile Knowledge Engineering and Semantic Web (AKSW)
							(roeder@informatik.uni-leipzig.de)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
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
