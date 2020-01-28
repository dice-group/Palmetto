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
package org.aksw.palmetto.data;

import java.util.Arrays;

import com.carrotsearch.hppc.BitSet;

/**
 * This object contains the segmentations of a word set. Thus, it represents the set S containing pairs of word set sub
 * sets S_i=(W',W*).
 * 
 * @author m.roeder
 * 
 */
public class SegmentationDefinition {

    /**
     * Defines the W' of the subset pairs
     */
    public int segments[];
    /**
     * Defines for every W' in {@link #segments} an array of W* subsets.
     */
    public int conditions[][];
    /**
     * This BitSet defines which counts are needed by the segmentation scheme. Currently, this object is created but not
     * used by the system.
     */
    public BitSet neededCounts;

    public SegmentationDefinition(int[] segments, int[][] conditions,
            BitSet neededCounts) {
        this.segments = segments;
        this.conditions = conditions;
        this.neededCounts = neededCounts;
    }

    /**
     * @return the segments
     */
    public int[] getSegments() {
        return segments;
    }

    /**
     * @param segments
     *            the segments to set
     */
    public void setSegments(int[] segments) {
        this.segments = segments;
    }

    /**
     * @return the conditions
     */
    public int[][] getConditions() {
        return conditions;
    }

    /**
     * @param conditions
     *            the conditions to set
     */
    public void setConditions(int[][] conditions) {
        this.conditions = conditions;
    }

    /**
     * @return the neededCounts
     */
    public BitSet getNeededCounts() {
        return neededCounts;
    }

    /**
     * @param neededCounts
     *            the neededCounts to set
     */
    public void setNeededCounts(BitSet neededCounts) {
        this.neededCounts = neededCounts;
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(segments);
        result = 31 * result + Arrays.hashCode(conditions);
        result = 31 * result + ((neededCounts == null) ? 0 : neededCounts.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SegmentationDefinition other = (SegmentationDefinition) obj;
        if (!Arrays.equals(conditions, other.conditions))
            return false;
        if (neededCounts == null) {
            if (other.neededCounts != null)
                return false;
        } else if (!neededCounts.equals(other.neededCounts))
            return false;
        return Arrays.equals(segments, other.segments);
    }
}
