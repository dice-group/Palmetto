/**
 * Copyright (C) 2014 Michael Röder (michael.roeder@unister.de)
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

import java.util.Arrays;

import org.aksw.palmetto.data.SegmentationDefinition;
import org.junit.Assert;

import com.carrotsearch.hppc.BitSet;
import com.carrotsearch.hppc.IntObjectOpenHashMap;
import com.carrotsearch.hppc.IntOpenHashSet;

public abstract class AbstractSegmentatorTest {

    public void testSubsetCreator(int wordsetSize, Segmentator subsetCreator,
            int expectedSegments[], int expectedConditions[][]) {
        IntObjectOpenHashMap<IntOpenHashSet> segmentToConditionMapping = new IntObjectOpenHashMap<IntOpenHashSet>();
        BitSet neededCounts = new BitSet();
        createSets(expectedSegments, expectedConditions,
                segmentToConditionMapping, neededCounts);

        SegmentationDefinition definition = subsetCreator.getSubsetDefinition(wordsetSize);

        compare(definition, segmentToConditionMapping, neededCounts);
    }

    private void createSets(int[] expectedSegments, int[][] expectedConditions,
            IntObjectOpenHashMap<IntOpenHashSet> segmentToConditionMapping,
            BitSet neededCounts) {
        IntOpenHashSet conditionSet;
        for (int i = 0; i < expectedSegments.length; i++) {
            conditionSet = new IntOpenHashSet(expectedConditions[i].length);
            conditionSet.add(expectedConditions[i]);
            segmentToConditionMapping.put(expectedSegments[i], conditionSet);

            neededCounts.set(expectedSegments[i]);
            for (int j = 0; j < expectedConditions[i].length; j++) {
                neededCounts
                        .set(expectedSegments[i] | expectedConditions[i][j]);
            }
        }
    }

    private void compare(SegmentationDefinition definition,
            IntObjectOpenHashMap<IntOpenHashSet> segmentToConditionMapping,
            BitSet neededCounts) {
        IntOpenHashSet conditionSet;

        Assert.assertEquals(segmentToConditionMapping.size(),
                definition.segments.length);
        for (int i = 0; i < definition.segments.length; i++) {
            Assert.assertTrue("got unexpected segment "
                    + definition.segments[i], segmentToConditionMapping
                    .containsKey(definition.segments[i]));
            conditionSet = segmentToConditionMapping
                    .get(definition.segments[i]);
            for (int j = 0; j < definition.conditions[i].length; ++j) {
                Assert.assertEquals(
                        "expected " + conditionSet.size() + " conditions "
                                + conditionSet.toString()
                                + " for segment ["
                                + definition.segments[i] + "] but got " + definition.conditions[i].length + " "
                                + Arrays.toString(definition.conditions[i]) + ".",
                        conditionSet.size(),
                        definition.conditions[i].length);
                Assert.assertTrue("got unexpected condition "
                        + definition.conditions[i][j] + " for segment "
                        + definition.segments[i],
                        conditionSet.contains(definition.conditions[i][j]));
            }
        }

        // Assert.assertEquals(neededCounts, definition.neededCounts);
    }
}
