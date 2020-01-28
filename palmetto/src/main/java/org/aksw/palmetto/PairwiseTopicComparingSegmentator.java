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
package org.aksw.palmetto;

import java.util.Arrays;

import org.aksw.palmetto.data.SegmentationDefinition;
import org.aksw.palmetto.subsets.Segmentator;

import com.carrotsearch.hppc.BitSet;

/**
 * <p>
 * Simple example of a {@link Segmentator} that gets a word set comprising two
 * topics and creates a {@link SegmentationDefinition} with which every word of
 * one of the topics is compared to every other word of the other topic.
 * <b>Note</b> that the word set size passed to this Segmentator has to be even!
 * </p>
 * 
 * <p>
 * This class is part of https://github.com/AKSW/Palmetto/issues/2
 * </p>
 * 
 * @author Michael R&ouml;der (roeder@informatik.uni-leipzig.de)
 *
 */
public class PairwiseTopicComparingSegmentator implements Segmentator {

	@Override
	public SegmentationDefinition getSubsetDefinition(int wordsetSize) {
		// we assume, that the word set contains two topics. Thus, the number
		// has to be even
		if ((wordsetSize % 2) != 0) {
			throw new IllegalArgumentException(
					"Got a word set size that is not even. Thus, it can not contain two topics that have an equal length.");
		}
		/*
		 * Code the combinations of elements not with ids but with bits. 01 is
		 * only the first element, 10 is the second and 11 is the combination of
		 * both.
		 */
		int singleTopicSize = wordsetSize / 2;
		int secondTopicLowestBit = 1 << singleTopicSize;
		int conditions[][] = new int[wordsetSize][singleTopicSize];
		int segments[] = new int[wordsetSize];
		int condBit,
			condPos,
			bit = 1,
			pos = 0;
		int mask = (1 << wordsetSize) - 1;
		BitSet neededCounts = new BitSet(1 << wordsetSize);
		while (bit < mask) {
			segments[pos] = bit;
			neededCounts.set(bit);
			condPos = 0;
			// if this is a word of the first topic
			if (pos < singleTopicSize) {
				condBit = secondTopicLowestBit;
				while (condBit < mask) {
					neededCounts.set(bit + condBit);
					conditions[pos][condPos] = condBit;
					++condPos;
					condBit = condBit << 1;
				}
			} else {
				condBit = 1;
				while (condBit < secondTopicLowestBit) {
					neededCounts.set(bit + condBit);
					conditions[pos][condPos] = condBit;
					++condPos;
					condBit = condBit << 1;
				}
			}
			bit = bit << 1;
			++pos;
		}
		return new SegmentationDefinition(segments, conditions, neededCounts);
	}

	@Override
	public String getName() {
		return "one-topic";
	}

	public static void main(String[] args) {
		PairwiseTopicComparingSegmentator segmentator = new PairwiseTopicComparingSegmentator();
		SegmentationDefinition definition = segmentator.getSubsetDefinition(10);
		for (int i = 0; i < definition.segments.length; ++i) {
			System.out.print(definition.segments[i]);
			System.out.print(" : ");
			System.out.println(Arrays.toString(definition.conditions[i]));
		}
	}

}
