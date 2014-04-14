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

import java.util.Arrays;

import org.junit.Assert;

import com.carrotsearch.hppc.BitSet;
import com.carrotsearch.hppc.IntObjectOpenHashMap;
import com.carrotsearch.hppc.IntOpenHashSet;

public abstract class AbstractSubsetCreatorTest {

    public void testSubsetCreator(int wordsetSize, SubsetCreator subsetCreator,
            int expectedSegments[], int expectedConditions[][]) {
        IntObjectOpenHashMap<IntOpenHashSet> segmentToConditionMapping = new IntObjectOpenHashMap<IntOpenHashSet>();
        BitSet neededCounts = new BitSet();
        createSets(expectedSegments, expectedConditions,
                segmentToConditionMapping, neededCounts);

        SubsetDefinition definition = subsetCreator.getSubsetDefinition(wordsetSize);

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

    private void compare(SubsetDefinition definition,
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
