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
