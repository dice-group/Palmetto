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
package org.aksw.palmetto.evaluate.rank;

import java.util.BitSet;
import java.util.function.IntUnaryOperator;

import org.dice_research.topicmodeling.commons.sort.AssociativeSort;

/**
 * A class to calculate a ranking for an array of given values.
 * 
 * @author Michael R&ouml;der (michael.roeder@uni-paderborn.de)
 *
 */
public class Ranker {
    
    protected SharedRankDeterminer determiner;
    
    public Ranker() {
        this.determiner = new AverageRankDeterminer();
    }
    
    public Ranker(SharedRankDeterminer determiner) {
        this.determiner = determiner;
    }

    /**
     * Ranks the values in the given array and replaces the values by their rank.
     * 
     * @param values        an array of the values for which the ranks should be
     *                      calculated. Note that the values will be replaced by
     *                      ranks
     * @param sortAscending a flag indicating whether the values should be sorted in
     *                      ascending (the lowest value is the best) or descending
     *                      (the highest value is the best) order.
     * @return The given double array with the calculated ranks
     */
    public double[] rank(double[] values, boolean sortAscending) {
        // Search for NaN values
        BitSet isNaNValue = new BitSet(values.length);
        for (int i = 0; i < values.length; ++i) {
            if(Double.isNaN(values[i])) {
            isNaNValue.set(i);}
        }
        int[] localNaNs = new int[(int) isNaNValue.cardinality()];
        int[] ids = new int[values.length - localNaNs.length];
        double localValues[] = new double[ids.length];
        int idPos=0;
        int nanPos=0;
        for (int i = 0; i < values.length; ++i) {
            if(isNaNValue.get(i)) {
                localNaNs[nanPos] = i;
                ++nanPos;
            } else {
                ids[idPos] = i;
                localValues[idPos]=values[i];
                ++idPos;
            }
        }
        // 1. Sort object IDs based on the given values
        localValues = AssociativeSort.quickSort(localValues, ids);
        // create ranks
        if (sortAscending) {
            // create ranks starting with the element at position 0 and increasing the id
            localValues = createRanks(localValues, ids, 0, localValues.length, (i) -> i + 1);
        } else {
            // create ranks starting with the element at the last position 0 and decreasing
            // the id
            localValues = createRanks(localValues, ids, localValues.length - 1, -1, (i) -> i - 1);
        }
        // Copy ranks
        for (int i = 0; i < ids.length; ++i) {
            values[ids[i]] = localValues[i];
        }
        // Handle NaN value ranks
        if(localNaNs.length > 0) {
            double sharedNaNRank = determiner.determineSharedRank(localValues.length + 1, localNaNs.length);
            for (int i = 0; i < localNaNs.length; ++i) {
                values[localNaNs[i]] = sharedNaNRank;
            }
        }
        return values;
    }

    private double[] createRanks(double[] sortedValues, int[] sortedIds, int startPos, int endPos,
            IntUnaryOperator stepFunction) {
        int pos = startPos;
        int rank = 1;
        int firstDiffPos;
        int diff;
        while (pos != endPos) {
            // Find first position that has a different value
            firstDiffPos = stepFunction.applyAsInt(pos);
            // While, we haven't reached the end AND the two values are the same; we do not have to check for NaN
            while ((firstDiffPos != endPos) && (sortedValues[pos] == sortedValues[firstDiffPos])) {
                firstDiffPos = stepFunction.applyAsInt(firstDiffPos);
            }
            // Figure out how many ranks we have to take care of
            diff = Math.abs(firstDiffPos - pos);
            // Calculate the (shared) rank (in case diff == 1, it is simply the rank
            double sharedRank = determiner.determineSharedRank(rank, diff);
            for (int i = 0; i < diff; ++i) {
                sortedValues[pos] = sharedRank;
                pos = stepFunction.applyAsInt(pos);
            }
            // At this line, pos has already been moved and points on firstDiffPos
            // Update the rank accordingly
            rank += diff;
        }
        return sortedValues;
    }
    
    public static interface SharedRankDeterminer {
        public double determineSharedRank(int minRank, int numberOfSharedRanks);
    }
    
    public static class AverageRankDeterminer implements SharedRankDeterminer {
        public double determineSharedRank(int minRank, int numberOfSharedRanks) {
            return minRank + ((numberOfSharedRanks - 1) / 2.0);
        }
    }
    
    public static class MinRankDeterminer implements SharedRankDeterminer {
        public double determineSharedRank(int minRank, int numberOfSharedRanks) {
            return minRank;
        }
    }
}
